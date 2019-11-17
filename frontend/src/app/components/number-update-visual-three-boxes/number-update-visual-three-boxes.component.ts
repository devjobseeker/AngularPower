import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { AudioRecordService } from '../../services/utils/audio-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

@Component({
  selector: 'app-number-update-visual-three-boxes',
  templateUrl: './number-update-visual-three-boxes.component.html',
  styleUrls: ['./number-update-visual-three-boxes.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class NumberUpdateVisualThreeBoxesComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private audioRecordService: AudioRecordService,
    private dataRecordService: DataRecordService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    this.indexOfVideo = 0;
    this.checkData = false;
    this.userInput = [];
    this.lastUserInput = [];
    this.userInputInOneGroup = [];
    this.gameMode = "start";
    this.practiceListIndex = 1;
    this.practiceGroupIndex = 0;
    this.realGroupIndex = 0;
    this.realTrialIndex = 1;

    this.initSyncData();
  }

  //public variables
  gameMode: String;
  leftValue: String;
  rightValue: String;
  midValue: String;
  coins;
  rocks;

  //private variables
  private indexOfVideo;
  private currentList: number[];  //store current left and right value
  private currentIncrement: number[];
  private initialValue: number[];
  private practiceListIndex: number;  //initial starting value is 1
  private practiceGroupIndex: number; //initial starting value is 0
  private realTrialIndex: number; //initial starting value is 1
  private realGroupIndex: number; //initial starting value is 0
  private checkData;
  private startTime;
  private endTime;
  private totalCorrectCount;  //total real trial correct count
  private totalTrialCount;  //total real trial count
  private startRecording = false;
  private userInput: number[];
  private lastUserInput: number[];
  private userInputInOneGroup;

  private practiceFeedbackVideoNames;

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Practice"
    }
    this.gameProgressService.updateGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.initialValue = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value    
          this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
          this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][this.practiceListIndex];
          
          setTimeout(() => {
            this.leftValue = this.initialValue[0] + "";
            this.midValue = this.initialValue[1] + "";
            this.rightValue = this.initialValue[2] + "";

            setTimeout(() => {
              this.showNextIncrement();
              setTimeout(() => {
                this.readyToAnswer();
              }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
            }, NumberUpdateVisualThreeBoxesComponent.showInitialValuesDuration);
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
    if(this.realTrialIndex >= NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex].length){
      this.realGroupIndex++;
      this.realTrialIndex = 1;
    }
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Progress"
    }
    this.gameProgressService.updateGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          this.initialValue = NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex][0]; //the first pair in each group is the initial value
          this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
          this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex][this.realTrialIndex];
          document.getElementById("feedback-video-div").style.display = "none";
          document.getElementById("block-div").style.display = "block";
          setTimeout(() => {
            this.leftValue = this.initialValue[0] + "";
            this.midValue = this.initialValue[1] + "";
            this.rightValue = this.initialValue[2] + "";

            setTimeout(() => {
              this.showNextIncrement();
              setTimeout(() => {
                this.readyToAnswer();
              }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
            }, NumberUpdateVisualThreeBoxesComponent.showInitialValuesDuration);
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

  private readyToAnswer(){
    this.leftValue = "";
    this.rightValue = "";
    this.midValue = "";
    document.getElementById("left-span").style.borderColor = "green";
    document.getElementById("right-span").style.borderColor = "green";
    document.getElementById("mid-span").style.borderColor = "green";
    //start recording
    this.startRecord();
    this.startTime = Date.now();
    //add keydown event
    this.recordUserInputFromKeyboard();
  }

  private showNextIncrement(){
    this.leftValue = this.currentIncrement[0] == 0 ? "" : "+" + this.currentIncrement[0] + "";
    this.midValue = this.currentIncrement[1] == 0 ? "" : "+" + this.currentIncrement[1] + "";
    this.rightValue = this.currentIncrement[2] == 0 ? "" : "+" + this.currentIncrement[2] + "";
    document.getElementById("left-span").style.borderColor = "red";
    document.getElementById("right-span").style.borderColor = "red";
    document.getElementById("mid-span").style.borderColor = "red";
  }

  private startRecord(){
    this.audioRecordService.startRecord();
    this.startRecording = true;
  }

  private processRecord = (event) => {
    let keyCode = event.keyCode;
    //user press any number, or * will stop recording
    if((keyCode >= 97 && keyCode <= 105) || (keyCode >= 49 && keyCode <= 57) || keyCode == 106){
      this.startRecording = false;
      this.endTime = new Date().getTime();
      //window.removeEventListener("keydown", this.processRecord);
      this.audioRecordService.stopRecord();
      let blob = this.audioRecordService.getAudioBlobData();
      let groupIndex = this.realGroupIndex;
      let listIndex = this.realTrialIndex;
      if(this.gameMode == "practice_trial"){
        groupIndex = this.practiceGroupIndex;
        listIndex = this.practiceListIndex;
      }
      let fileName = groupIndex + "_" + listIndex + "_" + NumberUpdateVisualThreeBoxesComponent.numOfBoxes + "boxes";
      //console.log("audio name: " + fileName);
      if(this.gameMode === "practice_trial"){
        fileName += "_practice";
      }
      let formData = new FormData();
      formData.append(fileName, blob, fileName);
      formData.append("childId", sessionStorage.getItem("childId"));
      formData.append("experimenter", sessionStorage.getItem("experimenter"));
      formData.append("grade", sessionStorage.getItem("grade"));
      this.audioRecordService.sendAudioData("./api/numberupdatevisual/audio", formData).subscribe(
        (data) => {
          let result = data;
          if(!result){
            this.errorService.internalError();
          }
        },
        (err) => {
          this.errorService.internalError();
        }
      );
    }
  }

  private recordUserInputFromKeyboard(){
    window.addEventListener("keydown", this.pressKeyboard);
  }

  private pressKeyboard = (event) => {    
    if(this.startRecording){
      this.processRecord(event);
    }

    let keyCode = event.keyCode;
    //keyCode = 106 means user press "*"
    if(keyCode === 106){
      this.checkData = true;
    }
    if((keyCode >= 97 && keyCode <= 105) || (keyCode >= 49 && keyCode <= 57)){      
      if(this.userInput.length < NumberUpdateVisualThreeBoxesComponent.numOfBoxes){
        let keyValue = event.key;
        this.userInput.push(parseInt(keyValue));
        if(this.userInput.length == NumberUpdateVisualThreeBoxesComponent.numOfBoxes){
          //send data
          this.userInputInOneGroup.push(this.userInput);
          if(this.gameMode == "practice_trial"){
            this.sendData(this.goToNextPracticeItem, "practice");
          }else{
            this.sendData(this.goToNextRealItem);
          }
        }
      }
    }
  }

  private goToNextRealItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.realTrialIndex == NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex].length - 1){
      //show feedback and go to next group
      this.showFeedback();
    }else{
      this.maintainLastUserInput();
      this.realTrialIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex][this.realTrialIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
    }
  }

  private goToNextRealGroup = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextRealGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    
    if(this.realGroupIndex == NumberUpdateVisualThreeBoxesComponent.realList.length - 1){
      //game over
      this.endGame();
    }else{
      this.realGroupIndex++;
      this.realTrialIndex = 1;
      this.initialValue = NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex][0]; //the first pair in each group is the initial value
      this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
      this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.realList[this.realGroupIndex][this.realTrialIndex];
      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.leftValue = this.initialValue[0] + "";
      this.midValue = this.initialValue[1] + "";
      this.rightValue = this.initialValue[2] + "";

      setTimeout(() => {
        this.showNextIncrement();
        setTimeout(() => {
          this.readyToAnswer();
        }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
      }, NumberUpdateVisualThreeBoxesComponent.showInitialValuesDuration);
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": this.gameMode
    }
    this.gameProgressService.updateGameProgress("./api/numberupdatevisual/gameover", formData, () => {}).subscribe(
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

  private goToNextPracticeItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.practiceListIndex == NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex].length - 1){
      //show feedback and go to next group
      this.showPracticeFeedback();
    }else{
      this.maintainLastUserInput();
      this.practiceListIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][this.practiceListIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
    }
  }

  private goToNextPracticeGroup = () =>{
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    
    if(this.practiceGroupIndex == NumberUpdateVisualThreeBoxesComponent.practiceList.length - 1){
      //go to real trial
      this.realGroupIndex = 0;
      this.realTrialIndex = 1;
      this.startRealTrials();
    }else{
      this.practiceGroupIndex++;
      this.practiceListIndex = 1;
      this.initialValue = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
      this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
      this.currentIncrement = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][this.practiceListIndex];
      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.leftValue = this.initialValue[0] + "";
      this.midValue = this.initialValue[1] + "";
      this.rightValue = this.initialValue[2] + "";

      setTimeout(() => {
        this.showNextIncrement();
        setTimeout(() => {
          this.readyToAnswer();
        }, NumberUpdateVisualThreeBoxesComponent.showIncrementDuration);
      }, NumberUpdateVisualThreeBoxesComponent.showInitialValuesDuration);
    }
  }

  private showFeedback = () => {
    if(this.realGroupIndex == NumberUpdateVisualThreeBoxesComponent.realList.length - 1){
      this.goToNextRealGroup();
    }else{
      document.getElementById("block-div").style.display = "none";
      document.getElementById("right-span").style.borderColor = "black";
      document.getElementById("left-span").style.borderColor = "black";
      document.getElementById("mid-span").style.borderColor = "black";
      document.getElementById("feedback-video-div").style.display = "block";

      document.getElementById("feedback-video").setAttribute("src", NumberUpdateVisualThreeBoxesComponent.feedbackVideoSource["correct_right_1"]);    
      (document.getElementById("feedback-video") as HTMLVideoElement).load();
      (document.getElementById("feedback-video") as HTMLVideoElement).play();

      document.getElementById("feedback-video").addEventListener("ended", this.goToNextRealGroup);
    }
  }

  private showPracticeFeedback = () => {
    let userAnswerCorrect = true;
    let left = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][0][0];
    let mid = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][0][1];
    let right = NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][0][2];
    for(let i = 1; i < NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex].length; i++){
      left = left + NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][i][0];
      mid = mid + NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][i][1];
      right = right + NumberUpdateVisualThreeBoxesComponent.practiceList[this.practiceGroupIndex][i][2];
      if(i < this.userInputInOneGroup.length && 
        (this.userInputInOneGroup[i - 1][0] != left || this.userInputInOneGroup[i - 1][1] != mid || this.userInputInOneGroup[i - 1][2] != right)){
        userAnswerCorrect = false;
        break;
      }
    }
    
    document.getElementById("block-div").style.display = "none";
    document.getElementById("right-span").style.borderColor = "black";
    document.getElementById("left-span").style.borderColor = "black";
    document.getElementById("mid-span").style.borderColor = "black";
    document.getElementById("feedback-video-div").style.display = "block";

    if(userAnswerCorrect){
      //play great job
      if(this.practiceGroupIndex < NumberUpdateVisualThreeBoxesComponent.practiceList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_fantastic_1", "correct_fantastic_2", "correct_fantastic_3"]);
      }
    }else{
      //play opps
      if(this.practiceGroupIndex == 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_1", "incorrect_oops_2"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops_1", "incorrect_oops_2"]);
      }
    }
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userInput": this.userInput,
      "lastUserInput": this.lastUserInput,
      "stimuliInitialVal": this.initialValue,
      "stimuliIncrementInput": this.currentIncrement,
      "stimuliInputBefore": this.currentList,
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
      "numOfBoxes": NumberUpdateVisualThreeBoxesComponent.numOfBoxes
    }
    if(trialType != undefined || trialType != null){
      formData["trialType"] = trialType;
    }else{
      formData["currentListIndex"] = this.realTrialIndex;
      formData["currentGroupIndex"] = this.realGroupIndex;
    }

    this.dataRecordService.sendUserData("./api/numberupdatevisual", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.totalTrialCount = result["totalTrialCount"];
          this.totalCorrectCount = result["totalCorrectCount"];
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

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", NumberUpdateVisualThreeBoxesComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length > 0){
      document.getElementById(videoId).addEventListener("ended", this.playPracticeFeedbackVideo);
    }else{
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeGroup);
    }
  }

  private playPracticeFeedbackVideo = () => {
    let videoId = "feedback-video";
    document.getElementById(videoId).setAttribute("src", NumberUpdateVisualThreeBoxesComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeGroup);
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          if(result["gameStatus"] === "In Practice"){
            this.gameMode = "practice_trial";
            this.startPracticeTrials();
          }else if(result["gameStatus"] === "In Progress"){
            this.gameMode = "real_trial";
            // this.realTrialIndex = result["currentListIndex"] != undefined ? result["currentListIndex"] : 1;
            this.realGroupIndex = result["currentGroupIndex"] != undefined ? result["currentGroupIndex"] : 0;
            this.totalCorrectCount = result["totalCorrectCount"] != undefined ? result["totalCorrectCount"] : 0;
            this.totalTrialCount = result["totalTrialCount"] != undefined ? result["totalTrialCount"] : 0;
            if(result["currentListIndex"] != undefined){
              this.realTrialIndex = result["currentListIndex"];
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
      },
    )
  }

  private initSyncData(){
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    }
    this.httpService.post("./api/numberupdatevisual/syncdata", formData).subscribe(
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

  playIntroVideos(): void{
    let videoId = "intro-video";
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualThreeBoxesComponent.introVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", this.playNextIntroVideo);
  }

  showExitBtn(): void{
    document.getElementById("end-btn").style.display = "block";
  }

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < NumberUpdateVisualThreeBoxesComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualThreeBoxesComponent.introVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private maintainLastUserInput(){
    this.lastUserInput = [];
    for(let i = 0; i < this.userInput.length; i++){
      this.lastUserInput.push(this.userInput[i]);
    }
  }

  private static introVideoSource = [
    "../../../assets/videos/number-update-visual/Intro_1.mp4",
    "../../../assets/videos/number-update-visual/Intro_2.mp4",
    "../../../assets/videos/number-update-visual/Intro_3.mp4",
    "../../../assets/videos/number-update-visual/Intro_4.mp4",
    "../../../assets/videos/number-update-visual/Intro_5.mp4",
    "../../../assets/videos/number-update-visual/Intro_6.mp4",
    "../../../assets/videos/number-update-visual/Intro_7.mp4",
    "../../../assets/videos/number-update-visual/Intro_8.mp4",
    "../../../assets/videos/number-update-visual/Intro_9.mp4",
    "../../../assets/videos/number-update-visual/Intro_10.mp4",
    "../../../assets/videos/number-update-visual/Intro_11.mp4",
    "../../../assets/videos/number-update-visual/Intro_12.mp4",
    "../../../assets/videos/number-update-visual/Intro_13.mp4",
    "../../../assets/videos/number-update-visual/Intro_14.mp4",
    "../../../assets/videos/number-update-visual/Intro_15.mp4",
    "../../../assets/videos/number-update-visual/Intro_16.mp4",
    "../../../assets/videos/number-update-visual/Intro_17.mp4",
    "../../../assets/videos/number-update-visual/Intro_18.mp4",
    "../../../assets/videos/number-update-visual/Intro_19.mp4",
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/number-update-visual/Feedback_Great_Job.mp4",
    "correct_fantastic_1": "../../../assets/videos/number-update-visual/Feedback_Fantastic.mp4",
    "correct_fantastic_2": "../../../assets/videos/number-update-visual/Feedback_Earn_Gold_Coins.mp4",
    "correct_fantastic_3": "../../../assets/videos/number-update-visual/Feedback_Earn_Rocks.mp4",
    "correct_right_1": "../../../assets/videos/number-update-visual/Feedback_Right.mp4",
    "correct_right_2": "../../../assets/videos/number-update-visual/Feedback_Helping_in_Factory.mp4",
    "correct_right_3": "../../../assets/videos/number-update-visual/Feedback_Make_Some_Toys.mp4",
    "incorrect_oops_1": "../../../assets/videos/number-update-visual/Feedback_Oops_1.mp4",
    "incorrect_oops_2": "../../../assets/videos/number-update-visual/Feedback_Oops_2.mp4",
    "ending": "../../../assets/videos/number-update-visual/Ending.mp4",
  };
  private static showIncrementDuration = 1000;  //how many millionseconds to show the increment.
  private static showInitialValuesDuration = 2000;

  private static practiceList = [
    [ [1, 1, 1], [0, 0, 1] ],
    [ [2, 2, 1], [1, 0, 0], [1, 0, 0]],
    [ [2, 3, 1], [1, 0, 0], [0, 0, 1]]
  ];
  private static realList = [
    [ [4, 1, 5], [0, 0, 1], [1, 0, 0], [1, 0, 0], [0, 1, 0], [0, 1, 0]],
    [ [1, 2, 4], [1, 0, 0], [0, 1, 0], [1, 0, 0], [0, 0, 1], [0, 1, 0]],
    [ [4, 2, 4], [0, 0, 1], [0, 0, 1], [0, 1, 0], [1, 0, 0], [0, 1, 0]]
  ];
  private static numOfBoxes = 3;
}
