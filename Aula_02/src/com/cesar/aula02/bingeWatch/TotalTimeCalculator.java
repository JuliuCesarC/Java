package com.cesar.aula02.bingeWatch;

import com.cesar.aula02.model.Title;

public class TotalTimeCalculator {
  private int totalTime;

  public int getTotalTime() {
    return totalTime;
  }
  
  public void include(Title tt){
    totalTime += tt.getDuration();
  }
}
