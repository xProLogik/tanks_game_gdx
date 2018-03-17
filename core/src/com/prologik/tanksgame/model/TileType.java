package com.prologik.tanksgame.model;

public enum TileType {
  EMPTY, BRICS, STONE, GRASS, WATER, ICE;
  public static String nameByNumber(int number){
    TileType[] types = values();
    return types[number].name().toLowerCase();
  }
}
