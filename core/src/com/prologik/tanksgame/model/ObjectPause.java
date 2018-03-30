package com.prologik.tanksgame.model;

import com.badlogic.gdx.math.Vector2;

class ObjectPause extends GameObject {
  private String nameRegion;
  private float timeFromGameOver = 0;

  ObjectPause(String nameRegion, Vector2 position, float width, float height) {
    super(nameRegion, 0, position, width, height);
    this.nameRegion = nameRegion;
  }

  void update(float delta) {
    if (position.y < 0)
      position.y += delta * 5f;
    if (this.nameRegion.equals("gameover")){
      timeFromGameOver+=delta;
      this.getBounds().setPosition(this.position.x, this.position.y);
    }
  }
}
