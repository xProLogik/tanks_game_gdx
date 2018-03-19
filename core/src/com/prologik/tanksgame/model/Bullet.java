package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MovableObject {


  Bullet(String nameRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(nameRegion, position, width, height, direction, 15f);
  }

  @Override
  public void leftTheField() {
    this.setCanMove(false);
  }

  @Override
  public boolean isLeftTheField() {
    return ((GameWorld.PLAYFIELD_MIN_Y > this.position.y + 0.5f) ||
        (GameWorld.PLAYFIELD_MIN_X > this.position.x + 0.5f) ||
        (GameWorld.PLAYFIELD_MAX_X < this.position.x + this.getBounds().getBoundingRectangle().getWidth() - 0.5f) ||
        (GameWorld.PLAYFIELD_MAX_Y < this.position.y + this.getBounds().getBoundingRectangle().getHeight() - 0.5f));
  }

  @Override
  boolean collide(GameObject obj) {
    Polygon bounds = obj.getBounds();
    return ((this.position.x + .4f < bounds.getX() + bounds.getBoundingRectangle().getWidth()) &&
        (bounds.getX() + .4f < this.position.x + this.getBounds().getBoundingRectangle().getWidth()) &&
        (this.position.y + .4f < bounds.getY() + bounds.getBoundingRectangle().getHeight()) &&
        (bounds.getY() + .4f < this.position.y + this.getBounds().getBoundingRectangle().getHeight()));
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
