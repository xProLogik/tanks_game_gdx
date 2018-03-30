package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.prologik.tanksgame.model.Enemy;
import com.prologik.tanksgame.model.GameWorld;
import com.prologik.tanksgame.model.Player;

public class WorldController {

  private GameWorld world;

  public WorldController(GameWorld world) {
    this.world = world;
  }

  public void update(float delta) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.P))
      if (world.isPause()) {
        world.setPause(false);
        world.objectPauses.clear();
        GameWorld.sounds.forEach((name, sound) -> sound.resume());
      } else {
        world.setPause(true);
        world.createWindowPause();
        GameWorld.sounds.forEach((name, sound) -> sound.pause());
      }
  }
}
