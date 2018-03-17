package com.prologik.tanksgame.control;

import com.prologik.tanksgame.model.Enemy;
import com.prologik.tanksgame.model.GameWorld;
import com.prologik.tanksgame.model.Player;
import com.prologik.tanksgame.model.Tank;

public class WorldController {
  private GameWorld world;
  private Enemy enemy;
  private Player player;
  public WorldController(GameWorld world) {
    this.world = world;
    this.enemy = world.getEnemy();
    this.player = world.getPlayer();
  }
  public void update(float delta){
    enemy.update(delta);
    player.update(delta);
  }

}
