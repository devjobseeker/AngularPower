import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RandomNumberService {

  constructor() { }

  generateRandomIntsForProvidedListLength(listLength){
    let result = [];
    while(result.length < listLength){
      let temp = this.generateRandomInt(1, 10);
      if(temp != 7 && !result.includes(temp)){
        result.push(temp);
      }
    }

    return result;
  }

  generateRandomIntsWithNoRepeatWithinSixElements(listLength){
    let result = [];
    while(result.length < listLength){
      let temp = this.generateRandomInt(1, 10);
      if(temp != 7 && !result.slice(-6).includes(temp)){
        result.push(temp);
      }
    }
    return result;
  }

  //return an integer that between min and max. equal or larger than min, lower than max.
  generateRandomInt(min, max){
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
  }

  generateRandomIntsForAllListLength(listLengths, trialsInEachListLength){
    let result = [];
    for(let i = 0; i < listLengths.length; i++){
      for(let o = 0; o < trialsInEachListLength; o++){
        let digits = [];
        while(digits.length < i){
          let temp = this.generateRandomInt(1, 10);
          if(temp != 7 && digits.indexOf(temp) < 0){
            digits.push(temp);
          }
        }
      }
    }

    return result;
  }

  randomizeAList(list:Array<any>){
    let length = list.length;
    for(let i = 0; i < length / 2; i++){
      let randNum = this.generateRandomInt(1, length + 1);
      //swap
      let temp = list[i];
      list[i] = list[randNum - 1];
      list[randNum - 1] = temp;
    }
    return list;
  }
}