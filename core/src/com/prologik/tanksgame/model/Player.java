package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.control.PlayerContoller;

public class Player extends Tank {

  private TankLevel tankLevel;
  private PlayerContoller contoller;

  Player(TankLevel tankLevel, Vector2 position) {
    super(tankLevel.getNameRegion(), position, new Vector2(0, 1), 10f * GameWorld.SPRITE_SIZE);
    contoller = new PlayerContoller(this);
    this.tankLevel = tankLevel;
  }


  public void move(float delta, Vector2 moveDirection) {
    this.setDirection(moveDirection);
    super.move(delta);
  }

  TankLevel getTankLevel() {
    return tankLevel;
  }

  void setTankLevel(TankLevel tankLevel) {
    this.tankLevel = tankLevel;
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
}
