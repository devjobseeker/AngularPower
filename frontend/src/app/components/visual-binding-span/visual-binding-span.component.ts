/* NOT USE */
import { Component, OnInit } from '@angular/core';
import * as createjs from 'createjs-module';
import { trigger, transition, animate, style } from '@angular/animations';

import {DataRecordService} from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

@Component({
  selector: 'app-visual-binding-span',
  templateUrl: './visual-binding-span.component.html',
  styleUrls: ['./visual-binding-span.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class VisualBindingSpanComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private randomService: RandomNumberService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //init variables
    this.indexOfVideo = 0;
    this.checkData = false;
    this.indexOfTrial = 1;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.realTrialIndex = 0;
    this.resetUserInput();

    this.loadSrcQueue();
    this.gameMode = "start";

    this.initSyncData();
  }

  //public variables
  gameMode: String;
  coins;
  rocks;

  //private variables
  private indexOfVideo;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private trialResultsInSameListLength;
  private currentPolygonList: number[];
  private currentLocationList: number[];
  //private currentListLength;
  private userLocationInput: number[] =[];
  private userPolygonInput: number[] =[];
  private stimuliInput: number[] = [];  //length = 16
  private realTrialIndex: number;
  private checkData;
  private endTime;
  private startTime;
  private practiceListIndex;
  private practiceFeedbackVideoNames;
  private allChoices: number[] = [];

  //canvas variables
  private stage;
  private queue: createjs.LoadQueue;
  private canvasScale;
  private locations: String[] = []; //save locations names
  private boxes: String[] = []; //save boxes names
  private polygonContainers: String[] = [];
  private initialPolygonsPos = [];
  private gameBoardPos = [];  //save each box x and y in game board

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Practice"
    }
    this.gameProgressService.updateGameProgress("./api/visualbinding/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.currentLocationList = VisualBindingSpanComponent.practiceLocationList[this.practiceListIndex];
          this.currentPolygonList = VisualBindingSpanComponent.practicePolygonList[this.practiceListIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            this.initGame();
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
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Progress"
    }
    this.gameProgressService.updateGameProgress("./api/visualbinding/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          this.currentLocationList = VisualBindingSpanComponent.realLocationList[this.realTrialIndex];
          this.currentPolygonList = VisualBindingSpanComponent.realPolygonList[this.realTrialIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            document.getElementById("game-canvas").style.display = "block";
            document.getElementById("next-trial-btn").style.display = "none";
            if(this.stage == undefined || this.stage == null){
              this.initGame();
            }else{
              setTimeout( () => { 
                this.showPolygons();
              }, 1000);
            }
            
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
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanComponent.introVideoSource[0]);
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

  clickNextBtn(): void{
    //remove all polygons from polygonContainer
    for(let i = 0; i < this.polygonContainers.length; i++){
      this.stage.removeChild(this.stage.getChildByName(this.polygonContainers[i]));
    }
    this.stage.update();
    //send data
    this.endTime = new Date().getTime();
    if(this.gameMode === "practice_trial"){
      this.sendData(this.showFeedback, "practice");
      // setTimeout(() => {
      //   this.showFeedback();
      // }, 500);
    }else{
      this.sendData(this.goToNextTrial);
    }
  }

  private initGame = () => {
    this.stage = new createjs.Stage("game-canvas");
    this.resizeCanvas();
    createjs.Touch.enable(this.stage);

    this.drawGameBoard();
    setTimeout( () => {
      this.showPolygons();
    }, 1000);

    window.addEventListener("resize", this.resizeCanvas);
  }

  private drawGameBoard(){
    this.locations = [];
    this.boxes = [];
    this.gameBoardPos = [];
    for(let i = 1; i <= 16; i++){
      let box = new createjs.Shape();
      box.graphics.setStrokeStyle(5).beginStroke("#990000").rect(0, 0, VisualBindingSpanComponent.squareLen, VisualBindingSpanComponent.squareLen);
      box.name = "box-" + i;
      let location = new createjs.Container();

      if(i % 4 == 1){
        location.x = VisualBindingSpanComponent.boardStartPos.x;
        location.y = VisualBindingSpanComponent.boardStartPos.y + VisualBindingSpanComponent.squareLen * Math.floor(i / 4);
      }else if(i % 4 == 2){
        location.x = VisualBindingSpanComponent.boardStartPos.x + VisualBindingSpanComponent.squareLen;
        location.y = VisualBindingSpanComponent.boardStartPos.y + VisualBindingSpanComponent.squareLen * Math.floor(i / 4);
      }else if(i % 4 == 3){
        location.x = VisualBindingSpanComponent.boardStartPos.x + VisualBindingSpanComponent.squareLen * 2;
        location.y = VisualBindingSpanComponent.boardStartPos.y + VisualBindingSpanComponent.squareLen * Math.floor(i / 4);
      }else if(i % 4 == 0){
        location.x = VisualBindingSpanComponent.boardStartPos.x + VisualBindingSpanComponent.squareLen * 3;
        location.y = VisualBindingSpanComponent.boardStartPos.y + VisualBindingSpanComponent.squareLen * Math.floor( (i - 1) / 4);
      } 
      location.setBounds(location.x, location.y, VisualBindingSpanComponent.squareLen, VisualBindingSpanComponent.squareLen);
      location.addChild(box);
      location.name = "location-" + i;
      this.boxes.push(box.name);
      this.gameBoardPos.push({x: location.x, y: location.y});
      this.locations.push(location.name);
      this.stage.addChild(location);
    }

    this.stage.update();
  }

  private showPolygons(){
    let prefix = this.gameMode == "practice_trial" ? "p" : "t";
    for(let i = 0; i < this.currentPolygonList.length; i++){
      setTimeout(() => {
        let polygon = new createjs.Bitmap(this.queue.getResult(prefix + this.currentPolygonList[i]));
        polygon.x = this.gameBoardPos[this.currentLocationList[i] - 1].x;
        polygon.y = this.gameBoardPos[this.currentLocationList[i] - 1].y;
        this.stage.addChild(polygon);
        this.stage.update();

        setTimeout(() => {
          this.stage.removeChild(polygon);
          this.stage.update();

          if(i == this.currentPolygonList.length - 1){
            this.showPolygonOptions()
          }
        }, 1000);
      }, 2000 * i);
      
    }
  }

  private showPolygonOptions(){
    //draw 8 polygons.
    //randomize the order from 1 to 8
    document.getElementById("next-trial-btn").style.display = "block";
    this.polygonContainers = [];
    this.randomizePolygonChoices(8);
    let prefix = this.gameMode == "practice_trial" ? "p" : "t";
    for(let i = 0; i < this.allChoices.length; i++){
      let polygon = new createjs.Bitmap(this.queue.getResult(prefix + this.allChoices[i]));
      let container = new createjs.Container();
      container.y = VisualBindingSpanComponent.boardStartPos.y + VisualBindingSpanComponent.squareLen * Math.floor(i / 2);
      if(i % 2 == 0){
        container.x = VisualBindingSpanComponent.boardStartPos.x + 4 * VisualBindingSpanComponent.squareLen;
      }else{
        container.x = VisualBindingSpanComponent.boardStartPos.x + 5 * VisualBindingSpanComponent.squareLen;
      }
      this.initialPolygonsPos[this.allChoices[i]] = {x: container.x, y: container.y};
      container.name = "container-" + this.allChoices[i];
      this.polygonContainers.push(container.name);
      container.addChild(polygon);
      container.setBounds(polygon.x, polygon.y, VisualBindingSpanComponent.squareLen, VisualBindingSpanComponent.squareLen);
      this.stage.addChild(container);

      container.addEventListener("pressmove", this.dragPolygon);

      container.addEventListener("pressup", this.dropPolygon);
    }
    this.stage.update();
    this.startTime = new Date().getTime();
    window.addEventListener("keydown", this.pressKeyboard);
  }

  //return index in location
  private overlapInGameBoard(obj1: createjs.Container, objList: String[]){
    for(let i = 0; i < objList.length; i++){
      let location: createjs.Container = this.stage.getChildByName(objList[i]);
      if(location != null && location != undefined && this.overlap(obj1, location)){
        return i;
      }
    }
    return -1;
  }

  private overlap(obj1: createjs.Container, obj2: createjs.Container){
    var bound1 = obj1.getBounds().clone();
    var bound2 = obj2.getBounds().clone();

    if(obj1.x >= bound2.x + bound2.width / 2 || obj1.x + bound1.width <= bound2.x + bound2.width / 2 
      || obj1.y >= bound2.y + bound2.height / 2 || obj1.y + bound1.height <= bound2.y + bound2.height / 2){
        return false;
    }
    return true;
  }

  private dragPolygon = (evt: createjs.MouseEvent) => {
    //evt.target is createjs.Shape. evt.currentTarget is createjs.Container
    evt.currentTarget.x = evt.stageX / this.canvasScale - VisualBindingSpanComponent.squareLen / 2 * this.canvasScale;
    evt.currentTarget.y = evt.stageY / this.canvasScale - VisualBindingSpanComponent.squareLen / 2 * this.canvasScale;
    this.stage.update();
    let index = this.overlapInGameBoard(evt.currentTarget, this.locations);
    if(index >= 0){
      //evt.currentTarget.alpha = 0.2;
      //get box by name
      // let box: createjs.Shape = this.stage.getChildByName(this.boxes[index]);
      // box.graphics.clear();
      // box.graphics.setStrokeStyle(2).beginStroke("yellow").rect(0, 0, 100, 100);
    }else{
      //evt.currentTarget.alpha = 1;
      //this.boxes[locationIndex].graphics.clear();
      //this.boxes[locationIndex].graphics.setStrokeStyle(2).beginStroke("yellow").rect(0, 0, 100, 100);
    }
  }

  private dropPolygon = (evt: createjs.MouseEvent) => {
    let index = this.overlapInGameBoard(evt.currentTarget, this.locations);
    if(index >= 0){
      let location: createjs.Container = this.stage.getChildByName(this.locations[index]);
      evt.currentTarget.x = location.x;
      evt.currentTarget.y = location.y;

      // let box: createjs.Shape = this.stage.getChildByName(this.boxes[index]);
      // box.alpha = 1;
      // box.graphics.clear();
      // box.graphics.setStrokeStyle(2).beginStroke("black").rect(0, 0, 100, 100);
      //console.log(this.boxes[locationIndex].name);
      //location.alpha = 1;
      evt.currentTarget.removeEventListener("pressmove", this.dragPolygon);
      evt.currentTarget.removeEventListener("pressup", this.dropPolygon);
      let name = evt.currentTarget.name;
      //console.log(name);
      let polyIndex = parseInt(name.split("-")[1]);
      this.userLocationInput.push(index + 1);
      this.userPolygonInput.push(polyIndex);
      //this.userInput[index] = polyIndex;
      //console.log(this.userInput);
      //console.log(this.stimuliInput);
    }else{
      //get polygon index from current target name
      let name = evt.currentTarget.name;
      
      let polyIndex = parseInt(name.split("-")[1]);
      evt.currentTarget.x = this.initialPolygonsPos[polyIndex].x;
      evt.currentTarget.y = this.initialPolygonsPos[polyIndex].y;
    }
    evt.currentTarget.alpha = 1;
    this.stage.update(evt);
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userLocationInput": this.userLocationInput,
      "userPolygonInput": this.userPolygonInput,
      "stimuliLocationInput": this.currentLocationList,
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

    this.dataRecordService.sendUserData("./api/visualbinding", formData, () => {}).subscribe(
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
    this.resetUserInput();
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    if(this.practiceListIndex < VisualBindingSpanComponent.practiceLocationList.length){
      document.getElementById("game-canvas").style.display = "block";
      setTimeout( () => {
        this.currentLocationList = VisualBindingSpanComponent.practiceLocationList[this.practiceListIndex];
        this.currentPolygonList = VisualBindingSpanComponent.practicePolygonList[this.practiceListIndex];
        this.generateStimuliInput();
        this.showPolygons();
      }, 1000);
    }else{
      //go to real trial
      this.startRealTrials();
    }
  }

  private goToNextTrial = () => {
    //update
    this.updateTrialsResultInSameListLength();
    this.resetUserInput();
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= VisualBindingSpanComponent.realLocationList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > VisualBindingSpanComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentLocationList = VisualBindingSpanComponent.realLocationList[this.realTrialIndex];
      this.currentPolygonList = VisualBindingSpanComponent.realPolygonList[this.realTrialIndex];
      this.generateStimuliInput();
      document.getElementById("game-canvas").style.display = "block";
      document.getElementById("next-trial-btn").style.display = "none";
      setTimeout( () => {
        this.showPolygons();
      }, 1000);
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": this.gameMode
    }
    this.gameProgressService.updateGameProgress("./api/visualbinding/gameover", formData, () => {}).subscribe(
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

  private showFeedback = () => {
    let userAnswerCorrect = true;
    let userInput = this.generateUserInput();
    for(let i = 0; i < userInput.length; i++){
      if(userInput[i] != this.stimuliInput[i]){
        userAnswerCorrect = false;
        break;
      }
    }
    
    document.getElementById("game-canvas").style.display = "none";
    document.getElementById("next-trial-btn").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < VisualBindingSpanComponent.practiceLocationList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job_earn_coins"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex < VisualBindingSpanComponent.practiceLocationList.length - 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_try_again"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops", "do_some_more"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial); 
    }
  }

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < VisualBindingSpanComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", VisualBindingSpanComponent.introVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextIntroVideo);
      document.getElementById("next-btn").style.display = "block";
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
    this.gameProgressService.fetchGameProgress("./api/visualbinding/progress", formData, () => {}).subscribe(
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
    this.httpService.post("./api/visualbinding/syncdata", formData).subscribe(
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

  private loadSrcQueue(){
    this.queue = new createjs.LoadQueue();
    this.queue.loadManifest([
      {id: "p1", src: "../../../assets/images/visual-binding/practice/1.png"},
      {id: "p2", src: "../../../assets/images/visual-binding/practice/2.png"},
      {id: "p3", src: "../../../assets/images/visual-binding/practice/3.png"},
      {id: "p4", src: "../../../assets/images/visual-binding/practice/4.png"},
      {id: "p5", src: "../../../assets/images/visual-binding/practice/5.png"},
      {id: "p6", src: "../../../assets/images/visual-binding/practice/6.png"},
      {id: "p7", src: "../../../assets/images/visual-binding/practice/7.png"},
      {id: "p8", src: "../../../assets/images/visual-binding/practice/8.png"},
      {id: "t1", src: "../../../assets/images/visual-binding/real/1.png"},
      {id: "t2", src: "../../../assets/images/visual-binding/real/2.png"},
      {id: "t3", src: "../../../assets/images/visual-binding/real/3.png"},
      {id: "t4", src: "../../../assets/images/visual-binding/real/4.png"},
      {id: "t5", src: "../../../assets/images/visual-binding/real/5.png"},
      {id: "t6", src: "../../../assets/images/visual-binding/real/6.png"},
      {id: "t7", src: "../../../assets/images/visual-binding/real/7.png"},
      {id: "t8", src: "../../../assets/images/visual-binding/real/8.png"},
    ]);
  }

  private generateStimuliInput():number[]{
    //init with 0
    for(let i = 0; i < VisualBindingSpanComponent.boardCount; i++){
      this.stimuliInput[i] = 0;
    }
    //assign values
    for(let i = 0; i < this.currentLocationList.length; i++){
      this.stimuliInput[this.currentLocationList[i] - 1] = this.currentPolygonList[i];
    }
    return this.stimuliInput;
  }

  private generateUserInput():number[]{
    let userInput = [];
    for(let i = 0; i < VisualBindingSpanComponent.boardCount; i++){
      userInput[i] = 0;
    }
    for(let i = 0; i < this.userLocationInput.length; i++){
      userInput[this.userLocationInput[i] - 1] = this.userPolygonInput[i];
    }
    return userInput;
  }

  private resetUserInput(){
    this.userLocationInput = [];
    this.userPolygonInput = [];
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

  private resizeCanvas = (event?) => {
    let scaleW = window.innerWidth / VisualBindingSpanComponent.canvasSize.w;
    let scaleH = window.innerHeight / VisualBindingSpanComponent.canvasSize.h;
    document.getElementById("game-canvas").setAttribute("width","1000")
    document.getElementById("game-canvas").setAttribute("height",0.9 * window.innerHeight + "");
    this.canvasScale = scaleW < scaleH ? scaleW : scaleH;
    if(this.canvasScale < 1){
      //this.stage.canvas.width = VisualBindingSpanComponent.canvasSize.w * this.canvasScale;
      //this.stage.canvas.height = VisualBindingSpanComponent.canvasSize.h * this.canvasScale;
      this.stage.scaleX = this.stage.scaleY = this.canvasScale;
      this.stage.update();
    }else{
      this.canvasScale = 1;
    }
  }

  private overlap1(obj1: createjs.Container, obj2: createjs.Container){
    var bound1 = obj1.getBounds().clone();
    var bound2 = obj2.getBounds().clone();
    var pt = obj1.globalToLocal(bound2.x, bound2.y);

    var h1 =  -(bound1.height / 2 + bound2.height);
    var h2 = bound2.height / 2;
    var w1 = -(bound1.width / 2 + bound2.width);
    var w2 = bound2.width / 2;

    if(pt.x > w2 || pt.x < w1) return false;
    if(pt.y > h2 || pt.y < h1) return false;
    
    return true;
  }

  //consts
  
  private static introVideoSource = [
    "../../../assets/videos/visual-binding/Intro_1.mp4",
    "../../../assets/videos/visual-binding/Intro_2.mp4",
    "../../../assets/videos/visual-binding/Intro_3.mp4",
    "../../../assets/videos/visual-binding/Intro_4.mp4",
    "../../../assets/videos/visual-binding/Intro_5.mp4",
    "../../../assets/videos/visual-binding/Intro_6.mp4",
    "../../../assets/videos/visual-binding/Intro_7.mp4",
    "../../../assets/videos/visual-binding/Intro_8.mp4",
    "../../../assets/videos/visual-binding/Intro_9.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/visual-binding/Feedback_Great_Job.mp4",
    "correct_great_job_earn_coins": "../../../assets/videos/visual-binding/Feedback_Great_Job_Earn_Coins.mp4",
    "do_some_more": "../../../assets/videos/visual-binding/Feedback_Do_Some_More.mp4",
    "incorrect_oops": "../../../assets/videos/visual-binding/Feedback_Ooh.mp4",
    "incorrect_oops_try_again": "../../../assets/videos/visual-binding/Feedback_Ooh_Try_Again.mp4",
    "ending_great": "../../../assets/videos/visual-binding/Ending.mp4"
  };
  private static videoSource = [
    "../../../assets/videos/visual-binding/Intro_1.mp4",
    "../../../assets/videos/visual-binding/Intro_2.mp4",
    "../../../assets/videos/visual-binding/Intro_3.mp4",
    "../../../assets/videos/visual-binding/Intro_4.mp4",
    "../../../assets/videos/visual-binding/Intro_5.mp4",
    "../../../assets/videos/visual-binding/Intro_6.mp4",
    "../../../assets/videos/visual-binding/Intro_7.mp4",
    "../../../assets/videos/visual-binding/Intro_8.mp4",
    "../../../assets/videos/visual-binding/Intro_9.mp4",
    "../../../assets/videos/visual-binding/Feedback_Great_Job.mp4",
    "../../../assets/videos/visual-binding/Feedback_Ooh.mp4",
    "../../../assets/videos/visual-binding/Ending.mp4",
  ];

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static practicePolygonList = [
    [8],
    [3, 1],
    [5, 8]
  ];
  private static practiceLocationList = [
    [10],
    [16, 6],
    [8, 5]
  ];
  private static realPolygonList = [
    [3, 6],
    [1, 4],
    [2, 7],
    [4, 5],
    [7, 1, 6],
    [3, 4, 5],
    [2, 7, 8],
    [4, 3, 2],
    [1, 6, 7, 5],
    [4, 3, 8, 2],
    [5, 6, 1, 8],
    [3, 2, 4, 7]
  ];
  private static realLocationList = [
    [12, 14],
    [10, 1],
    [14, 5],
    [12, 13],
    [7, 15, 5],
    [10, 4, 13],
    [14, 8, 1],
    [15, 7, 9],
    [12, 9, 11, 4],
    [16, 6, 8, 15],
    [6, 12, 3, 11],
    [4, 5, 7, 1]
  ];

  //private static listLengthTrialOrdinal = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4 ];

  //canvas setting
  private static squareLen = 150;
  private static boardStartPos = {x: 100, y: 50};
  private static canvasSize = {w: 1000, h: 780};
  private static boardCount = 16;
}
