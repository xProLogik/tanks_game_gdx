package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bonus extends GameObject {

  private BonusType bonusType;

  Bonus(String nameRegion,int indexRegion, Vector2 position, float width, float height) {
    super(nameRegion,indexRegion, position, width, height);
    this.bonusType = BonusType.typeByNumber(indexRegion);
  }


  public void update(float runTime) {
    this.blink(runTime);
  }


  BonusType getBonusType() {
    return bonusType;
  }

  public void setBonusType(BonusType bonusType) {
    this.bonusType = bonusType;
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
