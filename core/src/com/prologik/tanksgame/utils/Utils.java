package com.prologik.tanksgame.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static Integer[][] levelParser(String levelPath){
    Integer[][] result = null;
    try(BufferedReader reader = new BufferedReader(new FileReader(new File(levelPath)))) {
      String line;
      List<Integer[]> lvlLines = new ArrayList<>();
      while ((line = reader.readLine())!= null)
        lvlLines.add(str2int_arrays(line.split(" ")));

      result = new Integer[lvlLines.size()][lvlLines.get(0).length];
      for (int i = 0; i < lvlLines.size(); i++) {
        result[i]=lvlLines.get(i);
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    return result;
  }
  private static Integer[] str2int_arrays(String[] sArr) {
    Integer[] result = new Integer[sArr.length];
    for (int i = 0; i <sArr.length; i++)
      result[i]=Integer.parseInt(sArr[i]);
    return result;
  }
}
