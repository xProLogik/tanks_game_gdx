package com.prologik.tanksgame.model.gameworld.anotherobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.prologik.tanksgame.model.gameworld.GameWorld;
import com.prologik.tanksgame.view.MainMenuScreen;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelManager {

  private GameWorld gameWorld;
  private String level;

  public LevelManager(GameWorld gameWorld) {
    this.gameWorld = gameWorld;
  }

  public void loadLevel(String nameLevel) {
    this.level = nameLevel;
    GameWorld.sounds.get("startlevel").play(0.5f);
    gameWorld.setLevel(new Level(level, this));
    gameWorld.setBufferedEnemies(generateEnemyForLevel(nameLevel));
  }

  public static ArrayList<String> readOpenLevels() {
    ArrayList<String> openedLevels = new ArrayList<>();
    FileHandle saveFile = Gdx.files.internal("save/save.sv");
    try (Scanner scanner = new Scanner(new FileReader(new File((saveFile.path()))))) {
      while (scanner.hasNextLine())
        openedLevels.add(scanner.nextLine());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return openedLevels;
  }

  private void writeOpenLevels(int numberLevel) {
    FileHandle saveFile = Gdx.files.internal("save/save.sv");
    FileHandle[] list = Gdx.files.internal("levels/gamedata/").list();
    List<String> listOpenLevels = new ArrayList<>();
    Pattern p = Pattern.compile("[0-9]{1,2}");
    Matcher m;
    for (FileHandle file : list) {
      int number = 0;
      m = p.matcher(file.name());
      if (m.find()) number = Integer.parseInt(m.group());
      if (number <= numberLevel) {
        listOpenLevels.add(file.name());
      }
    }
    if (listOpenLevels.size() > readOpenLevels().size()) {
      try (FileWriter writer = new FileWriter(saveFile.path(), false)) {
        int size = listOpenLevels.size();
        int i = 0;
        for (String level : listOpenLevels) {
          i++;
          writer.write(level);
          if (i < size) writer.append("\r\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  int[][] levelParser(String levelPath) {
    int sizeField = 26;
    int[][] result = new int[sizeField][sizeField];
    try (Scanner scanner = new Scanner(new FileReader(new File(levelPath)))) {
      for (int i = 0; i < sizeField; i++)
        for (int j = 0; j < sizeField; j++)
          result[i][j] = scanner.nextInt();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return result;
  }

  public static void levelParserToFile(String fileName, List<Box> boxes, Map<String, Integer> enemyCount) {
    try (FileWriter writer = new FileWriter(fileName, false)) {
      int[][] result = new int[26][26];
      for (int x = 0; x < 26; x++)
        for (int y = 0; y < 26; y++) {
          result[x][y] = 0;
        }
      for (Box box : boxes) {
        result[(int) box.position.x + 17][(int) box.position.y + 13] = box.getBoxType().ordinal();
      }
      for (int y = 0; y < 26; y++) {
        for (int x = 0; x < 26; x++) {
          writer.append("" + result[x][y]);
          if (x < 25) writer.append(" ");
        }
        writer.append("\r\n");
      }
      int i = 0;
      for (Map.Entry<String, Integer> entry : enemyCount.entrySet()) {
        writer.write(entry.getKey() + " " + entry.getValue());
        i++;
        if (i < 4) writer.append("\r\n");
      }
      writer.flush();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  private Map<String, Integer> enemyParser(String levelPath) {
    Map<String, Integer> result = new HashMap<>();
    int sizeField = 26;
    try (Scanner scanner = new Scanner(new FileReader(new File(levelPath)))) {
      for (int i = 0; i < sizeField; i++)
        scanner.nextLine();
      while (scanner.hasNextLine()) {
        String name = scanner.next();
        int count = scanner.nextInt();
        result.put(name, count);
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return result;
  }

  private List<Byte> generateEnemyForLevel(String nameLevel) {
    List<Byte> bufferedEnemies = new ArrayList<>();
    Map<String, Integer> enemyParser = enemyParser(nameLevel);
    Pattern p = Pattern.compile("[0-9]");
    Matcher m;
    for (Map.Entry<String, Integer> entry : enemyParser.entrySet()) {
      String typeEnemy = entry.getKey();
      m = p.matcher(typeEnemy);
      byte type = Byte.parseByte(m.find() ? m.group() : "1");
      for (int i = 0; i < entry.getValue(); i++) {
        bufferedEnemies.add(type);
      }
    }
    return bufferedEnemies;
  }

  public void changeLevel() {
    Pattern p = Pattern.compile("[0-9]{1,2}");
    Matcher m = p.matcher(level);
    int numberLevel = 0;
    if (m.find()) {
      numberLevel = Integer.parseInt(m.group()) + 1;
    }
    String nameLevel = "levels/gamedata/level" + numberLevel + ".lvl";
    if (numberLevel < Gdx.files.internal("levels/gamedata/").list().length + 1) {
      writeOpenLevels(numberLevel);
      gameWorld.loadLevel(nameLevel);
    } else {
      gameWorld.mainGame.setScreen(new MainMenuScreen(gameWorld.mainGame));
      gameWorld.dispose();
    }
  }
}
