package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.prologik.tanksgame.model.GameWorld;

public class WorldRenderer implements Disposable {

  public static float CAM_HEIGHT = 26f;
  public static float CAM_WIDTH = 36f;
  private Viewport viewport;
  private SpriteBatch batcher;
  private OrthographicCamera camera;

  public WorldRenderer() {
    float aspectRatio = CAM_WIDTH/CAM_HEIGHT;
    camera = new OrthographicCamera(CAM_WIDTH, CAM_HEIGHT);
    viewport = new FitViewport(600*aspectRatio, 600, camera);
    batcher = new SpriteBatch();
    batcher.setProjectionMatrix(camera.combined);
  }


  public void render(GameWorld world, float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batcher.begin();
    world.draw(batcher);
    batcher.end();
  }


  @Override
  public void dispose() {
    batcher.dispose();
  }

  public void resize(int width, int height) {
    viewport.update(width, height);
  }
}
