import { Component, OnInit } from '@angular/core';

import { DataUploadService } from '../../services/data-upload/data-upload.service';

@Component({
  selector: 'app-data-upload',
  templateUrl: './data-upload.component.html',
  styleUrls: ['./data-upload.component.css']
})
export class DataUploadComponent implements OnInit {

  constructor(private dataService: DataUploadService,) { }

  ngOnInit() {
    this.disableTestBtn = false;
    this.showTestResult = false;
    this.testSuccess = false;
    this.initDataList();
    this.requestItemIndex = 0;
  }

  disableTestBtn;
  showTestResult;
  testSuccess;
  testFailMsg;
  uploadFailMsg;

  requestItemIndex;
  dataList;

  public testConnection(){
    this.disableTestBtn = true;
    this.dataService.get("./api/upload/testconnection").subscribe(
      (data) => {
        this.showTestResult = true;
        this.disableTestBtn = false;
        if(data == true){
          this.testSuccess = true;
        }else{
          this.testSuccess = false;
          this.testFailMsg = "Please check your internet and make sure you use ASU vpn.";
        }
      },
      (err) => {
        this.disableTestBtn = false;
        this.showTestResult = true;
        this.testSuccess = false;
        this.testFailMsg = "Please check your internet and make sure you use vpn.";
      }
    )
  }

  public uploadData(){
    if(this.requestItemIndex >= this.dataList.length){
      return;
    }
    if(this.dataList[this.requestItemIndex].numOfRecord == 0){
      this.requestItemIndex++;
      this.uploadData();
    }else{
      this.dataList[this.requestItemIndex].status = "processing";
      this.dataService.post(this.dataList[this.requestItemIndex].link, null).subscribe(
        (data) => {
          if(data == true){
            this.dataList[this.requestItemIndex].status = "complete";
          }else{
            this.dataList[this.requestItemIndex].status = "fail";
            this.uploadFailMsg = "Please test connection first and try it again.";
          }
          this.requestItemIndex++;
          this.uploadData();
        },
        (err) => {
          this.dataList[this.requestItemIndex].status = "fail";
          this.uploadFailMsg = "Please test connection first and try it again.";
          this.requestItemIndex++;
        }
      );
    }
  }

  public initDataList(){
    //if status = complete, show green check mark
    //if status = fail, show red times icon
    //if status = processing, show refresh icon
    //if status = start, show nothing
    this.dataService.get("./api/upload/total").subscribe(
      (data) => {
        if(data != undefined){
          this.dataList = data;
        }
      }
    );
  }
}