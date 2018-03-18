package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.prologik.tanksgame.control.PlayerContoller;

public class Player{

  private Tank tank;
  private PlayerContoller contoller;

  Player(TextureAtlas textureAtlas){
 //   tank = new Tank(textureAtlas.findRegion("tank1",0),-4f,-13f,2f,2f);
    contoller = new PlayerContoller(tank);
  }
//  public void draw(SpriteBatch batch){
//    tank.draw(batch);
//
//  }
  public void update(float delta){
    contoller.handle(delta);
  }
}
