package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.math.Vector2;

public class Points extends GameObject {
  private float pointsTime = 0;

  public Points(int countPoints, Vector2 position) {
    super(countPoints + "points", 0, position, 2, 1);
  }

  public void update(float delta) {
    pointsTime += delta;
  }

  public float getPointsTime() {
    return pointsTime;
  }
}
