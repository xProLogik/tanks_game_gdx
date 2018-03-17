package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.model.Enemy;
import com.prologik.tanksgame.model.GameWorld;
import com.prologik.tanksgame.model.Level;
import com.prologik.tanksgame.model.Player;
import sun.font.GraphicComponent;

public class WorldRenderer {

  private SpriteBatch batcher;
  private OrthographicCamera camera;
  private GameWorld world;
  private WorldController worldController;
  private Player player;
  private Enemy enemy;
  private Level level;

  public WorldRenderer(GameWorld world) {
    this.world = world;
    float aspectRatio = (float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
    camera = new OrthographicCamera(26f, 26f/aspectRatio);
    level = world.getLevel();
    player = world.getPlayer();
    enemy = world.getEnemy();
    batcher = new SpriteBatch();
    batcher.setProjectionMatrix(camera.combined);
  }


  public void render(float runTime) {
    Gdx.gl.glClearColor(0,0,0,1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batcher.begin();
    level.draw(batcher);
    player.draw(batcher);
    enemy.draw(batcher);
    batcher.end();
  }


}
