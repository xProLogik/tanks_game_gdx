package com.prologik.tanksgame.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.prologik.tanksgame.model.GameWorld;

public class WorldRenderer implements Disposable {

  private SpriteBatch batcher;
  private OrthographicCamera camera;
  private ShapeRenderer shapeRenderer;

  public WorldRenderer() {
    float aspectRatio = (float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
    camera = new OrthographicCamera(26f, 26f/aspectRatio);
    shapeRenderer = new ShapeRenderer();
    batcher = new SpriteBatch();
    batcher.setProjectionMatrix(camera.combined);
    shapeRenderer.setProjectionMatrix(camera.combined);
  }


  public void render(GameWorld world, float delta) {
    Gdx.gl.glClearColor(0,0,0,1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    drawPlayFieldCarcass();

    batcher.begin();
    world.draw(batcher);
    batcher.end();
    drawPlayFieldCarcass();
  }

  private void drawPlayFieldCarcass() {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(0.5f, 0.2f, .2f, 1);
    for (int i = (int)GameWorld.PLAYFIELD_MIN_X; i <=(int)GameWorld.PLAYFIELD_MAX_X; i++)
    {
      shapeRenderer.line(i, GameWorld.PLAYFIELD_MIN_Y, i,  GameWorld.PLAYFIELD_MAX_Y);
    }

    for (int i = (int)GameWorld.PLAYFIELD_MIN_Y; i <=(int)GameWorld.PLAYFIELD_MAX_Y; i++)
    {
      shapeRenderer.line(GameWorld.PLAYFIELD_MIN_X,  i, GameWorld.PLAYFIELD_MAX_X, i);
    }

    shapeRenderer.end();
  }


  @Override
  public void dispose() {
    batcher.dispose();
    shapeRenderer.dispose();
  }
}
