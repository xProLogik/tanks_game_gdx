package com.prologik.tanksgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.prologik.tanksgame.view.AbstractScreen;
import com.prologik.tanksgame.view.ScreenManager;
import com.prologik.tanksgame.view.Screens;

public class MainGame extends Game {
  private FPSLogger fpsLogger;


  public void create() {
    ScreenManager.getInstance().initialize(this);
    ScreenManager.getInstance().show(Screens.GAME);
    fpsLogger = new FPSLogger();
  }

  public void dispose() {
    super.dispose();
    ScreenManager.getInstance().dispose();
  }

  public void render() {
    AbstractScreen screen = getScreen();

    screen.render(Gdx.graphics.getDeltaTime());

    fpsLogger.log();
  }

  @Override
  public AbstractScreen getScreen () {
    return (AbstractScreen) super.getScreen();
  }

//  private Assets assets;
//  private Screen gameScreen;
//
//  @Override
//  public void create() {
//    assets = new Assets();
//    gameScreen = new GameScreen();
//    setScreen(gameScreen);
//  }
//
//  @Override
//  public void dispose() {
//    super.dispose();
//    gameScreen.dispose();
//    assets.dispose();
//  }

}
