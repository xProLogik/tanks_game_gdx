package com.prologik.tanksgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.prologik.tanksgame.utils.AssetsManager;
import com.prologik.tanksgame.view.ConstructionScreen;
import com.prologik.tanksgame.view.MainMenuScreen;

public class MainGame extends Game {

  public final float WIDTH = 36f;
  public final float HEIGHT = 28f;
  public OrthographicCamera camera;
  public SpriteBatch batcher;
  public Viewport viewport;
  public ShapeRenderer shapeRenderer;
  public BitmapFont bitmapFont;
  public static TextureAtlas sptriteAtlas;
  private static AssetsManager manager;
  private FPSLogger fpsLogger;

  public void create() {

    manager = new AssetsManager();
    manager.load();
    manager.manager.finishLoading();
    sptriteAtlas = manager.manager.get("sprites/MyAtlas.atlas", TextureAtlas.class);

    camera = new OrthographicCamera(WIDTH, HEIGHT);
    viewport = new FitViewport(WIDTH, HEIGHT);
    batcher = new SpriteBatch();
    shapeRenderer = new ShapeRenderer();

    bitmapFont = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"),
        Gdx.files.internal("fonts/myfont.png"),false,false);
    bitmapFont.getData().setScale(0.04f);

    this.setScreen(new MainMenuScreen(this));
    fpsLogger = new FPSLogger();
  }

  public void dispose() {
    super.dispose();
    sptriteAtlas.dispose();
    batcher.dispose();
    shapeRenderer.dispose();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    viewport.update(width, height);
  }

  public void render() {

    camera.update();
    batcher.setProjectionMatrix(camera.combined);
    shapeRenderer.setProjectionMatrix(camera.combined);
    screen.render(Gdx.graphics.getDeltaTime());

  }



}
