//This is the version currently using.
import { Component, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import { trigger, transition, animate, style } from '@angular/animations';

import {DataRecordService} from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-visual-binding-span-v2',
  templateUrl: './visual-binding-span-v2.component.html',
  styleUrls: ['./visual-binding-span-v2.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class VisualBindingSpanV2Component implements OnInit {

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
  // gems = [];
  gameBlock1 = [];
  gameBlock2 = [];
  gameBlock3 = [];
  gameBlock4 = [];
  gameBlock5 = [];
  gameBlock6 = [];
  gameBlock7 = [];
  gameBlock8 = [];
  gameBlock9 = [];
  gameBlock10 = [];
  gameBlock11 = [];
  gameBlock12 = [];
  gameBlock13 = [];
  gameBlock14 = [];
  gameBlock15 = [];
  gameBlock16 = [];

  gem1 = [];
  gem2 = [];
  gem3 = [];
  gem4 = [];
  gem5 = [];
  gem6 = [];
  gem7 = [];
  gem8 = [];

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
          this.currentLocationList = VisualBindingSpanV2Component.practiceLocationList[this.practiceListIndex];
          this.currentPolygonList = VisualBindingSpanV2Component.practicePolygonList[this.practiceListIndex];
          this.generateStimuliInput();
          setTimeout( () => {
            this.showPolygons();
          }, 1000);
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
          this.currentLocationList = VisualBindingSpanV2Component.realLocationList[this.realTrialIndex];
          this.currentPolygonList = VisualBindingSpanV2Component.realPolygonList[this.realTrialIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            document.getElementById("game-board-div").style.display = "flex";
            document.getElementById("next-trial-btn").style.display = "none";
            setTimeout( () => {
              this.showPolygons();
            }, 1000);
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
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanV2Component.introVideoSource[0]);
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
    //send data
    this.endTime = new Date().getTime();
    if(this.gameMode === "practice_trial"){
      this.sendData(this.showFeedback, "practice");
    }else{
      this.sendData(this.goToNextTrial);
    }

    //remove all polygons from polygonContainer
    // for(let i = 0; i < this.polygonContainers.length; i++){
    //   this.stage.removeChild(this.stage.getChildByName(this.polygonContainers[i]));
    // }
  }

  dropBackGem(event: CdkDragDrop<string[]>){
    if(event.previousContainer !== event.container && event.container.data.length == 0){
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex)
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    if(event.previousContainer !== event.container && event.container.data.length == 0){
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }

    // if (event.previousContainer === event.container) {
    //   // moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    //   moveItemInArray(event.container.data, event.previousIndex, event.previousIndex);
    // } else {
    //   //console.log(event.item);
    //   console.log(event.container.data.length);
    //   transferArrayItem(event.previousContainer.data,
    //                     event.container.data,
    //                     event.previousIndex,
    //                     event.currentIndex);
    // }
  }

  private showPolygons = () => {
    for(let i = 0; i < this.currentPolygonList.length; i++){
      setTimeout(() => {
        if(this.currentLocationList[i] === 1){
          this.gameBlock1 = [];
          this.gameBlock1.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 2){
          this.gameBlock2 = [];
          this.gameBlock2.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 3){
          this.gameBlock3 = [];
          this.gameBlock3.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 4){
          this.gameBlock4 = [];
          this.gameBlock4.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 5){
          this.gameBlock5 = [];
          this.gameBlock5.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 6){
          this.gameBlock6 = [];
          this.gameBlock6.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 7){
          this.gameBlock7 = [];
          this.gameBlock7.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 8){
          this.gameBlock8 = [];
          this.gameBlock8.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 9){
          this.gameBlock9 = [];
          this.gameBlock9.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 10){
          this.gameBlock10 = [];
          this.gameBlock10.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 11){
          this.gameBlock11 = [];
          this.gameBlock11.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 12){
          this.gameBlock12 = [];
          this.gameBlock12.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 13){
          this.gameBlock13 = [];
          this.gameBlock13.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 14){
          this.gameBlock14 = [];
          this.gameBlock14.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 15){
          this.gameBlock15 = [];
          this.gameBlock15.push(this.currentPolygonList[i]);
        }else if(this.currentLocationList[i] === 16){
          this.gameBlock16 = [];
          this.gameBlock16.push(this.currentPolygonList[i]);
        }

        setTimeout(() => {
          this.emptyGameBlocks();

          if(i == this.currentPolygonList.length - 1){
            this.showPolygonOptions()
          }
        }, 1000);
      }, 2000 * i);
    }

    //this.initGameBlocks();

    // setTimeout( () => {
    //   this.showPolygons();
    // }, 1000);
  }

  private showPolygonOptions(){
    document.getElementById("next-trial-btn").style.display = "block";
    this.randomizePolygonChoices(8);
    this.gem1.push(this.allChoices[0]);
    this.gem2.push(this.allChoices[1]);
    this.gem3.push(this.allChoices[2]);
    this.gem4.push(this.allChoices[3]);
    this.gem5.push(this.allChoices[4]);
    this.gem6.push(this.allChoices[5]);
    this.gem7.push(this.allChoices[6]);
    this.gem8.push(this. allChoices[7]);
    // this.gems = this.allChoices;
    this.startTime = new Date().getTime();

    window.addEventListener("keydown", this.pressKeyboard);
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    //generate user polygon input and user location input
    this.generateUserLocationPolygon();
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

  private generateUserLocationPolygon(){
    this.userLocationInput = [];
    this.userPolygonInput = [];
    if(this.gameBlock1.length > 0){
      this.userLocationInput.push(1);
      this.userPolygonInput.push(this.gameBlock1[0]);
    }
    if(this.gameBlock2.length > 0){
      this.userLocationInput.push(2);
      this.userPolygonInput.push(this.gameBlock2[0]);
    }
    if(this.gameBlock3.length > 0){
      this.userLocationInput.push(3);
      this.userPolygonInput.push(this.gameBlock3[0]);
    }
    if(this.gameBlock4.length > 0){
      this.userLocationInput.push(4);
      this.userPolygonInput.push(this.gameBlock4[0]);
    }
    if(this.gameBlock5.length > 0){
      this.userLocationInput.push(5);
      this.userPolygonInput.push(this.gameBlock5[0]);
    }
    if(this.gameBlock6.length > 0){
      this.userLocationInput.push(6);
      this.userPolygonInput.push(this.gameBlock6[0]);
    }
    if(this.gameBlock7.length > 0){
      this.userLocationInput.push(7);
      this.userPolygonInput.push(this.gameBlock7[0]);
    }
    if(this.gameBlock8.length > 0){
      this.userLocationInput.push(8);
      this.userPolygonInput.push(this.gameBlock8[0]);
    }
    if(this.gameBlock9.length > 0){
      this.userLocationInput.push(9);
      this.userPolygonInput.push(this.gameBlock9[0]);
    }
    if(this.gameBlock10.length > 0){
      this.userLocationInput.push(10);
      this.userPolygonInput.push(this.gameBlock10[0]);
    }
    if(this.gameBlock11.length > 0){
      this.userLocationInput.push(11);
      this.userPolygonInput.push(this.gameBlock11[0]);
    }
    if(this.gameBlock12.length > 0){
      this.userLocationInput.push(12);
      this.userPolygonInput.push(this.gameBlock12[0]);
    }
    if(this.gameBlock13.length > 0){
      this.userLocationInput.push(13);
      this.userPolygonInput.push(this.gameBlock13[0]);
    }
    if(this.gameBlock14.length > 0){
      this.userLocationInput.push(14);
      this.userPolygonInput.push(this.gameBlock14[0]);
    }
    if(this.gameBlock15.length > 0){
      this.userLocationInput.push(15);
      this.userPolygonInput.push(this.gameBlock15[0]);
    }
    if(this.gameBlock16.length > 0){
      this.userLocationInput.push(16);
      this.userPolygonInput.push(this.gameBlock16[0]);
    }
  }

  private goToNextPracticeTrial = (event) => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeTrial);
    this.emptyGameBlocks();
    this.resetUserInput();
    // this.gems = [];
    this.emptyGems();
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    if(this.practiceListIndex < VisualBindingSpanV2Component.practiceLocationList.length){
      document.getElementById("game-board-div").style.display = "flex";
      setTimeout( () => {
        this.currentLocationList = VisualBindingSpanV2Component.practiceLocationList[this.practiceListIndex];
        this.currentPolygonList = VisualBindingSpanV2Component.practicePolygonList[this.practiceListIndex];
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
    this.emptyGameBlocks();
    this.resetUserInput();
    this.emptyGems();
    // this.gems = [];
    this.checkData = false;

    this.realTrialIndex++;
    this.indexOfTrial++;

    //check the 4 answer in the same list length
    if(this.realTrialIndex >= VisualBindingSpanV2Component.realLocationList.length || !this.trialResultsInSameListLength.includes(1)){
      this.endGame();
    }else{
      if(this.indexOfTrial > VisualBindingSpanV2Component.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }

      this.currentLocationList = VisualBindingSpanV2Component.realLocationList[this.realTrialIndex];
      this.currentPolygonList = VisualBindingSpanV2Component.realPolygonList[this.realTrialIndex];
      this.generateStimuliInput();
      document.getElementById("game-board-div").style.display = "flex";
      document.getElementById("next-trial-btn").style.display = "none";
      setTimeout( () => {
        this.showPolygons();
      }, 1000 + 1000);
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
    
    document.getElementById("game-board-div").style.display = "none";
    document.getElementById("next-trial-btn").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < VisualBindingSpanV2Component.practiceLocationList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job_earn_coins"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex < VisualBindingSpanV2Component.practiceLocationList.length - 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_try_again"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops", "do_some_more"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanV2Component.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", VisualBindingSpanV2Component.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
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
    if(this.indexOfVideo < VisualBindingSpanV2Component.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", VisualBindingSpanV2Component.introVideoSource[this.indexOfVideo]);
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

  private generateStimuliInput():number[]{
    //init with 0
    for(let i = 0; i < VisualBindingSpanV2Component.boardCount; i++){
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
    for(let i = 0; i < VisualBindingSpanV2Component.boardCount; i++){
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

  private emptyGameBlocks(){
    this.gameBlock1 = [];
    this.gameBlock2 = [];
    this.gameBlock3 = [];
    this.gameBlock4 = [];
    this.gameBlock5 = [];
    this.gameBlock6 = [];
    this.gameBlock7 = [];
    this.gameBlock8 = [];
    this.gameBlock9 = [];
    this.gameBlock10 = [];
    this.gameBlock11 = [];
    this.gameBlock12 = [];
    this.gameBlock13 = [];
    this.gameBlock14 = [];
    this.gameBlock15 = [];
    this.gameBlock16 = [];
  }

  private emptyGems(){
    this.gem1 = [];
    this.gem2 = [];
    this.gem3 = [];
    this.gem4 = [];
    this.gem5 = [];
    this.gem6 = [];
    this.gem7 = [];
    this.gem8 = [];
  }

  // gems = [
  //   "assets/images/visual-binding/real/1.png",
  //   "assets/images/visual-binding/real/2.png",
  //   "assets/images/visual-binding/real/3.png",
  //   "assets/images/visual-binding/real/4.png",
  //   "assets/images/visual-binding/real/5.png",
  //   "assets/images/visual-binding/real/6.png",
  //   "assets/images/visual-binding/real/7.png",
  // ]

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
  private static boardCount = 16;
}
