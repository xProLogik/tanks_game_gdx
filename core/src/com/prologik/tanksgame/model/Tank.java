package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.prologik.tanksgame.control.TankContoller;

public class Tank extends GameObject {

  private TankContoller tankContoller;
  public Tank(TextureRegion textureRegion, float x, float y, float width, float height) {
    super(textureRegion, x, y, width, height);
    tankContoller = new TankContoller(bounds);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
    tankContoller.handle();
  }
}
