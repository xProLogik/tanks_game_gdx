package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

class Eagle extends GameObject {
  private boolean gameover = false;
  private float timeAfterGameOver = 0;

  Eagle() {
    super("alive", 0,
        new Vector2(-1f * GameWorld.SPRITE_SIZE, GameWorld.PLAYFIELD_MIN_Y),
        2f * GameWorld.SPRITE_SIZE, 2f * GameWorld.SPRITE_SIZE);
  }

  void gameover() {

      this.getObject().setRegion(new Sprite(GameWorld.textureAtlas.findRegion("dead")));
      gameover = true;
      GameWorld.sounds.forEach((name, sound) -> sound.stop());
      GameWorld.sounds.get("bigexploison").play(0.3f);
      GameWorld.sounds.get("gameover").play(0.5f);

  }

  boolean isGameover() {
    return gameover;
  }
}
