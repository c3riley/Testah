package org.testah;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TestMany {

  @Test
  public void test() throws InterruptedException {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    for (int i = 0; i < 4; i++) {
      System.out.println(Thread.currentThread().getId() + " COOL " + sdf.format(new Date()));
      Thread.sleep(1000);
    }
  }
  
}
