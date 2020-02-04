import java.io.Serializable;
import java.security.SecureRandom;

public class PasswordGenerator implements Serializable {
  private static final long serialVersionUID = 1L;

  private final char[] LOWERCASE = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

  private final char[] UPPERCASE = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
      'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

  private final char[] NUMBERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

  private final char[] SYMBOLS = new char[] { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '{', '}', '~', '`', '[',
      ']', '-', '_', '=', '+', ',', '.', '<', '>', '/', '?' };

  // ints for selecting character set
  private final int LC = 0;
  private final int UC = 1;
  private final int NUM = 2;
  private final int SYM = 3;

  public static void main(String[] args) {
    // used for testing
    PasswordGenerator pg = new PasswordGenerator();

    System.out.println(pg.generate(15));
    System.out.println(pg.generate(1));
    System.out.println(pg.generate(100));
  } // end main

  public PasswordGenerator() {
    // don't need
  } // end ConstrUCtor

  public String generate(int length) {
    // validate length
    if (length < 12) {
      length = 12;
    } else if (length > 50) {
      length = 50;
    }

    // initialize variables
    String password = "";
    SecureRandom rand = new SecureRandom();
    int prevChoice = -1;

    // generate random password
    for (int i = 0; i < length; i++) {
      // select which character set to choose from
      int charSet = rand.nextInt(4);

      // make it less likely to pick from the same group twice in a row
      if (charSet == prevChoice) {
        int repeat = rand.nextInt(100);
        if (repeat < 35) {
          charSet = rand.nextInt(4);
        }
      }

      // choose random character from character set
      if (charSet == this.LC) {
        int index = rand.nextInt(this.LOWERCASE.length);
        password += this.LOWERCASE[index];
        prevChoice = charSet;
      } else if (charSet == this.UC) {
        int index = rand.nextInt(this.UPPERCASE.length);
        password += this.UPPERCASE[index];
        prevChoice = charSet;
      } else if (charSet == this.NUM) {
        int index = rand.nextInt(this.NUMBERS.length);
        password += this.NUMBERS[index];
        prevChoice = charSet;
      } else if (charSet == this.SYM) {
        int index = rand.nextInt(this.SYMBOLS.length);
        password += this.SYMBOLS[index];
        prevChoice = charSet;
      } // end if else
    } // end for

    return password;
  } // end generate
} // end PasswordGenerator