import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { AudioRecordService } from '../../services/utils/audio-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-number-update-auditory',
  templateUrl: './number-update-auditory.component.html',
  styleUrls: ['./number-update-auditory.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class NumberUpdateAuditoryComponent implements OnInit {

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
    this.numOfBoxes = 2;
    this.audioIndex = 0;
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
  coins;
  rocks;
  numOfBoxes;
  showCoins: boolean;

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

  private audioIndex; //initial audio index
  private practiceFeedbackVideoNames;

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Practice",
      "grade": sessionStorage.getItem("grade"),
      "numOfBoxes": this.numOfBoxes,
      "currentListIndex": this.practiceGroupIndex,
      "currentGroupIndex": this.practiceListIndex
    }
    this.gameProgressService.updateGameProgress("./api/numberupdateauditory/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          if(this.numOfBoxes == 2){
            this.initialValue = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
            this.currentList = [this.initialValue[0], this.initialValue[1]];
            this.currentIncrement = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];
          }else{
            this.initialValue = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
            this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
            this.currentIncrement = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];
          }
          this.audioIndex = 0;
          setTimeout(() => {
            this.playInitialAudios();
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
    if(this.numOfBoxes == 3 && this.realGroupIndex == NumberUpdateAuditoryComponent.realTripleList.length - 1
        && this.realTrialIndex >= NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex].length){
          this.endGame();
    }else{
      if((this.numOfBoxes == 2 && this.realTrialIndex >= NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex].length)
          || (this.numOfBoxes == 3 && this.realTrialIndex >= NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex].length)){
        this.realGroupIndex++; 
      }
      this.realTrialIndex = 1;

      if(this.numOfBoxes == 2 && this.realGroupIndex >= NumberUpdateAuditoryComponent.realDoubleList.length){
        this.numOfBoxes = 3;
        this.realGroupIndex = 0;
        this.gameMode ="intro";
        setTimeout(() => {
          this.playTripleIntroVideos();
        }, 0);
      }else{
        if(this.numOfBoxes == 2){
          this.initialValue = NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
          this.currentList = [this.initialValue[0], this.initialValue[1]];
          this.currentIncrement = NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];
        }else{
          this.initialValue = NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
          this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
          this.currentIncrement = NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];
        }
        
        let formData = {
          "childId": sessionStorage.getItem("childId"),
          "gameStatus": "In Progress",
          "grade": sessionStorage.getItem("grade"),
          "numOfBoxes": this.numOfBoxes,
          "currentListIndex": this.realGroupIndex,
          "currentGroupIndex": this.realTrialIndex
        }
        this.gameProgressService.updateGameProgress("./api/numberupdateauditory/progress", formData, () => {}).subscribe(
          (data) => {
            let result = data;
            if(result != undefined){
              this.gameMode = "real_trial";
              document.getElementById("feedback-video-div").style.display = "none";
              document.getElementById("block-div").style.display = "block";
              this.audioIndex = 0;
              setTimeout(() => {
                this.playInitialAudios();
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

  private readyToAnswer = () => {
    document.getElementById("left-span").style.borderColor = "green";
    document.getElementById("right-span").style.borderColor = "green";
    if(this.numOfBoxes == 3){
      document.getElementById("mid-span").style.borderColor = "green";
    }
    //start recording    
    this.startRecord();
    this.startTime = Date.now();
    //add keydown event
    this.recordUserInputFromKeyboard();
  }

  private playNextIncrementAudio = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.playNextIncrementAudio);
    document.getElementById("feedback-video-div").style.display = "none";
    document.getElementById("left-span").style.borderColor = "black";
    document.getElementById("right-span").style.borderColor = "black";
    if(this.numOfBoxes == 3){
      document.getElementById("mid-span").style.borderColor = "black";
    }
    document.getElementById("block-div").style.display = "block";
    let audioName;
    if(this.currentIncrement[0] != 0){
      audioName = this.numOfBoxes == 2 ? "cf" + this.currentIncrement[0] : "apple_cf" + this.currentIncrement[0];
    }else if(this.currentIncrement[1] != 0){
      audioName = this.numOfBoxes == 2 ? "cm" + this.currentIncrement[1] : "apple_cm" + this.currentIncrement[1];
    }else if(this.numOfBoxes == 3 && this.currentIncrement[2] != 0){
      audioName = "apple_ck" + this.currentIncrement[2];
    }

    if(audioName != undefined){
      let audio = new Audio();
      audio.src = "../../../assets/audios/number-update-auditory/" + audioName + ".mp3";
      audio.load();
      audio.play();
      audio.addEventListener("ended", this.readyToAnswer);
    }
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
      this.audioRecordService.sendAudioData("./api/numberupdateauditory/audio", formData).subscribe(
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
      //send audio file
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
        //console.log(this.userInput);
        if(this.userInput.length == this.numOfBoxes){
          //send data
          this.userInputInOneGroup.push(this.userInput);
          if(this.gameMode == "practice_trial"){
            if(this.numOfBoxes == 2){
              this.sendData(this.goToNextDoublePracticeItem, "practice");
            }else{
              this.sendData(this.goToNextTriplePracticeItem, "practice");
            }
          }else{
            if(this.numOfBoxes == 2){
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
    if(this.realTrialIndex == NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex].length - 1){
      //show feedback and go to next group
      this.showFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.realTrialIndex++;
      this.userInput = [];
      this.currentIncrement = NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];

      this.playCatchButterfliesVideo();
    }
  }

  private goToNextTripleRealItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.realTrialIndex == NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex].length - 1){
      //show feedback and go to next group
      this.showFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.realTrialIndex++;
      this.userInput = [];
      this.checkData = false;
      this.currentIncrement = NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];

      this.playCatchButterfliesVideo();
    }
  }

  private goToNextRealGroup = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextRealGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    this.audioIndex = 0;
    this.checkData = false;
    
    if(this.numOfBoxes == 3 && this.realGroupIndex == NumberUpdateAuditoryComponent.realTripleList.length - 1){
      //game over
      this.endGame();
    }else{
      this.realGroupIndex++;
      this.realTrialIndex = 1;
      if(this.numOfBoxes == 2){
        this.initialValue = NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1]];
        this.currentIncrement = NumberUpdateAuditoryComponent.realDoubleList[this.realGroupIndex][this.realTrialIndex];
      }else{
        this.initialValue = NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
        this.currentIncrement = NumberUpdateAuditoryComponent.realTripleList[this.realGroupIndex][this.realTrialIndex];
      }
      
      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.playInitialAudios();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/numberupdateauditory/gameover", formData, () => {}).subscribe(
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
    if(this.practiceListIndex == NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex].length - 1){
      //show feedback and go to next group
      this.showPracticeFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.practiceListIndex++;
      this.userInput = [];
      this.checkData = false;
      this.currentIncrement = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];

      this.playCatchButterfliesVideo();
    }
  }

  private goToNextTriplePracticeItem = () => {
    this.currentList[0] = this.currentList[0] + this.currentIncrement[0];
    this.currentList[1] = this.currentList[1] + this.currentIncrement[1];
    this.currentList[2] = this.currentList[2] + this.currentIncrement[2];
    if(this.practiceListIndex == NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex].length - 1){
      //show feedback and go to next group
      this.showPracticeFeedback();
    }else{
      //last input = current user input
      this.maintainLastUserInput();
      this.practiceListIndex++;
      this.userInput = [];
      this.checkData = false;
      this.currentIncrement = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];

      this.playCatchButterfliesVideo();
    }
  }

  private goToNextPracticeGroup = () =>{
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeGroup);
    this.userInputInOneGroup = [];
    this.userInput = [];
    this.lastUserInput = [];
    this.audioIndex = 0;
    this.checkData = false;
    
    if((this.numOfBoxes == 2 && this.practiceGroupIndex == NumberUpdateAuditoryComponent.practiceDoubleList.length - 1)
        || (this.numOfBoxes == 3 && this.practiceGroupIndex == NumberUpdateAuditoryComponent.practiceTripleList.length - 1)){
      //go to real trial
      this.realGroupIndex = 0;
      this.realTrialIndex = 1;
      this.startRealTrials();
    }else{
      this.practiceGroupIndex++;
      this.practiceListIndex = 1;

      if(this.numOfBoxes == 2){
        this.initialValue = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1]];
        this.currentIncrement = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][this.practiceListIndex];
      }else{
        this.initialValue = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][0]; //the first pair in each group is the initial value
        this.currentList = [this.initialValue[0], this.initialValue[1], this.initialValue[2]];
        this.currentIncrement = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][this.practiceListIndex];
      }

      document.getElementById("feedback-video-div").style.display = "none";
      document.getElementById("block-div").style.display = "block";
      this.playInitialAudios();
    }
  }

  //show feedback for real trial
  private showFeedback = () => {
    let gameOver = false;
    if(this.numOfBoxes == 3 && this.realGroupIndex >= NumberUpdateAuditoryComponent.realTripleList.length - 1){
      gameOver = true;
    }

    if(!gameOver){
      if(this.numOfBoxes == 2 && this.realGroupIndex == NumberUpdateAuditoryComponent.realDoubleList.length - 1){
        //go to num of box = 3
        this.numOfBoxes = 3;
        this.userInputInOneGroup = [];
        this.userInput = [];
        this.lastUserInput = [];
        this.audioIndex = 0;
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
        if(this.numOfBoxes == 3){
          document.getElementById("mid-span").style.borderColor = "black";
        }
        document.getElementById("feedback-video-div").style.display = "block";
        document.getElementById("feedback-video").setAttribute("src", NumberUpdateAuditoryComponent.feedbackVideoSource["do_another"]);    
        (document.getElementById("feedback-video") as HTMLVideoElement).load();
        (document.getElementById("feedback-video") as HTMLVideoElement).play();

        document.getElementById("feedback-video").addEventListener("ended", this.goToNextRealGroup);
      }
      
      // if(this.gameMode === "real_trial" && this.numOfBoxes == 2 && this.realGroupIndex >= NumberUpdateAuditoryComponent.realDoubleList.length - 1){
      //   //start triple, play triple intro video
      //   this.numOfBoxes = 3;
      //   this.realGroupIndex = 0;
      //   this.realTrialIndex = 1;
      //   this.gameMode ="intro";
      //   setTimeout(() => {
      //     this.playTripleIntroVideos();
      //   }, 0);
      // }else{
      //   //check block correct or not
      //   // if(this.userAnswerCorrectPerBlock){
      //   //   //play correct videos
      //   //   this.playPracticeFeedbackVideos(["start_set"]);
      //   // }else{
      //   //   //play incorrect videos
      //   //   this.playPracticeFeedbackVideos(["incorrect_oops"]);
      //   // }
      // }
    }else{
      this.endGame();
    }
  }

  private showPracticeFeedback = () => {
    let userAnswerCorrect = true;
    if(this.numOfBoxes == 2){
      let left = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][0][0];
      let right = NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][0][1];
      for(let i = 1; i < NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex].length; i++){
        left = left + NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][i][0];
        right = right + NumberUpdateAuditoryComponent.practiceDoubleList[this.practiceGroupIndex][i][1];
        if(i <= this.userInputInOneGroup.length && (this.userInputInOneGroup[i - 1][0] != left || this.userInputInOneGroup[i - 1][1] != right)){
          userAnswerCorrect = false;
          break;
        }
      }
    }else{
      let left = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][0][0];
      let mid = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][0][1];
      let right = NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][0][2];
      // console.log("left: " + left + " , " + "mid: " + mid + ", " + "right: " + right);
      // console.log("before loop: ");
      for(let i = 1; i < NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex].length; i++){
        left = left + NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][i][0];
        mid = mid + NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][i][1];
        right = right + NumberUpdateAuditoryComponent.practiceTripleList[this.practiceGroupIndex][i][2];
        // console.log("i: " + i);
        // console.log("left: " + left + " , " + "mid: " + mid + ", " + "right: " + right);
        // console.log(this.userInputInOneGroup);
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
    if(this.numOfBoxes == 3){
      document.getElementById("mid-span").style.borderColor = "black";
    }
    document.getElementById("feedback-video-div").style.display = "block";

    if(userAnswerCorrect){
      //play great job
      if(this.numOfBoxes === 2){
        if(this.practiceGroupIndex === 0){
          this.playPracticeFeedbackVideos(["2_correct_right"]);
        }else if(this.practiceGroupIndex === 1){
          this.playPracticeFeedbackVideos(["2_correct_great_job"]);
        }else{
          this.playPracticeFeedbackVideos(["2_correct_earn_coins"]);
        }
      }else{
        if(this.practiceGroupIndex === 0){
          this.playPracticeFeedbackVideos(["3_correct_right"]);
        }else if(this.practiceGroupIndex === 1){
          this.playPracticeFeedbackVideos(["3_correct_great_job"]);
        }else{
          this.playPracticeFeedbackVideos(["2_correct_earn_coins"]);
        }
      }
    }else{
      //play opps
      if(this.numOfBoxes === 2){
        if(this.practiceGroupIndex === 0){
          this.playPracticeFeedbackVideos(["2_oops", "2_oops_look", "2_oops_try_another"]);
        }else if(this.practiceGroupIndex === 1){
          this.playPracticeFeedbackVideos(["2_oops_do_some_more"]);
        }else{
          this.playPracticeFeedbackVideos(["2_oops_earn_coins"]);
        }
      }else{
        if(this.practiceGroupIndex === 0){
          this.playPracticeFeedbackVideos(["3_oops", "3_oops_look", "3_oops_try_another"]);
        }else if(this.practiceGroupIndex === 1){
          this.playPracticeFeedbackVideos(["3_oops_do_some_more"]);
        }else{
          this.playPracticeFeedbackVideos(["3_oops_remember", "3_oops_try_your_best"]);
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

    this.dataRecordService.sendUserData("./api/numberupdateauditory", formData, () => {}).subscribe(
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
    document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
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
    this.gameProgressService.fetchGameProgress("./api/numberupdateauditory/progress", formData, () => {}).subscribe(
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
    this.httpService.post("./api/numberupdateauditory/syncdata", formData).subscribe(
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
      document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.introVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", this.playNextIntroVideo);
  }

  playTripleIntroVideos(): void{ 
    let videoId = "intro-video";
    this.indexOfVideo = 0;
    document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.tripleIntroVideoSource[0]);
    document.getElementById(videoId).setAttribute("type", "video/mp4");
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.indexOfVideo++;
    document.getElementById("intro-video").addEventListener("ended", this.playNextTripleIntroVideo);
  }

  private playNextTripleIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo > 1){
      this.showCoins = false;
    }
    if(this.indexOfVideo < NumberUpdateAuditoryComponent.tripleIntroVideoSource.length){
      document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.tripleIntroVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextTripleIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  showExitBtn(): void{
    document.getElementById("end-btn").style.display = "block";
  }

  private playInitialAudios = () => {
    let audio = new Audio();
    let prefix = "m";
    if(this.audioIndex % this.numOfBoxes == 0){
      prefix = "f"; //female
    }else if(this.audioIndex % this.numOfBoxes == 1){
      prefix = "m"; //male
    }else if(this.numOfBoxes == 3 && this.audioIndex % this.numOfBoxes == 2){
      prefix = "k"; //kid
    }
    audio.src = "../../../assets/audios/number-update-auditory/" + prefix + this.initialValue[this.audioIndex] + ".mp3";
    audio.load();
    audio.play();
    this.audioIndex++;
    if(this.audioIndex > this.initialValue.length - 1){
      audio.addEventListener("ended", () => {
        setTimeout(this.playCatchButterfliesVideo, 1000);
      });
    }else{
      audio.addEventListener("ended", () => {
        setTimeout(() => {
          this.playInitialAudios();
        }, 1000);
      });
    }
  }

  private playCatchButterfliesVideo = () => {
    document.getElementById("block-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(this.numOfBoxes == 2){
      document.getElementById("feedback-video").setAttribute("src", NumberUpdateAuditoryComponent.catchButterfliesVideoSource);
    }else{
      document.getElementById("feedback-video").setAttribute("src", NumberUpdateAuditoryComponent.pickAppleVideoSource);
    }
    (document.getElementById("feedback-video") as HTMLVideoElement).load();
    (document.getElementById("feedback-video") as HTMLVideoElement).play();
    document.getElementById("feedback-video").addEventListener("ended", this.playNextIncrementAudio);
  }

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < NumberUpdateAuditoryComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", NumberUpdateAuditoryComponent.introVideoSource[this.indexOfVideo]);
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
    this.gameProgressService.fetchGameProgress("./api/numberupdateauditory/progress", formData, () => {}).subscribe(
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
    "../../../assets/videos/number-update-auditory/Intro_1.mp4",
    "../../../assets/videos/number-update-auditory/Intro_2.mp4",
    "../../../assets/videos/number-update-auditory/Intro_3.mp4",
    "../../../assets/videos/number-update-auditory/Intro_4.mp4",
    "../../../assets/videos/number-update-auditory/Intro_5.mp4",
    "../../../assets/videos/number-update-auditory/Intro_6.mp4",
    "../../../assets/videos/number-update-auditory/Intro_7.mp4",
    "../../../assets/videos/number-update-auditory/Intro_8.mp4",
    "../../../assets/videos/number-update-auditory/Intro_9.mp4",
    "../../../assets/videos/number-update-auditory/Intro_10.mp4",
    "../../../assets/videos/number-update-auditory/Intro_11.mp4",
    "../../../assets/videos/number-update-auditory/Intro_12.mp4",
    "../../../assets/videos/number-update-auditory/Intro_13.mp4",
  ];
  private static feedbackVideoSource = {
    "2_correct_right" : "../../../assets/videos/number-update-auditory/Feedback_Right.mp4",
    "2_correct_great_job": "../../../assets/videos/number-update-auditory/Feedback_Great_Job.mp4",
    "2_correct_earn_coins": "../../../assets/videos/number-update-auditory/Feedback_Great_Job_Earn_Coins.mp4",
    "2_oops": "../../../assets/videos/number-update-auditory/Feedback_Oops.mp4",
    "2_oops_look": "../../../assets/videos/number-update-auditory/Feedback_Look.mp4",
    "2_oops_try_another": "../../../assets/videos/number-update-auditory/Feedback_Try_Another.mp4",
    "2_oops_do_some_more": "../../../assets/videos/number-update-auditory/Feedback_Oops_Do_Some_More.mp4",
    "2_oops_earn_coins": "../../../assets/videos/number-update-auditory/Feedback_Oops_Earn_Coins.mp4",

    "3_correct_right" : "../../../assets/videos/number-update-auditory/Feedback_3boxes_Right.mp4",
    "3_correct_great_job": "../../../assets/videos/number-update-auditory/Feedback_3boxes_Great_Job.mp4",
    "3_oops": "../../../assets/videos/number-update-auditory/Feedback_3boxes_Oops.mp4",
    "3_oops_look": "../../../assets/videos/number-update-auditory/Feedback_3boxes_Look.mp4",
    "3_oops_try_another": "../../../assets/videos/number-update-auditory/Feedback_3boxes_Try_Another.mp4",
    "3_oops_do_some_more": "../../../assets/videos/number-update-auditory/Feedback_3boxes_Oops_Do_Some_More.mp4",
    "3_oops_remember": "../../../assets/videos/number-update-auditory/Feedback_Oops_Remember.mp4",
    "3_oops_try_your_best": "../../../assets/videos/number-update-auditory/Feedback_Try_Your_Best.mp4",

    "do_another": "../../../assets/videos/number-update-auditory/Lets_Do_Another.mp4",
  };
  private static tripleIntroVideoSource = [
    "../../../assets/videos/number-update-auditory/Ending_Two_Boxes_1.mp4",
    "../../../assets/videos/number-update-auditory/Ending_Two_Boxes_2.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_1.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_2.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_3.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_4.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_5.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_6.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_7.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_8.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_9.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_10.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_11.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_12.mp4",
    "../../../assets/videos/number-update-auditory/Triples/Intro_13.mp4",
  ];
  private static catchButterfliesVideoSource = "../../../assets/videos/number-update-auditory/Catch_Butterfly.mp4";
  private static pickAppleVideoSource = "../../../assets/videos/number-update-auditory/Pick_Apples.mp4";

  private static practiceDoubleList = [
    [ [1, 1], [0, 1]],
    [ [1, 2], [1, 0], [1, 0] ],
    [ [1, 1], [0, 1], [0, 1] ]
  ];
  private static realDoubleList = [
    [ [1, 2], [1, 0], [0, 1], [1, 0], [0, 1], [0, 1]],
    [ [2, 1], [0, 1], [1, 0], [1, 0], [0, 1], [1, 0]],
    [ [4, 3], [1, 0], [1, 0], [0, 1], [0, 1], [0, 1]]
  ];
  private static practiceTripleList = [
    [ [1, 1, 1], [0, 1, 0]],
    [ [1, 1, 1], [1, 0, 0], [0, 0, 1] ],
    [ [1, 1, 2], [0, 1, 0], [1, 0, 0] ]
  ];
  private static realTripleList = [
    [ [5, 4, 3], [0, 0, 1], [0, 1, 0], [1, 0, 0], [0, 1, 0], [0, 0, 1]],
    [ [5, 1, 1], [0, 0, 1], [0, 1, 0], [0, 1, 0], [0, 0, 1], [1, 0, 0]],
    [ [4, 2, 2], [1, 0, 0], [0, 1, 0], [0, 0, 1], [0, 1, 0], [0, 0, 1]]
  ];
}