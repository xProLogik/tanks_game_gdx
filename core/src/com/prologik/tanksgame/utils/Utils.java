package com.prologik.tanksgame.utils;

import com.prologik.tanksgame.control.WorldRenderer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Utils {
  public static int[][] levelParser(String levelPath) {
    int sizeField = (int) WorldRenderer.CAM_HEIGHT;
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
}
