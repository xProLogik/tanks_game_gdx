package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject {


  Bullet(Vector2 position, Vector2 direction) {
    super("bullet", position, GameWorld.SPRITE_SIZE,
        GameWorld.SPRITE_SIZE, direction, 15f);
  }


  @Override
  public boolean isLeftTheField() {
    return (getCollisionRect().x < GameWorld.PLAYFIELD_MIN_X ||
        getCollisionRect().y < GameWorld.PLAYFIELD_MIN_Y ||
        getCollisionRect().x + getCollisionRect().width > GameWorld.PLAYFIELD_MAX_X ||
        getCollisionRect().y + getCollisionRect().height > GameWorld.PLAYFIELD_MAX_Y);
  }

  @Override
  void leftTheField() {
    super.leftTheField();
    GameWorld.createSmallExploison(this);
  }

  @Override
  boolean collide(Box box) {
    return ((box.getBoxType().equals(BoxType.BRICS) ||
        box.getBoxType().equals(BoxType.STONE))) && this.collide((GameObject) box);
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
