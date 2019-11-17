import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import {DataRecordService} from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-visual-span-running',
  templateUrl: './visual-span-running.component.html',
  styleUrls: ['./visual-span-running.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class VisualSpanRunningComponent implements OnInit {

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
    this.indexOfVideo = 0;
    this.checkData = false;
    this.indexOfTrial = 1;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.enableClick = true;
    this.realTrialIndex = 0;
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
    this.gameProgressService.updateGameProgress("./api/visualspanrunning/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          setTimeout(() => {
            document.getElementById("ready-div").style.display = "none";
          }, 2000);
          this.currentList = VisualSpanRunningComponent.practiceTrialList[this.practiceListIndex];
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
    this.gameProgressService.updateGameProgress("./api/visualspanrunning/progress", formData, () => {}).subscribe(
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
          this.currentList = VisualSpanRunningComponent.realTrialList[this.realTrialIndex];
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
      document.getElementById(videoId).setAttribute("src", VisualSpanRunningComponent.introVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", this.playNextIntroVideo);
  }

  showExitBtn(): void{
    document.getElementById("game-achievement-div").style.display = "block";
    document.getElementById("end-btn").style.display = "block";
  }

  clickPolygon(event){
    if(this.enableClick){
      let polygon = event.target.getAttribute("data-polygenid");
      let path = this.gameMode == "practice_trial" ? "/assets/images/visual-span-running/practice/" : "/assets/images/visual-span-running/real/";
      this.userAnswer.push({"value": parseInt(polygon), "img": "url('" + path + polygon + ".png')"});
      this.vsUserInput.push(parseInt(polygon));

      if(this.userAnswer.length >= this.currentListLength){
        this.enableClick = false;        
      }
    }
  }

  clickNextBtn(): void{
    //send data
    this.endTime = new Date().getTime();
    if(this.gameMode === "practice_trial"){
      this.sendData(this.showFeedback, "practice");
    }else{
      this.sendData(this.goToNextTrial);
    }
  }

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < VisualSpanRunningComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", VisualSpanRunningComponent.introVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextIntroVideo);
      document.getElementById("next-btn").style.display = "block";
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
        let path = this.gameMode == "practice_trial" ? "../../../assets/images/visual-span-running/practice/" : "../../../assets/images/visual-span-running/real/";
        polygon.setAttribute("src", path + this.currentList[i] + ".png");
        polygon.setAttribute("width", "500px");
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
      this.randomizePolygonChoices();
      this.userAnswer = [];
      document.getElementById("polygon-div").style.display = "none";
      document.getElementById("answer-div").style.display = "block";
      document.getElementById("next-trial-btn").style.display = "block";
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
      formData["indexOfTrial"] = VisualSpanRunningComponent.listLengthTrialOrdinal[this.realTrialIndex];
      formData["currentListIndex"] = this.realTrialIndex;
      formData["consecutiveTrialResult"] = this.trialResultsInSameListLength;
    }

    this.dataRecordService.sendUserData("./api/visualspanrunning", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          this.rocks = result["rocks"];
          this.coins = result["coins"];
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
    this.vsUserInput = [];
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    if(this.practiceListIndex < VisualSpanRunningComponent.practiceTrialList.length){
      this.currentList = VisualSpanRunningComponent.practiceTrialList[this.practiceListIndex];
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
    let userAnswerCorrect = false;
    for(let i = 0; i < this.vsUserInput.length; i++){
      if(this.vsUserInput[this.vsUserInput.length - 1 - i] == this.currentList[this.currentListLength - 1 - i]){
        userAnswerCorrect = true;
        break;
      }
    }
    document.getElementById("answer-div").style.display = "none";
    document.getElementById("next-trial-btn").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";

    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < VisualSpanRunningComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["correct_try_another"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex == 0){
        this.playPracticeFeedbackVideos(["incorrect"]);
      }else if(this.practiceListIndex == 1){
        this.playPracticeFeedbackVideos(["opps_try_another"]);
      }else{
        this.playPracticeFeedbackVideos(["oops_earn_coins"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", VisualSpanRunningComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", VisualSpanRunningComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
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
    this.vsUserInput = [];    
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= VisualSpanRunningComponent.realTrialList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > VisualSpanRunningComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentList = VisualSpanRunningComponent.realTrialList[this.realTrialIndex];
      document.getElementById("answer-div").style.display = "none";
      document.getElementById("next-trial-btn").style.display = "none";
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
    this.gameProgressService.updateGameProgress("./api/visualspanrunning/gameover", formData, () => {}).subscribe(
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
      let hasShapeCorrect = false;
      for(let i = 0; i < userInputLength && i < this.currentList.length; i++){
        if(this.vsUserInput[userInputLength - 1 - i] === this.currentList[this.currentList.length - 1 - i]){
          hasShapeCorrect = true;
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
    this.gameProgressService.fetchGameProgress("./api/visualspanrunning/progress", formData, () => {}).subscribe(
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
            this.startRealTrials();
          }else if(result["gameStatus"] === "Complete"){
            this.gameMode = "complete";
            this.coins = result["coins"];
            this.rocks = result["rocks"];
          }else{
            this.gameMode ="intro";
            setTimeout(() => {
              this.playIntroVideos();
            }, 0);
          }
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  private initSyncData(){
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    }
    this.httpService.post("./api/visualspanrunning/syncdata", formData).subscribe(
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
    for(var i = 0; i < VisualSpanRunningComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", VisualSpanRunningComponent.videoSource[i], true);
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

  private randomizePolygonChoices(){
    this.allChoices = [1, 2, 3, 4, 5, 6, 7, 8];
    for(let i = 0; i < this.allChoices.length / 2; i++){
      let randNum = this.randomService.generateRandomInt(1, 9);
      //swap
      let temp = this.allChoices[i];
      this.allChoices[i] = this.allChoices[randNum - 1];
      this.allChoices[randNum - 1] = temp;
    }
  }

  //consts
  private static introVideoSource = [
    "../../../assets/videos/visual-span-running/Intro_1.mp4",
    "../../../assets/videos/visual-span-running/Intro_2.mp4",
    "../../../assets/videos/visual-span-running/Intro_3.mp4",
    "../../../assets/videos/visual-span-running/Intro_4.mp4",
    "../../../assets/videos/visual-span-running/Intro_5.mp4",
    "../../../assets/videos/visual-span-running/Intro_6.mp4",
    "../../../assets/videos/visual-span-running/Intro_7.mp4",
    "../../../assets/videos/visual-span-running/Intro_8.mp4",
    "../../../assets/videos/visual-span-running/Intro_9.mp4",
    "../../../assets/videos/visual-span-running/Intro_10.mp4",
    "../../../assets/videos/visual-span-running/Intro_11.mp4",
    "../../../assets/videos/visual-span-running/Intro_12.mp4",
    "../../../assets/videos/visual-span-running/Intro_13.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/visual-span-running/Feedback_Great_Job.mp4",
    "correct_try_another": "../../../assets/videos/visual-span-running/Feedback_Right_Try_Another.mp4",
    "opps_try_another": "../../../assets/videos/visual-span-running/Feedback_Oops_Try_Another.mp4",
    "oops_earn_coins": "../../../assets/videos/visual-span-running/Feedback_Oops_Earn_Coins.mp4",
    "incorrect": "../../../assets/videos/visual-span-running/Feedback_Incorrect.mp4",
    "ending_great": "../../../assets/videos/visual-span-running/Ending_Great_Job.mp4"
  };
  private static videoSource = [
    "../../../assets/videos/visual-span-running/Intro_1.mp4",
    "../../../assets/videos/visual-span-running/Intro_2.mp4",
    "../../../assets/videos/visual-span-running/Intro_3.mp4",
    "../../../assets/videos/visual-span-running/Intro_4.mp4",
    "../../../assets/videos/visual-span-running/Intro_5.mp4",
    "../../../assets/videos/visual-span-running/Intro_6.mp4",
    "../../../assets/videos/visual-span-running/Intro_7.mp4",
    "../../../assets/videos/visual-span-running/Intro_8.mp4",
    "../../../assets/videos/visual-span-running/Intro_9.mp4",
    "../../../assets/videos/visual-span-running/Intro_10.mp4",
    "../../../assets/videos/visual-span-running/Intro_11.mp4",
    "../../../assets/videos/visual-span-running/Intro_12.mp4",
    "../../../assets/videos/visual-span-running/Intro_13.mp4",
    "../../../assets/videos/visual-span-running/Intro_14.mp4",
    "../../../assets/videos/visual-span-running/Intro_15.mp4",
    "../../../assets/videos/visual-span-running/Intro_16.mp4",
    "../../../assets/videos/visual-span-running/Feedback_Great_Job.mp4",
    "../../../assets/videos/visual-span-running/Feedback_Oops.mp4",
    "../../../assets/videos/visual-span-running/Feedback_Oops_Touch.mp4",
    "../../../assets/videos/visual-span-running/Feedback_Pick_Wrong.mp4",
    "../../../assets/videos/visual-span-running/Feedback_Try_Another.mp4",
    "../../../assets/videos/visual-span-running/Ending_Fantastic_1.mp4",
    "../../../assets/videos/visual-span-running/Ending_Fantastic_2.mp4"
  ];

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static practiceTrialList = [
    [1, 4, 8, 6, 7, 3],
    [5, 6, 7, 4],
    [1, 2, 3, 6, 4]
  ];
  private static listLengthTrialOrdinal = [1, 1, 1, 2, 2, 1, 2, 2, 3, 3, 4, 3, 4, 3, 4, 4];
  private static realTrialList = [
    [3, 7, 6, 1, 4],
    [7, 8, 5, 2],
    [5, 8, 6, 2, 4, 1],
    [7, 3, 4, 2, 6, 8],
    [3, 8, 2, 7],
    [4, 1, 7],
    [2, 5, 1],
    [6, 3, 4, 2, 8],
    [5, 3, 7],
    [3, 1, 2, 8],
    [5, 6, 1, 3],
    [2, 8, 1, 3, 5],
    [7, 4, 1, 2, 5],
    [6, 3, 7, 1, 5, 8],
    [1, 5, 7, 8, 2, 6],
    [1, 4, 8]
  ];
}
