import { Component, OnInit } from '@angular/core';

import { AudioRecordService } from '../../services/utils/audio-record.service';
import { ErrorService } from '../../services/errors/error.service';
import { FileService } from '../../services/files/file.service';

@Component({
  selector: 'app-audio-record',
  templateUrl: './audio-record.component.html',
  styleUrls: ['./audio-record.component.css']
})
export class AudioRecordComponent implements OnInit {

  constructor(
    private audioRecordService: AudioRecordService,
    private errorService: ErrorService,
    private fileService: FileService,
  ) { }

  ngOnInit() {
    this.startRecording = false;
  }

  //public variable
  fileUid;
  startRecording;

  startRecord(){
    this.audioRecordService.startRecord();
    this.startRecording = true;
  }

  endRecord(){
    this.audioRecordService.stopRecord();
    this.startRecording = false;
    let blob = this.audioRecordService.getAudioBlobData();
    let formData = new FormData();
    formData.append("Test_Recording", blob, "Test_Recording");
    this.audioRecordService.sendAudioData("./api/audiorecord", formData).subscribe(
      (data) => {
        let result = data;
        if(result != undefined){
          this.fileUid = result["documentId"];
          setTimeout(() => {
            document.getElementById("test-audio-div").innerHTML = "<audio controls>" +
              "<source src='./api/audiorecord/play?fileUid=" + this.fileUid + "' type='audio/wav' /> " +
              "</audio>";
          }, 0);
          
        }else{
          this.errorService.internalError();
        }
      },
      (err) => {
        this.errorService.internalError();
      }
    );
  }

  downloadFile(){
    this.fileService.downloadFile("./api/audiorecord/download", {"fileUid": this.fileUid}, () => {}).subscribe();
  }

}
