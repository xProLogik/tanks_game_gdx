package com.prologik.tanksgame.model.constuction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.prologik.tanksgame.MainGame;
import com.prologik.tanksgame.control.construction.ConstructionController;
import com.prologik.tanksgame.control.construction.MyTextListener;
import com.prologik.tanksgame.model.gameworld.anotherobjects.*;
import com.prologik.tanksgame.model.gameworld.movableobjects.Enemy;
import com.prologik.tanksgame.model.gameworld.movableobjects.TankLevel;
import com.prologik.tanksgame.utils.gui.Button;
import com.prologik.tanksgame.utils.gui.ButtonPlus;
import com.prologik.tanksgame.view.MainMenuScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructionWorld {

  public MainGame mainGame;
  private ConstructionController controller;
  private MyTextListener listener = new MyTextListener();

  private List<Vector2> importantPositionsFull = new ArrayList<Vector2>() {{
    add(new Vector2(-17, 11));
    add(new Vector2(7, 11));
    add(new Vector2(-5, 11));
    add(new Vector2(-7, -13));
    add(new Vector2(-3, -13));
    add(new Vector2(-5, -13));
    add(new Vector2(-1, -13));
    add(new Vector2(-9, -13));
  }};
  private Vector2 importantPositionTopRight = new Vector2(-3, -11);
  private Vector2 importantPositionTopLeft = new Vector2(-7, -11);
  private Vector2 importantPositionTop = new Vector2(-5, -11);

  private List<Box> startBoxes = new ArrayList<>();
  private List<ConstructorBox> boxes = new ArrayList<>();
  private List<ConstructorBox> removedBoxes = new ArrayList<>();
  private List<Box> finishLevelMap = new ArrayList<>();

  private Map<Rectangle, ConstructorBox> positionsConstructor = new HashMap<>();
  private Eagle eagle;
  private ConstructorBox selectBox;

  public Map<String, ButtonPlus> buttonsPlus = new HashMap<>();
  public Map<String, Button> buttonsMinus = new HashMap<>();
  private List<Enemy> enemyIcon = new ArrayList<>();
  private List<Icon> numberIcon = new ArrayList<>();

  private Map<String, Integer> enemyCount = new HashMap<String, Integer>() {{
    put("EnemyTankLevel_1", 0);
    put("EnemyTankLevel_2", 0);
    put("EnemyTankLevel_3", 0);
    put("EnemyTankLevel_4", 0);
  }};

  private Button saveButton;
  private Button parseButton;
  private Button backButton;

  private boolean startSaveLevel = false;

  private float zeroCoordX;
  private float zeroCoordY;
  private float sideField;
  private float outlineX;
  private float outlineY;
  private float outlineWidth = 2f;
  private float outlineHeight = 2f;
  private String levelName;
  private boolean fewEnemies = false;

  public ConstructionWorld(MainGame game) {
    this.mainGame = game;
    controller = new ConstructionController(this);
    zeroCoordX = -(mainGame.WIDTH - 2) / 2;
    zeroCoordY = -(mainGame.HEIGHT - 2) / 2;
    sideField = mainGame.HEIGHT - 2;
    outlineX = zeroCoordX + sideField + 1;

    eagle = new Eagle(new Vector2(zeroCoordX + (sideField / 2) - 1f, zeroCoordY));
    createStartLevel();
    createConstructMenu();
    saveButton = new Button(new Vector2(10.5f, -13f), 5, 2f, "SAVE", mainGame);
    backButton = new Button(new Vector2(10.5f, -10f), 5, 2f, "BACK", mainGame);
  }

  private void createConstructMenu() {
    ConstructorBox selectBox = null;
    int j = 0;
    for (int i = 0; i < 7; i++) {
      String state = "full";
      j++;
      if (i == 1) {
        state = "left";
        j--;
      }
      if (i == 3) {
        state = "right";
        j--;
      }
      ConstructorBox newConstruct = new ConstructorBox(BoxType.values()[j], state,
          new Vector2(zeroCoordX + sideField + 1, zeroCoordY + sideField - 2 - 3 * i));
      if (selectBox == null) selectBox = newConstruct;
      positionsConstructor.put(newConstruct.bound, newConstruct);
    }
    startSelectBox(selectBox);

  }

  private void startSelectBox(ConstructorBox selectBox) {

    this.selectBox = selectBox;
    outlineY = selectBox.getPosition().y;
  }

  private void createStartLevel() {
    for (Vector2 position : eagle.getPositionAround())
      startBoxes.add(new Box(BoxType.BRICS, position));
  }

  private void drawPlayFieldWithCarcass() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.setColor(0, 0, 0, 1);
    mainGame.shapeRenderer.rect(zeroCoordX, zeroCoordY, sideField, sideField);
    mainGame.shapeRenderer.end();

    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    mainGame.shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
    for (int i = 2; i < sideField; i += 2) {
      mainGame.shapeRenderer.line(zeroCoordX, zeroCoordY + i, zeroCoordX + sideField, zeroCoordY + i);
      mainGame.shapeRenderer.line(zeroCoordX + i, zeroCoordY, zeroCoordX + i, zeroCoordY + sideField);
    }
    mainGame.shapeRenderer.end();
  }

  public void draw(SpriteBatch batch) {
    if (!startSaveLevel) {
      saveButton.draw(batch);
      backButton.draw(batch);
      drawSquad();
      drawPlayFieldWithCarcass();
      batch.begin();
      eagle.draw(batch);
      startBoxes.forEach(box -> box.draw(batch));
      positionsConstructor.values().forEach(box -> box.draw(batch));
      boxes.forEach(box -> box.constrBoxes.forEach(b -> b.draw(batch)));
      batch.end();
      drawOutline();
    }
    if (startSaveLevel) {
      drawBlockAddEnemy();
      if (parseButton != null) parseButton.draw(batch);
      batch.begin();
      if (fewEnemies)
        mainGame.bitmapFont.draw(batch, "FEW ENEMIES", -5, 5);
      enemyIcon.forEach(enemy -> enemy.draw(batch));
      numberIcon.forEach(number -> number.draw(batch));
      batch.end();
      buttonsPlus.values().forEach(buttonPlus -> buttonPlus.draw(batch));
      buttonsMinus.values().forEach(button -> button.draw(batch));


    }
  }

  private void drawBlockAddEnemy() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.setColor(Color.BLACK);
    mainGame.shapeRenderer.rect(-11.5f, -7f, 23, 10);
    mainGame.shapeRenderer.end();
  }

  private void drawSquad() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    mainGame.shapeRenderer.setColor(0, 0, 0, 1);
    mainGame.shapeRenderer.rect(outlineX, 8, 2, 2);
    mainGame.shapeRenderer.rect(outlineX, 2, 2, 2);
    mainGame.shapeRenderer.end();
  }

  private void drawOutline() {
    mainGame.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    mainGame.shapeRenderer.setColor(1f, 0f, 0f, 1);
    mainGame.shapeRenderer.rect(outlineX, outlineY, outlineWidth, outlineHeight);
    mainGame.shapeRenderer.end();
  }

  public void update(float delta) {
    updateEnemyCount();
    controller.update(delta);
    if (listener.getText() != null && levelName == null) {
      levelName = listener.getText();
      File newFile = new File("levels/user/" + levelName + ".lvl");
      if (newFile.exists()) {
        levelName = null;
        listener.setTextNull();
        inputFileName();
        System.out.println("Есть файл");
      } else try {
        newFile.createNewFile();
        parseLevelToFile();
        System.out.println("Создан файл файл");
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  private void updateEnemyCount() {
    if (!numberIcon.isEmpty()) {
      Pattern p = Pattern.compile("[0-9]");
      Matcher m;
      for (Map.Entry<String, Integer> entry : enemyCount.entrySet()) {
        String nameEnemy = entry.getKey();
        m = p.matcher(nameEnemy);
        int number = Byte.parseByte(m.find() ? m.group() : "1");
        numberIcon.get(number - 1).getObject().setRegion(MainGame.sptriteAtlas.findRegion("number", entry.getValue()));
      }
    }
  }

  public void dispose() {
  }


  public void selectCostructrBox(Vector2 mousePosition) {
    for (Map.Entry<Rectangle, ConstructorBox> entry : positionsConstructor.entrySet())
      if (mouseInRect(mousePosition, entry.getKey())) {
        if (!entry.getValue().state.equals("full") && entry.getValue().equals(selectBox))
          changeState(entry.getValue());
        outlineY = entry.getValue().getPosition().y;
        selectBox = entry.getValue();
      }
  }

  private void changeState(ConstructorBox box) {
    List<Box> removed = new ArrayList<>();
    switch (box.state) {
      case "left":
        removed.add(box.constrBoxes.parallelStream()
            .filter(b -> b.position.equals(box.position)).findFirst().orElse(null));
        box.constrBoxes.add(new Box(box.type, new Vector2(box.position.x + 1, box.position.y + 1)));
        box.state = "top";
        break;
      case "top":
        removed.add(box.constrBoxes.parallelStream()
            .filter(b -> b.position.x == box.position.x && b.position.y == box.position.y + 1)
            .findFirst().orElse(null));
        box.constrBoxes.add(new Box(box.type, new Vector2(box.position.x + 1, box.position.y)));
        box.state = "right";
        break;
      case "right":
        removed.add(box.constrBoxes.parallelStream()
            .filter(b -> b.position.x == box.position.x + 1 && b.position.y == box.position.y + 1)
            .findFirst().orElse(null));
        box.constrBoxes.add(new Box(box.type, box.position));
        box.state = "bottom";
        break;
      case "bottom":
        removed.add(box.constrBoxes.parallelStream()
            .filter(b -> b.position.x == box.position.x + 1 && b.position.y == box.position.y)
            .findFirst().orElse(null));
        box.constrBoxes.add(new Box(box.type, new Vector2(box.position.x, box.position.y + 1)));
        box.state = "left";
        break;
    }
    box.constrBoxes.removeAll(removed);
  }


  private boolean mouseInRect(Vector2 mousePosition, Rectangle rect) {
    return mousePosition.x >= rect.getX() && mousePosition.y >= rect.getY() &&
        mousePosition.x <= rect.getX() + rect.getWidth() &&
        mousePosition.y <= rect.getY() + rect.getHeight();
  }

  public void addOnField(Vector2 mousePosition) {
    removedBoxes.clear();
    Vector2 position = new Vector2();
    if (mousePosition.x > 0) position.x = ((int) Math.ceil(mousePosition.x)) / 2 * 2 - 1;
    else position.x = ((int) Math.floor(mousePosition.x)) / 2 * 2 - 1;
    if (mousePosition.y > 0) position.y = ((int) Math.ceil(mousePosition.y)) / 2 * 2 - 1;
    else position.y = ((int) Math.floor(mousePosition.y)) / 2 * 2 - 1;
    ConstructorBox testBox;
    if (mouseInField(mousePosition))
      if (isNotImportant(position)) {
        testBox = boxes.parallelStream().filter(box -> box.position.epsilonEquals(position))
            .findFirst().orElse(null);
        if (testBox != null) {
          if (selectBox.state.equals("full")) {
            removedBoxes.add(testBox);
            if (!testBox.type.equals(selectBox.type))
              boxes.add(new ConstructorBox(selectBox.type, selectBox.state, position));
          } else {
            ConstructorBox newBox = new ConstructorBox(selectBox.type, selectBox.state, position);
            List<Box> removed = new ArrayList<>();
            boolean isAllTypeEquals = true;
            for (Box box : testBox.constrBoxes) {
              for (Box anotherBox : newBox.constrBoxes) {
                if (box.position.epsilonEquals(anotherBox.position)) {
                  removed.add(box);
                  if (!box.getBoxType().equals(anotherBox.getBoxType())) isAllTypeEquals = false;
                }
              }
            }
            testBox.constrBoxes.removeAll(removed);
            if (!isAllTypeEquals || removed.size() != 2)
              testBox.constrBoxes.addAll(newBox.constrBoxes);
            removed.clear();
            if (testBox.constrBoxes.isEmpty()) removedBoxes.add(testBox);
          }
        } else boxes.add(new ConstructorBox(selectBox.type, selectBox.state, position));

        boxes.removeAll(removedBoxes);
      }
  }

  private boolean isNotImportant(Vector2 position) {
    switch (selectBox.state) {
      case "left":
        return !importantPositionsFull.contains(position) &&
            !importantPositionTopRight.epsilonEquals(position) &&
            !importantPositionTop.epsilonEquals(position);
      case "right":
        return !importantPositionsFull.contains(position) &&
            !importantPositionTopLeft.epsilonEquals(position) &&
            !importantPositionTop.epsilonEquals(position);
      case "top":
        return !importantPositionsFull.contains(position);
      default:
        return !importantPositionsFull.contains(position) &&
            !importantPositionTopRight.epsilonEquals(position) &&
            !importantPositionTopLeft.epsilonEquals(position) &&
            !importantPositionTop.epsilonEquals(position);
    }

  }

  private boolean mouseInField(Vector2 mousePosition) {
    return mousePosition.x >= zeroCoordX && mousePosition.y >= zeroCoordY &&
        mousePosition.x <= zeroCoordX + sideField &&
        mousePosition.y <= zeroCoordY + sideField;
  }

  public void startSaveLevel() {
    startSaveLevel = true;
    createAddEnemyMenu();
  }

  private void createAddEnemyMenu() {
    Vector2 startPosition = new Vector2(-10, -2);
    for (int i = 0; i < 4; i++) {
      enemyIcon.add(new Enemy(TankLevel.values()[i], new Vector2(startPosition.x + 1 + 5 * i,
          startPosition.y + 2)));

      Button newButton = new Button(new Vector2(startPosition.x + 5 * i, startPosition.y + .3f),
          1, mainGame);
      newButton.setOutButton(new Color(0.6f, 0.6f, 0.6f, 1));
      newButton.setOverButton(new Color(0.4f, 0.4f, 0.4f, 1));
      buttonsMinus.put("EnemyTankLevel_" + (i + 1), newButton);

      int number = enemyCount.get("EnemyTankLevel_" + (i + 1));
      numberIcon.add(new Icon("number", number,
          new Vector2(startPosition.x + 1.5f + 5 * i, startPosition.y), 1, 1));
      ButtonPlus newButtonPlus = new ButtonPlus(new Vector2(startPosition.x + 3 + 5 * i, startPosition.y + .3f),
          1, mainGame);
      newButtonPlus.setOutButton(new Color(0.6f, 0.6f, 0.6f, 1));
      newButtonPlus.setOverButton(new Color(0.4f, 0.4f, 0.4f, 1));
      buttonsPlus.put("EnemyTankLevel_" + (i + 1), newButtonPlus);
    }
    parseButton = new Button(new Vector2(-2.5f, -6), 5, 2f, "SAVE", mainGame);
  }

  public boolean isStartSaveLevel() {
    return startSaveLevel;
  }

  public Button getSaveButton() {
    return saveButton;
  }


  public void addCountEnemy(String typeEnemy) {
    Integer count = enemyCount.get(typeEnemy);
    if (count != 9)
      count++;
    else count = 0;
    enemyCount.put(typeEnemy, count);
  }

  public void minusCountEnemy(String typeEnemy) {
    Integer count = enemyCount.get(typeEnemy);
    if (count != 0)
      count--;
    else count = 9;
    enemyCount.put(typeEnemy, count);
  }

  private void parseLevelToFile() {
    finishLevelMap.addAll(startBoxes);
    boxes.forEach(box -> finishLevelMap.addAll(box.constrBoxes));
    LevelManager.levelParserToFile("levels/user/" + levelName + ".lvl", finishLevelMap, enemyCount);
    mainGame.setScreen(new MainMenuScreen(mainGame));
  }

  public void inputFileName() {
    int countEnemy = 0;
    for (int count : enemyCount.values()) countEnemy += count;
    if (countEnemy == 0) fewEnemies = true;
    else {
      Gdx.input.getTextInput(listener, "Level Construction", "", "Enter name level");
      fewEnemies = false;
    }

  }


  public Button getParseButton() {
    return parseButton;
  }

  public Button getBackButton() {
    return backButton;
  }

  public void backScreen() {
    mainGame.setScreen(new MainMenuScreen(mainGame));
  }
}
