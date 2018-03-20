package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject {


  Bullet(String nameRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(nameRegion, position, width, height, direction, 15f * GameWorld.SPRITE_SIZE);
  }

  @Override
  public void leftTheField() {
    this.setCanMove(false);
  }

  @Override
  public boolean isLeftTheField() {
    return ((GameWorld.PLAYFIELD_MIN_Y > this.position.y + GameWorld.SPRITE_SIZE / 2) ||
        (GameWorld.PLAYFIELD_MIN_X > this.position.x + GameWorld.SPRITE_SIZE / 2) ||
        (GameWorld.PLAYFIELD_MAX_X < this.position.x + this.getObject().getWidth() - GameWorld.SPRITE_SIZE / 2) ||
        (GameWorld.PLAYFIELD_MAX_Y < this.position.y + this.getObject().getHeight() - GameWorld.SPRITE_SIZE / 2));
  }

  @Override
  boolean collide(GameObject obj) {
    Rectangle bounds = obj.getBounds();
    return ((this.position.x + .4f * GameWorld.SPRITE_SIZE < bounds.getX() + bounds.getWidth()) &&
        (bounds.getX() + .4f * GameWorld.SPRITE_SIZE < this.position.x + this.getBounds().getWidth()) &&
        (this.position.y + .4f * GameWorld.SPRITE_SIZE < bounds.getY() + bounds.getHeight()) &&
        (bounds.getY() + .4f * GameWorld.SPRITE_SIZE < this.position.y + this.getBounds().getHeight()));
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
