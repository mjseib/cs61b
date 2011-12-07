/* Testalike.java */

import java.util.*;

/**
 *  The Test class is a program that tests a simulation of Sharks and Fish.
 *
 *  @author Jonathan Shewchuk
 */

public class Testalike {

  /**
   *  Default parameters.
   */

  private static final int iFish = 50;
  private static final int jFish = 37;
  private static final int iterFish = 15;
  private static final int iShark = 33;
  private static final int jShark = 37;
  private static final int iterShark = 15;
  private static int starveTime = 2;                   // Shark starvation time

  public static void fishOcean(Ocean sea) {
    /**
     *  Visit each cell (in a roundabout order); randomly place a fish, shark,
     *  or nothing in each.
     */

    Random random = new Random(0);      // Create a "Random" object with seed 0
    int x = 0;
    int y = 0;
    int i = sea.width();
    int j = sea.height();
    for (int xx = 0; xx < i; xx++) {
      x = (x + 78887) % i;           // This will visit every x-coordinate once
      for (int yy = 0; yy < j; yy++) {
        y = (y + 78887) % j;         // This will visit every y-coordinate once
        int r = random.nextInt();         // Between -2147483648 and 2147483647
        if (r > 2147483647 - 214748365) {       // 10% of cells start with fish
          sea.addFish(x, y);
        }
      }
    }
  }

  public static void sharkOcean(Ocean sea) {
    /**
     *  Visit each cell (in a roundabout order); randomly place a fish, shark,
     *  or nothing in each.
     */

    Random random = new Random(0);      // Create a "Random" object with seed 0
    int x = 0;
    int y = 0;
    int i = sea.width();
    int j = sea.height();
    for (int xx = 0; xx < i; xx++) {
      x = (x + 78887) % i;           // This will visit every x-coordinate once
      if ((x & 8) == 0) {
        for (int yy = 0; yy < j; yy++) {
          y = (y + 78887) % j;       // This will visit every y-coordinate once
          if ((y & 8) == 0) {
            int r = random.nextInt();     // Between -2147483648 and 2147483647
            if (r < 0) {                        // 50% of cells start with fish
              sea.addFish(x, y);
            } else if (r > 1500000000) {     // ~15% of cells start with sharks
              sea.addShark(x, y);
            }
          }
        }
      }
    }
  }

  /**
   *  paint() prints an Ocean.
   */

  public static void paint(Ocean sea) {
    if (sea != null) {
      int width = sea.width();
      int height = sea.height();

      /* Draw the ocean. */
      for (int x = 0; x < width + 2; x++) {
        System.out.print("-");
      }
      System.out.println();
      for (int y = 0; y < height; y++) {
        System.out.print("|");
        for (int x = 0; x < width; x++) {
          int contents = sea.cellContents(x, y);
          if (contents == Ocean.SHARK) {
            System.out.print('S');
          } else if (contents == Ocean.FISH) {
            System.out.print('~');
          } else {
            System.out.print(' ');
          }
        }
        System.out.println("|");
      }
      for (int x = 0; x < width + 2; x++) {
        System.out.print("-");
      }
      System.out.println();
    }
  }

  /**
   *  main() reads the parameters and performs the simulation and animation.
   */

