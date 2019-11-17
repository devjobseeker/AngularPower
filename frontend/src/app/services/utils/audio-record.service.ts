import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

declare var p5: any;

@Injectable({
  providedIn: 'root'
})
export class AudioRecordService {

  constructor(private http: HttpClient) { }

  mic;
  recorder;
	soundFile;
	
	touchStarted(){
		p5.prototype.getAudioContext().resume();
	}

  startRecord(){
		p5.prototype.getAudioContext().resume();
		this.mic = new p5.AudioIn();
		
    this.mic.start(undefined, this.errorFunc);	//start(successCallback, errorCallback)
    this.recorder = new p5.SoundRecorder();
    this.recorder.setInput(this.mic);
    this.soundFile = new p5.SoundFile();
		this.recorder.record(this.soundFile);
  }

  stopRecord(){
    this.recorder.stop();
  }

  sendRecordData(url, formData, func){
		let response = {};
		this.http.post(url, formData)
			.subscribe(
				result => response["result"] =  result,	//success
				error => response["error"] = error,	//error
				func()	//complete
			);
		return response;
	}
	
	sendAudioData(url, formData){
		return this.http.post(url, formData).pipe(
			map((response: Response) => {
				return response;
			})
		)
	}

	getVolume = () => {
		return this.mic.getLevel();
	}

  getAudioBlobData(){
    var leftChannel, rightChannel;
	    leftChannel = this.soundFile.buffer.getChannelData(0);

	    // handle mono files
	    if (this.soundFile.buffer.numberOfChannels > 1) {
	      rightChannel = this.soundFile.buffer.getChannelData(1);
	    } else {
	      rightChannel = leftChannel;
	    }

	    var interleaved = this.interleave(leftChannel,rightChannel);

	    // create the buffer and view to create the .WAV file
	    var buffer = new ArrayBuffer(44 + interleaved.length * 2);
	    var view = new DataView(buffer);

	    // write the WAV container,
	    // check spec at: https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
	    // RIFF chunk descriptor
	    this.writeUTFBytes(view, 0, 'RIFF');
	    view.setUint32(4, 36 + interleaved.length * 2, true);
	    this.writeUTFBytes(view, 8, 'WAVE');
	    // FMT sub-chunk
	    this.writeUTFBytes(view, 12, 'fmt ');
	    view.setUint32(16, 16, true);
	    view.setUint16(20, 1, true);
	    // stereo (2 channels)
	    view.setUint16(22, 2, true);
	    view.setUint32(24, 44100, true);
	    view.setUint32(28, 44100 * 4, true);
	    view.setUint16(32, 4, true);
	    view.setUint16(34, 16, true);
	    // data sub-chunk
	    this.writeUTFBytes(view, 36, 'data');
	    view.setUint32(40, interleaved.length * 2, true);

	    // write the PCM samples
	    var lng = interleaved.length;
	    var index = 44;
	    var volume = 1;
	    for (var i = 0; i < lng; i++) {
	      view.setInt16(index, interleaved[i] * (0x7FFF * volume), true);
	      index += 2;
	    }
	    
	    return new Blob([view], {type: "audio/wav"});
  }

	private errorFunc = () => {
		alert("The device does not support audio recording!");
	}

  private interleave(leftChannel, rightChannel){
		var length = leftChannel.length + rightChannel.length;
	    var result = new Float32Array(length);

	    var inputIndex = 0;

	    for (var index = 0; index < length; ) {
	      result[index++] = leftChannel[inputIndex];
	      result[index++] = rightChannel[inputIndex];
	      inputIndex++;
	    }
	    return result;
	}
	
	private writeUTFBytes(view, offset, string){
		var lng = string.length;
	    for (var i = 0; i < lng; i++) {
	      view.setUint8(offset + i, string.charCodeAt(i));
	    }
	}
}