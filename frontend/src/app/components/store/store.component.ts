import { Component, OnInit } from '@angular/core';
import { trigger, state, transition, animate, style, keyframes } from '@angular/animations';

import { ProfileService } from '../../services/profile/profile.service';
import { StoreService } from '../../services/store/store.service';
import { ErrorService } from '../../services/errors/error.service';

@Component({
  selector: 'app-store',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.css'],
  animations: [
    trigger("imgAnimation", [
      state("small", style({transform: "scale(0.7, 0.7)"})),
      state("large", style({transform: "scale(1.1, 1.1)"})),
      transition("small <=> large", [
        animate("1s")
      ])
    ])
  ]
})
export class StoreComponent implements OnInit {

  constructor(
    private profileService: ProfileService,
    private storeService: StoreService,
    private errorService: ErrorService,
  ) { 
    this.currentStudent = sessionStorage.getItem("childId");
    this.grade = sessionStorage.getItem("grade");
  }

  ngOnInit() {
    //fetch student total coins
    this.fetchStudentInfo();
  }

  //public variables
  currentStudent: String;
  gameMode: String;
  firstRowItems;
  secondRowItems;
  thirdRowItems;
  fourthRowItems;
  selectedItem;
  totalCoins;
  notEnoughMoney: Boolean;
  purchasedItems;
  pirateCharater;

  private grade: String;
  private storeItems; //array

  refreshStoreItems(){
    let formData = {
      "grade": this.grade
    }
    this.storeItems = [];
    this.firstRowItems = [];
    this.secondRowItems = [];
    this.thirdRowItems = [];
    this.fourthRowItems = [];
    this.storeService.fetchStoreItem("./api/store/items", formData, () => {})
    .subscribe(
      (data: Array<any>) => {
        if(data != undefined){
          for(let i = 0; i < data.length; i++){
            let item = {"id": data[i].itemId, "name": data[i].name, "src": data[i].itemSrc, "price": data[i].price};
            if(i < 3){
              this.firstRowItems.push(item);
            }else if(i < 6){
              this.secondRowItems.push(item);
            }else if(i < 9){
              this.thirdRowItems.push(item);
            }else{
              this.fourthRowItems.push(item);
            }
            this.storeItems.push(item);
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

  chooseAnItem(event: any){
    this.gameMode = "purchase";
    let selectedItemId = event.target.dataset.itemid;
    for(let i = 0; i < this.storeItems.length; i++){
      if(selectedItemId == this.storeItems[i].id){
        this.selectedItem = this.storeItems[i];
        if(this.selectedItem.price > this.totalCoins){
          this.notEnoughMoney = true;
        }else{
          this.notEnoughMoney = false;
        }
        break;
      }
    }
  }

  cancelPurchase(){
    this.selectedItem = [];
    this.gameMode = "store";
  }

  purchaseItem(){
    //call api, then go back to store
    let formData = {
      "childId": this.currentStudent,
      "grade": this.grade,
      "purchasedItemId": this.selectedItem.id
    }
    this.storeService.purcharseItem("./api/store/purchase", formData, () => {})
    .subscribe(
      (data) => {
        if(data != undefined && data){
          this.selectedItem = [];
          this.fetchStudentInfo();
          this.gameMode = "store";
        }
      },
      (err) => {
        this.errorService.networkError();
      }
    )
  }

  displayAchievement(){
    this.pirateCharater = sessionStorage.getItem("pirateCharacter");
    let formData = {
      "studentId": this.currentStudent,
      "grade": this.grade
    };
    this.storeService.fetchPurchasedItems("./api/store/purchaseditems", formData, () => {})
      .subscribe(
        (data: Array<any>) =>{
          if(data != undefined){
            this.purchasedItems = [];
            for(let i = 0; i < data.length; i++){
              let rand = Math.random();
              let item = {"id": data[i].itemId, "name": data[i].itemName, "src": data[i].itemSrc, "state": rand > 0.5 ? "large" : "small"};
              this.purchasedItems.push(item);
            }
            this.gameMode = "display";
            setInterval(() => {
              for(let i = 0; i < this.purchasedItems.length; i++){
                this.purchasedItems[i].state = (this.purchasedItems[i].state === "small" ? "large" : "small");
              }
            }, 1000);
          }else{
            this.errorService.internalError();
          }
        },
        (err) => {
          this.errorService.networkError();
        }
      )
  }

  goToDashboard(){
    window.location.href = "./app/dashboard";
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
            if(student.intro1Completed && (student.day1Completed || student.day2Completed)){
              this.gameMode = "store";
              this.totalCoins = student.totalCoins == undefined ? 0 : student.totalCoins;
              //fetch 12 store items
              this.refreshStoreItems();
            }else{
              window.location.href = "./app/dashboard";
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
}