//Name: Casey Au
//class: cs 311
//date: 2-21-17
//not included in this file are the input files needed to run this program
//it needs at least 2 files, a reserved words one and one that is gets searched
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Character;

public final class Proj2 {
  static int[] _switch = new int[54];
  static char[] symbol = new char[1200];
  static int[] next = new int[1200];

  public static void main(String[] args) throws Exception {
    char holder;// character of word comparing to symbol chart
    int ptr;// index at switch array
    int lastEmpty = 0;// index of last empty spot in symbol table
    String word = "";// word comparing to symbol table
    File file = new File("input.txt");
    Scanner scnr = new Scanner(file);

    File javaFile = new File("WordCount.java");
    Scanner scanner = new Scanner(javaFile);
    int sort = 0;
    // initializing arrays with -1
    Arrays.fill(_switch, -1);
    //printTables();

    // reading reserved words only
    while (scnr.hasNextLine()) {
      String[] parts = scnr.nextLine().split("\\s+");
      // reads ind words
      for (int x = 0; x < parts.length; x++) {
        word = parts[x];
        // reading ind letters
        int letterIndex = 0;
        // @holder = char, individual letter
        holder = word.charAt(letterIndex);
        // @ptr = index at switch array('A' = 0, 'a' = 26)
        ptr = trans(holder);
        // case new letter
        if (_switch[ptr] == -1) {
          // @lastEmpty = last open spot on symbol table
          _switch[ptr] = lastEmpty;
          // inserts word in symbol table not inluding first letter
          for (int j = lastEmpty; j < (lastEmpty + word.length() - 1); j++) {
            holder = word.charAt(++letterIndex);
            symbol[j] = holder;
          }
          lastEmpty += word.length();
          symbol[lastEmpty - 1] = '*';
          //printTables();
        } else {
          letterIndex++;
          holder = word.charAt(letterIndex);
          // @point = starts taking value at switch,pointer comparing
          // in symbol table
          int point = _switch[ptr];
          while (letterIndex < word.length()) {
            // case 1 letter:compared same as laetter in symbol
            // table at index
            if (symbol[point] == holder) {
              letterIndex++;
              holder = word.charAt(letterIndex);
              point++;
              // case 2: letter being compared =! symbol table and
              // there is already a jump point in next table
            } else if (next[point] != 0) {
              point = next[point];
              // case 3: letter being compared != symbol table and
              // no jump point,rest of word inserted into symbol
              // table at lastEmpty
            } else if (next[point] == 0) {
              next[point] = lastEmpty;
              for (int i = letterIndex; i < word.length(); i++) {
                symbol[lastEmpty] = word.charAt(i);
                lastEmpty++;
              }
              // only star rn bc we are reading in reserved words
              symbol[lastEmpty] = '*';
              lastEmpty++;
              //printTables();
              break;
            }
          }
        }
      }
    }

    // reading in java file
    while (scanner.hasNextLine()) {
      //@test = each line of the file
      String test = scanner.nextLine();
      //weed out blank lines
      if (test.length() > 0) {
        //weeds out all unnecessary characters
        String[] blocks = test.split("\\s+|\\.|\\(|\\)|[-+*/=]|\\}|\\{|\"|\\<|\\>|[,:;]|\\}|\\{|\\[|\\]");
        for (int y = 0; y < blocks.length; y++) {
          //weeds out the huge spaces in the beginning ^ split
          if(blocks[y].length()>1){
            //reinitializing word as empty
            word = "";
            //@count points to each letter at word looking at
            int count = 0;
            //@sort = char of word looking at
            sort = (int) blocks[y].charAt(count);
            //only a-z, A-Z, $, and _ are allowed as first letters, 39 = '' cause i couldnt split it and not loose the letter
            if ((sort > 64 && sort < 91) || (sort > 96 && sort < 123) || sort == 95 || sort == 36 || sort == 39) {
              if(sort == 39){
                count++;
              }
              word += blocks[y].charAt(count);
              count++;
              sort = (int) blocks[y].charAt(count);
              //permits a-z, A-Z, $, _ , and 0-9 as identifiers
              while (((sort > 64 && sort < 91) || (sort > 96 && sort < 123) || sort == 95 || sort == 36|| (sort > 47 && sort < 58)) && count < blocks[y].length()) {
                word += blocks[y].charAt(count);
                count++;
              }
              //can now look at ind words that are just words without worry
              int letterIndex = 0;
              // @holder = char, individual letter
              holder = word.charAt(letterIndex);
              // @ptr = index at switch array('A' = 0, 'a' = 26)
              ptr = trans(holder);
              // case new letter
              if (_switch[ptr] == -1) {
                // @lastEmpty = last open spot on symbol table
                _switch[ptr] = lastEmpty;
                // inserts word in symbol table not inluding first letter
                for (int j = lastEmpty; j < (lastEmpty + word.length() - 1); j++) {
                  holder = word.charAt(++letterIndex);
                  symbol[j] = holder;
                }
                lastEmpty += word.length();
                symbol[lastEmpty - 1] = '?';
                System.out.print(word+"? ");
                //printTables();
                }else{
                letterIndex++;
                holder = word.charAt(letterIndex);
                // @point = starts taking value at switch,pointer comparing
                // in symbol table
                int point = _switch[ptr];
                while (letterIndex <= word.length()) {
                  // case 1 letter:compared same as laetter in symbol
                  // table at index
                  if ((symbol[point] == holder)) {
                    if(letterIndex<word.length()-1){
                      letterIndex++;
                      holder = word.charAt(letterIndex);
                      point++;
                    }else{
                      letterIndex++;
                      point++;
                    }        
                    // case 2: word being compared is found to be reserved
                  }else if(letterIndex==word.length() && ((int)symbol[point]==42)){
                    System.out.println(word+"* ");
                    printTables();
                    break;
                    //case 3: word being compared is found to be an id showed befor
                  }else if(letterIndex==word.length() && ((int)symbol[point]==63)){
                    System.out.print(word+"@ ");
                    break;
                    // case 2: letter being compared =! symbol table and
                    // there is already a jump point in next table
                  }else if (next[point] != 0) {
                    point = next[point];
                    // case 3: letter being compared != symbol table and
                    // no jump point,rest of word inserted into symbol
                    // table at lastEmpty
                  } else if (next[point] == 0) {
                    next[point] = lastEmpty;
                    for (int i = letterIndex; i < word.length(); i++) {
                      symbol[lastEmpty] = word.charAt(i);
                      lastEmpty++;
                    }
                    //new identifiers
                    symbol[lastEmpty] = '?';
                    System.out.print(word+"? ");
                    lastEmpty++;
                    //printTables();
                    break;
                  }
                }
              }
            }
          }

        }
        System.out.println();
      }
    }
    scnr.close();
    scanner.close();
    System.exit(0);
  }

