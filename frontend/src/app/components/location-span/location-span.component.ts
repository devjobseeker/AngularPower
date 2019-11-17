import { Component, OnInit } from '@angular/core';
import * as createjs from 'createjs-module';
import { trigger, transition, animate, style } from '@angular/animations';

import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { DataRecordService } from '../../services/utils/data-record.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-location-span',
  templateUrl: './location-span.component.html',
  styleUrls: ['./location-span.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class LocationSpanComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //this.preloadVideoSource();
    //init variables
    this.indexOfVideo = 1;
    this.indexOfEndVideo = 0;
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
  private indexOfEndVideo;
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
    this.gameProgressService.updateGameProgress("./api/locationspan/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          setTimeout(() => {
            this.drawCircles();
            this.currentList = LocationSpanComponent.practiceTrialList[this.practiceIndex];
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
    this.gameProgressService.updateGameProgress("./api/locationspan/progress", formData, () => {}).subscribe(
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
            this.currentList = LocationSpanComponent.realTrialList[this.realTrialIndex];
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
    let source = LocationSpanComponent.introVideoSource;
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

  playEndingVideo(): void{
    let videoId = "end-video";
    if(this.indexOfEndVideo < 1){
      document.getElementById(videoId).setAttribute("src", LocationSpanComponent.feedbackVideoSource["ending2"]); 
      document.getElementById(videoId).removeEventListener("ended", this.playEndingVideo);   
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      document.getElementById(videoId).addEventListener("ended", this.showExitBtn);
      this.indexOfEndVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playEndingVideo);
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
    center.x = LocationSpanComponent.centerPosition.x;
    center.y = LocationSpanComponent.centerPosition.y;
    this.stage.addChild(center);
    //draw 8 dots
    this.dotContainer = new createjs.Container();
    this.dotContainer.x = 0;
    this.dotContainer.y = 0;
    this.dotContainer.alpha = 0;
    this.stage.addChild(this.dotContainer);
    for(var i = 0; i < LocationSpanComponent.circlePositions.length; i++){
      var dot = new createjs.Shape();
      dot.graphics.beginFill("red").drawCircle(0, 0, 20);
      dot.x = LocationSpanComponent.circlePositions[i].x;
      dot.y = LocationSpanComponent.circlePositions[i].y;
      dot.name = LocationSpanComponent.circlePositions[i].name;
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
        this.showArrow(this.currentList[i] - 1, LocationSpanComponent.animationConfig.moveDuration, LocationSpanComponent.arrowOnInterval);
      }, i * (LocationSpanComponent.arrowOnInterval + LocationSpanComponent.arrowOffInterval + LocationSpanComponent.animationConfig.moveDuration));
    }

    setTimeout(() => {
      this.enableDotTouchEvent();
      this.dotContainer.alpha = 1;
      this.startTime = new Date().getTime();
      this.stage.update();
      window.addEventListener("keydown", this.pressKeyboard);
    }, this.currentList.length * (LocationSpanComponent.animationConfig.moveDuration + LocationSpanComponent.arrowOnInterval + LocationSpanComponent.arrowOffInterval))
  }

  //index range is from 0 to 7.
  showArrow(index, speed, duration){
    var degree = LocationSpanComponent.circlePositions[index].degree;
    var lineX = LocationSpanComponent.centerPosition.x + LocationSpanComponent.centerToEdgeRadius * 0.8 * Math.sin(degree / 180 * Math.PI);
    var lineY = LocationSpanComponent.centerPosition.y - LocationSpanComponent.centerToEdgeRadius * 0.8 * Math.cos(degree / 180 * Math.PI);
    var moveArrowListener = createjs.Ticker.on("tick", this.stage);
    var line = new createjs.Shape();
    line.graphics.beginStroke("black").setStrokeStyle(12).moveTo(LocationSpanComponent.centerPosition.x, LocationSpanComponent.centerPosition.y);
    line.graphics.lineTo(lineX, lineY);
    // var cmd = line.graphics.lineTo(LocationSpanComponent.centerPosition.x, LocationSpanComponent.centerPosition.y).command;
    // createjs.Tween.get(cmd).to({x: lineX, y: lineY }, speed);

    // var triangle = new createjs.Shape();
    // triangle.graphics.beginFill("black").drawPolyStar(LocationSpanComponent.centerPosition.x, LocationSpanComponent.centerPosition.y, 30, 3, 0, 
    //   LocationSpanComponent.circlePositions[index].degree - 90);
    // createjs.Tween.get(triangle).to(
    //   {x: (LocationSpanComponent.circlePositions[index].x - LocationSpanComponent.centerPosition.x) * 0.8, 
    //     y: (LocationSpanComponent.circlePositions[index].y - LocationSpanComponent.centerPosition.y) * 0.8 }, 
    //     speed);
    
    var triangle = new createjs.Shape();
    triangle.graphics.beginFill("black").drawPolyStar(lineX, lineY, 30, 3, 0, LocationSpanComponent.circlePositions[index].degree - 90);

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
      this.showArrow(event.target.id, LocationSpanComponent.animationConfig.moveDuration, LocationSpanComponent.arrowOnIntervalAfterClick);
      setTimeout(() => {
        this.numOfTouch ++;
        this.lsUserInput.push(event.target.id + 1);
        if(this.numOfTouch >= this.currentList.length){
          //send data
          this.endTime = new Date().getTime();
          if(this.gameMode === "practice_trial"){
            this.sendData(this.showFeedback, "practice");
          }else{
            this.sendData(this.goToNextTrial);
          }
        }else{
          this.enableDotTouch = true;
        }
      }, LocationSpanComponent.animationConfig.moveDuration + LocationSpanComponent.arrowOnIntervalAfterClick);
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

  private sendData(callback, trialType?){
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
      formData["indexOfTrial"] = this.indexOfTrial;
      formData["currentListIndex"] = this.realTrialIndex;
      formData["consecutiveTrialResult"] = this.trialResultsInSameListLength;
    }

    this.dataRecordService.sendUserData("./api/locationspan", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          this.rocks = result["rocks"];
          this.coins = result["coins"];
          setTimeout(() => {
            callback();
          }, LocationSpanComponent.trialInterval);
          // callback();
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
  }

  private goToNextTrial = () => {
    //update
    this.updateTrialsResultInSameListLength();
    this.enableDotTouch = false;
    this.numOfTouch = 0;
    this.lsUserInput = [];    
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= LocationSpanComponent.realTrialList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > LocationSpanComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentList = LocationSpanComponent.realTrialList[this.realTrialIndex];
      this.dotContainer.alpha = 0;
      this.showArrows();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/locationspan/gameover", formData, () => {}).subscribe(
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
        if(this.lsUserInput[i] === this.currentList[i]){
          hasDigitCorrect = true;
          break;
        }
      }
      this.trialResultsInSameListLength[this.indexOfTrial - 1] = hasDigitCorrect ? 1: 0;
    }
  }

  private showFeedback = () => {
    //check the last answer correct
    let userAnswerCorrect = true; 
    if(this.lsUserInput.length != this.currentList.length){
      userAnswerCorrect = false;
    }else{
      for(let i = 0; i < this.lsUserInput.length; i++){
        if(this.lsUserInput[i] != this.currentList[i]){
          userAnswerCorrect = false;
          break;
        }
      }
    }

    this.enableDotTouch = false;
    this.dotContainer.alpha = 0;
    this.stage.update();
    //play feedback video
    document.getElementById("circles-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      if(this.practiceIndex == 0){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else if(this.practiceIndex == 1){
        this.playPracticeFeedbackVideos(["correct_great_job_touch"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job_earn_coins", "try_your_best", "dont_forget"]);
      }
    }else{
      if(this.practiceIndex == 0){
        this.playPracticeFeedbackVideos(["incorrect_oops"]);
      }else if(this.practiceIndex == 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_remember"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops_not_point", "earn_coins", "try_your_best", "dont_forget"]);
      }
    }
  }

  private goToNextPracticeTrial = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeTrial);
    document.getElementById("feedback-video-div").style.display = "none";
    this.enableDotTouch = false;
    this.numOfTouch = 0;
    this.lsUserInput = [];
    this.checkData = false;

    this.practiceIndex++;
    
    if(this.practiceIndex < LocationSpanComponent.practiceTrialList.length){
      this.currentList = LocationSpanComponent.practiceTrialList[this.practiceIndex];
      document.getElementById("circles-div").style.display = "block";
      this.showArrows();
    }else{
      this.gameMode = "real_trial";
      this.startRealTrials();
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", LocationSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", LocationSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    let videoSource = LocationSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]];
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
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial);
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/locationspan/progress", formData, () => {}).subscribe(
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
    this.httpService.post("./api/locationspan/syncdata", formData).subscribe(
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
    for(var i = 0; i < LocationSpanComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", LocationSpanComponent.videoSource[i], true);
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
    "../../../assets/videos/location-span/Intro_1.mp4",
    "../../../assets/videos/location-span/Intro_2.mp4",
    "../../../assets/videos/location-span/Intro_3.mp4",
    "../../../assets/videos/location-span/Intro_4.mp4",
    "../../../assets/videos/location-span/Intro_5.mp4",
    "../../../assets/videos/location-span/Intro_6.mp4",
    "../../../assets/videos/location-span/Intro_7.mp4",
    "../../../assets/videos/location-span/Intro_8.mp4",
    "../../../assets/videos/location-span/Intro_9.mp4",
    "../../../assets/videos/location-span/Intro_10.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/location-span/Feedback_Great_Job.mp4",
    "correct_great_job_touch" : "../../../assets/videos/location-span/Feedback_Great_Job_Touch.mp4",
    "correct_great_job_earn_coins" : "../../../assets/videos/location-span/Feedback_Great_Job_Earn_Coins.mp4",
    "incorrect_oops": "../../../assets/videos/location-span/Feedback_Oops.mp4",
    "incorrect_oops_remember": "../../../assets/videos/location-span/Feedback_Oops_Remember.mp4",
    "incorrect_oops_not_point": "../../../assets/videos/location-span/Feedback_Oops_Not_Point.mp4",
    "try_your_best": "../../../assets/videos/location-span/Feedback_Try_Your_Best.mp4",
    "dont_forget": "../../../assets/videos/location-span/Feedback_Dont_Forget.mp4",
    "earn_coins": "../../../assets/videos/location-span/Feedback_Earn_Coins.mp4",
    "ending1": "../../../assets/videos/location-span/Ending_Great_Job.mp4"
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
    "../../../assets/videos/location-span/Intro_9.mp4",
    "../../../assets/videos/location-span/Intro_10.mp4",
    "../../../assets/videos/location-span/Feedback_Great_Job.mp4",
    "../../../assets/videos/location-span/Feedback_Oops.mp4",
    "../../../assets/videos/location-span/Feedback_Try_Some_More.mp4",
    "../../../assets/videos/location-span/Feedback_Do_Something_Else.mp4",
    "../../../assets/videos/location-span/Ending_1.mp4"
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
  private static trialInterval = 1000;

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static practiceTrialList = [
    [1],
    [3, 6],
    [4, 8]
  ];
  private static realTrialList = [
    [6, 8],
    [1, 6],
    [8, 5],
    [1, 3],
    [8, 4, 6],
    [3, 7, 4],
    [2, 4, 1],
    [1, 6, 2],
    [2, 8, 5, 1],
    [3, 6, 2, 7],
    [2, 4, 1, 6],
    [5, 7, 4, 6],
    [8, 6, 1, 7, 2],
    [6, 2, 8, 1, 3],
    [2, 5, 3, 7, 4],
    [5, 8, 4, 6, 1],
    [8, 4, 2, 5, 3, 6],
    [4, 7, 3, 1, 5, 8],
    [8, 5, 3, 7, 4, 6],
    [7, 5, 1, 6, 8, 3]
  ];
}