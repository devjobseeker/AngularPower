import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import {DataRecordService} from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-visual-span',
  templateUrl: './visual-span.component.html',
  styleUrls: ['./visual-span.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class VisualSpanComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private randomService: RandomNumberService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //init variables
    this.indexOfVideo = 1;
    this.checkData = false;
    this.gameMode = "start";
    this.indexOfTrial = 1;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.enableClick = true;
    this.realTrialIndex = 0;

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
  userAnswer;

  //private variables
  private indexOfVideo;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private trialResultsInSameListLength;
  private currentList: number[];
  private currentListLength;
  private vsUserInput: number[] = [];
  private realTrialIndex: number;
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
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Practice"
    }
    this.gameProgressService.updateGameProgress("./api/visualspan/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          setTimeout(() => {
            document.getElementById("ready-div").style.display = "none";
          }, 2000);
          this.currentList = VisualSpanComponent.practiceTrialList[this.practiceListIndex];
          this.showPolygons();
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
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Progress"
    }
    this.gameProgressService.updateGameProgress("./api/visualspan/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          setTimeout(() => {
            document.getElementById("ready-div").style.display = "block";
          }, 0);
          
          setTimeout(() => {
            document.getElementById("ready-div").style.display = "none";
          }, 2000);
          this.currentList = VisualSpanComponent.realTrialList[this.realTrialIndex];
          this.showPolygons();
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
    let source = VisualSpanComponent.introVideoSource;
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
      for(let i = 0; i < this.userAnswer.length; i++){
        if(this.userAnswer[i].value == 0){
          let path = this.gameMode == "practice_trial" ? "/assets/images/visual-span/practice/" : "/assets/images/visual-span/real/";
          this.userAnswer[i].img = "url('" + path + polygon + ".png')";
          this.userAnswer[i].value = parseInt(polygon);
          this.vsUserInput.push(this.userAnswer[i].value);
          break;
        }
      }

      if(this.userAnswer[this.userAnswer.length - 1].value != 0){
        this.enableClick = false;
        //send data
        this.endTime = new Date().getTime();
        if(this.gameMode === "practice_trial"){
          this.sendData(this.showFeedback, "practice");
        }else{
          this.sendData(this.goToNextTrial);
        }
      }
    }
  }

  private showPolygons(){
    this.currentListLength = this.currentList.length;
    setTimeout(() => {
      document.getElementById("polygon-div").style.display = "block";
    }, 0);
    for(let i = 0; i < this.currentListLength; i++){
      setTimeout(() => {
        if(i > 0){
          document.getElementById("polygon-div").innerHTML = "";
        }
        let polygon = document.createElement("img");
        let path = this.gameMode == "practice_trial" ? "../../../assets/images/visual-span/practice/" : "../../../assets/images/visual-span/real/";
        polygon.setAttribute("src", path + this.currentList[i] + ".png");
        polygon.setAttribute("width", "200px");
        polygon.style.display = "block";
        polygon.style.margin = "0 auto";
        polygon.classList.add("img-fluid");
        document.getElementById("polygon-div").appendChild(polygon);
      }, 2000 * (i + 1));
    }

    this.readyToAnswer();
  }

  private readyToAnswer(){
    setTimeout(() => {
      document.getElementById("polygon-div").innerHTML = "";
      this.randomizePolygonChoices(8);
      this.userAnswer = [];
      for(let i = 0; i < this.currentListLength; i++){
        this.userAnswer.push({"value": 0, "img": "url('/assets/images/empty.png')"});
      }
      document.getElementById("empty-block-div").style.width = 200 * this.currentListLength + "px";
      document.getElementById("polygon-div").style.display = "none";
      document.getElementById("answer-div").style.display = "block";
      window.addEventListener("keydown", this.pressKeyboard);
      this.startTime = new Date().getTime();
      this.enableClick = true;
    }, 2000 * (this.currentListLength + 1));
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userInput": this.vsUserInput,
      "stimuliInput": this.currentList,
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

    this.dataRecordService.sendUserData("./api/visualspan", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          this.rocks = result["rocks"];
          this.coins = result["coins"];
          setTimeout(() => {
            callback();
          }, 500);
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
    this.vsUserInput = [];
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    if(this.practiceListIndex < VisualSpanComponent.practiceTrialList.length){
      this.currentList = VisualSpanComponent.practiceTrialList[this.practiceListIndex];
      document.getElementById("ready-div").style.display = "block";
      setTimeout(() => {
        document.getElementById("ready-div").style.display = "none";
      }, 2000);
      this.showPolygons();
    }else{
      //go to real trial
      this.startRealTrials();
    }
  }

  private showFeedback = () => {
    let userAnswerCorrect = true;
    if(this.vsUserInput.length != this.currentList.length){
      userAnswerCorrect = false;
    }else{
      for(let i = 0; i < this.vsUserInput.length; i++){
        if(this.vsUserInput[i] != this.currentList[i]){
          userAnswerCorrect = false;
          break;
        }
      }
    }
    
    document.getElementById("answer-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < VisualSpanComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job", "do_some_more"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex == 1){
        this.playPracticeFeedbackVideos(["oops"]);
      }else{
        this.playPracticeFeedbackVideos(["oops_3", "do_some_more"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", VisualSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", VisualSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
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
    this.vsUserInput = [];    
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= VisualSpanComponent.realTrialList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > VisualSpanComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentList = VisualSpanComponent.realTrialList[this.realTrialIndex];
      document.getElementById("answer-div").style.display = "none";
      document.getElementById("ready-div").style.display = "block";    
      setTimeout(() => {
        document.getElementById("ready-div").style.display = "none";
      }, 2000);
      this.showPolygons();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": this.gameMode
    }
    this.gameProgressService.updateGameProgress("./api/visualspan/gameover", formData, () => {}).subscribe(
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
      let userInputLength = this.vsUserInput.length;
      let hasShapeCorrect = true;
      for(let i = 0; i < userInputLength && i < this.currentList.length; i++){
        if(this.vsUserInput[i] != this.currentList[i]){
          hasShapeCorrect = false;
          break;
        }
      }
      this.trialResultsInSameListLength[this.indexOfTrial - 1] = hasShapeCorrect ? 1: 0;
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
      "grade": sessionStorage.getItem("grade"),
    };
    this.gameProgressService.fetchGameProgress("./api/visualspan/progress", formData, () => {}).subscribe(
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
            this.gameMode ="intro";
          }
        }else{
          this.errorService.internalError();
        }
      },
      (err) => this.errorService.networkError(),
    )
  }

  private initSyncData(){
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    }
    this.httpService.post("./api/visualspan/syncdata", formData).subscribe(
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
    for(var i = 0; i < VisualSpanComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", VisualSpanComponent.videoSource[i], true);
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

  private randomizePolygonChoices(length){
    this.allChoices = [];
    for(let i = 1; i <= length; i++){
      this.allChoices.push(i);
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
    "../../../assets/videos/visual-span/Intro_1.mp4",
    "../../../assets/videos/visual-span/Intro_2.mp4",
    "../../../assets/videos/visual-span/Intro_3.mp4",
    "../../../assets/videos/visual-span/Intro_4.mp4",
    "../../../assets/videos/visual-span/Intro_5.mp4",
    "../../../assets/videos/visual-span/Intro_6.mp4",
    "../../../assets/videos/visual-span/Intro_7.mp4",
    "../../../assets/videos/visual-span/Intro_8.mp4",
    "../../../assets/videos/visual-span/Intro_9.mp4",
    "../../../assets/videos/visual-span/Intro_10.mp4",
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/visual-span/Feedback_Great_Job.mp4",
    "do_some_more":"../../../assets/videos/visual-span/Feedback_Do_Some_More.mp4",
    "oops": "../../../assets/videos/visual-span/Feedback_Oops.mp4",
    "oops_3": "../../../assets/videos/visual-span/Feedback_Oops_3.mp4",
    "ending": "../../../assets/videos/visual-span/Ending.mp4"
  };
  private static videoSource = [
    "../../../assets/videos/visual-span/Intro_1.mp4",
    "../../../assets/videos/visual-span/Intro_2.mp4",
    "../../../assets/videos/visual-span/Intro_3.mp4",
    "../../../assets/videos/visual-span/Intro_4.mp4",
    "../../../assets/videos/visual-span/Intro_5.mp4",
    "../../../assets/videos/visual-span/Intro_6.mp4",
    "../../../assets/videos/visual-span/Intro_7.mp4",
    "../../../assets/videos/visual-span/Intro_8.mp4",
    "../../../assets/videos/visual-span/Intro_9.mp4",
    "../../../assets/videos/visual-span/Intro_10.mp4",
    "../../../assets/videos/visual-span/Feedback_Great_Job.mp4",
    "../../../assets/videos/visual-span/Feedback_Oops.mp4",
    "../../../assets/videos/visual-span/Feedback_Do_Some_More.mp4",
    "../../../assets/videos/visual-span/Ending_Great_Job.mp4"
  ];

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static practiceTrialList = [
    [3],
    [6, 1],
    [1, 5]
  ];
  private static realTrialList = [
    [4, 1],
    [3, 2],
    [7, 5],
    [1, 8],
    [6, 7, 3],
    [5, 4, 8],
    [3, 2, 4],
    [6, 4, 2],
    [4, 1, 8, 2],
    [8, 6, 1, 2],
    [1, 8, 7, 3],
    [5, 2, 8, 1],
    [8, 6, 1, 7, 2],
    [1, 2, 4, 7, 3],
    [8, 7, 2, 3, 1],
    [6, 5, 4, 1, 7],
    [4, 8, 6, 2, 5, 1],
    [6, 8, 7, 4, 2, 1],
    [7, 5, 8, 2, 3, 4],
    [2, 1, 6, 3, 4, 7]
  ];
}