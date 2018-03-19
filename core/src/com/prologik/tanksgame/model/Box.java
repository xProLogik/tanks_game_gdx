package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class Box extends GameObject{

  private final BoxType boxType;

  Box(BoxType boxType, Vector2 position, float width, float height) {
    super(boxType.name().toLowerCase(), position, width, height);


    this.boxType = boxType;
  }

  BoxType getBoxType() {
    return boxType;
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
