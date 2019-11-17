import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { ErrorService } from '../../services/errors/error.service';
import { AudioRecordService } from '../../services/utils/audio-record.service';
import { DataRecordService } from '../../services/utils/data-record.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-non-word',
  templateUrl: './non-word.component.html',
  styleUrls: ['./non-word.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class NonWordComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private randomService: RandomNumberService,
    private errorService: ErrorService,
    private audioService: AudioRecordService,
    private dataRecordService: DataRecordService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    this.indexOfVideo = 1;
    this.indexOfEndVideo = 0;
    this.indexOfFeedbackVideo = 1;
    this.checkData = false;
    this.practiceIndex = 0;
    this.realTrialIndex = 0;

    this.enableRecording = false;

    this.canvasWidth = window.innerWidth;
    this.canvasHeight = NonWordComponent.canvasHeight + 2;

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
  @ViewChild("volumeCanvas") volumeCanvas: ElementRef;
  bridge: any[];

  candyBridge: any[];

  private indexOfVideo;
  private indexOfEndVideo;
  private indexOfFeedbackVideo;
  private currentList;
  private realTrialIndex: number;
  private checkData;
  private endTime;
  private startTime;
  private practiceIndex;
  private practiceFeedbackVideoNames;
  private enableRecording;

  //canvas setting
  private canvasWidth;
  private canvasHeight;
  private volumeContext: CanvasRenderingContext2D;
  private trackVolumeInterval;
 

  startGame(): void{
    this.fetchProgress();
  }

  playIntroVideos(): void{
    let videoId = "intro-video";
    let source = NonWordComponent.introVideoSource;
    if(this.indexOfVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playIntroVideos);
      this.gameMode = "testMic";
    }
  }

  startTestRecording(): void{
    setTimeout(() => {
      this.initVolumeCanvas();
      window.addEventListener("keydown", this.stopTestRecording);
    }, 0);
  }

  startPracticeTrials = () => {
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Practice",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/nonword/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          this.currentList = NonWordComponent.practiceTrialList[this.practiceIndex];
          this.playNonWordAudio();
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
  }

  startRealTrials = () => {
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": "In Progress",
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/nonword/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.initCandyBridge();
          for(let i = 0; i < this.realTrialIndex; i++){
            this.candyBridge[i].visible = true;
          }
          this.gameMode = "real_trial";
          this.currentList = NonWordComponent.realTrialList[this.realTrialIndex];
          this.candyBridge[this.realTrialIndex].visible = true;
          this.playNonWordAudio();
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    );
  }

  playFeedbackAfterTestRecording = () => {
    let videoId = "test-feedback-video";
    if(this.indexOfFeedbackVideo < NonWordComponent.feedbackAfterTestRecording.length){
      document.getElementById(videoId).setAttribute("src", NonWordComponent.feedbackAfterTestRecording[this.indexOfFeedbackVideo]); 
      document.getElementById(videoId).removeEventListener("ended", this.playFeedbackAfterTestRecording);   
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      document.getElementById(videoId).addEventListener("ended", this.startPracticeTrials);
      this.indexOfFeedbackVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playFeedbackAfterTestRecording);
    }
  }

  showExitBtn(){
    document.getElementById("game-achievement-div").style.display = "block";
    document.getElementById("end-btn").style.display = "block";
  }

  playEndingVideo(): void{
    let videoId = "end-video";
    if(this.indexOfEndVideo < 1){
      document.getElementById(videoId).setAttribute("src", NonWordComponent.feedbackVideoSource["ending_2"]); 
      document.getElementById(videoId).removeEventListener("ended", this.playEndingVideo);   
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      document.getElementById(videoId).addEventListener("ended", this.showExitBtn);
      this.indexOfEndVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playEndingVideo);
    }
  }

  private stopTestRecording = (event) => {
    if(this.enableRecording){
      let keyCode = event.keyCode;
      //press c(67) or m(77)
      if(keyCode == 67 || keyCode == 77){
        window.clearInterval(this.trackVolumeInterval);
        window.removeEventListener("keydown", this.stopTestRecording);
        this.audioService.stopRecord();
        this.enableRecording = false;
        //send audio blob to back
        let blob = this.audioService.getAudioBlobData();
        let fileName = "IMREADYTEST";
        let formData = new FormData();
        formData.append(fileName, blob, fileName);
        formData.append("childId", sessionStorage.getItem("childId"));
        formData.append("experimenter", sessionStorage.getItem("experimenter"));
        formData.append("grade", sessionStorage.getItem("grade"));
        this.audioService.sendAudioData("./api/nonword/audio", formData).subscribe(
          (data) => {
            if(data != undefined){
              this.gameMode = "testMicAfter";
            }else{
              this.errorService.internalError();
            }
          },
          (err) => {
            this.errorService.networkError();
          }
        )
        // this.audioService.sendRecordData("./api/nonword/audio", formData, () => {
        //   this.gameMode = "testMicAfter";
        // });
      }
    }
  }

  private stopRecording = (event) => {
    if(this.enableRecording){
      let keyCode = event.keyCode;
      if(keyCode == 67 || keyCode == 77){
        window.clearInterval(this.trackVolumeInterval);
        window.removeEventListener("keydown", this.stopRecording);
        this.audioService.stopRecord();
        this.enableRecording = false;
        let blob = this.audioService.getAudioBlobData();
        let fileName = "";
        if(this.gameMode == "practice_trial"){
          let i = this.practiceIndex + 1;
          fileName = "TrialIndex" + i + "_" + "NonWord" + this.currentList + "_Practice";
        }else{
          let i = this.realTrialIndex + 1;
          fileName = "TrialIndex" + i + "_" + "NonWord" + this.currentList;
        }
        let formData = new FormData();
        formData.append(fileName, blob, fileName);
        formData.append("childId", sessionStorage.getItem("childId"));
        formData.append("experimenter", sessionStorage.getItem("experimenter"));
        formData.append("grade", sessionStorage.getItem("grade"));
        this.audioService.sendAudioData("./api/nonword/audio", formData).subscribe(
          (data) => {
            if(data != undefined){
              //send data
              let document = {
                "documentId": data["documentId"],
                "fileName": data["fileName"]
              };
              if(this.gameMode == "practice_trial"){
                this.sendData(document, keyCode == 67);
              }else{
                this.sendData(document, keyCode == 67);
              }
            }
          },
          (err) => {
            this.errorService.networkError();
          }
        )
      }
    }
  }

  private sendData(document, userAnswerCorrect){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "stimuliInput": this.currentList,
      "userInputFileName": document.fileName,
      "userInputFileUid": document.documentId,
      "startTime": this.startTime,
      "endTime": this.endTime,
      "needCheck": this.checkData,
      "userAnswerCorrect": userAnswerCorrect
    };
    if(this.gameMode == "practice_trial"){
      formData["currentListIndex"] = this.practiceIndex;
      formData["trialType"] = "practice";
    }else{
      formData["currentListIndex"] = this.realTrialIndex;
    }
    this.dataRecordService.sendUserData("./api/nonword", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
          if(this.gameMode == "practice_trial"){
            this.showFeedback(userAnswerCorrect);
          }else{
            this.goToNextTrial();
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

  private goToNextTrial = () => {
    if(this.realTrialIndex < NonWordComponent.realTrialList.length - 1){
      this.realTrialIndex++;
      this.checkData = false;
      this.currentList = NonWordComponent.realTrialList[this.realTrialIndex];
      this.candyBridge[this.realTrialIndex].visible = true;
      this.playNonWordAudio();
    }else{
      //game over
      this.endGame();
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "gameStatus": this.gameMode,
      "grade": sessionStorage.getItem("grade")
    }
    this.gameProgressService.updateGameProgress("./api/nonword/gameover", formData, () => {}).subscribe(
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

  private showFeedback = (userAnswerCorrect) => {
    document.getElementById("feedback-video-div").style.display = "block";
    if(userAnswerCorrect){
      if(this.practiceIndex < NonWordComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["correct_great_job"]);  
      }else{
        this.playPracticeFeedbackVideos(["correct_great_job_3"]);  
      }
    }else{
      if(this.practiceIndex < NonWordComponent.practiceTrialList.length - 1){
        this.playPracticeFeedbackVideos(["uhoh"]);  
      }else{
        this.playPracticeFeedbackVideos(["uhoh_3_1", "uhoh_3_2"]);
      }
    }
  }

  private playPracticeFeedbackVideos(videoNames){
    let videoId = "feedback-video";
    this.practiceFeedbackVideoNames = videoNames;
    document.getElementById(videoId).setAttribute("src", NonWordComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
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
    document.getElementById(videoId).setAttribute("src", NonWordComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextPracticeTrial);
    }
  }

  private playNonWordAudio(){
    let audio = new Audio();
    audio.src = "../../../assets/audios/non-word/" + this.currentList + ".mp3";
    audio.load();
    audio.play();

    audio.addEventListener("ended", () => {
      this.initVolumeCanvas();
      window.addEventListener("keydown", this.stopRecording);
    })
  }

  private goToNextPracticeTrial = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextPracticeTrial);
    if(this.practiceIndex < NonWordComponent.practiceTrialList.length - 1){
      this.practiceIndex++;
      this.checkData = false;
      document.getElementById("feedback-video-div").style.display = "none";
      this.currentList = NonWordComponent.practiceTrialList[this.practiceIndex];
      this.playNonWordAudio();
    }else{
      this.gameMode = "real_trial";
      this.startRealTrials();
    }
  }

  private initVolumeCanvas = () => {
    (<HTMLCanvasElement>this.volumeCanvas.nativeElement).width = this.canvasWidth;
    (<HTMLCanvasElement>this.volumeCanvas.nativeElement).height = this.canvasHeight;
    this.volumeContext = (<HTMLCanvasElement>this.volumeCanvas.nativeElement).getContext("2d");
    this.volumeContext.lineWidth = NonWordComponent.canvasBorderW;
    this.volumeContext.rect(NonWordComponent.canvasBorderW, NonWordComponent.canvasBorderW, 
      this.canvasWidth - 2 * NonWordComponent.canvasBorderW, NonWordComponent.canvasHeight - 2 * NonWordComponent.canvasBorderW);
    this.volumeContext.stroke();
    this.enableRecording = true;
    this.audioService.startRecord();
    this.trackVolumeInterval = window.setInterval(this.updateVolumeMeter, 10);
  }

  private updateVolumeMeter = () => {
    let volume = this.audioService.getVolume();
    this.volumeContext.clearRect(NonWordComponent.canvasBorderW, NonWordComponent.canvasBorderW, 
      this.canvasWidth - 2 * NonWordComponent.canvasBorderW, NonWordComponent.canvasHeight - 2 * NonWordComponent.canvasBorderW);
    this.volumeContext.fillStyle = "green";
    this.volumeContext.fillRect(NonWordComponent.canvasBorderW, NonWordComponent.canvasBorderW, 
      volume * (this.canvasWidth - 2 * NonWordComponent.canvasBorderW), NonWordComponent.canvasHeight - 2 * NonWordComponent.canvasBorderW);
  }

  private initCandyBridge(){
    this.candyBridge = [
      {img: "candy.png", visible: false},
      {img: "chocolate.png", visible: false},
      {img: "candy.png", visible: false},
      {img: "candy-stick.png", visible: false},
      {img: "candy-bar.png", visible: false},
      {img: "chocolate.png", visible: false},
      {img: "candy.png", visible: false},
      {img: "candy-stick.png", visible: false},
      {img: "candy-stick.png", visible: false},
      {img: "candy-bar.png", visible: false},
      {img: "candy.png", visible: false},
      {img: "chocolate.png", visible: false},
      {img: "candy.png", visible: false},
      {img: "candy-stick.png", visible: false},
      {img: "candy-bar.png", visible: false},
      {img: "chocolate.png", visible: false},
    ];
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/nonword/progress", formData, () => {}).subscribe(
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
    this.httpService.post("./api/nonword/syncdata", formData).subscribe(
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

  //consts
  private static introVideoSource = [
    "../../../assets/videos/non-word/Intro_1.mp4",
    "../../../assets/videos/non-word/Intro_2.mp4",
    "../../../assets/videos/non-word/Intro_3.mp4",
    "../../../assets/videos/non-word/Intro_4.mp4",
    "../../../assets/videos/non-word/Intro_5.mp4",
    "../../../assets/videos/non-word/Intro_6.mp4",
    "../../../assets/videos/non-word/Intro_7.mp4",
    // "../../../assets/videos/non-word/Intro_8.mp4",
    
  ];
  private static feedbackVideoSource = {
    "correct_great_job" : "../../../assets/videos/non-word/Feedback_Great_Job.mp4",
    "correct_great_job_3" : "../../../assets/videos/non-word/Feedback_Great_Job_3.mp4",
    "uhoh": "../../../assets/videos/non-word/Feedback_Uhoh.mp4",
    "uhoh_3_1": "../../../assets/videos/non-word/Feedback_Uhoh_3_1.mp4",
    "uhoh_3_2": "../../../assets/videos/non-word/Feedback_Uhoh_3_2.mp4",
    "ending_1": "../../../assets/videos/non-word/Ending_1.mp4",
    "ending_2": "../../../assets/videos/non-word/Ending_2.mp4",
  };
  private static feedbackAfterTestRecording = [
    "../../../assets/videos/non-word/Intro_9.mp4",
    "../../../assets/videos/non-word/Intro_10.mp4",
    // "../../../assets/videos/non-word/Im_Ready_Feedback_1.mp4",
    // "../../../assets/videos/non-word/Im_Ready_Feedback_2.mp4"
  ];
  private static practiceTrialList = ["yav", "bUfnik", "hWktcf"];
  private static realTrialList = [
    "vob",
    "nWg",
    "dUf",
    "pOv",
    "gEnfad",
    "n@mbog",
    "blvyEn",
    "wlft@f",
    "gYmtifnk",
    "hWdyekgev",
    "wivncktuf",
    "yitvcdgum",
    "hOtyekwiftcg",
    "yekbcntugwiv",
    "hUdbektifag",
    "wUdwefyipgud",
  ];
  private static canvasBorderW = 5;
  private static canvasHeight = 60;
}