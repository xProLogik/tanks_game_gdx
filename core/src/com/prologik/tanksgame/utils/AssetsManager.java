package com.prologik.tanksgame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetsManager {
  public final AssetManager manager = new AssetManager();

  public void load() {
    manager.load("sprites/MyAtlas.atlas", TextureAtlas.class);
  }
}
