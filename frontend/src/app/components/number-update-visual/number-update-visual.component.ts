import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { AudioRecordService } from '../../services/utils/audio-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-number-update-visual',
  templateUrl: './number-update-visual.component.html',
  styleUrls: ['./number-update-visual.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class NumberUpdateVisualComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private audioRecordService: AudioRecordService,
    private dataRecordService: DataRecordService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    this.indexOfVideo = 0;
    this.indexOfEndVideo = 0;
    this.checkData = false;
    this.userInput = [];
    this.lastUserInput = [];
    this.userInputInOneGroup = [];
    this.gameMode = "start";
    this.practiceListIndex = 1;
    this.practiceGroupIndex = 0;
    this.realGroupIndex = 0;
    this.realTrialIndex = 1;
    this.numOfBoxes = 2;
    this.showCoins = false;

    this.initSyncData();

    if(AppConfig.DISABLE_RIGHT_CLICK){
      window.addEventListener('contextmenu', function(e){
        e.preventDefault();
      }, false)
    }
  }

  //public variables
  gameMode: String;
  leftValue: String;
  rightValue: String;
  midValue: String;
  coins;
  rocks;
  numOfBoxes;
  showCoins: boolean;

  //private variables
  private indexOfVideo;
  private indexOfEndVideo;
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
      "gameStatus": "In Practice",
      "numOfBoxes": this.numOfBoxes,
      "currentListIndex": this.practiceGroupIndex,
      "currentGroupIndex": this.practiceListIndex
    }
    this.gameProgressService.updateGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          if(this.numOfBoxes === 2){
            this.initialValue = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value    
            this.currentList = [this.initialValue[0], this.initialValue[1]];
            this.currentIncrement = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];
          }else{
            this.initialValue = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value    
            this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
            this.currentIncrement = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];
          }
          
          setTimeout(() => {
            this.leftValue = this.initialValue[0] + "";
            if(this.numOfBoxes === 2){
              this.rightValue = this.initialValue[1] + "";
            }else{
              this.midValue = this.initialValue[1] + "";
              this.rightValue = this.initialValue[2] + "";
            }
            

            setTimeout(() => {
              this.showNextIncrement();
              setTimeout(() => {
                this.readyToAnswer();
              }, NumberUpdateVisualComponent.showIncrementDuration);
            }, NumberUpdateVisualComponent.showInitialValuesDuration);
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
    if(this.numOfBoxes === 3 && this.realGroupIndex === NumberUpdateVisualComponent.realTripleList.length - 1
      && this.realTrialIndex >= NumberUpdateVisualComponent.realTripleList[this.realGroupIndex].length){
        this.endGame();
    }else{
      if((this.numOfBoxes === 2 && this.realTrialIndex >= NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex].length)
      || (this.numOfBoxes === 3 && this.realTrialIndex >= NumberUpdateVisualComponent.realTripleList[this.realGroupIndex].length)){
        this.realGroupIndex++;
      }
      this.realTrialIndex = 1;

      if(this.numOfBoxes === 3&& this.realGroupIndex >= NumberUpdateVisualComponent.realDoubleList.length){
        this.numOfBoxes = 2;
        this.realGroupIndex = 0;
        this.gameMode = "intro";
        setTimeout(() => {
          this.playTripleIntroVideos();
        }, 0);
      }else{
        if(this.numOfBoxes === 2){
          this.initialValue = NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
          this.currentList = [this.initialValue[0], this.initialValue[1]];
          this.currentIncrement = NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];
        }else{
          this.initialValue = NumberUpdateVisualComponent.realTripleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
          this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
          this.currentIncrement = NumberUpdateVisualComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];
        }

        let formData = {
          "childId": sessionStorage.getItem("childId"),
          "grade": sessionStorage.getItem("grade"),
          "gameStatus": "In Progress",
          "numOfBoxes": this.numOfBoxes,
          "currentListIndex": this.realGroupIndex,
          "currentGroupIndex": this.realTrialIndex
        }
        this.gameProgressService.updateGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
          (data) => {
            let result = data;
            if(result != undefined){
              this.gameMode = "real_trial";
              document.getElementById("feedback-video-div").style.display = "none";
              document.getElementById("block-div").style.display = "block";
              setTimeout(() => {
                this.leftValue = this.initialValue[0] + "";
                if(this.numOfBoxes === 2){
                  this.rightValue = this.initialValue[1] + "";
                }else{
                  this.midValue = this.initialValue[1] + "";
                  this.rightValue = this.initialValue[2] + "";
                }
                
                setTimeout(() => {
                  this.showNextIncrement();
                  setTimeout(() => {
                    this.readyToAnswer();
                  }, NumberUpdateVisualComponent.showIncrementDuration);
                }, NumberUpdateVisualComponent.showInitialValuesDuration);
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
    }
  }

  playEndingVideo(): void{
    let videoId = "end-video";
    if(this.indexOfEndVideo < 1){
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.feedbackVideoSource["ending_2"]); 
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
    document.getElementById("end-btn").style.display = "block";
  }

  private readyToAnswer(){
    this.leftValue = "";
    this.rightValue = "";
    if(this.numOfBoxes === 3){
      this.midValue = "";
      document.getElementById("mid-span").style.borderColor = "green";
    }
    document.getElementById("left-span").style.borderColor = "green";
    document.getElementById("right-span").style.borderColor = "green";
    //start recording    
    this.startRecord();
    this.startTime = Date.now();
    //add keydown event
    this.recordUserInputFromKeyboard();
  }

  private showNextIncrement(){
    this.leftValue = this.currentIncrement[0] == 0 ? "" : "+" + this.currentIncrement[0] + "";
    if(this.numOfBoxes === 2){
      this.rightValue = this.currentIncrement[1] == 0 ? "" : "+" + this.currentIncrement[1] + "";
    }else{
      this.midValue = this.currentIncrement[1] == 0 ? "" : "+" + this.currentIncrement[1] + "";
      this.rightValue = this.currentIncrement[2] == 0 ? "" : "+" + this.currentIncrement[2] + "";
      document.getElementById("mid-span").style.borderColor = "red";
    }
    
    document.getElementById("left-span").style.borderColor = "red";
    document.getElementById("right-span").style.borderColor = "red";
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
      this.audioRecordService.stopRecord();
      let blob = this.audioRecordService.getAudioBlobData();
      let groupIndex = this.realGroupIndex + 1;
      let listIndex = this.realTrialIndex;
      if(this.gameMode == "practice_trial"){
        groupIndex = this.practiceGroupIndex + 1;
        listIndex = this.practiceListIndex;
      }
      let fileName = "BlockIndex" + groupIndex + "_" + "TrialIndex" + listIndex + "_" + "NumOfBoxes" + this.numOfBoxes;
      if(this.gameMode === "practice_trial"){
        fileName += "_Practice";
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
      if(this.userInput.length < this.numOfBoxes){
        let keyValue = event.key;
        this.userInput.push(parseInt(keyValue));
        if(this.userInput.length == this.numOfBoxes){
          //send data
          this.userInputInOneGroup.push(this.userInput);
          if(this.gameMode == "practice_trial"){
            if(this.numOfBoxes === 2){
              this.sendData(this.goToNextDoublePracticeItem, "practice");
            }else{
              this.sendData(this.goToNextTriplePracticeItem, "practice");
            }
          }else{
            if(this.numOfBoxes === 2){
              this.sendData(this.goToNextDoubleRealItem);
            }else{
              this.sendData(this.goToNextTripleRealItem);
            }
          }
        }
      }
    }
  }

  private goToNextDoubleRealItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    if(this.realTrialIndex == NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex].length - 1){
      //show feedback and go to next group
      this.showFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.realTrialIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualComponent.showIncrementDuration);
    }
  }

  private goToNextTripleRealItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.realTrialIndex == NumberUpdateVisualComponent.realTripleList[this.realGroupIndex].length - 1){
      //show feedback and go to next group
      this.showFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.realTrialIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualComponent.showIncrementDuration);
    }
  }

  private goToNextRealGroup = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextRealGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    
    if(this.numOfBoxes === 3 && this.realGroupIndex == NumberUpdateVisualComponent.realTripleList.length - 1){
      //game over
      this.endGame();
    }else{
      this.realGroupIndex++;
      this.realTrialIndex = 1;
      if(this.numOfBoxes === 2){
        this.initialValue = NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1]];
        this.currentIncrement = NumberUpdateVisualComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];
      }else{
        this.initialValue = NumberUpdateVisualComponent.realTripleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
        this.currentIncrement = NumberUpdateVisualComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];
      }
      
      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.leftValue = this.initialValue[0] + "";
      if(this.numOfBoxes === 2){
        this.rightValue = this.initialValue[1] + "";
      }else{
        this.midValue = this.initialValue[1] + "";
        this.rightValue = this.initialValue[2] + "";
      }
      

      setTimeout(() => {
        this.showNextIncrement();
        setTimeout(() => {
          this.readyToAnswer();
        }, NumberUpdateVisualComponent.showIncrementDuration);
      }, NumberUpdateVisualComponent.showInitialValuesDuration);
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

  private goToNextDoublePracticeItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    if(this.practiceListIndex == NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex].length - 1){
      //show feedback and go to next group
      this.showPracticeFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.practiceListIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualComponent.showIncrementDuration);
    }
  }

  private goToNextTriplePracticeItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.practiceListIndex == NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex].length - 1){
      //show feedback and go to next group
      this.showPracticeFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.practiceListIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];

      this.showNextIncrement();
      setTimeout(() => {
        this.readyToAnswer();
      }, NumberUpdateVisualComponent.showIncrementDuration);
    }
  }

  private goToNextPracticeGroup = () =>{
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    
    if((this.numOfBoxes == 2 && this.practiceGroupIndex == NumberUpdateVisualComponent.practiceDoubleList.length - 1)
        || (this.numOfBoxes == 3 && this.practiceGroupIndex == NumberUpdateVisualComponent.practiceTripleList.length - 1)){
      //go to real trial
      this.realGroupIndex = 0;
      this.realTrialIndex = 1;
      this.startRealTrials();
    }else{
      this.practiceGroupIndex++;
      this.practiceListIndex = 1;
      if(this.numOfBoxes === 2){
        this.initialValue = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1]];
        this.currentIncrement = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];
      }else{
        this.initialValue = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
        this.currentIncrement = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];
      }
      
      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.leftValue = this.initialValue[0] + "";
      if(this.numOfBoxes === 2){
        this.rightValue = this.initialValue[1] + "";
      }else{
        this.midValue = this.initialValue[1] + "";
        this.rightValue = this.initialValue[2] + "";
      }

      setTimeout(() => {
        this.showNextIncrement();
        setTimeout(() => {
          this.readyToAnswer();
        }, NumberUpdateVisualComponent.showIncrementDuration);
      }, NumberUpdateVisualComponent.showInitialValuesDuration);
    }
  }

  private showFeedback = () => {
    let gameOver = false;
    if(this.numOfBoxes == 3 && this.realGroupIndex >= NumberUpdateVisualComponent.realTripleList.length - 1){
      gameOver = true;
    }
    if(!gameOver){
      if(this.numOfBoxes == 2 && this.realGroupIndex == NumberUpdateVisualComponent.realDoubleList.length - 1){
        this.numOfBoxes = 3;
        this.userInputInOneGroup = [];
        this.userInput = [];
        this.lastUserInput = [];
        this.practiceListIndex = 1;
        this.practiceGroupIndex = 0;
        this.realGroupIndex = 0;
        this.realTrialIndex = 1;
        this.checkData = false;
        this.gameMode ="intro";
        setTimeout(() => {
          this.fetchCoins();
        }, 0);
      }else{
        document.getElementById("block-div").style.display = "none";
        document.getElementById("right-span").style.borderColor = "black";
        document.getElementById("left-span").style.borderColor = "black";
        if(this.numOfBoxes === 3){
          document.getElementById("mid-span").style.borderColor = "black";
        }
        document.getElementById("feedback-video-div").style.display = "block";

        document.getElementById("feedback-video").setAttribute("src", NumberUpdateVisualComponent.feedbackVideoSource["do_another"]);    
        (document.getElementById("feedback-video") as HTMLVideoElement).load();
        (document.getElementById("feedback-video") as HTMLVideoElement).play();

        document.getElementById("feedback-video").addEventListener("ended", this.goToNextRealGroup);
      }
    }else{
      this.endGame();
    }
  }

  private showPracticeFeedback = () => {
    let userAnswerCorrect = true;
    if(this.numOfBoxes === 2){
      let left = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][0][0];
      let right = NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][0][1];
      for(let i = 1; i < NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex].length; i++){
        left = left + NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][i][0];
        right = right + NumberUpdateVisualComponent.practiceDoubleList[this.practiceGroupIndex][i][1];
        // console.log(this.userInputInOneGroup[i - 1][0] != left);
        // console.log(this.userInputInOneGroup[i - 1][1] != right);
        // console.log("i= " + i + " , userinput.length= " + this.userInputInOneGroup.length);
        if(i <= this.userInputInOneGroup.length && (this.userInputInOneGroup[i - 1][0] != left || this.userInputInOneGroup[i - 1][1] != right)){
          userAnswerCorrect = false;
          break;
        }
      }
    }else{
      let left = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][0][0];
      let mid = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][0][1];
      let right = NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][0][2];
      for(let i = 1; i < NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex].length; i++){
        left = left + NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][i][0];
        mid = mid + NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][i][1];
        right = right + NumberUpdateVisualComponent.practiceTripleList[this.practiceGroupIndex][i][2];
        if(i <= this.userInputInOneGroup.length 
          && (this.userInputInOneGroup[i - 1][0] != left || this.userInputInOneGroup[i - 1][1] != mid || this.userInputInOneGroup[i - 1][2] != right)){
          userAnswerCorrect = false;
          break;
        }
      }
    }
    
    document.getElementById("block-div").style.display = "none";
    document.getElementById("right-span").style.borderColor = "black";
    document.getElementById("left-span").style.borderColor = "black";
    if(this.numOfBoxes === 3){
      document.getElementById("mid-span").style.borderColor = "black";
    }
    document.getElementById("feedback-video-div").style.display = "block";

    if(userAnswerCorrect){
      //play great job
      if(this.numOfBoxes == 2){
        if(this.practiceGroupIndex < NumberUpdateVisualComponent.practiceDoubleList.length - 1){
          this.playPracticeFeedbackVideos(["correct_great_job"]);
        }else{
          this.playPracticeFeedbackVideos(["correct_great_job_3", "earn_coins"]);
        }
      }else{
        if(this.practiceGroupIndex < NumberUpdateVisualComponent.practiceTripleList.length - 1){
          this.playPracticeFeedbackVideos(["correct_great_job"]);
        }else{
          this.playPracticeFeedbackVideos(["3_correct_great_job_3_1", "3_correct_great_job_3_2"]);
        }
      }
    }else{
      //play opps
      if(this.numOfBoxes == 2){
        if(this.practiceGroupIndex == 0){
          this.playPracticeFeedbackVideos(["oops_1_1", "oops_1_2"]);
        }else if(this.practiceGroupIndex == 1){
          this.playPracticeFeedbackVideos(["oops_2_1", "oops_2_2"]);
        }else{
          this.playPracticeFeedbackVideos(["oops_2_1", "oops_3", "earn_coins"]);
        }
      }else{
        if(this.practiceGroupIndex == 0){
          this.playPracticeFeedbackVideos(["oops_1_1", "3_oops_1"]);
        }else if(this.practiceGroupIndex == 1){
          this.playPracticeFeedbackVideos(["3_oops_2_1", "3_oops_2_2"]);
        }else{
          this.playPracticeFeedbackVideos(["3_oops_2_1", "3_oops_3", "3_correct_great_job_3_2"]);
        }
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
      "numOfBoxes": this.numOfBoxes
    }
    if(trialType != undefined && trialType == "practice"){
      formData["trialType"] = trialType;
      formData["currentListIndex"] = this.practiceListIndex;
      formData["currentGroupIndex"] = this.practiceGroupIndex;
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

  //play the first feedback video
  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
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
          if(result["numOfBoxes"] != undefined ){
            this.numOfBoxes = result["numOfBoxes"];
          }
          if(result["gameStatus"] === "In Practice"){
            this.gameMode = "practice_trial";
            this.practiceListIndex = 1;
            this.practiceGroupIndex = 0; 
            this.startPracticeTrials();
          }else if(result["gameStatus"] === "In Progress"){
            this.gameMode = "real_trial";
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
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.introVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", this.playNextIntroVideo);
  }

  playTripleIntroVideos(): void{
    let videoId = "intro-video";
    this.indexOfVideo = 0;
    document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.tripleIntroVideoSource[0]);
    document.getElementById(videoId).setAttribute("type", "video/mp4");
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.indexOfVideo++;
    document.getElementById("intro-video").addEventListener("ended", this.playNextTripleIntroVideo);
  }

  private playNextTripleIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo > 2){
      this.showCoins = false;
    }
    if(this.indexOfVideo < NumberUpdateVisualComponent.tripleIntroVideoSource.length){
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.tripleIntroVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextTripleIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < NumberUpdateVisualComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", NumberUpdateVisualComponent.introVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private fetchCoins():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/numberupdatevisual/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.coins = result["coins"];
          this.rocks = result["rocks"];
          this.showCoins = true;
          this.playTripleIntroVideos();
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      },
    )
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
    "../../../assets/videos/number-update-visual/Intro_13.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/number-update-visual/Feedback_Great_Job.mp4",
    "correct_great_job_3": "../../../assets/videos/number-update-visual/Feedback_Great_Job_3.mp4",
    "oops_1_1": "../../../assets/videos/number-update-visual/Feedback_Oops_1_1.mp4",
    "oops_1_2": "../../../assets/videos/number-update-visual/Feedback_Oops_1_2.mp4",
    "oops_2_1": "../../../assets/videos/number-update-visual/Feedback_Oops_2_1.mp4",
    "oops_2_2": "../../../assets/videos/number-update-visual/Feedback_Oops_2_2.mp4",
    "oops_3": "../../../assets/videos/number-update-visual/Feedback_Oops_3.mp4",
    "earn_coins": "../../../assets/videos/number-update-visual/Feedback_Earn_Coins.mp4",
    "do_another": "../../../assets/videos/number-update-visual/Feedback_Lets_Do_Another.mp4",

    "3_correct_great_job_3_1": "../../../assets/videos/number-update-visual/Feedback_3boxes_Great_Job_3_1.mp4",
    "3_correct_great_job_3_2": "../../../assets/videos/number-update-visual/Feedback_3boxes_Great_Job_3_2.mp4",
    "3_oops_1": "../../../assets/videos/number-update-visual/Feedback_3boxes_Oops_1.mp4",
    "3_oops_2_1": "../../../assets/videos/number-update-visual/Feedback_3boxes_Oops_2_1.mp4",
    "3_oops_2_2": "../../../assets/videos/number-update-visual/Feedback_3boxes_Oops_2_2.mp4",
    "3_oops_3": "../../../assets/videos/number-update-visual/Feedback_3boxes_Oops_3.mp4",

    "ending_2": "../../../assets/videos/number-update-visual/Ending_2.mp4",
  };
  private static tripleIntroVideoSource = [
    "../../../assets/videos/number-update-visual/Ending_Two_Boxes_1.mp4",
    "../../../assets/videos/number-update-visual/Ending_Two_Boxes_2.mp4",
    "../../../assets/videos/number-update-visual/Ending_Two_Boxes_3.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_1.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_2.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_3.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_4.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_5.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_6.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_7.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_8.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_9.mp4",
    "../../../assets/videos/number-update-visual/Triples/Intro_10.mp4",
  ];
  private static showIncrementDuration = 1000;  //how many millionseconds to show the increment.
  private static showInitialValuesDuration = 2000;

  private static practiceDoubleList = [
    [ [1, 1], [1, 0]],
    [ [2, 1], [0, 1], [1, 0] ],
    [ [2, 2], [1, 0], [1, 0] ]
  ];
  private static realDoubleList = [
    [ [1, 4], [0, 1], [0, 1], [1, 0], [1, 0], [1, 0]],
    [ [4, 3], [0, 1], [1, 0], [1, 0], [0, 1], [0, 1]],
    [ [1, 1], [0, 1], [0, 1], [0, 1], [1, 0], [0, 1]]
  ];
  private static practiceTripleList = [
    [ [1, 1, 1], [0, 1, 0]],
    [ [2, 1, 3], [1, 0, 0], [1, 0, 0] ],
    [ [3, 1, 3], [1, 0, 0], [0, 0, 1] ]
  ];
  private static realTripleList = [
    [ [4, 1, 5], [0, 0, 1], [1, 0, 0], [1, 0, 0], [0, 1, 0], [0, 1, 0]],
    [ [1, 2, 4], [1, 0, 0], [0, 1, 0], [1, 0, 0], [0, 0, 1], [0, 1, 0]],
    [ [4, 2, 4], [0, 0, 1], [0, 0, 1], [0, 1, 0], [1, 0, 0], [0, 1, 0]]
  ];
}