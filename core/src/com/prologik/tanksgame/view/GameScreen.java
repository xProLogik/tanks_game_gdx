package com.prologik.tanksgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.model.Tank;

public class GameScreen implements Screen {

  private SpriteBatch batch;
  private Texture texture;
  private Tank tank;

  @Override
  public void show() {
    batch = new SpriteBatch();
    texture = new Texture("sprites.png");
    tank = new Tank(texture,0,0,288,256);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();
    tank.draw(batch);
    batch.end();
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
    texture.dispose();
    batch.dispose();
  }
}
