package com.prologik.tanksgame.control;

import com.prologik.tanksgame.model.Enemy;
import com.prologik.tanksgame.model.GameWorld;
import com.prologik.tanksgame.model.Player;

public class WorldController {
  private GameWorld world;
  public WorldController(GameWorld world) {
    this.world = world;
  }
  public void update(float delta){
    world.update(delta);
  }
}
