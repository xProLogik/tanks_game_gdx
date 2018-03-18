package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class MovableObject extends GameObject {

  private float velocity;
  private boolean canMove = false;
  private Vector2 direction;
  private Vector2 position;
  private float rotationAngle;

  MovableObject(TextureRegion textureRegion, Vector2 position, float width, float height, Vector2 direction, float velocity) {
    super(textureRegion, position, width, height);
    this.direction = direction;
    this.velocity = velocity;
    this.position = position;
    recalculateRotationAngle(this.direction);
  }

  public void update(float delta) {
    if (isCanMove()) {
      this.getBounds().setPosition(this.position.x, this.position.y);
    } else lastPosition();

    this.getBounds().setRotation(rotationAngle);
  }

  private void recalculateRotationAngle(Vector2 direction) {
    if (direction.x > 0.0f)
      rotationAngle = -90;
    else if (direction.x < 0.0f)
      rotationAngle = 90;
    else if (direction.y > 0.0f)
      rotationAngle = 0;
    else if (direction.y < 0.0f)
      rotationAngle = 180;
  }

  public Vector2 getDirection() {
    return direction;
  }

  public void setDirection(Vector2 direction) {
    this.direction = direction;
    recalculateRotationAngle(direction);
  }

  boolean isCanMove() {
    return canMove;
  }

  public void setCanMove(boolean canMove) {
    this.canMove = canMove;
  }

  void move(float delta) {


    if (isCanMove()) {
      this.position.x += delta * velocity * direction.x;
      this.position.y += delta * velocity * direction.y;
    }
    canMove = true;
    leftTheField();
  }


  private void leftTheField() {
    if ((GameWorld.PLAYFIELD_MIN_Y +1> this.position.y) ||
        (GameWorld.PLAYFIELD_MIN_X +1> this.position.x) ||
        (GameWorld.PLAYFIELD_MAX_X +1< this.position.x + this.getBounds().getBoundingRectangle().getWidth()) ||
        (GameWorld.PLAYFIELD_MAX_Y +1< this.position.y + this.getBounds().getBoundingRectangle().getHeight())) {
      this.canMove = false;
      lastPosition();
    }
  }

  boolean collide(Box box) {
    Polygon bounds = box.getBounds();
    return ((this.position.x < bounds.getX() + bounds.getBoundingRectangle().getWidth()) &&
        (bounds.getX() < this.position.x + this.getBounds().getBoundingRectangle().getWidth()) &&
        (this.position.y < bounds.getY() + bounds.getBoundingRectangle().getHeight()) &&
        (bounds.getY() < this.position.y + this.getBounds().getBoundingRectangle().getHeight()));
  }

  public void lastPosition() {
    this.position.x = Math.round(this.position.x);
    this.position.y = Math.round(this.position.y);
  }
}
