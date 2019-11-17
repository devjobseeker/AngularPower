import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { trigger, transition, animate, style } from '@angular/animations';

import {DataRecordService} from '../../services/utils/data-record.service';
import {AudioRecordService} from '../../services/utils/audio-record.service';
import {RandomNumberService} from '../../services/utils/random-number.service';
import {GameProgressService } from '../../services/game-progress/game-progress.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-digit-span',
  templateUrl: './digit-span.component.html',
  styleUrls: ['./digit-span.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})

export class DigitSpanComponent implements OnInit {

  constructor(
    private dataRecordService: DataRecordService, 
    private audioRecordService: AudioRecordService,
    private randomService: RandomNumberService,
    private gameProgressService: GameProgressService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService,
  ) { 

  }

  ngOnInit() {
    //init variables
    this.indexOfVideo = 1;
    this.checkData = false;
    this.indexOfTrial = 1;
    this.listLengthArrayIndex = 0;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.audioIndex = 0;
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

  //private variables
  private indexOfVideo;
  private currentRandomDigits;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private trialResultsInSameListLength;
  private currentListLength;
  private listLengthArrayIndex; //index in the list length array
  private checkData;
  private endTime;
  private startTime;
  private practiceListLength;
  private practiceListIndex;
  private practiceDigits;
  private practiceFeedbackVideoNames;
  private audioIndex;

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Practice",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/digitspan/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.generatePracticeDigits();
          this.playPracticeAudios();
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
    this.gameProgressService.updateGameProgress("./api/digitspan/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          this.generateRandomDigits();
          this.audioIndex = 0;
          this.playAudios();
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
    let source = DigitSpanComponent.introVideoSource;
    if(this.indexOfVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      let videoSource = document.getElementById(videoId).getAttribute("src");
      if(videoSource.indexOf("Intro_4") >= 0){
        //set video float to right
        //document.getElementById(videoId).style.cssFloat = "right";
      }
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

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/digitspan/progress", formData, () => {})
      .subscribe(
        (data) => {
          let result = data;
          if(result != undefined && result != null){
            if(result["gameStatus"] === "In Practice"){
              this.gameMode = "practice_trial";
              this.startPracticeTrials();
            }else if(result["gameStatus"] === "In Progress"){
              this.gameMode = "real_trial";
              if(result["currentListIndex"] != undefined && result["currentListIndex"] != null){
                this.listLengthArrayIndex = result["currentListIndex"];
              }else{
                this.listLengthArrayIndex = 0;
              }
              if(result["consecutiveTrialResult"] != undefined && result["consecutiveTrialResult"] != null){
                this.trialResultsInSameListLength = result["consecutiveTrialResult"];
              }
              this.indexOfTrial = this.listLengthArrayIndex % 4 + 1;
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
    this.httpService.post("./api/digitspan/syncdata", formData).subscribe(
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
    for(var i = 0; i < DigitSpanComponent.videoSource.length; i++){
			var req = new XMLHttpRequest();
			req.open("GET", DigitSpanComponent.videoSource[i], true);
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

  private generateRandomDigits(){
    this.currentRandomDigits = DigitSpanComponent.listLengthDigits[this.listLengthArrayIndex];
    this.currentListLength = this.currentRandomDigits.length;
    //this.currentRandomDigits = this.randomService.generateRandomIntsForProvidedListLength(this.currentListLength);  //remove random digits generation
  }

  private generatePracticeDigits(){
    this.practiceDigits = DigitSpanComponent.practiceListLengthDigits[this.practiceListIndex];
    this.practiceListLength = this.practiceDigits.length;
  }

  private playAudios = () => {
    let audio = new Audio();
    audio.src = "../../../assets/audios/digit-span/" + this.currentRandomDigits[this.audioIndex] + ".mp3";
    audio.load();
    audio.play();
    if(this.audioIndex === this.currentListLength - 1){
      audio.addEventListener("ended", () => {
        //show green box
        document.getElementById("robot-div").style.display = "none";
        document.getElementById("green-box-div").style.display = "block";
        //start record what kids said
        this.startRecord();
        this.startTime = Date.now();
        //add keydown event
        this.recordUserInputFromKeyboard();
      })
    }else{
      this.audioIndex++;
      audio.addEventListener("ended", this.playAudios);
    }    
  }

  private startRecord(){
    this.audioRecordService.startRecord();
    window.addEventListener("keydown", this.processRecord);
  }

  private processRecord = (event) => {
    let keyCode = event.keyCode;
    if(keyCode === 106 || keyCode === 13 || (keyCode >= 97 && keyCode <= 105) || (keyCode >= 49 && keyCode <= 57)){
      window.removeEventListener("keydown", this.processRecord);
      this.audioRecordService.stopRecord();
      this.endTime = Date.now();
      let blob = this.audioRecordService.getAudioBlobData();
      let fileName;
      if(this.gameMode === "practice_trial"){
        let i = this.practiceListIndex + 1;
        fileName = "ListLength" + this.practiceListLength + "_" + "TrialIndex" + i + "_Practice";
      }else{
        fileName = "ListLength" + this.currentListLength + "_" + "TrialIndex" + this.indexOfTrial;
      }
      let formData = new FormData();
      formData.append(fileName, blob, fileName);
      formData.append("childId", sessionStorage.getItem("childId"));
      formData.append("experimenter", sessionStorage.getItem("experimenter"));
      formData.append("grade", sessionStorage.getItem("grade"));
      this.audioRecordService.sendAudioData("./api/digitspan/audio", formData).subscribe(
        (data) => {
          let result = data;
          if(!result){
            this.errorService.internalError();
          }
        },
        (err) => {
          this.errorService.networkError();
        }
      )
    }
  }

  private recordUserInputFromKeyboard(){
    window.addEventListener("keydown", this.pressKeyboard);
  }

  private pressKeyboard = (event) => {
    let digitsJson = document.getElementById("digits-span").textContent;
    let digitsArray = [];
    if(digitsJson){
      digitsArray = JSON.parse(digitsJson);
    }
    let key = parseInt(event.key);
    let keyCode = event.keyCode;
    let sendData = false;
    //keyCode = 106 means user press "*"; keyCode = 13 means user press "Enter".
    if(keyCode === 106){
      this.checkData = true;
      sendData = true;
    }else if(keyCode === 13){
      sendData = true;
    }else if(!isNaN(key)){
      if((keyCode >= 97 && keyCode <= 105) || (keyCode >= 49 && keyCode <= 57)){
        digitsArray.push(key);
        if(digitsArray.length === this.currentListLength){
          sendData = true;
        }else{
          document.getElementById("digits-span").textContent = JSON.stringify(digitsArray);
        }
      }
    }

    if(sendData){
      if(this.gameMode === "real_trial"){
        this.updateTrialsResultInSameListLength(digitsArray);
      }
      this.sendKeyboardData(digitsArray);
    }
  }

  private sendKeyboardData(digitsArray){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userInput": digitsArray,
      "stimuliInput": this.currentRandomDigits,
      "indexOfTrial": this.indexOfTrial,  //from 1 to 4
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
      "currentListIndex": this.listLengthArrayIndex,
      "consecutiveTrialResult": this.trialResultsInSameListLength
    }
    this.dataRecordService.sendUserData("./api/digitspan", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          document.getElementById("digits-span").textContent = undefined;
          //go to next round or game over
          if(this.listLengthArrayIndex >= DigitSpanComponent.listLengthDigits.length - 1 || !this.trialResultsInSameListLength.includes(1)){
            this.endGame();
          }else{
            this.goToNextRealTrial();
          }
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
    
  }

  private updateTrialsResultInSameListLength(digitsArray){
    //skip the first 4 trials
    if(this.listLengthArrayIndex > 3){
      if(digitsArray.length != this.currentRandomDigits.length){
        this.trialResultsInSameListLength[this.indexOfTrial - 1] = 0;
        return;
      }
      for(let i = 0; i < digitsArray.length; i++){
        if(digitsArray[i] != this.currentRandomDigits[i]){
          this.trialResultsInSameListLength[this.indexOfTrial - 1] = 0;
          break;
        }
      }
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/digitspan/gameover", formData, () => {}).subscribe(
      (data) => { 
        let result = data;
        if(result != undefined){
          this.gameMode = "complete";
          this.rocks = result["rocks"];
          this.coins = result["coins"];
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
  }

  private goToNextRealTrial(){
    this.indexOfTrial++;
    this.listLengthArrayIndex++;
    this.audioIndex = 0;
    if(this.indexOfTrial > DigitSpanComponent.numOfTrialsInListLength){
      this.indexOfTrial = 1;
      this.trialResultsInSameListLength = [1, 1, 1, 1];
    }
    this.checkData = false;
    document.getElementById("robot-div").style.display = "block";
    document.getElementById("green-box-div").style.display = "none";
    this.generateRandomDigits();
    this.playAudios();
  }

  private playPracticeAudios = () => {
    let audio = new Audio();
    audio.src = "../../../assets/audios/digit-span/" + this.practiceDigits[this.audioIndex] + ".mp3";
    audio.load();
    audio.play();
    if(this.audioIndex === this.practiceListLength - 1){
      audio.addEventListener("ended", () => {
        //show green box
        document.getElementById("robot-div").style.display = "none";
        document.getElementById("green-box-div").style.display = "block";
        //start record what kids said
        this.startRecord();
        this.startTime = Date.now();
        //add keydown event
        this.recordPracticeUserInputFromKeyboard();
      })
    }else{
      this.audioIndex++;
      audio.addEventListener("ended", this.playPracticeAudios);
    }    
  }

  private recordPracticeUserInputFromKeyboard(){
    window.addEventListener("keydown", this.pressKeyboardInPractice);
  }

  private pressKeyboardInPractice = (event) => {
    let digitsJson = document.getElementById("digits-span").textContent;
    let digitsArray = [];
    if(digitsJson){
      digitsArray = JSON.parse(digitsJson);
    }
    let key = parseInt(event.key);
    let keyCode = event.keyCode;
    let sendData = false;
    //keyCode = 106 means user press "*"; keyCode = 13 means user press "Enter".
    if(keyCode === 106){
      this.checkData = true;
      sendData = true;
    }else if(keyCode === 13){
      sendData = true;
    }else if(!isNaN(key)){
      if((keyCode >= 97 && keyCode <= 105) || (keyCode >= 49 && keyCode <= 57)){
        digitsArray.push(key);
        if(digitsArray.length === this.practiceListLength){
          sendData = true;
        }else{
          document.getElementById("digits-span").textContent = JSON.stringify(digitsArray);
        }
      }
    }

    if(sendData){
      this.sendPracticeKeyboardData(digitsArray);
    }
  }

  private sendPracticeKeyboardData(digitsArray){
    window.removeEventListener("keydown", this.pressKeyboardInPractice);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userInput": digitsArray,
      "stimuliInput": this.practiceDigits,
      "indexOfTrial": this.practiceListIndex, //zero index
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
      "trialType": "practice",
      "currentListIndex": this.practiceListIndex,
    }
    this.dataRecordService.sendUserData("./api/digitspan", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          document.getElementById("digits-span").textContent = undefined;
          //show feedback. if correct, show great job audio.
          let userAnswerCorrect = true;
          for(let i = 0 ; i < digitsArray.length; i++){
            if(digitsArray[i] != this.practiceDigits[i]){
              userAnswerCorrect = false;
              break;
            }
          }
          //hide green box
          document.getElementById("green-box-div").style.display = "none";
          document.getElementById("feedback-video-div").style.display = "block";
          if(userAnswerCorrect){
            //good feeback
            if(this.practiceListIndex == 0 || this.practiceListIndex == 1){
              this.playPracticeFeedbackVideos(["correct_great_job_try_some_more"]);
            }else{
              this.playPracticeFeedbackVideos(["correct_fantastic"]);
            }
          }else{
            //bad feedback
            if(this.practiceListIndex == 0 || this.practiceListIndex == 1){
              this.playPracticeFeedbackVideos(["incorrect_oops", "try_some_more"]);
            }else{
              this.playPracticeFeedbackVideos(["incorrect_uhoh", "try_your_best", "do_some_more"]);
            }
          }
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
    document.getElementById(videoId).setAttribute("src", DigitSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", DigitSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    let videoSource = DigitSpanComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]];
    if(videoSource.indexOf("Do_Some_More") >= 0 || videoSource.indexOf("Fantastic") >= 0){
      //document.getElementById(videoId).style.cssFloat = "right";
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
    if(this.practiceListIndex >= DigitSpanComponent.practiceListLengthDigits.length - 1){
      this.gameMode = "real_trial";
      this.startRealTrials();
    }else{
      this.goToNextPracticeTrial();
    }
  }

  private goToNextPracticeTrial(){
    this.practiceListIndex ++;
    this.audioIndex = 0;
    this.checkData = false;
    document.getElementById("robot-div").style.display = "block";
    document.getElementById("green-box-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "none";
    this.generatePracticeDigits();
    this.playPracticeAudios();
  }

  //consts
  private static introVideoSource = [
    "../../../assets/videos/digit-span/Intro_1.mp4",
    "../../../assets/videos/digit-span/Intro_2.mp4",
    "../../../assets/videos/digit-span/Intro_3.mp4",
    "../../../assets/videos/digit-span/Intro_4.mp4",
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/digit-span/Feedback_Great_Job.mp4",
    "correct_great_job_try_some_more": "../../../assets/videos/digit-span/Feedback_Great_Job_Try_Some_More.mp4",
    "correct_fantastic": "../../../assets/videos/digit-span/Feedback_Fantastic.mp4",
    "do_some_more": "../../../assets/videos/digit-span/Feedback_Do_Some_More.mp4",
    "try_some_more": "../../../assets/videos/digit-span/Feedback_Try_Some_More.mp4",
    "try_your_best": "../../../assets/videos/digit-span/Feedback_Try_Your_Best.mp4",
    "incorrect_oops": "../../../assets/videos/digit-span/Feedback_Oops.mp4",
    "incorrect_uhoh": "../../../assets/videos/digit-span/Feedback_Uh_Oh.mp4",
    "ending": "../../../assets/videos/digit-span/Ending_Great_Job.mp4"
  }
  
  private static videoSource = [
    "../../../assets/videos/digit-span/Intro_1.mp4",
    "../../../assets/videos/digit-span/Intro_2.mp4",
    "../../../assets/videos/digit-span/Intro_3.mp4",
    "../../../assets/videos/digit-span/Intro_4.mp4",
    "../../../assets/videos/digit-span/Feedback_Great_Job.mp4",
    "../../../assets/videos/digit-span/Feedback_Do_Some_More.mp4",
    "../../../assets/videos/digit-span/Feedback_Great_Job_Try_Some_More.mp4",
    "../../../assets/videos/digit-span/Feedback_Try_Some_More.mp4",
    "../../../assets/videos/digit-span/Feedback_Try_Your_Best.mp4",
    "../../../assets/videos/digit-span/Feedback_Fantastic.mp4",
    "../../../assets/videos/digit-span/Feedback_Oops.mp4",
    "../../../assets/videos/digit-span/Feedback_Uh_Oh.mp4",
    "../../../assets/videos/digit-span/Ending_Great_Job.mp4"
  ];
  //private static listLengthArray = [2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8];
  //private static practiceListLengthArray = [1, 2, 2];
  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static listLengthDigits = [
    [4, 8],
    [5, 9],
    [4, 6],
    [1, 4],
    [6, 2, 9],
    [4, 2, 6],
    [2, 1, 8],
    [1, 9, 6],
    [6, 8, 2, 4],
    [1, 9, 4, 2],
    [3, 1, 9, 6],
    [8, 2, 6, 1],
    [6, 3, 5, 1, 4],
    [5, 3, 8, 2, 6],
    [8, 6, 9, 4, 1],
    [3, 1, 8, 2, 6],
    [4, 1, 2, 5, 8, 6],
    [6, 2, 5, 1, 9, 3],
    [5, 9, 6, 8, 3, 1],
    [9, 1, 3, 5, 8, 4],
    [6, 4, 5, 3, 8, 1, 9],
    [5, 8, 4, 6, 2, 9, 3],
    [1, 8, 3, 5, 2, 6, 4],
    [8, 3, 6, 2, 5, 1, 4],
    [3, 6, 2, 4, 1, 8, 5, 9],
    [1, 6, 4, 8, 5, 3, 9, 2],
    [1, 3, 8, 4, 2, 5, 9, 6],
    [2, 6, 3, 9, 4, 8, 5, 1]
  ];
  private static practiceListLengthDigits = [
    [5],
    [2, 4],
    [1, 9]
  ];
}