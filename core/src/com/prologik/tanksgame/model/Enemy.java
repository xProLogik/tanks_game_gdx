package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy  extends Tank{

  private int random;

  Enemy(String nameRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(nameRegion, position, width, height, direction, 10f);
  }


  private void randomMove() {
    random = (int)(Math.random() * 100);
    switch(currentFacing){
      case RIGHT:
        if(random < 15){
          this.direction.x = -1;
          this.direction.y = 0;
        }
        else if(random < 30){
          this.direction.x = 0;
          this.direction.y = 1;
        }
        else if(random < 45){
          this.direction.x = 0;
          this.direction.y = -1;
        }
        break;
      case LEFT:
        if(random < 15){
          this.direction.x = 1;
          this.direction.y = 0;
        }
        else if(random < 30){
          this.direction.x = 0;
          this.direction.y = -1;
        }
        else if(random < 45){
          this.direction.x = 0;
          this.direction.y = 1;
        }
        break;
      case DOWN:
        if(random < 15){
          this.direction.x = 0;
          this.direction.y = 1;
        }
        else if(random < 30){
          this.direction.x = 1;
          this.direction.y = 0;
        }
        else if(random < 45){
          this.direction.x = -1;
          this.direction.y = 0;
        }
        break;
      case UP:
        if(random < 15){
          this.direction.x = 0;
          this.direction.y = -1;
        }
        else if(random < 30){
          this.direction.x = -1;
          this.direction.y = 0;
        }
        else if(random < 45){
          this.direction.x = 1;
          this.direction.y = 0;
        }
        break;
    }
    this.recalculateRotationAngle(this.direction);
  }
  void randomMovement() {
    if (Math.random() < .03) {
      randomMove();
    }
    if (Math.random() < .01) {
      this.shot();

    }
  }

  @Override
  public void leftTheField() {
    super.leftTheField();
    randomMove();
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }
}
