import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { AudioRecordService } from '../../services/utils/audio-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-phonological-binding',
  templateUrl: './phonological-binding.component.html',
  styleUrls: ['./phonological-binding.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class PhonologicalBindingComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private audioRecordService: AudioRecordService,
    private dataRecordService: DataRecordService,
    private randomService: RandomNumberService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    //init variables
    this.indexOfVideo = 0;
    this.indexOfEndVideo = 0;
    this.checkData = false;
    this.indexOfTrial = 1;
    this.trialResultsInSameListLength = [1, 1, 1, 1];
    this.practiceListIndex = 0;
    this.realTrialIndex = 0;
    this.responseIndex = 0;
    //this.responseTime = 0;
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
  private indexOfEndVideo;
  private indexOfTrial; //index of real trial, range will be 1 - 4
  private trialResultsInSameListLength;
  private currentWordList: number[];
  private currentSoundList: number[];
  private currentResponseSoundList: number[];
  private currentListLength;
  private userWordInput: number[] =[];
  private userSoundInput: number[] =[];
  private stimuliInput: number[] = [];
  private realTrialIndex: number;
  private checkData;
  private endTime;
  private startTime;
  //private responseTime;
  private practiceListIndex;
  private practiceFeedbackVideoNames;
  private responseIndex: number;

  private playSoundIndex: number = 0;
  private startRecording = false;
  //private numOfAudios: number = 0;

  //sound -- dad, nonword -- baby

  startGame(): void{
    this.fetchProgress(); 
  }

  startPracticeTrials(): void{
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Practice"
    }
    this.gameProgressService.updateGameProgress("./api/phonological/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.currentSoundList = PhonologicalBindingComponent.practiceSoundList[this.practiceListIndex];
          this.currentWordList = PhonologicalBindingComponent.practiceWordList[this.practiceListIndex];
          this.currentResponseSoundList = PhonologicalBindingComponent.practiceResponseSoundList[this.practiceListIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            this.playSoundAndWord(); 
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
    this.gameProgressService.updateGameProgress("./api/phonological/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "real_trial";
          this.currentSoundList = PhonologicalBindingComponent.realSoundList[this.realTrialIndex];
          this.currentWordList = PhonologicalBindingComponent.realWordList[this.realTrialIndex];
          this.currentResponseSoundList = PhonologicalBindingComponent.realResponseSoundList[this.realTrialIndex];
          this.generateStimuliInput();
          setTimeout(() => {
            this.playSoundAndWord();
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
      document.getElementById(videoId).setAttribute("src", PhonologicalBindingComponent.introVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", this.playNextIntroVideo);
  }

  playEndingVideo(): void{
    let videoId = "end-video";
    if(this.indexOfEndVideo < 1){
      document.getElementById(videoId).setAttribute("src", PhonologicalBindingComponent.feedbackVideoSource["ending_2"]); 
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

  private goToNextPracticeTrial = (event) => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeTrial);
    this.resetUserInput();
    this.checkData = false;
    
    document.getElementById("feedback-video-div").style.display = "none";

    this.practiceListIndex++;
    this.responseIndex = 0;
    if(this.practiceListIndex < PhonologicalBindingComponent.practiceSoundList.length){
      document.getElementById("robot-div").style.display = "block";
      setTimeout( () => {
        this.currentSoundList = PhonologicalBindingComponent.practiceSoundList[this.practiceListIndex];
        this.currentWordList = PhonologicalBindingComponent.practiceWordList[this.practiceListIndex];
        this.currentResponseSoundList = PhonologicalBindingComponent.practiceResponseSoundList[this.practiceListIndex];
        this.generateStimuliInput();
        this.playSoundAndWord(); 
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
    //console.log("add index of trial")
    this.indexOfTrial++;
    this.responseIndex = 0;

    //no need to check the 4 answer in the same list length  //|| !this.trialResultsInSameListLength.includes(1)
    if(this.realTrialIndex >= PhonologicalBindingComponent.realSoundList.length){
      this.endGame();
    }else{
      if(this.indexOfTrial > PhonologicalBindingComponent.numOfTrialsInListLength){
        this.indexOfTrial = 1;
        this.trialResultsInSameListLength = [1, 1, 1, 1];
      }
      document.getElementById("green-box-div").style.display = "none";
      document.getElementById("robot-div").style.display = "block";
      setTimeout( () => {
        this.currentSoundList = PhonologicalBindingComponent.realSoundList[this.realTrialIndex];
        this.currentWordList = PhonologicalBindingComponent.realWordList[this.realTrialIndex];
        this.currentResponseSoundList = PhonologicalBindingComponent.realResponseSoundList[this.realTrialIndex];
        this.generateStimuliInput();
        this.playSoundAndWord();
      }, 1000);
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": this.gameMode
    }
    this.gameProgressService.updateGameProgress("./api/phonological/gameover", formData, () => {}).subscribe(
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

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", PhonologicalBindingComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", PhonologicalBindingComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial);
    }
  }

  private playSoundAndWord(){
    document.getElementById("robot-div").style.display = "block";
    this.currentListLength = this.currentSoundList.length;
    this.playSoundIndex = 0;
    this.playSound();
  }

  private playSound = () => {
    let soundPath = "../../../assets/audios/phonological-binding/real/sound/";
    if(this.gameMode == "practice_trial"){
      soundPath = "../../../assets/audios/phonological-binding/practice/sound/";
    }
    let sound = new Audio();
    sound.src = soundPath + this.currentSoundList[this.playSoundIndex] + ".mp3";
    sound.load();
    sound.play();
    sound.addEventListener("ended", this.playNonword);
  }

  private playNonword = () => {
    let nonwordPath = "../../../assets/audios/phonological-binding/real/nonword/";
    if(this.gameMode == "practice_trial"){
      nonwordPath = "../../../assets/audios/phonological-binding/practice/nonword/";
    }
    let nonword = new Audio();
    nonword.src = nonwordPath + this.currentWordList[this.playSoundIndex] + ".mp3";
    nonword.load();
    nonword.play();
    if(this.playSoundIndex == this.currentListLength - 1){
      nonword.addEventListener("ended", () => {
        setTimeout(() => {
          this.startTime = Date.now();
          this.showEarAndPlaySound();
        }, 1000);
      })
      
    }else{
      nonword.addEventListener("ended", () => {
        setTimeout(() => {
          //play sound
          this.playSoundIndex++;
          this.playSound();
        }, 500);
      })
    }
  }

  private showEarAndPlaySound(){
    document.getElementById("robot-div").style.display = "none"; 
    document.getElementById("ear-div").style.display = "block";
    let path = "../../../assets/audios/phonological-binding/real/sound/";
    if(this.gameMode == "practice_trial"){
      path = "../../../assets/audios/phonological-binding/practice/sound/";
    }
    //play sound
    let sound = new Audio();
    sound.src = path + this.currentResponseSoundList[this.responseIndex] + ".mp3";
    sound.load();
    sound.play();
    sound.addEventListener("ended", this.readyToAnswer);
  }

  private readyToAnswer = () => {
    document.getElementById("ear-div").style.display = "none";
    document.getElementById("green-box-div").style.display = "block";
    this.startRecord();
    //this.startTime = Date.now();
    //add keydown event
    this.recordUserInputFromKeyboard();
  }

  private startRecord(){
    this.audioRecordService.startRecord();
    this.startRecording = true;
  }

  private processRecord = (event) => {
    let keyCode = event.keyCode;
    //user press c, m, or * will stop recording
    if(keyCode === 106 || keyCode === 77 || keyCode === 67){
      this.startRecording = false;
      //window.removeEventListener("keydown", this.processRecord);
      this.audioRecordService.stopRecord();
      let blob = this.audioRecordService.getAudioBlobData();
      let fileName;
      if(this.gameMode === "practice_trial"){
        let i = this.practiceListIndex + 1;
        fileName = "ListLength" + this.currentListLength + "_" + "TrialIndex" + i + "_" + "SoundIndex" + this.currentResponseSoundList[this.responseIndex] + "_Practice";
      }else{
        fileName = "ListLength" + this.currentListLength + "_" + "TrialIndex" + this.indexOfTrial + "_" + "SoundIndex" + this.currentResponseSoundList[this.responseIndex];
      }
      let formData = new FormData();
      formData.append(fileName, blob, fileName);
      formData.append("childId", sessionStorage.getItem("childId"));
      formData.append("experimenter", sessionStorage.getItem("experimenter"));
      formData.append("grade", sessionStorage.getItem("grade"));
      this.audioRecordService.sendAudioData("./api/phonological/audio", formData).subscribe(
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
    //this.endTime = new Date().getTime();
    if(this.startRecording){
      this.processRecord(event);
    }

    //this.responseTime += this.endTime - this.startTime;
    let keyCode = event.keyCode;
    //keyCode = 106 means user press "*"
    if(keyCode === 106){
      this.checkData = true;
    }
    //c - 67, m - 77
    //if user press c, user answer correct; press m, user answer incorrect.
    if(keyCode === 67 || keyCode === 77){
      this.userSoundInput.push(this.currentResponseSoundList[this.responseIndex]);
      if(keyCode === 67){
        this.userWordInput.push(this.stimuliInput[this.currentResponseSoundList[this.responseIndex] - 1]);
      }else if(keyCode === 77){
        this.userWordInput.push(-1);
      }

      if(this.responseIndex < this.currentListLength - 1){
        this.responseIndex++;
        document.getElementById("green-box-div").style.display = "none";
        this.showEarAndPlaySound();
      }else{
        this.endTime = new Date().getTime();
        if(this.gameMode === "practice_trial"){
          this.sendData(() => {
            setTimeout(() => {
              this.showFeedback();
            }, 500);
          }, "practice");
        }else{
          this.sendData(this.goToNextTrial);
        }
      }
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
    
    document.getElementById("green-box-div").style.display = "none";
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      //play great job
      if(this.practiceListIndex < PhonologicalBindingComponent.practiceSoundList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great"]);
      }else{
        this.playPracticeFeedbackVideos(["correct_great_earn_coins"]);
      }
    }else{
      //play opps
      if(this.practiceListIndex == 0){
        this.playPracticeFeedbackVideos(["incorrect_oops", "replay_1"]);
      }else if(this.practiceListIndex == 1){
        this.playPracticeFeedbackVideos(["incorrect_oops_try_your_best"]);
      }else{
        this.playPracticeFeedbackVideos(["incorrect_oops_earn_coins"]);
      }
    }
  }

  private sendData(callback, trialType?){
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "userSoundInput": this.userSoundInput,
      "userWordInput": this.userWordInput,
      "stimuliSoundInput": this.currentSoundList,
      "stimuliWordInput": this.currentWordList,
      //"responseTime": this.responseTime,
      "needCheck": this.checkData,
      "endTime": this.endTime,
      "startTime": this.startTime
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

    this.dataRecordService.sendUserData("./api/phonological", formData, () => {}).subscribe(
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

  private playNextIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < PhonologicalBindingComponent.introVideoSource.length){
      document.getElementById(videoId).setAttribute("src", PhonologicalBindingComponent.introVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/phonological/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          if(result["gameStatus"] === "In Practice"){
            this.gameMode = "practice_trial";
            this.startPracticeTrials();
          }else if(result["gameStatus"] === "In Progress"){
            this.gameMode = "real_trial";
            if(result["currentListIndex"] != undefined){
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
    this.httpService.post("./api/phonological/syncdata", formData).subscribe(
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

  private generateStimuliInput():number[]{
    //init with 0
    for(let i = 0; i < PhonologicalBindingComponent.nonWordCount; i++){
      this.stimuliInput[i] = 0;
    }
    //assign values
    for(let i = 0; i < this.currentSoundList.length; i++){
      this.stimuliInput[this.currentSoundList[i] - 1] = this.currentWordList[i];
    }
    return this.stimuliInput;
  }

  private generateUserInput():number[]{
    let userInput = [];
    for(let i = 0; i < PhonologicalBindingComponent.nonWordCount; i++){
      userInput[i] = 0;
    }
    for(let i = 0; i < this.currentResponseSoundList.length; i++){
      userInput[this.currentResponseSoundList[i] - 1] = this.userWordInput[i];
    }
    return userInput;
  }

  private resetUserInput(){
    this.userSoundInput = [];
    this.userWordInput = [];
  }

  private static introVideoSource = [
    "../../../assets/videos/phonological-binding/Intro_1.mp4",
    "../../../assets/videos/phonological-binding/Intro_2.mp4",
    "../../../assets/videos/phonological-binding/Intro_3.mp4",
    "../../../assets/videos/phonological-binding/Intro_4.mp4",
    "../../../assets/videos/phonological-binding/Intro_5.mp4",
    "../../../assets/videos/phonological-binding/Intro_6.mp4",
    "../../../assets/videos/phonological-binding/Intro_7.mp4",
    "../../../assets/videos/phonological-binding/Intro_8.mp4",
    "../../../assets/videos/phonological-binding/Intro_9.mp4",
    "../../../assets/videos/phonological-binding/Intro_10.mp4"
  ];
  private static feedbackVideoSource = {
    "correct_great" : "../../../assets/videos/phonological-binding/Feedback_Great.mp4",
    "correct_great_earn_coins": "../../../assets/videos/phonological-binding/Feedback_Great_Earn_Coins.mp4",
    "incorrect_oops": "../../../assets/videos/phonological-binding/Feedback_Oops.mp4",
    "incorrect_oops_earn_coins": "../../../assets/videos/phonological-binding/Feedback_Oops_Earn_Coins.mp4",
    "incorrect_oops_try_your_best": "../../../assets/videos/phonological-binding/Feedback_Oops_Try_Your_Best.mp4",
    "replay_1": "../../../assets/videos/phonological-binding/Feedback_Replay_1.mp4",
    "replay_2_1": "../../../assets/videos/phonological-binding/Feedback_Replay_2_1.mp4",
    "replay_2_2": "../../../assets/videos/phonological-binding/Feedback_Replay_2_2.mp4",
    "ending_1": "../../../assets/videos/phonological-binding/Ending_1.mp4",
    "ending_2": "../../../assets/videos/phonological-binding/Ending_2.mp4",
  };
  private static videoSource = [
    "../../../assets/videos/phonological-binding/Intro_1.mp4",
    "../../../assets/videos/phonological-binding/Intro_2.mp4",
    "../../../assets/videos/phonological-binding/Intro_3.mp4",
    "../../../assets/videos/phonological-binding/Intro_4.mp4",
    "../../../assets/videos/phonological-binding/Intro_5.mp4",
    "../../../assets/videos/phonological-binding/Intro_6.mp4",
    "../../../assets/videos/phonological-binding/Intro_7.mp4",
    "../../../assets/videos/phonological-binding/Intro_8.mp4",
    "../../../assets/videos/phonological-binding/Intro_9.mp4",
    "../../../assets/videos/phonological-binding/Intro_10.mp4",
    "../../../assets/videos/phonological-binding/Intro_11.mp4",
    "../../../assets/videos/phonological-binding/Intro_12.mp4",
    "../../../assets/videos/phonological-binding/Intro_13.mp4",
    "../../../assets/videos/phonological-binding/Intro_14.mp4",
    "../../../assets/videos/phonological-binding/Intro_15.mp4",
  ];

  private static numOfTrialsInListLength = 4; //for each list length, there will be 4 trails.
  private static nonWordCount = 41;

  //baby
  private static practiceWordList = [
    [4],
    [5, 2],
    [1, 3]
  ];
  //dad
  private static practiceSoundList = [
    [1],
    [2, 4],
    [3, 5]
  ];
  //dad
  private static practiceResponseSoundList = [
    [1],
    [4, 2],
    [5, 3]
  ];
  //baby
  private static realWordList = [
    [19, 12],
    [32, 24],
    [13, 3],
    [36, 20],
    [6, 9, 17],
    [30, 23, 31],
    [18, 2, 35],
    [33, 16, 7],
    [37, 34, 38, 27],
    [25, 10, 21, 8],
    [14, 1, 11, 15],
    [40, 39, 41, 22]
  ];
  //dad
  private static realSoundList = [
    [7, 16],
    [15, 8],
    [6, 21],
    [20, 31],
    [26, 19, 28],
    [12, 9, 38],
    [29, 35, 25],
    [17, 10, 32],
    [11, 18, 34, 41],
    [22, 39, 24, 36],
    [13, 14, 30, 40],
    [33, 23, 37, 27]
  ];
  //dad
  private static realResponseSoundList = [
    [7, 16],
    [8, 15],
    [21, 6],
    [20, 31],
    [28, 26, 19],
    [9, 38, 12],
    [29, 25, 35],
    [32, 17, 10],
    [34, 18, 41, 11],
    [22, 36, 39, 24],
    [14, 30, 13, 40],
    [27, 33, 23, 37]
  ];
}
