package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.math.Vector2;

public class ObjectPause extends GameObject {
  private String nameRegion;


  public ObjectPause(String nameRegion, Vector2 position, float width, float height) {
    super(nameRegion, 0, position, width, height);
    this.nameRegion = nameRegion;
  }

  public void update(float delta) {
    if (position.y < 0)
      position.y += delta * 5f;
    if (this.nameRegion.equals("gameover")) {
      this.getBounds().setPosition(this.position.x, this.position.y);
    }
  }
}
