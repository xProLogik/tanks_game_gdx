package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject {


  Bullet(String nameRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(nameRegion, position, width, height, direction, 15f * GameWorld.SPRITE_SIZE);
  }


  @Override
  public boolean isLeftTheField() {
    return (getCollisionRect().x + GameWorld.SPRITE_SIZE / 2 < GameWorld.PLAYFIELD_MIN_X ||
        getCollisionRect().y + GameWorld.SPRITE_SIZE / 2 < GameWorld.PLAYFIELD_MIN_Y ||
        getCollisionRect().x + getCollisionRect().width - GameWorld.SPRITE_SIZE / 2 > GameWorld.PLAYFIELD_MAX_X ||
        getCollisionRect().y + getCollisionRect().height - GameWorld.SPRITE_SIZE / 2 > GameWorld.PLAYFIELD_MAX_Y);
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