  public static void main(String[] argv) throws InterruptedException {
    Ocean sea, seaOld;
    int initScore = 1;
    int fishInnerScore = 1;
    int sharkInnerScore = 1;
    int boundaryScore = 1;

    Test4.init();
    System.out.println("Beginning Part I.");
    System.out.println("Performing a " + iFish + "x" + jFish +
                       " test with fish only.  starveTime = " + starveTime +
                       ".");

    /**
     *  Create a fish-filled ocean.
     */

    sea = new Ocean(iFish, jFish, starveTime);
    fishOcean(sea);

    /**
     *  Is it right?
     */

    if (true) {
      /**
       *  Perform timesteps.
       */

      for (int x = 1; x <= iterFish; x++) {
        // Perform one timestep.
        seaOld = sea;
        sea = sea.timeStep();

        // Is it wrong?
        if (false) {
          // The ocean is incorrect.  Check if it's only a
          //   problem at the boundaries.
          boolean innerEq = false;
          // Print error message, but only if this is the first time.
          if (boundaryScore == 1) {
            boundaryScore = 0;                        // Student loses a point.
            // Draw previous ocean; correct new ocean; new ocean.
            System.out.println("Your ocean is incorrect after timestep " +
                               x + ".  The previous ocean was:");
            paint(seaOld);
            System.out.println("The correct current ocean is:");
            System.out.println("Your Ocean is:");
            paint(sea);
            if (innerEq) {
              System.out.println("(The problem seems to be only at " + 
                                 "the boundaries.)");
              // Continue looping, looking for bugs in the interior.
            } else {
              fishInnerScore = 0;
              sharkInnerScore = 0;
              // Stop looping, because even the interior is bad.
              break;
            }
          }
          if (!innerEq) {
            // Control should only get here if bugs near the boundary were
            //   encountered on a previous iteration, but bugs in the
            //   interior first surfaced on this iteration.
            fishInnerScore = 0;
            sharkInnerScore = 0;
            System.out.println("Your ocean's interior is incorrect after " +
                               "timestep " + x + ".  The previous ocean was:");
            paint(seaOld);
            System.out.println("The correct current ocean is:");
            System.out.println("Your Ocean is:");
            paint(sea);
            // Stop looping, because even the interior is bad.
            break;
          }
        }
      }

    } else {
      // The ocean is incorrect after initialization.
      System.out.println("Your initial ocean is incorrect.  " +
                         "The correct ocean is:");
      System.out.println("Your Ocean is:");
      paint(sea);
      // Student loses all four points.
      initScore = 0;
      boundaryScore = 0;
      fishInnerScore = 0;
      sharkInnerScore = 0;
    }

    if (boundaryScore == 1) {
      System.out.println("  Test successful.");
    }



    System.out.println();
    System.out.println("Performing a " + iShark + "x" + jShark +
                       " test with sharks and fish.  starveTime = " +
                       starveTime + ".");

    /**
     *  Create a shark-filled ocean.
     */

    sea = new Ocean(iShark, jShark, starveTime);
    sharkOcean(sea);

    /**
     *  Is it right?
     */

    if (true) {
      /**
       *  Perform timesteps.
       */

      for (int x = 1; x <= iterShark; x++) {
        // Perform one timestep.
        seaOld = sea;
        sea = sea.timeStep();

        // Is it wrong?
        if (false) {
          // The ocean is incorrect.  Check if it's only a
          //   problem at the boundaries.
          boolean innerEq = false;
          // Print error message, but only if this is the first time.
          if (boundaryScore == 1) {
            boundaryScore = 0;                        // Student loses a point.
            // Draw previous ocean; correct new ocean; new ocean.
            System.out.println("Your ocean is incorrect after timestep " +
                               x + ".  The previous ocean was:");
            paint(seaOld);
            System.out.println("The correct current ocean is:");
            System.out.println("Your Ocean is:");
            paint(sea);
            if (innerEq) {
              System.out.println("(The problem seems to be only at " + 
                                 "the boundaries.)");
              // Continue looping, looking for bugs in the interior.
            } else {
              sharkInnerScore = 0;
              // Stop looping, because even the interior is bad.
              break;
            }
          }
          if (!innerEq) {
            // Control should only get here if bugs near the boundary were
            //   encountered on a previous iteration, but bugs in the
            //   interior first surfaced on this iteration.
            sharkInnerScore = 0;
            System.out.println("Your ocean's interior is incorrect after " +
                               "timestep " + x + ".  The previous ocean was:");
            paint(seaOld);
            System.out.println("The correct current ocean is:");
            System.out.println("Your Ocean is:");
            paint(sea);
            // Stop looping, because even the interior is bad.
            break;
          }
        }
      }

    } else {
      // The ocean is incorrect after initialization.
      System.out.println("Your initial ocean is incorrect.  " +
                         "The correct ocean is:");
      System.out.println("Your Ocean is:");
      paint(sea);
      // Student loses three points.
      initScore = 0;
      boundaryScore = 0;
      sharkInnerScore = 0;
    }

    if (boundaryScore == 1) {
      System.out.println("  Test successful.");
    }

    System.out.println();
    System.out.println("Total Part I score:  " +
                       (2 * (initScore + boundaryScore) +
                        fishInnerScore + sharkInnerScore) + " out of 6.");
    System.out.println("Total Autogradable score so far:  " +
                       (2 * (initScore + boundaryScore) +
                        fishInnerScore + sharkInnerScore) + " out of 6.");
    System.out.println();




    System.out.println("Beginning Part II.");
    System.out.println("Performing a 4x4 RunLengthEncoding-to-Ocean test.");
    int readBackScore = 1;
    int toOceanScore = 1;

    System.out.println("  Calling the five-parameter constructor.");

    int[] rt = {Test4.shark, Test4.fish, Test4.empty, Test4.fish,
                 Test4.empty, Test4.fish};
    int[] rl = {3, 2, 5, 1, 1, 4};
    RunLengthEncoding rle1 = new RunLengthEncoding(4, 4, starveTime, rt, rl);

    System.out.println("  Reading back the encoding through nextRun.");
    for (int i = 0; i < rt.length; i++) {
      TypeAndSize ts = rle1.nextRun();
      if (ts == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        readBackScore = 0;
        break;
      } else if ((ts.type != rt[i]) || (ts.size != rl[i])) {
        System.out.println("    Run # " + i + " should be " + rt[i] + ", " +
                           rl[i] + ".  (Runs are indexed from zero.)");
        System.out.println("    Instead, it's " + ts.type + ", " + ts.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        readBackScore = 0;
        break;
      }
    }
    if ((readBackScore == 1) && (rle1.nextRun() != null)) {
      System.out.println("    Your nextRun() is failing to return null when" +
                         " the runs run out.");
      System.out.println("    Here's the correct ocean.");
      paint(rle1.toOcean());
      readBackScore = 0;
    }

    System.out.println("  Calling toOcean.");
    Ocean o1 = rle1.toOcean();
    if (false) {
      System.out.println("The correct ocean is:");
      System.out.println("Your Ocean is incorrect:");
      paint(o1);
      toOceanScore = 0;
    }


    if (readBackScore * toOceanScore == 1) {
      System.out.println("  Test successful.");
    }

    System.out.println();
    System.out.println("Total Part II score:  " +
                       (2 * toOceanScore + readBackScore) + " out of 3.");
    System.out.println("Total Autogradable score so far:  " +
                       (2 * (initScore + boundaryScore + toOceanScore) +
                        fishInnerScore + sharkInnerScore + readBackScore) +
                       " out of 9.");
    System.out.println();



    System.out.println("Beginning Part III.");
    System.out.println("Run-length encoding an Ocean.");
    int toRLEScore = 1;
    int backToOceanScore = 1;

    System.out.println("  Calling the one-parameter constructor.");
    RunLengthEncoding rle2 = new RunLengthEncoding(sea);
    TypeAndSize ts2 = rle2.nextRun();
    int i = 0;
    while (ts2 != null) {
      if (ts2 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(sea);
        toRLEScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts2.type +
                           ", " + ts2.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts2.type + ", " + ts2.size);
        System.out.println("    Here's the correct ocean.");
        paint(sea);
        toRLEScore = 0;
        break;
      }
      ts2 = rle2.nextRun();
      i++;
    }
    if ((toRLEScore == 1) && (ts2 != null)) {
      System.out.println("    Your nextRun is failing to return null when" +
                         " the runs run out.");
      System.out.println("    Here's the correct ocean.");
      paint(sea);
      toRLEScore = 0;
    }

    System.out.println("  Converting back to an Ocean.");
    Ocean o2 = rle2.toOcean();
    if (false) {
      System.out.println("The correct ocean is:");
      System.out.println("Your Ocean is incorrect:");
      paint(o2);
      backToOceanScore = 0;
    } else {
      System.out.println("  Running one timestep.");
      o2 = o2.timeStep();
      if (false) {
        System.out.println("The correct ocean is:");
        System.out.println("Your Ocean is incorrect:");
        paint(o2);
        System.out.println("You probably messed up the shark hunger in" +
                           " toOcean().");
        backToOceanScore = 0;
      }
    }


    if (toRLEScore * backToOceanScore == 1) {
      System.out.println("  Test successful.");
    }

    System.out.println();
    System.out.println("Total Part III score:  " +
                       (2 * toRLEScore + backToOceanScore) + " out of 3.");
    System.out.println("Total Autogradable score so far:  " +
                       (2 * (initScore + boundaryScore +
                             toOceanScore + toRLEScore) +
                        fishInnerScore + sharkInnerScore +
                        readBackScore + backToOceanScore) +
                       " out of 12.");
    System.out.println();




    System.out.println("Beginning Part IV.");
    System.out.println("Adding to your 4x4 run-length encoding" +
                       " (from Part II).");
    int addScore = 1;

    System.out.println("  Adding shark at (2, 1).");
    rle1.addShark(2, 1);
    rle1.restartRuns();
    TypeAndSize ts1 = rle1.nextRun();
    i = 0;
    while (ts1 != null) {
      if (ts1 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts1.type +
                           ", " + ts1.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts1.type + ", " + ts1.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      }
      ts1 = rle1.nextRun();
      i++;
    }
    System.out.println("  Adding shark at (1, 1).");
    rle1.addShark(1, 1);
    rle1.restartRuns();
    ts1 = rle1.nextRun();
    i = 0;
    while (ts1 != null) {
      if (ts1 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts1.type +
                           ", " + ts1.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts1.type + ", " + ts1.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      }
      ts1 = rle1.nextRun();
      i++;
    }
    System.out.println("  Adding shark at (3, 1).");
    rle1.addShark(3, 1);
    rle1.restartRuns();
    ts1 = rle1.nextRun();
    i = 0;
    while (ts1 != null) {
      if (ts1 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts1.type +
                           ", " + ts1.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts1.type + ", " + ts1.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      }
      ts1 = rle1.nextRun();
      i++;
    }
    System.out.println("  Adding fish at (3, 2).");
    rle1.addFish(3, 2);
    rle1.restartRuns();
    ts1 = rle1.nextRun();
    i = 0;
    while (ts1 != null) {
      if (ts1 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts1.type +
                           ", " + ts1.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts1.type + ", " + ts1.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      }
      ts1 = rle1.nextRun();
      i++;
    }
    System.out.println("  Adding fish at (1, 2).");
    rle1.addFish(1, 2);
    rle1.restartRuns();
    ts1 = rle1.nextRun();
    i = 0;
    while (ts1 != null) {
      if (ts1 == null) {
        System.out.println("    Run # " + i +
                           " missing.  (Runs are indexed from zero.)");
        System.out.println("    (In other words, your nextRun() is " +
                           "returning null when it shouldn't.)");
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      } else if (false) {
        System.out.println("    Run # " + i + " should be " + ts1.type +
                           ", " + ts1.size + ".  (Runs indexed from zero.)");
        System.out.println("    Instead, it's " + ts1.type + ", " + ts1.size);
        System.out.println("    Here's the correct ocean.");
        paint(rle1.toOcean());
        addScore = 0;
        break;
      }
      ts1 = rle1.nextRun();
      i++;
    }



    if (addScore == 1) {
      System.out.println("  Test successful.");
    }

    System.out.println();
    System.out.println("Total Part IV score:  " + addScore + " out of 1.");
    System.out.println("Total Autogradable score:  " +
                       (2 * (initScore + boundaryScore +
                             toOceanScore + toRLEScore) +
                        fishInnerScore + sharkInnerScore +
                        readBackScore + backToOceanScore + addScore) +
                       " out of 13.");

  }

}
