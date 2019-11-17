import { Component, OnInit } from '@angular/core';
import * as createjs from 'createjs-module';
import { trigger, transition, animate, style } from '@angular/animations';

import { GameProgressService } from '../../services/game-progress/game-progress.service';
import {DataRecordService} from '../../services/utils/data-record.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-location-span-running',
  templateUrl: './location-span-running.component.html',
  styleUrls: ['./location-span-running.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class LocationSpanRunningComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //init variables
    this.indexOfVideo = 1;
    this.checkData = false;
    this.practiceIndex = 0;
    this.realTrialIndex = 0;
    this.enableDotTouch = false;
    this.numOfTouch = 0;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.indexOfTrial = 1;
    this.gameMode = "start";

    this.initSyncData();

    if(AppConfig.DISABLE_RIGHT_CLICK){
      window.addEventListener('contextmenu', function(e){
        e.preventDefault();
      }, false)
    }
    
  }

  //public variables
  gameMode:String;
  coins;
  rocks;

  //private variables
  private indexOfVideo;
  private endTime;
  private startTime;
  private checkData;
  private trialResultsInSameListLength;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private realTrialIndex: number;
  private currentList: number[];
  private numOfTouch: number;
  private lsUserInput: number[] = [];
  private practiceFeedbackVideoNames;
  private practiceIndex: number;

  private stage;
  private dotContainer;
  private enableDotTouch: boolean;

  startGame():void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Practice",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/locationspanrunning/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          setTimeout(() => {
            this.drawCircles();
            this.currentList = LocationSpanRunningComponent.practiceTrialList[this.practiceIndex];
            this.showArrows();
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
    this.gameProgressService.updateGameProgress("./api/locationspanrunning/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          setTimeout(() => {
            if(this.dotContainer == undefined){
              this.drawCircles();
            }
            this.dotContainer.alpha = 0;
            document.getElementById("circles-div").style.display = "block";
            this.currentList = LocationSpanRunningComponent.realTrialList[this.realTrialIndex];
            this.showArrows();
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
    let source = LocationSpanRunningComponent.introVideoSource;
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

  clickNextBtn(): void{
    //check 
    this.endTime = new Date().getTime();
    if(this.gameMode === "practice_trial"){
      this.sendData(this.showFeedback, null, "practice");
    }else{
      //update
      this.updateTrialsResultInSameListLength();
      this.sendData(this.goToNextTrial, 1000);
    }
  }

  showExitBtn(): void{
    document.getElementById("game-achievement-div").style.display = "block";
    document.getElementById("end-btn").style.display = "block";
  }

  drawCircles(){
    this.stage = new createjs.Stage("circles-canvas");
    this.resizeCanvas();
    var center = new createjs.Shape();
    center.graphics.beginFill("black").drawCircle(0, 0, 20);
    center.x = LocationSpanRunningComponent.centerPosition.x;
    center.y = LocationSpanRunningComponent.centerPosition.y;
    this.stage.addChild(center);
    //draw 8 dots
    this.dotContainer = new createjs.Container();
    this.dotContainer.x = 0;
    this.dotContainer.y = 0;
    this.dotContainer.alpha = 0;
    this.stage.addChild(this.dotContainer);
    for(var i = 0; i < LocationSpanRunningComponent.circlePositions.length; i++){
      var dot = new createjs.Shape();
      dot.graphics.beginFill("red").drawCircle(0, 0, 20);
      dot.x = LocationSpanRunningComponent.circlePositions[i].x;
      dot.y = LocationSpanRunningComponent.circlePositions[i].y;
      dot.name = LocationSpanRunningComponent.circlePositions[i].name;
      dot.id = i;
      dot.addEventListener("click", this.touchDotEvent);
      this.dotContainer.addChild(dot);
    }
    this.stage.update();

    //add resize event listener
    window.addEventListener("resize", this.resizeCanvas);
  }

  showArrows(){
    for(let i = 0; i < this.currentList.length; i++){
      setTimeout(() => {
        this.showArrow(this.currentList[i] - 1, LocationSpanRunningComponent.animationConfig.moveDuration, LocationSpanRunningComponent.arrowOnInterval);
      }, i * (LocationSpanRunningComponent.arrowOnInterval + LocationSpanRunningComponent.arrowOffInterval + LocationSpanRunningComponent.animationConfig.moveDuration));
    }

    setTimeout(() => {
      this.enableDotTouchEvent();
      this.dotContainer.alpha = 1;
      document.getElementById("next-trial-btn").style.display = "block";
      this.startTime = new Date().getTime();
      this.stage.update();
      window.addEventListener("keydown", this.pressKeyboard);
    }, this.currentList.length * (LocationSpanRunningComponent.animationConfig.moveDuration + LocationSpanRunningComponent.arrowOffInterval + LocationSpanRunningComponent.arrowOnInterval))
  }

  //index range is from 0 to 7.
  showArrow(index, speed, duration){
    var degree = LocationSpanRunningComponent.circlePositions[index].degree;
    var lineX = LocationSpanRunningComponent.centerPosition.x + LocationSpanRunningComponent.centerToEdgeRadius * 0.8 * Math.sin(degree / 180 * Math.PI);
    var lineY = LocationSpanRunningComponent.centerPosition.y - LocationSpanRunningComponent.centerToEdgeRadius * 0.8 * Math.cos(degree / 180 * Math.PI);
    var moveArrowListener = createjs.Ticker.on("tick", this.stage);
    var line = new createjs.Shape();
    line.graphics.beginStroke("black").setStrokeStyle(12).moveTo(LocationSpanRunningComponent.centerPosition.x, LocationSpanRunningComponent.centerPosition.y);
    line.graphics.lineTo(lineX, lineY);
    // var cmd = line.graphics.lineTo(LocationSpanRunningComponent.centerPosition.x, LocationSpanRunningComponent.centerPosition.y).command;
    // createjs.Tween.get(cmd).to({x: lineX, y: lineY }, speed);

    // var triangle = new createjs.Shape();
    // triangle.graphics.beginFill("black").drawPolyStar(LocationSpanRunningComponent.centerPosition.x, LocationSpanRunningComponent.centerPosition.y, 30, 3, 0, 
    //   LocationSpanRunningComponent.circlePositions[index].degree - 90);
    // createjs.Tween.get(triangle).to(
    //   {x: (LocationSpanRunningComponent.circlePositions[index].x - LocationSpanRunningComponent.centerPosition.x) * 0.8, 
    //     y: (LocationSpanRunningComponent.circlePositions[index].y - LocationSpanRunningComponent.centerPosition.y) * 0.8 }, 
    //     speed);
    var triangle = new createjs.Shape();
    triangle.graphics.beginFill("black").drawPolyStar(lineX, lineY, 30, 3, 0, LocationSpanRunningComponent.circlePositions[index].degree - 90);

    this.stage.addChild(line, triangle);
    this.stage.update();

    setTimeout(() => {
      //remove arrow
      this.stage.removeChild(line, triangle);
      this.stage.update();
      createjs.Ticker.off("tick", moveArrowListener);
    }, speed + duration);
  }

  enableDotTouchEvent(){
    this.enableDotTouch = true;
  }

  private touchDotEvent = (event) => {
    if(this.enableDotTouch){
      this.enableDotTouch = false;
      this.showArrow(event.target.id, LocationSpanRunningComponent.animationConfig.moveDuration, LocationSpanRunningComponent.arrowOnIntervalAfterClick);
      this.numOfTouch ++;
      this.lsUserInput.push(event.target.id + 1);
      setTimeout(() => {
        this.enableDotTouch = true;
      }, LocationSpanRunningComponent.animationConfig.moveDuration + LocationSpanRunningComponent.arrowOnIntervalAfterClick);
    }
  }

  private pressKeyboard = (event) => {
    let keyCode = event.keyCode;
    //keyCode = 106 means user press "*"
    if(keyCode === 106){
      this.checkData = true;
    }
  }

  private resizeCanvas = (event?) => {
    let minLength = window.innerHeight < window.innerWidth ? window.innerHeight : window.innerWidth;
    if(minLength < 600){
      //resize
      this.stage.canvas.width = minLength * 0.9;
      this.stage.canvas.height = minLength * 0.9;
      this.stage.scaleX = this.stage.scaleY = minLength * 0.9 / 600;
      this.stage.update();
    }
  }

  private sendData(callback, timeDelay, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userInput": this.lsUserInput,
      "stimuliInput": this.currentList,
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
    }
    if(trialType != undefined || trialType != null){
      formData["trialType"] = trialType;
      formData["indexOfTrial"] = this.practiceIndex;
      formData["currentListIndex"] = this.practiceIndex;
    }else{
      formData["indexOfTrial"] = LocationSpanRunningComponent.listLengthTrialOrdinal[this.realTrialIndex];
      formData["currentListIndex"] = this.realTrialIndex;
      formData["consecutiveTrialResult"] = this.trialResultsInSameListLength;
    }

    this.dataRecordService.sendUserData("./api/locationspanrunning", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          if(timeDelay != null){
            setTimeout(function(){
              callback();
            }, timeDelay);
          }else{
            callback();
          }
        }
      }
    );
  }

  private goToNextTrial = () => {
    this.enableDotTouch = false;
    this.numOfTouch = 0;
    this.lsUserInput = [];    
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= LocationSpanRunningComponent.realTrialList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > LocationSpanRunningComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentList = LocationSpanRunningComponent.realTrialList[this.realTrialIndex];
      this.dotContainer.alpha = 0;
      document.getElementById("next-trial-btn").style.display = "none";
      this.showArrows();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/locationspanrunning/gameover", formData, () => {}).subscribe(
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
      let userInputLength = this.lsUserInput.length;
      let hasDigitCorrect = false;
      for(let i = 0; i < userInputLength && i < this.currentList.length; i++){
        if(this.lsUserInput[userInputLength - 1 - i] === this.currentList[this.currentList.length - 1 - i]){
          hasDigitCorrect = true;
          break;
        }
      }
      this.trialResultsInSameListLength[this.indexOfTrial - 1] = hasDigitCorrect ? 1: 0;
    }
  }

  private showFeedback = () => {
    //check the last answer correct
    let userAnswerCorrect = false;
    for(let i = 0; i < this.lsUserInput.length; i++){
      if(this.lsUserInput[this.lsUserInput.length - 1 - i] == this.currentList[this.currentList.length - 1 - i]){
        userAnswerCorrect = true;
        break;
      }
    }

    this.enableDotTouch = false;
    this.dotContainer.alpha = 0;
    this.stage.update();
    //play feedback video
    document.getElementById("circles-div").style.display = "none";
    document.getElementById("next-trial-btn").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      if(this.practiceIndex < LocationSpanRunningComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_lets_start"]);
      }
    }else{
      if(this.practiceIndex < LocationSpanRunningComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["oops_1"]);
      }else{
        this.playPracticeFeedbackVideos(["oops_3", "oops_lets_start"]);
      }
      
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", LocationSpanRunningComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length > 0){
      document.getElementById(videoId).addEventListener("ended", this.playPracticeFeedbackVideo);
    }else{
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeRound);
    }
  }

  private playPracticeFeedbackVideo = () => {
    let videoId = "feedback-video";
    document.getElementById(videoId).setAttribute("src", LocationSpanRunningComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    let videoSource = LocationSpanRunningComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]];
    if(videoSource.indexOf("Do_Some_More") >= 0 || videoSource.indexOf("Fantastic") >= 0){
      document.getElementById(videoId).style.cssFloat = "right";
    }else{
      document.getElementById(videoId).style.cssFloat = "none";
    }
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeRound);
    }
  }

  private goToNextPracticeRound = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeRound);
    document.getElementById("feedback-video-div").style.display = "none";
    
    this.numOfTouch = 0;
    this.lsUserInput = [];
    this.checkData = false;
    this.practiceIndex++;
    
    if(this.practiceIndex < LocationSpanRunningComponent.practiceTrialList.length){
      this.currentList = LocationSpanRunningComponent.practiceTrialList[this.practiceIndex];
      document.getElementById("circles-div").style.display = "block";
      this.showArrows();
    }else{
      this.gameMode = "real_trial";
      this.startRealTrials();
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/locationspanrunning/progress", formData, () => {}).subscribe(
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
            this.indexOfTrial = (this.realTrialIndex) % 4 + 1;
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
    this.httpService.post("./api/locationspanrunning/syncdata", formData).subscribe(
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
    for(var i = 0; i < LocationSpanRunningComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", LocationSpanRunningComponent.videoSource[i], true);
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

  //consts
  private static introVideoSource = [
    "../../../assets/videos/location-span-running/Intro_1.mp4",
    "../../../assets/videos/location-span-running/Intro_2.mp4",
    "../../../assets/videos/location-span-running/Intro_3.mp4",
    "../../../assets/videos/location-span-running/Intro_4.mp4",
    "../../../assets/videos/location-span-running/Intro_5.mp4",
    "../../../assets/videos/location-span-running/Intro_6.mp4",
    "../../../assets/videos/location-span-running/Intro_7.mp4",
    "../../../assets/videos/location-span-running/Intro_8.mp4",
    "../../../assets/videos/location-span-running/Intro_9.mp4",
    "../../../assets/videos/location-span-running/Intro_10.mp4",
    "../../../assets/videos/location-span-running/Intro_11.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/location-span-running/Feedback_Great_Job.mp4",
    "correct_great_lets_start" : "../../../assets/videos/location-span-running/Feedback_Great_Lets_Start.mp4",
    "oops_1": "../../../assets/videos/location-span-running/Feedback_Oops_1.mp4",
    "oops_3": "../../../assets/videos/location-span-running/Feedback_Oops_3.mp4",
    "oops_lets_start": "../../../assets/videos/location-span-running/Feedback_Oops_Lets_Start.mp4"
  };
  private static videoSource = [
    "../../../assets/videos/location-span/Intro_1.mp4",
    "../../../assets/videos/location-span/Intro_2.mp4",
    "../../../assets/videos/location-span/Intro_3.mp4",
    "../../../assets/videos/location-span/Intro_4.mp4",
    "../../../assets/videos/location-span/Intro_5.mp4",
    "../../../assets/videos/location-span/Intro_6.mp4",
    "../../../assets/videos/location-span/Intro_7.mp4",
    "../../../assets/videos/location-span/Intro_8.mp4",
    "../../../assets/videos/location-span/Feedback_Great_Job.mp4",
    "../../../assets/videos/location-span/Feedback_Oops.mp4",
    "../../../assets/videos/location-span/Feedback_Try_Some_More.mp4",
    "../../../assets/videos/location-span/Feedback_Do_Something_Else.mp4",
    "../../../assets/videos/location-span/Ending_Great_Job.mp4"
  ];

  private static circlePositions = [
    {x: 414, y: 22, name: "22.5", degree: 22.5},
    {x: 577, y: 186, name: "67.5", degree: 67.5},
    {x: 577, y: 414, name: "112.5", degree: 112.5},
    {x: 414, y: 577, name: "157.5", degree: 157.5},
    {x: 186, y: 577, name: "202.5", degree: 202.5},
    {x: 22, y: 414, name: "247.5", degree: 247.5},
    {x: 22, y: 186, name: "292.5", degree: 292.5},
    {x: 186, y: 22, name: "337.5", degree: 337.5}
  ];
  private static centerPosition = {
    x: 300, y: 300
  };
  private static centerToEdgeRadius = 300;
  private static animationConfig = {
    "moveDuration": 10,
  }

  private static arrowOnInterval = 1000;
  private static arrowOffInterval = 500;
  private static arrowOnIntervalAfterClick = 200;

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static practiceTrialList = [
    [8, 7, 1, 4, 6, 2, 5, 3],
    [4, 2, 5, 3, 6, 1],
    [5, 2, 6, 8, 3, 7, 1],
  ];
  private static listLengthTrialOrdinal = [1, 1, 1, 1, 2, 2, 3, 2, 3, 2, 3, 3, 4, 4, 4, 4];
  private static realTrialList = [
    [8, 6, 1, 4, 7],
    [6, 3, 7, 1, 4, 8, 5],
    [5, 8, 2, 7, 3, 1, 4, 6],
    [8, 6, 1, 5, 7, 3],
    [6, 4, 1, 7, 2, 8, 3, 5],
    [7, 5, 2, 6, 3, 8, 4],
    [3, 8, 6, 2, 7, 5, 1],
    [3, 7, 2, 5, 1],
    [1, 3, 2, 6, 4],
    [5, 7, 3, 6, 4, 1],
    [4, 6, 2, 5, 7, 3],
    [5, 3, 6, 4, 1, 7, 2, 8],
    [7, 4, 8, 2, 1, 5, 3],
    [6, 3, 7, 4, 8],
    [1, 3, 7, 5, 2, 6, 8, 4],
    [6, 1, 5, 2, 4, 7]
  ];
}
