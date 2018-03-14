package com.prologik.tanksgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.prologik.tanksgame.view.GameScreen;

public class MainGame extends Game {
  private Screen gameScreen;
  @Override
  public void create() {
    gameScreen = new GameScreen();
    setScreen(gameScreen);
  }
}
