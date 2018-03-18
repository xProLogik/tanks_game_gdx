package com.prologik.tanksgame.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;

public class ScreenManager {
  private static ScreenManager instance;

  private Game game;

  private IntMap<Screen> screensMap;

  public ScreenManager() {
    screensMap = new IntMap<>();
  }

  public static ScreenManager getInstance() {
    if(null == instance){
      instance = new ScreenManager();
    }
    return instance;
  }

  public void initialize(Game game){
    this.game = game;
  }

  public void show(Screens screens){
    if(null == game) return;
    if(!screensMap.containsKey(screens.ordinal())){
      screensMap.put(screens.ordinal(), screens.getScreenInstance());
    }
    game.setScreen(screensMap.get(screens.ordinal()));
  }

  public void dispose(Screens screens){
    if (!screensMap.containsKey(screens.ordinal())) return;
    screensMap.remove(screens.ordinal()).dispose();
  }

  public void dispose(){
    for(Screen screen : screensMap.values()){
      screen.dispose();
    }
    screensMap.clear();
    instance = null;
  }
}
