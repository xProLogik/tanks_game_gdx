package com.prologik.tanksgame.model.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.mainmenu.MainMenuController;
import com.prologik.tanksgame.model.gameworld.anotherobjects.LevelManager;
import com.prologik.tanksgame.utils.gui.Button;
import com.prologik.tanksgame.view.ConstructionScreen;
import com.prologik.tanksgame.view.GameScreen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainMenu {

  private int countPlayers = 0;
  private final Sprite bg;
  private final Sprite cursor;
  public final MainGame mainGame;
  private final MainMenuController controller;
  private Map<String, Button> levelButton = new HashMap<>();
  private ArrayList<Button> buttons = new ArrayList<>();
  private boolean isSelectedLevel = false;
  private boolean screenScroll = false;
  private Vector2 mouseSavePosition = new Vector2();
  private float maxCamPositionY;
  private float minCamPositionY;

  public MainMenu(MainGame game) {
    this.mainGame = game;
    controller = new MainMenuController(this);

    bg = new Sprite(MainGame.sptriteAtlas.findRegion("bgmainmenu"));
    bg.setSize(mainGame.WIDTH, mainGame.HEIGHT);
    bg.setCenter(0, 0);


    buttons.add(new Button(new Vector2(-4f, -5f), 7.5f, 2, "1 PLAYER", mainGame));
    buttons.add(new Button(new Vector2(-4f, -8f), 8.5f, 2, "2 PLAYERS", mainGame));
    buttons.add(new Button(new Vector2(-4f, -11f), 12.5f, 2, "CONSTRUCTION", mainGame));
    buttons.forEach(b -> b.setColorButton(Color.BLACK));
    cursor = new Sprite(MainGame.sptriteAtlas.findRegion("cursor"));
    cursor.setSize(2, 2);
    cursor.setPosition(-7, -5);
  }


  public void draw(SpriteBatch batch) {
    if (!isSelectedLevel) {
      batch.begin();
      bg.draw(batch);
      cursor.draw(batch);
      batch.end();
      buttons.forEach(b -> b.draw(batch));
    } else {
      batch.begin();
      cursor.draw(batch);
      batch.end();
      levelButton.forEach((s, button) -> button.draw(batch));
    }
  }

  public void dispose() {
  }

  public void update(float delta) {
    controller.update(delta);
    if (!isSelectedLevel)
      updatePositionCursor(buttons);
    else updatePositionCursor(levelButton.values());
  }

  private void updatePositionCursor(Collection<Button> buttons) {
    for (Button button : buttons)
      if (button.getPosition().y == cursor.getY()) button.setColorText(Color.YELLOW);
      else button.setColorText(Color.WHITE);
    if (screenScroll) {
      if (cursor.getY() < mainGame.camera.position.y - 14) {
        mainGame.camera.position.set(mainGame.camera.position.x, cursor.getY() + 13, 0);
      }
      if (cursor.getY() > mainGame.camera.position.y + 11)
        mainGame.camera.position.set(mainGame.camera.position.x, cursor.getY() - 11, 0);
    }
  }

  public void changeCursorPosition(int direction, float maxY, float minY) {
    if (direction > 0)
      if (cursor.getY() != maxY) cursor.setPosition(cursor.getX(), cursor.getY() + 3);
      else cursor.setPosition(cursor.getX(), minY);
    if (direction < 0)
      if (cursor.getY() != minY) cursor.setPosition(cursor.getX(), cursor.getY() - 3);
      else cursor.setPosition(cursor.getX(), maxY);
  }

  public ArrayList<Button> getButtons() {
    return buttons;
  }

  public void changeColorButton(Button button) {
    cursor.setPosition(cursor.getX(), button.getPosition().y);
  }

  public void selectMenu(Button button) {
    switch (button.getTextButton()) {
      case "1 PLAYER":
        selectedLevel();
        countPlayers = 1;
        break;
      case "2 PLAYERS":
        selectedLevel();
        countPlayers = 2;
        break;
      case "CONSTRUCTION":
        mainGame.setScreen(new ConstructionScreen(mainGame));
        break;
    }
  }

  private void selectedLevel() {
    FileHandle[] userFileList = Gdx.files.internal("levels/user/").list();
    int countButton = LevelManager.readOpenLevels().size() + userFileList.length;

    int buttonPositionY;
    if (countButton > 9) {
      buttonPositionY = 11;
      screenScroll = true;
    } else
      buttonPositionY = 11 - (9 - countButton) / 2 * 3;

    Pattern p = Pattern.compile("[0-9]{1,2}");
    Matcher m;
    Button newButton;
    for (String level : LevelManager.readOpenLevels()) {
      m = p.matcher(level);
      int numberButton = 0;
      if (m.find()) {
        numberButton = Integer.parseInt(m.group());
      }
      newButton = new Button(new Vector2(-4, buttonPositionY),
          9, 2, "LEVEL " + numberButton, mainGame);
      newButton.setColorButton(Color.BLACK);
      levelButton.put("levels/gamedata/" + level, newButton);
      buttonPositionY -= 3;
    }

    for (FileHandle file : userFileList) {
      String nameButton = file.name().substring(0, file.name().lastIndexOf(".lvl"));
      newButton = new Button(new Vector2(-4, buttonPositionY),
          nameButton.length() + 1, 2, nameButton, mainGame);
      newButton.setColorButton(Color.BLACK);
      levelButton.put("levels/user/" + file.name(), newButton);
      buttonPositionY -= 3;
    }
    cursor.setPosition(cursor.getX(), levelButton.values().stream()
        .max((o1, o2) -> Float.compare(o1.getPosition().y, o2.getPosition().y)).get().getPosition().y);
    isSelectedLevel = true;
    maxCamPositionY = levelButton.values().stream()
        .max((o1, o2) -> Float.compare(o1.getPosition().y, o2.getPosition().y)).get().getPosition().y - 11;
    minCamPositionY = levelButton.values().stream()
        .min((o1, o2) -> Float.compare(o1.getPosition().y, o2.getPosition().y)).get().getPosition().y + 13;
  }

  public Sprite getCursor() {
    return cursor;
  }

  public boolean isSelectedLevel() {
    return isSelectedLevel;
  }

  public Map<String, Button> getLevelButton() {
    return levelButton;
  }

  public boolean isScreenScroll() {
    return screenScroll;
  }

  public void changeCameraPosition(Vector2 mousePosition, float delta) {
    mainGame.camera.position.set(mainGame.camera.position.x,
        mainGame.camera.position.y - (mousePosition.y - mouseSavePosition.y) * delta, 0);
    if (mainGame.camera.position.y > maxCamPositionY) mainGame.camera.position.set(mainGame.camera.position.x,
        maxCamPositionY, 0);
    if (mainGame.camera.position.y < minCamPositionY) mainGame.camera.position.set(mainGame.camera.position.x,
        minCamPositionY, 0);

    if (screenScroll) {
      if (cursor.getY() < mainGame.camera.position.y - 14) {
        cursor.setPosition(cursor.getX(), cursor.getY() + 3);
      }
      if (cursor.getY() > mainGame.camera.position.y + 11)
        cursor.setPosition(cursor.getX(), cursor.getY() - 3);
    }
  }

  public Vector2 getMouseSavePosition() {
    return mouseSavePosition;
  }

  public void startGame(String nameLevel) {
    mainGame.setScreen(new GameScreen(mainGame, countPlayers, nameLevel));
  }
}
