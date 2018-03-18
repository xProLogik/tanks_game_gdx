package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject{


  Bullet(TextureRegion textureRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(textureRegion, position, width, height, direction, 15f);
  }

  @Override
  public void update(float delta) {
    super.update(delta);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
