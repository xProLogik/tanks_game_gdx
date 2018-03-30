package com.prologik.tanksgame.model;

public enum BonusType {
  HELMET, EXTRA_LIFE, GRENADE, SHOVEL, STAR, CLOCK;

  public static BonusType typeByNumber(int number){
    BonusType[] types = values();
    return types[number];
  }
}
