package com.prologik.tanksgame.model;

public enum TankLevel {
  LEVEL1,
  LEVEL2,
  LEVEL3,
  LEVEL4;

  TankLevel nextLevel(){
    return TankLevel.values()[this.ordinal()+1];
  }
  String getNameRegion(){
    return this.name().replace("LEVEL","tank");
  }
  static TankLevel typeByNumber(int number){
    TankLevel[] types = values();
    return types[number-1];

  }
}
