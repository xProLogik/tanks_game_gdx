package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bonus extends GameObject {

  private BonusType bonusType;
  private float bonusTime = 15;

  Bonus(Vector2 position) {
    super("bonus", (int)(Math.random()*5), position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE);
    this.bonusType = BonusType.typeByNumber(super.indexRegion);
  }

  float getBonusTime() {
    return bonusTime;
  }

  public void update(float delta, float runTime) {
    this.blink(runTime);
    bonusTime-=delta;
  }


  BonusType getBonusType() {
    return bonusType;
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
