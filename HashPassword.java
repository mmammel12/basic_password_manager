import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashPassword extends Password implements Encryptable {
  private static final long serialVersionUID = 1L;

  public static void main(String[] args) {
    // test the class
    System.out.println("Testing password: TestPassword1");
    HashPassword hp = new HashPassword("TestPassword1");

    // should print true
    System.out.println(hp.checkPassword("TestPassword1"));

    // should print false
    System.out.println(hp.checkPassword("TestPassword2"));

    // generate new pass
    hp.generatePass(12);

    // test generated password
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter generated password:");
    String response = scanner.nextLine();
    System.out.println(hp.checkPassword(response));

    // test getDate
    System.out.println(hp.getDate());
  } // end main

  public HashPassword(String pass) {
    // create random salt
    SecureRandom rand = new SecureRandom();
    this.salt = new byte[64];
    rand.nextBytes(this.salt);

    // set dateSet to current date
    this.dateSet = new Date();

    // salt and hash the password
    this.password = this.encrypt(pass);
  } // end constructor

  public void setPassword(String newPass) {
    // set dateSet to current date
    this.dateSet = new Date();

    // salt and hash the password
    this.password = this.encrypt(newPass);
  } // end setPassword

  public boolean checkPassword(String pass) {
    boolean valid = false;
    byte[] encPass;

    try {
      PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), this.salt, 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      encPass = factory.generateSecret(spec).getEncoded();

    } catch (Exception e) {
      System.out.println("Error: something went wrong while checking the password");
      // TODO write to error log
      encPass = null;
    } // end try catch

    if (Arrays.equals(this.password, encPass)) {
      valid = true;
    } // end if

    return valid;
  } // checkPassword

  public void generatePass(int length) {
    PasswordGenerator generator = new PasswordGenerator();
    String newPass = generator.generate(length);

    System.out.println("Generated Password:");
    System.out.println(newPass);

    Scanner scanner = new Scanner(System.in);
    boolean keepGoing = true;

    while (keepGoing) {
      System.out.println("\nWould you like to set this as your password? (y/n)");
      String response = scanner.nextLine();

      if (response.toUpperCase().equals("Y")) {
        this.setPassword(newPass);
        System.out.println("\nPassword successfully changed");
        System.out.println("\nPress enter to continue");
        scanner.nextLine();
        keepGoing = false;
      } else if (response.toUpperCase().equals("N")) {
        System.out.println("\nPassword change cancelled");
        System.out.println("\nPress enter to continue");
        scanner.nextLine();
        keepGoing = false;
      } else {
        System.out.println("\nError: Please enter either 'y' or 'n'");
      } // end if else
    } // end while
  } // end generatePass

  public Date getDate() {
    return this.dateSet;
  } // end getDate

  public byte[] encrypt(String pass) {
    byte[] encPass;
    try {
      PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), this.salt, 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      encPass = factory.generateSecret(spec).getEncoded();
    } catch (Exception e) {
      // TODO write to error log
      System.out.println("Error: something went wrong encrypting the password");
      encPass = null;
    } // end try catch

    return encPass;
  }
} // end HashPassword