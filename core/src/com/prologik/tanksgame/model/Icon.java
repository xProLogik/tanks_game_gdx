package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Icon extends GameObject {
  Icon(String nameRegion, int indexRegion, Vector2 position, float width, float height) {
    super(nameRegion, indexRegion, position, width, height);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
