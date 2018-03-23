package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Tank {

  private int random;

  Enemy(TankLevel tankLevel, Vector2 position) {
    super(tankLevel.getNameRegion().replace("tank","tankenemy"), position, new Vector2(0,-1),
        10f * GameWorld.SPRITE_SIZE);
  }

  void randomMove(){
    if (Math.random()*100<10)
      randomAngle();
  }
  private void randomAngle() {
    random = (int) (Math.random() * 100);
    switch (currentFacing) {
      case RIGHT:
        if (random < 15)
          this.setDirection(new Vector2(-1, 0));
        else if (random < 30)
          this.setDirection(new Vector2(0, 1));
        else if (random < 45)
          this.setDirection(new Vector2(0, -1));
        break;
      case LEFT:
        if (random < 15)
          this.setDirection(new Vector2(1, 0));
        else if (random < 30)
          this.setDirection(new Vector2(0, -1));
        else if (random < 45)
          this.setDirection(new Vector2(0, 1));
        break;
      case DOWN:
        if (random < 15)
          this.setDirection(new Vector2(0, 1));
        else if (random < 30)
          this.setDirection(new Vector2(1, 0));
        else if (random < 45)
          this.setDirection(new Vector2(-1, 0));
        break;
      case UP:
        if (random < 15)
          this.setDirection(new Vector2(0, -1));
        else if (random < 30)
          this.setDirection(new Vector2(-1, 0));
        else if (random < 45)
          this.setDirection(new Vector2(1, 0));
        break;
    }
    this.recalculateRotationAngle(this.direction);
  }

  void randomMovement() {
    if (Math.random() < .01) {
      randomAngle();
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
