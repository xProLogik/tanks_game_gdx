package com.prologik.tanksgame.view;

import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.model.GameWorld;


public class GameScreen extends AbstractScreen {


  private final GameWorld world;
  private final WorldRenderer renderer;
  private float runTime = 0;

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
  public void resize(int width, int height) {
    super.resize(width, height);
    renderer.resize(width,height);
  }

  @Override
  public void update(float delta) {
    runTime+=delta;
    world.update(delta,runTime);
  }

  @Override
  public void draw(float delta) {
    renderer.render(world);
  }


  @Override
  public boolean isDone() {
    return isDone;
  }
}
