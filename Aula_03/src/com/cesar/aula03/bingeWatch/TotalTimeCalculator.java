package com.cesar.aula03.bingeWatch;

import com.cesar.aula03.model.Title;

public class TotalTimeCalculator {
  private int totalTime;

  public int getTotalTime() {
    return totalTime;
  }
  
  public void include(Title tt){
    totalTime += tt.getDuration();
  }
}
