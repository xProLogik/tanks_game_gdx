package com.prologik.tanksgame.view;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {
  @Override
  public void show() {
    
  }

  @Override
  public void render(float delta) {
    update(delta);
    draw(delta);
  }

  public abstract void draw(float delta);

  public abstract void update(float delta);

  public abstract boolean isDone ();

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
