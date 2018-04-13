package com.prologik.tanksgame.model.gameworld.movableobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.model.gameworld.anotherobjects.Box;
import com.prologik.tanksgame.model.gameworld.anotherobjects.BoxType;
import com.prologik.tanksgame.model.gameworld.anotherobjects.GameObject;

public abstract class MovableObject extends GameObject {

  static final int LEFT = 90;
  static final int RIGHT = -90;
  static final int DOWN = 180;
  static final int UP = 0;
  int currentFacing;
  private float velocity;
  private boolean canMove = false;
  private Vector2 direction;
  private Vector2 newPosition = position;


  MovableObject(String nameRegion, Vector2 position, float width, float height, Vector2 direction, float velosity) {
    super(nameRegion, 0, position, width, height);
    this.direction = direction;
    this.velocity = velosity;
    recalculateRotationAngle(this.direction);
  }

  public void update(float delta) {
    if (this.isLeftTheField())
      this.leftTheField();

    if (this.isCanMove()) {
      position = newPosition;
    }
    this.getBounds().setPosition(this.position.x, this.position.y);
    this.getBounds().setRotation(currentFacing);

  }

  void leftTheField() {
    setCanMove(false);
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

  void setDirection(Vector2 direction) {
    if (!this.direction.epsilonEquals(direction))
      lastPosition();
    this.direction = direction;
    recalculateRotationAngle(direction);
  }

  public boolean isCanMove() {
    return canMove;
  }

  public void setCanMove(boolean canMove) {
    this.canMove = canMove;
  }

  void move(float delta) {

    newPosition = new Vector2(position);
    if (delta < 0.04) {
      newPosition.x = this.position.x + delta * velocity * direction.x;
      newPosition.y = this.position.y + delta * velocity * direction.y;
    } else {
      newPosition.x = this.position.x + 0.04f * velocity * direction.x;
      newPosition.y = this.position.y + 0.04f * velocity * direction.y;
    }

    this.canMove = true;

  }


  public boolean isLeftTheField() {
    return (getCollisionRect().x < GameWorld.PLAYFIELD_MIN_X || getCollisionRect().y < GameWorld.PLAYFIELD_MIN_Y ||
        getCollisionRect().x + getCollisionRect().width > GameWorld.PLAYFIELD_MAX_X ||
        getCollisionRect().y + getCollisionRect().height > GameWorld.PLAYFIELD_MAX_Y);
  }

  public boolean collide(GameObject obj) {
    Rectangle boundsObj = obj.getCollisionRect();
    Rectangle boundsThis = this.getCollisionRect();
    return ((newPosition.x < boundsObj.x + boundsObj.width) &&
        (boundsObj.x < newPosition.x + boundsThis.width) &&
        (newPosition.y < boundsObj.y + boundsObj.height) &&
        (boundsObj.y < newPosition.y + boundsThis.height));
  }

  public boolean collide(Box box) {
    return (box.getBoxType().equals(BoxType.BRICS) ||
        box.getBoxType().equals(BoxType.STONE) ||
        box.getBoxType().equals(BoxType.WATER)) && this.collide((GameObject) box);
  }

  public void lastPosition() {

    this.position.x = Math.round(this.position.x);
    this.position.y = Math.round(this.position.y);
  }

  float getVelocity() {
    return velocity;
  }

  void setVelocity(float velocity) {
    this.velocity = velocity;
  }

  public Vector2 getDirection() {
    return direction;
  }

  void setNewPosition(Vector2 newPosition) {
    this.newPosition = newPosition;
  }
}
