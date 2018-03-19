package com.prologik.tanksgame.model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class MovableObject extends GameObject {

  static final int LEFT = 90;
  static final int RIGHT = -90;
  static final int DOWN = 180;
  static final int UP = 0;
  int currentFacing;
  private float velocity;
  private boolean canMove = false;
  Vector2 direction;
  Vector2 position;

  MovableObject(String nameRegion, Vector2 position, float width, float height, Vector2 direction, float velosity) {
    super(nameRegion, position, width, height);
    this.direction = direction;
    this.velocity = velosity;
    this.position = position;
    recalculateRotationAngle(this.direction);
  }


  public void update(float delta) {
    if (isCanMove()) {
      this.getBounds().setPosition(this.position.x, this.position.y);
    }
    this.getBounds().setRotation(currentFacing);
  }

  void recalculateRotationAngle(Vector2 direction) {
    if (direction.x > 0.0f)
      currentFacing = RIGHT;
    else if (direction.x < 0.0f)
      currentFacing = LEFT;
    else if (direction.y > 0.0f)
      currentFacing = UP;
    else if (direction.y < 0.0f)
      currentFacing = DOWN;
  }

  Vector2 getDirection() {
    return direction;
  }

  void setDirection(Vector2 direction) {
    this.direction = direction;
    recalculateRotationAngle(direction);
  }

  boolean isCanMove() {
    return canMove;
  }

  void setCanMove(boolean canMove) {
    this.canMove = canMove;
  }

  void move(float delta) {
    if (isCanMove()) {
      this.position.x += delta * velocity * direction.x;
      this.position.y += delta * velocity * direction.y;
    }
    canMove = true;
    if (isLeftTheField()) this.leftTheField();
  }

  public void leftTheField() {
    lastPosition();
  }

  public boolean isLeftTheField() {
    return ((GameWorld.PLAYFIELD_MIN_Y > this.position.y) ||
        (GameWorld.PLAYFIELD_MIN_X > this.position.x) ||
        (GameWorld.PLAYFIELD_MAX_X < this.position.x + this.getBounds().getBoundingRectangle().getWidth()) ||
        (GameWorld.PLAYFIELD_MAX_Y < this.position.y + this.getBounds().getBoundingRectangle().getHeight()));
  }


  boolean collide(GameObject obj) {
    Polygon bounds = obj.getBounds();
    return ((this.position.x < bounds.getX() + bounds.getBoundingRectangle().getWidth()) &&
        (bounds.getX() < this.position.x + this.getBounds().getBoundingRectangle().getWidth()) &&
        (this.position.y < bounds.getY() + bounds.getBoundingRectangle().getHeight()) &&
        (bounds.getY() < this.position.y + this.getBounds().getBoundingRectangle().getHeight()));
  }

  boolean collide(Box box) {
    Polygon bounds = box.getBounds();
    return ((box.getBoxType().equals(BoxType.BRICS) ||
        box.getBoxType().equals(BoxType.STONE) ||
        box.getBoxType().equals(BoxType.WATER))) &&
        ((this.position.x < bounds.getX() + bounds.getBoundingRectangle().getWidth()) &&
        (bounds.getX() < this.position.x + this.getBounds().getBoundingRectangle().getWidth()) &&
        (this.position.y < bounds.getY() + bounds.getBoundingRectangle().getHeight()) &&
        (bounds.getY() < this.position.y + this.getBounds().getBoundingRectangle().getHeight()));
  }

  void lastPosition() {
    this.position.x = Math.round(this.position.x);
    this.position.y = Math.round(this.position.y);
  }
}