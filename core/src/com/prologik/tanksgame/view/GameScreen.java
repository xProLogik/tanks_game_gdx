package com.prologik.tanksgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prologik.tanksgame.model.Tank;

public class GameScreen implements Screen {

  private SpriteBatch batch;
  private Texture texture;
  private Tank tank;
  private OrthographicCamera camera;
  public static float deltaCff;

  @Override
  public void show() {
    batch = new SpriteBatch();
    texture = new Texture("sprites.png");
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    tank = new Tank(texture, -1f, -3f, 2f, 2f);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    deltaCff = delta;

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    tank.draw(batch);
    batch.end();
  }

  @Override
  public void resize(int width, int height) {
    float aspectRatio = (float) width/height;
    camera = new OrthographicCamera(26f, 26f/aspectRatio);
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
