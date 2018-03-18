package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class Box extends GameObject{

  Box(TextureRegion textureRegion, Vector2 position, float width, float height) {
    super(textureRegion, position, width, height);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