  // @charNum = number on ascii table
  // @return = index on switch array
  public static int trans(int charNum) {
    if ((charNum > 64) && (charNum < 91)) {
      return charNum - 65;
    } else if ((charNum > 96) && (charNum < 123)) {
      return charNum - 71;
    } else if (charNum == 95) {
      return 52;
    }
    return 53;
  }

  // prints out switch, symbol and next arrays formatted
  public static void printTables() {
    System.out.print("\t");
    for (int i = 65; i < 85; i++) {
      System.out.printf("%5c", (char) i);
    }
    System.out.print("\nswitch: ");
    // print 20 spots from switch array
    for (int i = 0; i < 20; i++) {
      System.out.printf("%5d", _switch[i]);
    }
    System.out.println("\n");

    // print char U-Z and a-n
    System.out.print("\t");
    for (int i = 85; i < 91; i++) {
      System.out.printf("%5c", (char) i);
    }
    for (int i = 97; i < 111; i++) {
      System.out.printf("%5c", (char) i);
    }
    // print 20 spots from switch array
    System.out.print("\nswitch: ");
    for (int i = 20; i < 40; i++) {
      System.out.printf("%5d", _switch[i]);
    }
    System.out.println("\n");

    // print char o-z and _ and $
    System.out.print("\t");
    for (int i = 111; i < 123; i++) {
      System.out.printf("%5c", (char) i);
    }
    System.out.printf("%5c%5c", (char) 95, (char) 36);
    System.out.print("\nswitch: ");
    // print 14 -1s
    for (int i = 40; i < 54; i++) {
      System.out.printf("%5d", _switch[i]);
    }
    System.out.println("\n");

    // print symbol and next tables
    // @plc = rows of 20
    int plc = 0;
    while (symbol[plc] != '\u0000') {
      // prints index of symbol and next
      System.out.print("\t");
      for (int i = plc; i < plc + 20; i++) {
        System.out.printf("%5d", i);
      }
      // prints symbol table in 20s
      System.out.print("\nsymbol: ");
      for (int i = plc; i < plc + 20; i++) {
        System.out.printf("%5c", symbol[i]);
      }
      // prints next table in 20 segments if anything is in it
      System.out.print("\nnext:\t");
      for (int i = plc; i < plc + 20; i++) {
        if (next[i] != 0) {
          System.out.printf("%5d", next[i]);
        } else {
          String s = " ";
          System.out.printf("%5s", s);
        }
      }
      plc += 20;
      System.out.println();
    }
    // separates all words
    System.out.println("\n*****************************");
  }
}
