package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.control.PlayerContoller;

public class Player extends Tank{

  private PlayerContoller contoller;

  Player(String nameRegion, Vector2 position, float width, float height, Vector2 direction) {
    super(nameRegion, position, width, height, direction,10f*GameWorld.SPRITE_SIZE);
    contoller = new PlayerContoller(this);
  }


  public void move(float delta, Vector2 moveDirection) {
    this.setDirection(moveDirection);
    super.move(delta);
  }

  @Override
  public void update(float delta) {
    super.update(delta);
    this.contoller.update(delta);
  }

  @Override
  public void draw(SpriteBatch batch) {
    super.draw(batch);
  }

  @Override
  public void leftTheField() {
    super.leftTheField();
  }
}
