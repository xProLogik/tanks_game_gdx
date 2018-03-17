package com.prologik.tanksgame.view;

import com.badlogic.gdx.Screen;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.WorldController;
import com.prologik.tanksgame.control.WorldRenderer;
import com.prologik.tanksgame.model.GameWorld;


public class GameScreen implements Screen {


  MainGame game;

  private GameWorld world;
  private WorldRenderer renderer;
  private WorldController controller;
  private float runTime;


  public GameScreen() {
    this.world = new GameWorld();
    this.controller = new WorldController(world);
    this.renderer = new WorldRenderer(world);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    runTime += delta;
    controller.update(delta);
    renderer.render(runTime);
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
