import { Component, OnInit } from '@angular/core';
import { trigger, transition, animate, style } from '@angular/animations';

import { DataRecordService } from '../../services/utils/data-record.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

import { AppConfig } from '../../app.config';

@Component({
  selector: 'app-repetition-auditory',
  templateUrl: './repetition-auditory.component.html',
  styleUrls: ['./repetition-auditory.component.css'],
  animations: [
    trigger("gameResultSlideIn", [
      transition(":enter", [
        style({transform: "translateX(-100%)"}),
        animate("500ms ease-in", style({transform: "translateY(0%"}))
      ])
    ])
  ]
})
export class RepetitionAuditoryComponent implements OnInit {

  constructor(
    private gameProgressService: GameProgressService,
    private dataRecordService: DataRecordService,
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    this.indexOfVideo = 0;
    this.checkData = false;
    this.userPressYesBtn = false;
    this.yesBtnDisabled = true;
    this.gameMode = "start"; 
    this.blockIndex = 0;
    this.trialIndex = 0;
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
  yesBtnDisabled;
  showCoins: boolean;

  //private variables
  private indexOfVideo;
  private currentList: number[];
  private currentAudio: number;
  private trialIndex: number;
  private blockIndex: number;
  private checkData;
  private startTime;
  private endTime;
  private userAnswerCorrectPerBlock;
  private userPressYesBtn;
  
  private repetitionCount;
  private totalCorrectCount;  //total real trial correct count
  private totalTrialCount;  //total real trial count

  private practiceFeedbackVideoNames;

  startGame(): void{
    this.fetchProgress();
  }

  startPracticeTrials(): void{
    this.showCoins = false;
    this.userAnswerCorrectPerBlock = true;
    if(this.repetitionCount === 2){
      this.currentList = RepetitionAuditoryComponent.practiceDoubleList[this.blockIndex];
    }else{
      this.currentList = RepetitionAuditoryComponent.practiceTripleList[this.blockIndex];
    }
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": "In Practice",
      "repetitionCount": this.repetitionCount
    }
    this.gameProgressService.updateGameProgress("./api/repetitionauditory/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.gameMode = "practice_trial";
          if(this.repetitionCount === 2){
            this.playInitialOneAudio();
          }else{
            setTimeout(() => {
              this.playInitialTwoAudios();  
            }, 0);
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

  startRealTrials(): void{
    this.userAnswerCorrectPerBlock = true;
    if((this.repetitionCount === 2 && this.trialIndex >= RepetitionAuditoryComponent.realDoubleList[this.blockIndex].length)
        || (this.repetitionCount === 3 && this.trialIndex >= RepetitionAuditoryComponent.realTripleList[this.blockIndex].length)){
      this.blockIndex++;
      this.trialIndex = 0;
    }else{
      this.trialIndex = 0;
    }
    if(this.repetitionCount === 2 && this.blockIndex >= RepetitionAuditoryComponent.realDoubleList.length){
      this.repetitionCount = 3;
      this.blockIndex = 0;
      this.gameMode ="intro";
      setTimeout(() => {
        this.playTripleIntroVideos();
      }, 0);
    }else{
      if(this.repetitionCount === 2){
        this.currentList = RepetitionAuditoryComponent.realDoubleList[this.blockIndex];
      }else{
        this.currentList = RepetitionAuditoryComponent.realTripleList[this.blockIndex];
      }
      let formData = {
        "childId": sessionStorage.getItem("childId"),
        "grade": sessionStorage.getItem("grade"),
        "gameStatus": "In Progress",
        "repetitionCount": this.repetitionCount
      }
      this.gameProgressService.updateGameProgress("./api/repetitionauditory/progress", formData, () => {}).subscribe(
        (data) => {
          let result = data;
          if(result != undefined){
            // document.getElementById("robot-div").style.display = "block";
            this.gameMode = "real_trial";
            if(this.repetitionCount === 2){
              this.playInitialOneAudio();
            }else{
              this.playInitialTwoAudios();
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
  }

  pressYesBtn(): void{
    this.userPressYesBtn = true;
    this.yesBtnDisabled = true;
    this.endTime = new Date().getTime();
    document.getElementById("yes-btn").style.border = "3px solid #000"; 
    setTimeout(() => {
      document.getElementById("yes-btn").style.border = "none";
    }, 200);
    //go to next trial
    //clearTimeout(this.autoSendDataEvent);
    //clearTimeout(this.autoHideImageEvent);
    //this.sendData(this.playNextAudio);
  }

  playIntroVideos(): void{
    let videoId = "intro-video";
    this.indexOfVideo = 0;
    document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.doubleIntroVideoSource[0]);
    document.getElementById(videoId).setAttribute("type", "video/mp4");
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.indexOfVideo++;
    document.getElementById("intro-video").addEventListener("ended", this.playNextDoubleIntroVideo);
  }

  playTripleIntroVideos(): void{
    this.gameMode ="intro";
    setTimeout(() => {
      let videoId = "intro-video"; 
      this.indexOfVideo = 0;
      document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.tripleIntroVideoSource[0]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
      document.getElementById("intro-video").addEventListener("ended", () => {
        this.showCoins = false;
        this.playNextTripleIntroVideo();
      });
    }, 0);
  }

  playDoubleCompleteVideos(): void{
    let videoId = "intro-video"; 
    this.indexOfVideo = 0;
    document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.doubleCompleteVideoSource[0]);
    document.getElementById(videoId).setAttribute("type", "video/mp4");
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.indexOfVideo++;
    document.getElementById(videoId).addEventListener("ended", () => {
      this.showCoins = false;
      this.playNextDoubleCompleteVideo();
    });
  }

  showExitBtn(): void{
    document.getElementById("end-btn").style.display = "block";
  }

  private playNextAudio = () => {
    this.userPressYesBtn = false;
    this.yesBtnDisabled = true;
    // document.getElementById("green-box-div").style.display = "none"; 
    let showFeedback = false;
    if(this.gameMode === "practice_trial"){
      if(!this.userAnswerCorrectPerBlock){
        showFeedback = true;
      }
    }
    if(showFeedback){
      this.showFeedback();
    }else{
      if(this.trialIndex < this.currentList.length - 1){
        // document.getElementById("robot-div").style.display = "block";
        this.trialIndex++;
        this.currentAudio = this.currentList[this.trialIndex];
        let audio = new Audio();
        audio.src = "../../../assets/audios/repetition-auditory/" + this.currentAudio + ".mp3";
        audio.load();
        audio.play();
        audio.addEventListener("ended", () => {
          // document.getElementById("robot-div").style.display = "none";
          // this.autoHideImageEvent = setTimeout(() => {
          //   document.getElementById("robot-div").style.display = "none";
          // }, 1000);
          this.readyToAnswer();
          this.yesBtnDisabled = false;
          this.startTime = new Date().getTime();
        });
      }else{
        // document.getElementById("robot-div").style.display = "none";
        this.showFeedback();
      }
    }
  }

  private readyToAnswer = () => {
    //show green box for 5 s
    window.addEventListener("keydown", this.pressKeyboard);
    // this.startTime = new Date().getTime();
    // document.getElementById("robot-div").style.display = "none";
    // document.getElementById("green-box-div").style.display = "block";
    //send data
    setTimeout(() => {
        this.sendData(this.playNextAudio);  //send data after 5s if user doesn't press yes btn
    }, RepetitionAuditoryComponent.audioOffInterval);
  }

  private sendData = (callback) => {
    window.removeEventListener("keydown", this.pressKeyboard);
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "experimenter": sessionStorage.getItem("experimenter"),
      "grade": sessionStorage.getItem("grade"),
      "startTime": this.startTime,
      "needCheck": this.checkData,
      "repetitionCount": this.repetitionCount,
      "blockIndex": this.blockIndex,
    }
    let correctResponse = "different";
    if(this.repetitionCount == 3){
      formData["stimuliInput1"] = this.currentList[this.trialIndex - 2];
      formData["stimuliInput2"] = this.currentList[this.trialIndex - 1];
      formData["stimuliInput3"] = this.currentAudio;
      formData["trialIndex"] = this.trialIndex;
      if(formData["stimuliInput1"] === formData["stimuliInput2"] && formData["stimuliInput2"] === formData["stimuliInput3"]){
        correctResponse = "same";
      }
    }else{
      formData["stimuliInput1"] = this.currentList[this.trialIndex - 1];
      formData["stimuliInput2"] = this.currentAudio;
      formData["trialIndex"] = this.trialIndex;
      if(formData["stimuliInput1"] === formData["stimuliInput2"]){
        correctResponse = "same";
      }
    }
    if(this.userPressYesBtn){
      formData["endTime"] = this.endTime;
      formData["userInput"] = "same";
    }else{
      formData["endTime"] = new Date().getTime();
      formData["userInput"] = "different";
    }
    if(this.gameMode === "practice_trial"){
      formData["trialType"] = "practice";
    }
    if(this.userAnswerCorrectPerBlock && correctResponse != formData["userInput"]){
      this.userAnswerCorrectPerBlock = false;
    }

    this.dataRecordService.sendUserData("./api/repetitionauditory", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined && result != null){
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

  private showFeedback = () => {
    let gameOver = false;
    if(this.gameMode === "real_trial" && this.repetitionCount == 3 && this.blockIndex >= RepetitionAuditoryComponent.realTripleList.length - 1){
      gameOver = true;
    }
    if(!gameOver){
      document.getElementById("feedback-video-div").style.display = "block";
      document.getElementById("yes-btn-div").style.display = "none";
      document.getElementById("robot-image-div").style.display = "none";

      if(this.gameMode === "real_trial" && this.repetitionCount == 2 && this.blockIndex >= RepetitionAuditoryComponent.realDoubleList.length - 1){
        //start triple, play triple intro video
        this.repetitionCount = 3;
        this.blockIndex = 0;
        this.trialIndex = 0;
        this.gameMode ="double_complete";
        this.yesBtnDisabled = true;
        setTimeout(() => {
          this.fetchCoins();
        }, 0);
      }else{
        //check block correct or not
        if(this.userAnswerCorrectPerBlock){
          if(this.repetitionCount == 2){
            if(this.blockIndex == 0){
              this.playPracticeFeedbackVideos(["correct_great_job_1"]);
            }else if(this.blockIndex == 1){
              this.playPracticeFeedbackVideos(["correct_great_job_2"]);
            }else{
              this.playPracticeFeedbackVideos(["correct_great_job_3_1", "correct_great_job_3_2"]);
            }
          }else{
            if(this.blockIndex == 0){
              this.playPracticeFeedbackVideos(["3_great_1"]);
            }else if(this.blockIndex == 1){
              this.playPracticeFeedbackVideos(["3_great_1"]);
            }else{
              this.playPracticeFeedbackVideos(["3_great_3_1", "3_great_3_2"]);
            }
          }
        }else{
          if(this.repetitionCount == 2){
            if(this.blockIndex == 0){
              this.playPracticeFeedbackVideos(["oops_1_1", "oops_1_2", "oops_1_3"]);
            }else if(this.blockIndex == 1){
              this.playPracticeFeedbackVideos(["oops_2"]);
            }else{
              this.playPracticeFeedbackVideos(["oops_3_1", "oops_3_2", "oops_3_3"]);
            }
          }else{
            if(this.blockIndex == 0){
              this.playPracticeFeedbackVideos(["3_oops_1_1", "3_oops_1_2", "3_oops_1_3"]);
            }else if(this.blockIndex == 1){
              this.playPracticeFeedbackVideos(["3_oops_2_1", "3_oops_2_2"]);
            }else{
              this.playPracticeFeedbackVideos(["3_oops_3_1", "3_oops_3_2", "3_oops_3_3"]);
            }
          }
        }
      }
    }else{
      this.endGame();
    }
  }

  private goToNextBlock = () => {
    document.getElementById("feedback-video").removeEventListener("ended", this.goToNextBlock);
    this.yesBtnDisabled = true;
    document.getElementById("feedback-video-div").style.display = "none";
    // document.getElementById("robot-div").style.display = "block";
    document.getElementById("robot-image-div").style.display = "block";
    document.getElementById("yes-btn-div").style.display = "block";
    if(this.repetitionCount === 2){
      this.goToNextDoubleBlock();
    }else{
      this.goToNextTripleBlock();
    }
  }

  private goToNextDoubleBlock = () => {
    this.userAnswerCorrectPerBlock = true;
    // document.getElementById("feedback-video-div").style.display = "none";
    // document.getElementById("robot-div").style.display = "block";
    this.trialIndex = 0;
    this.blockIndex++;
    if(this.gameMode === "practice_trial" && this.blockIndex >= RepetitionAuditoryComponent.practiceDoubleList.length){
      //start real trial
      this.blockIndex = 0;
      this.startRealTrials();
    }else{
      this.currentList = this.gameMode === "practice_trial" 
        ? RepetitionAuditoryComponent.practiceDoubleList[this.blockIndex] : RepetitionAuditoryComponent.realDoubleList[this.blockIndex];
      this.playInitialOneAudio();
    }
  }
  
  private goToNextTripleBlock = () => {
    this.userAnswerCorrectPerBlock = true;
    // document.getElementById("feedback-video-div").style.display = "none";
    // document.getElementById("robot-div").style.display = "block";
    this.trialIndex = 0;
    this.blockIndex++;

    if(this.gameMode === "practice_trial" && this.blockIndex >= RepetitionAuditoryComponent.practiceTripleList.length){
      //start real trial
      this.blockIndex = 0;
      this.startRealTrials();
    }else{
      this.currentList = this.gameMode === "practice_trial" 
        ? RepetitionAuditoryComponent.practiceTripleList[this.blockIndex] : RepetitionAuditoryComponent.realTripleList[this.blockIndex];
      this.playInitialTwoAudios();
    }
  }

  //used for triples
  private playInitialTwoAudios(){
    this.currentAudio = this.currentList[this.trialIndex];
    let audio = new Audio();
    audio.src = "../../../assets/audios/repetition-auditory/" + this.currentAudio + ".mp3";
    audio.load();
    audio.play();
    audio.addEventListener("ended", () => {
      //play the next audio
      // document.getElementById("robot-div").style.display = "none";
      setTimeout(() => {
        // document.getElementById("robot-div").style.display = "block";
        this.trialIndex++;
        this.currentAudio = this.currentList[this.trialIndex];
        let audio1 = new Audio();
        audio1.src = "../../../assets/audios/repetition-auditory/" + this.currentAudio + ".mp3";
        audio1.load();
        audio1.play();
        audio1.addEventListener("ended", () => {
          // document.getElementById("robot-div").style.display = "none";
          setTimeout(this.playNextAudio, RepetitionAuditoryComponent.audioOffInterval);
        });
      }, RepetitionAuditoryComponent.audioOffInterval);
    });
  }

  private playInitialOneAudio(){
    this.currentAudio = this.currentList[this.trialIndex];
    let audio = new Audio();
    audio.src = "../../../assets/audios/repetition-auditory/" + this.currentAudio + ".mp3";
    audio.load();
    audio.play();
    audio.addEventListener("ended", () => {
      // document.getElementById("robot-div").style.display = "none";
      setTimeout(this.playNextAudio, RepetitionAuditoryComponent.audioOffInterval);
    });
  }

  private pressKeyboard = (event) => {
    let keyCode = event.keyCode;
    //keyCode = 106 means user press "*"
    if(keyCode === 106){
      this.checkData = true;
    }
  }

  private endGame(){
    let formData = {
      "childId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade"),
      "gameStatus": this.gameMode
    }
    this.gameProgressService.updateGameProgress("./api/repetitionauditory/gameover", formData, () => {}).subscribe(
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
    document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);    
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length > 0){
      document.getElementById(videoId).addEventListener("ended", this.playPracticeFeedbackVideo);
    }else{
      document.getElementById(videoId).addEventListener("ended", this.goToNextBlock);
    }
  }

  private playPracticeFeedbackVideo = () => {
    let videoId = "feedback-video";
    document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.feedbackVideoSource[this.practiceFeedbackVideoNames[0]]);
    (document.getElementById(videoId) as HTMLVideoElement).load();
    (document.getElementById(videoId) as HTMLVideoElement).play();
    this.practiceFeedbackVideoNames.shift();
    if(this.practiceFeedbackVideoNames.length == 0){
      document.getElementById(videoId).removeEventListener("ended", this.playPracticeFeedbackVideo);
      document.getElementById(videoId).addEventListener("ended", this.goToNextBlock);
    }
  }

  private playNextDoubleIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < RepetitionAuditoryComponent.doubleIntroVideoSource.length){
      document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.doubleIntroVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextDoubleIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private playNextTripleIntroVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < RepetitionAuditoryComponent.tripleIntroVideoSource.length){
      document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.tripleIntroVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextTripleIntroVideo);
      document.getElementById("next-btn").style.display = "block";
    }
  }

  private playNextDoubleCompleteVideo = () => {
    let videoId = "intro-video";
    if(this.indexOfVideo < RepetitionAuditoryComponent.doubleCompleteVideoSource.length){
      document.getElementById(videoId).setAttribute("src", RepetitionAuditoryComponent.doubleCompleteVideoSource[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playNextDoubleCompleteVideo);
      document.getElementById("play-triple-intro-btn").style.display = "block";
    }
  }

  private fetchProgress():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/repetitionauditory/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          if(result["repetitionCount"] != undefined){
            this.repetitionCount = result["repetitionCount"];
          }else{
            this.repetitionCount = 2; //default value
          }
          if(result["gameStatus"] === "In Practice"){
            this.gameMode = "practice_trial";
            this.startPracticeTrials();
          }else if(result["gameStatus"] === "In Progress"){
            this.gameMode = "real_trial";
            this.blockIndex = result["currentGroupIndex"] != undefined ? result["currentGroupIndex"] : 0;
            this.totalCorrectCount = result["totalCorrectCount"] != undefined ? result["totalCorrectCount"] : 0;
            this.totalTrialCount = result["totalTrialCount"] != undefined ? result["totalTrialCount"] : 0;
            if(result["currentListIndex"] != undefined){
              this.trialIndex = result["currentListIndex"];
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

  private fetchCoins():void{
    let formData = {
      "studentId": sessionStorage.getItem("childId"),
      "grade": sessionStorage.getItem("grade")
    };
    this.gameProgressService.fetchGameProgress("./api/repetitionauditory/progress", formData, () => {}).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.coins = result["coins"];
          this.rocks = result["rocks"];
          this.showCoins = true;
          //show coins and rocks
          this.playDoubleCompleteVideos();
          //this.playTripleIntroVideos();
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
    this.httpService.post("./api/repetitionauditory/syncdata", formData).subscribe(
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

  //const
  private static doubleIntroVideoSource = [
    "../../../assets/videos/repetition-auditory/Intro_1.mp4",
    "../../../assets/videos/repetition-auditory/Intro_2.mp4",
    "../../../assets/videos/repetition-auditory/Intro_3.mp4",
    "../../../assets/videos/repetition-auditory/Intro_4.mp4",
    "../../../assets/videos/repetition-auditory/Intro_5.mp4",
    "../../../assets/videos/repetition-auditory/Intro_6.mp4",
    "../../../assets/videos/repetition-auditory/Intro_7.mp4",
    "../../../assets/videos/repetition-auditory/Intro_8.mp4",
    "../../../assets/videos/repetition-auditory/Intro_9.mp4",
    "../../../assets/videos/repetition-auditory/Intro_10.mp4"
  ];
  private static tripleIntroVideoSource = [
    "../../../assets/videos/repetition-auditory/Triples/Intro_1.mp4",
    "../../../assets/videos/repetition-auditory/Triples/Intro_2.mp4",
    "../../../assets/videos/repetition-auditory/Triples/Intro_3.mp4",
    "../../../assets/videos/repetition-auditory/Triples/Intro_4.mp4",
  ];
  private static doubleCompleteVideoSource = [
    "../../../assets/videos/repetition-auditory/Ending_Doubles_1.mp4",
    "../../../assets/videos/repetition-auditory/Ending_Doubles_2.mp4",
  ]
  private static feedbackVideoSource = {
    "correct_great_job_1" : "../../../assets/videos/repetition-auditory/Feedback_Great_Job_1.mp4",
    "correct_great_job_2" : "../../../assets/videos/repetition-auditory/Feedback_Great_Job_2.mp4",
    "correct_great_job_3_1" : "../../../assets/videos/repetition-auditory/Feedback_Great_Job_3_1.mp4",
    "correct_great_job_3_2" : "../../../assets/videos/repetition-auditory/Feedback_Great_Job_3_2.mp4",
    "oops_1_1": "../../../assets/videos/repetition-auditory/Feedback_Oops_1_1.mp4",
    "oops_1_2": "../../../assets/videos/repetition-auditory/Feedback_Oops_1_2.mp4",
    "oops_1_3": "../../../assets/videos/repetition-auditory/Feedback_Oops_1_3.mp4",
    "oops_2": "../../../assets/videos/repetition-auditory/Feedback_Oops_2.mp4",
    "oops_3_1": "../../../assets/videos/repetition-auditory/Feedback_Oops_3_1.mp4",
    "oops_3_2": "../../../assets/videos/repetition-auditory/Feedback_Oops_3_2.mp4",
    "oops_3_3": "../../../assets/videos/repetition-auditory/Feedback_Oops_3_3.mp4",

    "3_great_1": "../../../assets/videos/repetition-auditory/Feedback_Triple_Great_1.mp4",
    "3_great_3_1": "../../../assets/videos/repetition-auditory/Feedback_Triple_Great_3_1.mp4",
    "3_great_3_2": "../../../assets/videos/repetition-auditory/Feedback_Triple_Great_3_2.mp4",
    "3_oops_1_1": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_1_1.mp4",
    "3_oops_1_2": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_1_2.mp4",
    "3_oops_1_3": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_1_3.mp4",
    "3_oops_2_1": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_2_1.mp4",
    "3_oops_2_2": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_2_2.mp4",
    "3_oops_3_1": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_3_1.mp4",
    "3_oops_3_2": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_3_2.mp4",
    "3_oops_3_3": "../../../assets/videos/repetition-auditory/Feedback_Triple_Oops_3_3.mp4",
    
    "ending": "../../../assets/videos/repetition-auditory/Ending_Great_Job.mp4",
  };
  private static practiceDoubleList = [
    [4, 4],
    [1, 1, 5],
    [4, 4, 4],
  ];
  private static realDoubleList = [
    [1, 1, 1, 5, 5, 3, 1, 4, 4, 4, 2, 2, 2, 5, 5, 3, 3, 3, 5, 1, 1, 1, 4, 4, 4, 1, 1, 5, 2, 5, 5, 3, 1, 5, 5, 5, 2],  //37 count, doubles
    //[5, 5, 5, 4, 4, 4, 5, 4, 4, 4]  //test
  ];
  private static practiceTripleList = [
    [5, 5, 5],
    [2, 2, 4, 4, 4],
    [1, 1, 1, 1]
  ]
  private static realTripleList = [
    [4, 4, 2, 2, 2, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 4, 4, 4, 2, 2, 2, 4, 4, 4, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 1, 1, 1, 1]  //38 count, triples
    //[1, 2, 3, 3, 3, 3, 5, 5] //test
  ]
  private static audioOffInterval = 2500;
}