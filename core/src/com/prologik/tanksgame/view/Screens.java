package com.prologik.tanksgame.view;

import com.badlogic.gdx.Screen;

public enum Screens {
  MAIN_MENU {
    @Override
    protected Screen getScreenInstance() {
      return new MainMenu();
    }
  },

  GAME {
    @Override
    protected Screen getScreenInstance() {
      return new GameScreen();
    }
  },

  CREDITS {
    @Override
    protected Screen getScreenInstance() {
      return new MainMenu();
    }
  };

  protected abstract Screen getScreenInstance();
}
