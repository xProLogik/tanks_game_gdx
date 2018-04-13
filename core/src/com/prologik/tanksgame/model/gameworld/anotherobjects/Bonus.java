package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.GameWorld;

public class Bonus extends GameObject {

  private BonusType bonusType;
  private float bonusTime = 15;

  public Bonus(Vector2 position) {
    super("bonus", (int)(Math.random()*5), position, 2 * GameWorld.SPRITE_SIZE, 2 * GameWorld.SPRITE_SIZE);
    this.bonusType = BonusType.values()[super.indexRegion];

  }

  public float getBonusTime() {
    return bonusTime;
  }

  public void update(float delta, float runTime) {
    this.blink(runTime);
    bonusTime-=delta;
  }


  public BonusType getBonusType() {
    return bonusType;
  }

}
