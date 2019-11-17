import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-cross-modal-binding',
  templateUrl: './cross-modal-binding.component.html',
  styleUrls: ['./cross-modal-binding.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%)"}))
      ])
    ])
  ]
})
export class CrossModalBindingComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private randomService: RandomNumberService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //preload videos source.
    //this.preloadVideoSource();

    //init variables
    this.indexOfVideo = 1;
    this.checkData = false;
    this.indexOfTrial = 1;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.enableClick = true;
    this.realTrialIndex = 0;
    this.responseIndex = 0;
    this.gameMode = "start";

    this.initSyncData();

    if(AppConfig.DISABLE_RIGHT_CLICK){
      window.addEventListener('contextmenu', function(e){
        e.preventDefault();
      }, false)
    }
  }

  //public variables
  gameMode: String;
  coins;
  rocks;
  allChoices: number[] = [];

  //private variables
  private indexOfVideo;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private trialResultsInSameListLength;
  private currentWordList: number[];
  private currentPolygonList: number[];
  private currentResponseWordList: number[];
  private responseWordInput: number[] =[];
  private userPolygonInput: number[] =[];
  private stimuliInput: number[] = [];  //length = 36
  private currentListLength;
  private realTrialIndex: number;
  private responseIndex: number;
  private checkData;
  private endTime;
  private startTime;
  private practiceListIndex;
  private practiceFeedbackVideoNames;
  private enableClick;

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Practice",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/crossmodal/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.currentWordList = CrossModalBindingComponent.practiceWordTrialList[this.practiceListIndex];
          this.currentPolygonList = CrossModalBindingComponent.practicePolygonTrialList[this.practiceListIndex];
          this.currentResponseWordList = CrossModalBindingComponent.practiceResponseWordList[this.practiceListIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            this.showPolygonsAndWord();  
          }, 0);
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
    
  }

  startRealTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Progress",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/crossmodal/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          this.currentWordList = CrossModalBindingComponent.realWordTrialList[this.realTrialIndex];
          this.currentPolygonList = CrossModalBindingComponent.realPolygonTrialList[this.realTrialIndex];
          this.currentResponseWordList = CrossModalBindingComponent.realResponseWordList[this.realTrialIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            this.showPolygonsAndWord();  
          }, 0);
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
    
  }

  playIntroVideos(): void{
    let videoId = "intro-video";
    let source = CrossModalBindingComponent.introVideoSource;
    if(this.indexOfVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playIntroVideos);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  showExitBtn(): void{
    document.getElementById("game-achievement-div").style.display = "block";
    document.getElementById("end-btn").style.display = "block";
  }

  clickPolygon(event){
    if(this.enableClick){
      let polygon = event.target.getAttribute("data-polygenid");
      this.userPolygonInput.push(parseInt(polygon));

      this.enableClick = false;
      if(this.responseIndex < this.currentResponseWordList.length){
        //play next audio
        let audioPath = "../../../assets/audios/cross-modal-binding/real/";
        if(this.gameMode == "practice_trial"){
          audioPath = "../../../assets/audios/cross-modal-binding/practice/";
        }
        let wordIndex = this.currentResponseWordList[this.responseIndex];
        this.responseWordInput.push(wordIndex);
        let audio = new Audio();
        audio.src = audioPath + wordIndex + ".mp3";
        audio.load();
        audio.play();
        audio.addEventListener("ended", () => {
          this.enableClick = true;
        })
        this.responseIndex++;
      }else{
        //send data and go to next round
        this.endTime = new Date().getTime();
        if(this.gameMode === "practice_trial"){
          this.sendData(this.showFeedback, "practice");
        }else{
          this.sendData(this.goToNextTrial);
        }
      }
    }
  }

  private showPolygonsAndWord(){
    this.currentListLength = this.currentPolygonList.length;
    setTimeout(() => {
      document.getElementById("polygon-div").style.display = "block";
    }, 0);
    for(let i = 0; i < this.currentListLength; i++){
      setTimeout(() => {
        if(i > 0){
          document.getElementById("polygon-div").innerHTML = "";
        }
        
        let audioPath = "../../../assets/audios/cross-modal-binding/real/";
        let polyPath = "../../../assets/images/cross-modal-binding/real/";
        if(this.gameMode == "practice_trial"){
          audioPath = "../../../assets/audios/cross-modal-binding/practice/";
          polyPath = "../../../assets/images/cross-modal-binding/practice/";
        }
        //play sound
        let audio = new Audio();
        audio.src = audioPath + this.currentWordList[i] + ".mp3";
        audio.load();
        audio.play();

        let polygon = document.createElement("img");
        polygon.setAttribute("src", polyPath + this.currentPolygonList[i] + ".png");
        polygon.setAttribute("width", "300px");
        polygon.style.display = "block";
        polygon.style.margin = "0 auto";
        polygon.classList.add("img-fluid");
        document.getElementById("polygon-div").appendChild(polygon);

        if(i == this.currentListLength - 1){
          setTimeout(() => {
            this.readyToAnswer();
          }, 2000);
        }
      }, 2000 * i);
    }
  }

  private readyToAnswer(){
    document.getElementById("polygon-div").innerHTML = "";
    this.randomizePolygonChoices(6);
    document.getElementById("polygon-div").style.display = "none";
    document.getElementById("answer-div").style.display = "block";
    window.addEventListener("keydown", this.pressKeyboard);
    this.startTime = new Date().getTime();
    this.enableClick = true;
    //play sound
    let audioPath = "../../../assets/audios/cross-modal-binding/real/";
    if(this.gameMode == "practice_trial"){
      audioPath = "../../../assets/audios/cross-modal-binding/practice/";
    }
    let wordIndex = this.currentResponseWordList[this.responseIndex];
    this.responseWordInput.push(wordIndex);
    let audio = new Audio();
    audio.src = audioPath + wordIndex + ".mp3";
    audio.load();
    audio.play();

    this.responseIndex++;
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "responseWordInput": this.responseWordInput,
      "userPolygonInput": this.userPolygonInput,
      "stimuliWordInput": this.currentWordList,
      "stimuliPolygonInput": this.currentPolygonList,
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
    }
    if(trialType != undefined || trialType != null){
      formData["trialType"] = trialType;
      formData["indexOfTrial"] = this.practiceListIndex;
      formData["currentListIndex"] = this.practiceListIndex;
    }else{
      formData["indexOfTrial"] = this.indexOfTrial;
      formData["currentListIndex"] = this.realTrialIndex;
      formData["consecutiveTrialResult"] = this.trialResultsInSameListLength;
    }

    this.dataRecordService.sendUserData("./api/crossmodal", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          callback();
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();          
      }
    );
  }

  private goToNextPracticeTrial = (event) => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeTrial);
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    this.responseIndex = 0;
    this.userPolygonInput = [];
    this.responseWordInput = [];
    if(this.practiceListIndex < CrossModalBindingComponent.practicePolygonTrialList.length){
      this.currentPolygonList = CrossModalBindingComponent.practicePolygonTrialList[this.practiceListIndex];
      this.currentWordList = CrossModalBindingComponent.practiceWordTrialList[this.practiceListIndex];
      this.currentResponseWordList = CrossModalBindingComponent.practiceResponseWordList[this.practiceListIndex];
      this.generateStimuliInput();

      this.showPolygonsAndWord();
    }else{
      //go to real trial
      this.startRealTrials();
    }
  }

  private showFeedback= () => {
    let userAnswerCorrect = true;
    let userInput = this.generateUserInput();
    for(let i = 0; i < userInput.length; i++){
      if(userInput[i] != this.stimuliInput[i]){
        userAnswerCorrect = false;
        break;
      }
    }
    
    document.getElementById("answer-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < CrossModalBindingComponent.practicePolygonTrialList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job_earn_coins"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex < CrossModalBindingComponent.practicePolygonTrialList.length - 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_try_some_more"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops", "do_some_more"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", CrossModalBindingComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length > 0){
      document.getElementById(videoId).addEventListener("ended", this.playPracticeFeedbackVideo);
    }else{
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial);
    }
  }

  private playPracticeFeedbackVideo = () => {
    let videoId = "feedback-video";
    document.getElementById(videoId).setAttribute("src", CrossModalBindingComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    //let videoSource = VisualSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]];
    // if(videoSource.indexOf("Do_Some_More") >= 0 || videoSource.indexOf("Fantastic") >= 0){
    //   document.getElementById(videoId).style.cssFloat = "right";
    // }else{
    //   document.getElementById(videoId).style.cssFloat = "none";
    // }
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial);
    }
  }

  private goToNextTrial = () => {
    //update
    this.updateTrialsResultInSameListLength();  
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;
    this.responseIndex = 0;
    this.userPolygonInput = [];
    this.responseWordInput = [];
    //check the 4 answer in the same list length
    if(this.realTrialIndex >= CrossModalBindingComponent.realWordTrialList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > CrossModalBindingComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentPolygonList = CrossModalBindingComponent.realPolygonTrialList[this.realTrialIndex];
      this.currentWordList = CrossModalBindingComponent.realWordTrialList[this.realTrialIndex];
      this.currentResponseWordList = CrossModalBindingComponent.realResponseWordList[this.realTrialIndex];
      this.generateStimuliInput();
      document.getElementById("answer-div").style.display = "none";
      this.showPolygonsAndWord();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/crossmodal/gameover", formData, () => {}).subscribe(
      (data) => { 
        let result = data;
        if(result != undefined){
          this.rocks = result["rocks"];
          this.coins = result["coins"];
          this.gameMode = "complete";
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
  }

  private updateTrialsResultInSameListLength(){
    //skip the first 4 trials
    if(this.realTrialIndex > 3){
      let userAnswerCorrect = true;
      let userInput = this.generateUserInput();
      for(let i = 0; i < userInput.length; i++){
        if(userInput[i] != this.stimuliInput[i]){
          userAnswerCorrect = false;
          break;
        }
      }
      this.trialResultsInSameListLength[this.indexOfTrial - 1] = userAnswerCorrect ? 1: 0;
    }
  }

  private pressKeyboard = (event) => {
    let keyCode = event.keyCode;
    //keyCode = 106 means user press "*"
    if(keyCode === 106){
      this.checkData = true;
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/crossmodal/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          if(result["gameStatus"] === "In Practice"){
            this.gameMode = "practice_trial";
            this.startPracticeTrials();
          }else if(result["gameStatus"] === "In Progress"){
            this.gameMode = "real_trial";
            if(result["currentListIndex"] != undefined && result["currentListIndex"] != null){
              this.realTrialIndex = result["currentListIndex"];
            }else{
              this.realTrialIndex = 0;
            }
            if(result["consecutiveTrialResult"] != undefined && result["consecutiveTrialResult"] != null){
              this.trialResultsInSameListLength = result["consecutiveTrialResult"];
            }
            this.indexOfTrial = this.realTrialIndex % 4 + 1;
            this.startRealTrials();
          }else if(result["gameStatus"] === "Complete"){
            this.gameMode = "complete";
            this.coins = result["coins"];
            this.rocks = result["rocks"];
          }else{
            //this.gameMode = "start";
            this.gameMode ="intro";
          }
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      },
    )
  }

  private initSyncData(){
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    }
    this.httpService.post("./api/crossmodal/syncdata", formData).subscribe(
      (data) => {
        if(data != null && data["errorType"] != undefined){
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  private preloadVideoSource(): string[]{
    let result = [];
    for(var i = 0; i < CrossModalBindingComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", CrossModalBindingComponent.videoSource[i], true);
			req.responseType = "blob";
			req.onload = function(){
				if(this.status === 200){
					var videoBlob = this.response;
					var vid = URL.createObjectURL(videoBlob);
				  result.push(vid);
				}
			}
			req.onerror = function(){
				console.log("error");
			}
			req.send();
    }
    return result;
  }

  private generateStimuliInput():number[]{
    //init with 0
    for(let i = 0; i < CrossModalBindingComponent.nonWordCount; i++){
      this.stimuliInput[i] = 0;
    }
    //assign values
    for(let i = 0; i < this.currentWordList.length; i++){
      this.stimuliInput[this.currentWordList[i] - 1] = this.currentPolygonList[i];
    }
    return this.stimuliInput;
  }

  private generateUserInput():number[]{
    let userInput = [];
    for(let i = 0; i < CrossModalBindingComponent.nonWordCount; i++){
      userInput[i] = 0;
    }
    for(let i = 0; i < this.responseWordInput.length; i++){
      userInput[this.responseWordInput[i] - 1] = this.userPolygonInput[i];
    }
    return userInput;
  }

  private randomizePolygonChoices(length){
    this.allChoices = [];
    let totalPolygonsCount = 36;
    if(this.gameMode == "practice_trial"){
      totalPolygonsCount = 8;
    }
    let polygonOptions = [];
    for(let i = 1; i <= totalPolygonsCount; i++){
      polygonOptions.push(i);
    }

    for(let i = 0; i < this.currentPolygonList.length; i++){
      this.allChoices.push(this.currentPolygonList[i]);
    }
    while(this.allChoices.length < length){
      let randNum = this.randomService.generateRandomInt(0, polygonOptions.length);
      if(this.allChoices.indexOf(polygonOptions[randNum]) < 0){
        this.allChoices.push(polygonOptions[randNum]);
      }
    }
    for(let i = 0; i < length / 2; i++){
      let randNum = this.randomService.generateRandomInt(1, length + 1);
      //swap
      let temp = this.allChoices[i];
      this.allChoices[i] = this.allChoices[randNum - 1];
      this.allChoices[randNum - 1] = temp;
    }
  }

  //consts
  private static introVideoSource = [
    "../../../assets/videos/cross-modal-binding/Intro_1.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_2.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_3.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_4.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_5.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_6.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_7.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_8.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_9.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_10.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_11.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_12.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_13.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_14.mp4",
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/cross-modal-binding/Feedback_Great_Job.mp4",
    "correct_great_job_earn_coins" : "../../../assets/videos/cross-modal-binding/Feedback_Great_Job_Earn_Coins.mp4",
    "incorrect_oops": "../../../assets/videos/cross-modal-binding/Feedback_Ooh.mp4",
    "incorrect_oops_try_some_more": "../../../assets/videos/cross-modal-binding/Feedback_Ooh_Try_Some_More.mp4",
    "do_some_more": "../../../assets/videos/cross-modal-binding/Feedback_Do_Some_More.mp4",
    "ending": "../../../assets/videos/cross-modal-binding/Ending.mp4",
  };
  private static videoSource = [
    "../../../assets/videos/cross-modal-binding/Intro_1.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_2.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_3.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_4.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_5.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_6.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_7.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_8.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_9.mp4",
    "../../../assets/videos/cross-modal-binding/Intro_10.mp4",
    "../../../assets/videos/cross-modal-binding/Feedback_Great_Job.mp4",
    "../../../assets/videos/cross-modal-binding/Feedback_Ooh.mp4",
    "../../../assets/videos/cross-modal-binding/Ending_1.mp4",
    "../../../assets/videos/cross-modal-binding/Ending_2.mp4"
  ];

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static nonWordCount = 36;
  private static practiceWordTrialList = [
    [1],
    [2, 3],
    [4, 5]
  ];
  private static practicePolygonTrialList = [
    [2],
    [7, 1],
    [5, 8]
  ];
  private static practiceResponseWordList = [
    [1],
    [2, 3],
    [4, 5]
  ];
  private static realWordTrialList = [
    [7, 1],
    [4, 32],
    [5, 36],
    [20, 29],
    [30, 2, 34],
    [25, 19, 6],
    [28, 22, 16],
    [3, 23, 31],
    [9, 26, 24, 10],
    [13, 14, 27, 12],
    [33, 35, 15, 11],
    [21, 8, 18, 17]
  ];
  private static realPolygonTrialList = [
    [10, 31],
    [24, 17],
    [12, 32],
    [11, 2],
    [18, 19, 16],
    [1, 29, 8],
    [28, 20, 15],
    [26, 4, 30],
    [7, 14, 5, 27],
    [34, 25, 36, 35],
    [3, 22, 9, 13],
    [21, 23, 6, 13]
  ];
  private static realResponseWordList = [
    [1, 7],
    [4, 32],
    [36, 5],
    [29, 20],
    [30, 2, 34],
    [25, 6, 19],
    [28, 22, 16],
    [23, 31, 3],
    [9, 26, 24, 10],
    [27, 12, 14, 13],
    [11, 15, 33, 35],
    [18, 21, 17, 8]
  ];
}