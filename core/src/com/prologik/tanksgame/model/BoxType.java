package com.prologik.tanksgame.model;

public enum BoxType {
  EMPTY, BRICS, STONE, GRASS, WATER, ICE;



  static BoxType typeByNumber(int number){
    BoxType[] types = values();
    return types[number];

  }
}
