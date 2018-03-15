package com.prologik.tanksgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.Heading;
import com.prologik.tanksgame.model.Bullet;
import com.prologik.tanksgame.model.Tank;

public class GameScreen implements Screen {

  public static SpriteBatch batch;
  public static TextureAtlas textureAtlas;
  private Tank tank;
  private OrthographicCamera camera;
  public static float deltaCff;

  @Override
  public void show() {
    batch = new SpriteBatch();
    tank = new Tank(textureAtlas.findRegion("tank1",0), -1f, -3f, 2f, 2f);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
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
    textureAtlas.dispose();
    batch.dispose();
  }
}
