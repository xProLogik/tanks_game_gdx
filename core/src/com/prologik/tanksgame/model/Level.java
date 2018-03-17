package com.prologik.tanksgame.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.prologik.tanksgame.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Level {
  private List<Tile> levelMap1;
  private Integer[][] levelMap;

  Level(TextureAtlas textureAtlas) {
    levelMap = Utils.levelParser("level1.lvl");
    createLevel(textureAtlas);
  }

  private void createLevel(TextureAtlas textureAtlas) {
    levelMap1= new ArrayList<>();
    for (int i = 0; i < levelMap.length; i++) {
      for (int j = 0; j < levelMap[0].length; j++) {
        if (levelMap[i][j] != 0)
          levelMap1.add(new Tile(textureAtlas.findRegion(TileType.nameByNumber(levelMap[i][j])),
              j * 1f-13f, 12f-i * 1f, 1f, 1f));
      }
    }
  }

  public void draw(SpriteBatch batch) {
    for(Tile tile : levelMap1)
      tile.draw(batch);

  }
}
