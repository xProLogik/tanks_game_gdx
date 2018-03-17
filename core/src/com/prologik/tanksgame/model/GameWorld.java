package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameWorld {

  private TextureAtlas textureAtlas;
  private Level level;
  private Player player;
  private Enemy enemy;

  public GameWorld() {
    textureAtlas = new TextureAtlas("MyAtlas.atlas");
    level = new Level(textureAtlas);
    player = new Player(textureAtlas);
    enemy = new Enemy(textureAtlas);
  }

  public Player getPlayer() {
    return player;
  }
  public Enemy getEnemy(){
    return enemy;
  }

  public Level getLevel() {
    return level;
  }
}
