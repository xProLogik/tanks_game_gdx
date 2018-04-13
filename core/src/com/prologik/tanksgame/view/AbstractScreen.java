package com.prologik.tanksgame.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.MainGame;

public abstract class AbstractScreen implements Screen {
  final MainGame mainGame;

  AbstractScreen(MainGame mainGame) {
    this.mainGame = mainGame;
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    update(delta);
    draw(mainGame.batcher);
  }

  public abstract void draw(SpriteBatch batch);

  public abstract void update(float delta);


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
    mainGame.dispose();
  }
}
