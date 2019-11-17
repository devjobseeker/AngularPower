import { Component, OnInit } from '@angular/core';

import { ProfileService } from '../../services/profile/profile.service';
import { ErrorService } from '../../services/errors/error.service';
import { RandomNumberService } from '../../services/utils/random-number.service';
import { GameProgressService } from '../../services/game-progress/game-progress.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(
    private profileService: ProfileService,
    private errorService: ErrorService,
    private randomService: RandomNumberService,
    private gameProgressService: GameProgressService,
    private httpService: GeneralHttpService
  ) { 
    this.currentStudent = sessionStorage.getItem("childId");
    this.grade = sessionStorage.getItem("grade");
  }

  ngOnInit() {
    this.indexOfVideo = 1;
    this.indexOfContinueVideo = 1;
    this.fetchStudentInfo();
    //this.gameMode = "game";

    this.initSyncData();
  }

  currentStudent: String;
  grade: String;
  gameMode: String;
  pirateGender: String;
  pirateCharacter: String;
  pirateOptions: String[];
  listOfGames;
  gameIds;
  showGameStore;

  //private variables
  private indexOfVideo;
  private indexOfContinueVideo;
  private continueVideosSource;
  private noOfCompleteGames;

  playDay1IntroVideos(): void{
    let videoId = "intro-video";
    let source = DashboardComponent.day1IntroVideosSource;
    if(this.indexOfVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      let videoSource = document.getElementById(videoId).getAttribute("src");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playDay1IntroVideos);
      this.gameMode = "intro-gender";
      let audio = new Audio();
      audio.src = "../../../assets/audios/introduction/Female_Or_Male_1.mp3";
      audio.load();
      audio.play();
      audio.addEventListener("ended", () => {
        let audio2 = new Audio();
        audio2.src = "../../../assets/audios/introduction/Female_Or_Male_2.mp3";
        audio2.load();
        audio2.play();
        audio2.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
      })
      // audio.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
    }
  }

  playDay2IntroVideos(): void{
    let videoId = "intro-video";
    let source = DashboardComponent.day2IntroVideosSource;
    if(this.indexOfVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      let videoSource = document.getElementById(videoId).getAttribute("src");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playDay2IntroVideos);
      this.gameMode = "intro2-gender";
      let audio = new Audio();
      audio.src = "../../../assets/audios/introduction/day2/Female_Or_Male_1.mp3";
      audio.load();
      audio.play();
      audio.addEventListener("ended", () => {
        let audio2 = new Audio();
        audio2.src = "../../../assets/audios/introduction/day2/Female_Or_Male_2.mp3";
        audio2.load();
        audio2.play();
        audio2.addEventListener("ended", () => {
          let audio3 = new Audio();
          audio3.src = "../../../assets/audios/introduction/day2/Female_Or_Male_3.mp3";
          audio3.load();
          audio3.play();
          audio3.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
        })
      })
      // let audio = new Audio();
      // audio.src = "../../../assets/audios/introduction/Female_Or_Male.mp3";
      // audio.load();
      // audio.play();
      // audio.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
      // let formData = {
      //   "childId": this.currentStudent,
      //   "pirateCharacter": this.pirateCharacter,
      //   "intro2Completed": true,
      //   "grade": sessionStorage.getItem("grade")
      // }
      // let result;
      // this.profileService.updateStudentProfile("./api/dashboard/intro2", formData, () => {}).subscribe(
      //   (data) => {
      //     result = data;
      //     if(result){
      //       this.goToDay2Games();
      //       this.showGameStore = false;
      //     }else{
      //       this.errorService.internalError();
      //     }
      //   },
      //   (err) => {
      //     this.errorService.networkError();
      //   }
      // )
      
    }
  }

  clickNextBtn(): void{
    if(this.gameMode == "intro-gender"){
      this.gameMode = "intro-pirates";
      if(this.pirateGender === "f"){
        this.pirateOptions = ["female-pirate-1", "female-pirate-2", "female-pirate-3", "female-pirate-4", "female-pirate-5"];
      }else{
        this.pirateOptions = ["male-pirate-1", "male-pirate-2", "male-pirate-3", "male-pirate-4", "male-pirate-5"];
      }
      let audio = new Audio();
      audio.src = "../../../assets/audios/introduction/Pick_The_Pirate.mp3";
      audio.load();
      audio.play();
      audio.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
    }else if(this.gameMode == "intro-pirates"){
      this.gameMode = "intro-after-pirates";
      //play video. then go to game mode
      setTimeout(() => {
        let audio = new Audio();
        audio.src = "../../../assets/audios/introduction/Great_Choice.mp3";
        audio.load();
        audio.play();
        audio.addEventListener("ended", () => {
          //play intro 5 and 6
          document.getElementById("great-choice-div").style.display = "none";
          document.getElementById("intro-continue-div").style.display = "block";
          this.indexOfContinueVideo = 1;
          this.continueVideosSource = DashboardComponent.introContinueVideoSource;
          this.playContinueVideos();
        })
      }, 0);
    }
  }

  clickNextBtn2(): void{
    if(this.gameMode == "intro2-gender"){
      this.gameMode = "intro2-pirates";
      if(this.pirateGender === "f"){
        this.pirateOptions = ["female-pirate-1", "female-pirate-2", "female-pirate-3", "female-pirate-4", "female-pirate-5"];
      }else{
        this.pirateOptions = ["male-pirate-1", "male-pirate-2", "male-pirate-3", "male-pirate-4", "male-pirate-5"];
      }
      let audio = new Audio();
      audio.src = "../../../assets/audios/introduction/day2/Pick_The_Pirate.mp3";
      audio.load();
      audio.play();
      audio.addEventListener("ended", () => {document.getElementById("next-btn").style.display = "block"});
    }else if(this.gameMode == "intro2-pirates"){
      this.gameMode = "intro2-after-pirates";
      //play video. then go to game mode
      setTimeout(() => {
        let audio = new Audio();
        audio.src = "../../../assets/audios/introduction/day2/Great_Choice.mp3";
        audio.load();
        audio.play();
        audio.addEventListener("ended", () => {
          //play intro 5 and 6
          document.getElementById("great-choice-div").style.display = "none";
          document.getElementById("intro-continue-div").style.display = "block";
          this.indexOfContinueVideo = 0;
          this.continueVideosSource = DashboardComponent.intro2ContinueVideoSource;
          this.playContinueVideos();


        })
      }, 0);
    }
  }

  startGameBtn():void{
    let formData = {
      "childId": this.currentStudent,
      "pirateCharacter": this.pirateCharacter,
      "intro1Completed": true,
      "grade": sessionStorage.getItem("grade")
    }
    let result;
    this.profileService.updateStudentProfile("./api/dashboard/intro1", formData, () => {}).subscribe(
      (data) => {
        result = data;
        if(result){
          this.gameMode = "game";
          this.showGameStore = false;
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  startGame2Btn():void{
    let formData = {
      "childId": this.currentStudent,
      "pirateCharacter": this.pirateCharacter,
      "intro2Completed": true,
      "grade": sessionStorage.getItem("grade")
    }
    let result;
    this.profileService.updateStudentProfile("./api/dashboard/intro2", formData, () => {}).subscribe(
      (data) => {
        result = data;
        if(result){
          this.gameMode = "game";
          this.showGameStore = false;
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  goToDay1Games():void{
    this.gameMode = "game-day1";
    let list = [];
    for(let i = 0; i < DashboardComponent.day1Games.length; i++){
      list.push(i);
    }
    list = this.randomService.randomizeAList(list);
    
    this.listOfGames = {};
    this.gameIds = [];

    for(let i = 0; i < list.length; i++){
      this.gameIds.push(DashboardComponent.day1Games[list[i]].id); 
      this.listOfGames[this.gameIds[i]] = {
        "name": DashboardComponent.day1Games[list[i]].name,
        "link": DashboardComponent.day1Games[list[i]].link,
        "order": ""
      }
    }
    this.fetchListGameProgress("day1");
  }

  goToDay2Games():void{
    let student;
    let formData = {
      "studentId": this.currentStudent,
      "grade": this.grade
    };
    this.profileService.fetchStudentProfile("./api/dashboard/fetch", formData, () => {}).subscribe(
      (data) => {
        student = data;
        if(student != null){
          if(student.pirateCharacter != undefined){
            sessionStorage.setItem("pirateCharacter", student.pirateCharacter);
          }
          if(student.day1Completed != undefined && student.day1Completed
            && (student.intro2Completed == undefined || student.intro2Completed == false)){
              this.gameMode = "intro-2";
          }else{
            this.gameMode = "game-day2";
            let list = [];
            for(let i = 0; i < DashboardComponent.day2Games.length; i++){
              list.push(i);
            }
            list = this.randomService.randomizeAList(list);
            this.listOfGames = {};
            this.gameIds = [];
            for(let i = 0; i < list.length; i++){
              this.gameIds.push(DashboardComponent.day2Games[list[i]].id);
              this.listOfGames[this.gameIds[i]] = {
                "name": DashboardComponent.day2Games[list[i]].name,
                "link": DashboardComponent.day2Games[list[i]].link,
                "order": ""
              }
            }
            this.fetchListGameProgress("day2");
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

  backToGame(): void{
    this.gameMode = "game";
    this.showGameStore = false;
  }

  private fetchListGameProgress(whichDay){
    let result;
    this.noOfCompleteGames = 0;
    let url = "./api/dashboard/gamelist" + "?studentId=" + this.currentStudent + "&grade=" + this.grade;
    this.gameProgressService.fetchListGameProgress(url, this.gameIds, () => {}).subscribe(
      (data) => {
        result = data;
        if(result == null){
          this.errorService.internalError();
        }else if(result.length > 0){
          //map
          let order = 1;
          for(let i = 0; i < result.length; i++){
            if(result[i].gameStatus == "Complete"){
              this.noOfCompleteGames++;
              this.listOfGames[result[i].gameId].order = (order++).toString();
            }
          }
          if(whichDay == "day1"){
            this.showGameStore = this.noOfCompleteGames == DashboardComponent.day1Games.length;  
          }else{
            this.showGameStore = this.noOfCompleteGames == DashboardComponent.day2Games.length;
          }
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  // private playVideos(videoNames){
  //   if(videoNames.length > 0){
  //     document.getElementById("intro-continue").setAttribute("src", videoNames[0]);    
  //     (document.getElementById("intro-continue") as HTMLVideoElement).load();
  //     (document.getElementById("intro-continue") as HTMLVideoElement).play();
  //   }
  //   videoNames.shift();
  //   if(videoNames.length > 0){ 
  //     document.getElementById("intro-continue").addEventListener("ended", () => { this.playVideos(videoNames) });
  //   }else{
  //     document.getElementById("intro-continue").removeEventListener("ended", () => { this.playVideos(videoNames) });
  //     document.getElementById("intro-continue").addEventListener("ended", () => {
  //     document.getElementById("start-game-btn").style.display =" block";

  //       // document.getElementById("intro-continue-div").style.display = "none";
  //       // document.getElementById("intro-end-div").style.display = "block";
  //       // let audio = new Audio();
  //       // audio.src = "../../../assets/audios/introduction/Ready_To_Play.mp3";
  //       // audio.load();
  //       // audio.play();
  //       // audio.addEventListener("ended", () => {
  //       //   document.getElementById("start-game-btn").style.display =" block";
  //       // })
  //     });
  //   }
  // }

  private playContinueVideos = () => {
    let videoId = "intro-continue";
    let source = this.continueVideosSource;
    if(this.indexOfContinueVideo < source.length){
      document.getElementById(videoId).setAttribute("src", source[this.indexOfContinueVideo]);
      document.getElementById(videoId).setAttribute("type", "video/mp4");
      let videoSource = document.getElementById(videoId).getAttribute("src");
      (document.getElementById(videoId) as HTMLVideoElement).load();
      (document.getElementById(videoId) as HTMLVideoElement).play();
      this.indexOfContinueVideo++;
    }else{
      document.getElementById(videoId).removeEventListener("ended", this.playContinueVideos);
      document.getElementById("start-game-btn").style.display = "block";
    }
  }
  
  private fetchStudentInfo(){
    let student;
    let formData = {
      "studentId": this.currentStudent,
      "grade": this.grade
    };
    this.profileService.fetchStudentProfile("./api/dashboard/fetch", formData, () => {})
      .subscribe(
        (data) => {
          student = data;
          if(student != null){
            if(student.pirateCharacter != undefined){
              sessionStorage.setItem("pirateCharacter", student.pirateCharacter);
            }
            if(student.intro1Completed == undefined || student.intro1Completed == false){
              this.gameMode = "intro-1";
            }else{
              this.gameMode = "game";
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
    this.httpService.post("./api/dashboard/syncdata", formData).subscribe(
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
    "../../../assets/videos/introduction/Intro_1.mp4",
    "../../../assets/videos/introduction/Intro_2.mp4",
    "../../../assets/videos/introduction/Intro_3.mp4",
    "../../../assets/videos/introduction/Intro_4.mp4",
  ];
  private static day1IntroVideosSource = [
    "../../../assets/videos/introduction/day1/Intro_1.mp4",
    "../../../assets/videos/introduction/day1/Intro_2.mp4",
    "../../../assets/videos/introduction/day1/Intro_3.mp4",
    "../../../assets/videos/introduction/day1/Intro_4.mp4",
    "../../../assets/videos/introduction/day1/Intro_5.mp4",
  ];
  private static day2IntroVideosSource = [
    "../../../assets/videos/introduction/day2/Intro_1.mp4",
  ]
  private static introContinueVideoSource = [
    "../../../assets/videos/introduction/day1/Intro_6.mp4",
    "../../../assets/videos/introduction/day1/Intro_7.mp4",
    "../../../assets/videos/introduction/day1/Intro_8.mp4",
    "../../../assets/videos/introduction/day1/Intro_9.mp4",
    "../../../assets/videos/introduction/day1/Intro_10.mp4",
    "../../../assets/videos/introduction/day1/Intro_11.mp4",
    "../../../assets/videos/introduction/day1/Intro_12.mp4"
  ];
  private static intro2ContinueVideoSource = [
    "../../../assets/videos/introduction/day2/Intro_2.mp4",
    "../../../assets/videos/introduction/day2/Intro_3.mp4",
    "../../../assets/videos/introduction/day2/Intro_4.mp4",
  ];
  private static day1Games = [
    {id: "DS", name: "Digit Span", link: "/app/digitspan", order: ""},
    {id: "VSR", name: "Visual Span Running", link: "/app/visualspanrunning", order: ""},
    {id: "LS", name: "Location Span", link: "/app/locationspan", order: ""},
    {id: "NUA", name: "Number Updating - Auditory", link: "/app/numberupdateauditory", order: ""},
    {id: "RDV", name: "Repetition Detection - Visual", link: "/app/repetitionvisual", order: ""},
    {id: "PBS", name: "Phonological Binding Span", link: "/app/phonological", order: ""},
    {id: "VBS", name: "Visual Binding", link: "/app/visualbinding", order: ""},
  ];
  private static day2Games = [
    {id: "DSR", name: "Digit Span Running", link: "/app/digitspanrunning", order: ""},
    {id: "VS", name: "Visual Span", link: "/app/visualspan", order: ""},
    {id: "LSR", name: "Location Span Running", link: "/app/locationspanrunning", order: ""},
    {id: "NUV", name: "Number Updating - Visual", link: "/app/numberupdatevisual", order: ""},
    {id: "RDA", name: "Repetition Detection - Auditory", link: "/app/repetitionauditory", order: ""},
    {id: "NR", name: "Nonword Repetition", link: "/app/nonword", order: ""},
    {id: "CMB", name: "Cross Modal Binding", link: "/app/crossmodalbinding", order: ""},
  ]
}
