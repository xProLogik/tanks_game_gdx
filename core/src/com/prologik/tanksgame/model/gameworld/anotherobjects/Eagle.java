package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.model.gameworld.GameWorld;

public class Eagle extends GameObject {

  public Eagle(Vector2 position) {
    super("alive", 0, position,
        2f * GameWorld.SPRITE_SIZE, 2f * GameWorld.SPRITE_SIZE);
  }

  void gameover(GameWorld world) {
    createExploison(world);
    this.getObject().setRegion(new Sprite(MainGame.sptriteAtlas.findRegion("dead")));
    world.setGameover(true);
    GameWorld.sounds.forEach((name, sound) -> sound.stop());
    GameWorld.sounds.get("bigexploison").play(0.3f);
    GameWorld.sounds.get("gameover").play(0.5f);

  }

  private void createExploison(GameWorld world) {
    world.getExploisons().add(new Exploison(5, this.position));
  }
}
