package com.prologik.tanksgame.view;

import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.model.GameWorld;


public class GameScreen extends AbstractScreen {


  private final GameWorld world;
  private final WorldRenderer renderer;

  private boolean isDone = false;

  GameScreen() {
    world = new GameWorld();
    renderer = new WorldRenderer();
  }

  @Override
  public void dispose() {
    renderer.dispose();
    world.dispose();
  }

  @Override
  public void update(float delta) {
    world.update(delta);
  }

  @Override
  public void draw(float delta) {
    renderer.render(world, delta);
  }

  @Override
  public boolean isDone() {
    return isDone;
  }
}
