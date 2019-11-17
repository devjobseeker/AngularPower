import { Component, OnInit } from '@angular/core';

import { ErrorService } from '../../services/errors/error.service';
import { GeneralHttpService } from '../../services/utils/general-http.service';

@Component({
  selector: 'app-data-upload-redcap',
  templateUrl: './data-upload-redcap.component.html',
  styleUrls: ['./data-upload-redcap.component.css']
})
export class DataUploadRedcapComponent implements OnInit {

  constructor(
    private errorService: ErrorService,
    private httpService: GeneralHttpService
  ) { }

  ngOnInit() {
    this.processingRowIndex = 0;
    this.fetchDataToBeProcessed();
    this.disableTestBtn = false;
    this.disableUploadBtn = false;

    // this.apiURL = "https://redcap.chs.asu.edu/api/";
    // this.apiToken = "62512698E1121E280309CBA55BE24194";
    // this.deviceId = "1";
    this.testMessage = undefined;
    this.connectionSuccess = undefined;
  }

  //public variables
  apiURL: String;
  apiToken: String;
  deviceId: String;
  dataToBeProcessed;
  internalServerError: String;
  disableTestBtn;
  testMessage: String;
  connectionSuccess;
  disableUploadBtn;

  private processingRowIndex;

  public testConnection(){
    this.disableTestBtn = true;
    this.disableUploadBtn = true;
    let apiConfig = {
      "token": this.apiToken,
      "url": this.apiURL,
      "deviceId": this.deviceId
    };
    let formData = {
      "apiConfig": apiConfig
    }
    this.httpService.post("./api/redcap/testconnection", formData).subscribe(
      (data) => {
        if(data != undefined){
          this.disableTestBtn = false;
          this.disableUploadBtn = false;
          if(data["syncDataResult"] != undefined && data["syncDataResult"]["respCode"] == 200 && data["syncDataResult"]["affectedRowsCount"] == 1){
            this.connectionSuccess = true;
            this.testMessage = "Connection Success!";
          }else if(data["syncDataResult"] != undefined){
            this.connectionSuccess = false;
            if(data["syncDataResult"]["respCode"] == 200){
              this.testMessage = "Connection Fail! Please check your URL and API token.";
            }else{
              this.testMessage = data["syncDataResult"]["errorDetail"];
            }
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

  uploadData(){
    this.disableUploadBtn = true;
    if(this.processingRowIndex >= this.dataToBeProcessed.length){
      this.disableUploadBtn = false;
      return;
    }
    if(this.dataToBeProcessed[this.processingRowIndex].totalRecordsCount == 0
        || this.dataToBeProcessed[this.processingRowIndex].syncStatus === "Finished"){
          if(this.dataToBeProcessed[this.processingRowIndex].totalRecordsCount == 0){
            this.dataToBeProcessed[this.processingRowIndex].syncStatus = "NoData";
          }
      this.processingRowIndex++;
      this.uploadData();
    }else{
      this.dataToBeProcessed[this.processingRowIndex].syncStatus = "Processing";
      let apiConfig = {
        "token": this.apiToken,
        "url": this.apiURL,
        "deviceId": this.deviceId
      };
      let formData = {
        "childId": this.dataToBeProcessed[this.processingRowIndex].childId,
        "grade": this.dataToBeProcessed[this.processingRowIndex].grade,
        "gameId": this.dataToBeProcessed[this.processingRowIndex].gameId,
        "category": this.dataToBeProcessed[this.processingRowIndex].category,
        "cohort": this.dataToBeProcessed[this.processingRowIndex].cohort,
        "apiConfig": apiConfig
      };
      this.httpService.post("./api/redcap/processing", formData).subscribe(
        (data) => {
          if(data != undefined){
            if(data["syncDataResult"] != undefined && data["syncDataResult"]["respCode"] == 200 && data["syncDataResult"]["affectedRowsCount"] != undefined){
              this.dataToBeProcessed[this.processingRowIndex].syncStatus = data["syncStatus"];
            }else if(data["syncDataResult"] != undefined){
              this.dataToBeProcessed[this.processingRowIndex].syncStatus = "Fail";
              this.dataToBeProcessed[this.processingRowIndex].syncDataResult = {
                "errorDetail": "REDCap Response: " + data["syncDataResult"]["errorDetail"] + " Response code: " + data["syncDataResult"]["respCode"]
              }
            }
          }else{
            this.dataToBeProcessed[this.processingRowIndex].syncDataResult = {
              "errorDetail": "Something went wrong. Please screenshot the whole page especially the line where the error happen and send it to your developer."
            };
          }
          this.processingRowIndex++;
          this.uploadData();
        },
        (err) => {
          this.errorService.networkError();
        }
      );
    }
  }

  private fetchDataToBeProcessed(){
    this.httpService.get("./api/redcap/total", null).subscribe(
      (data: Array<any>) => {
        if(data != undefined){
          this.dataToBeProcessed = [];
          for(let i = 0; i < data.length; i++){
            this.dataToBeProcessed.push(data[i]);
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