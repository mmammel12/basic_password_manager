import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class StandardPassword extends Password implements Encryptable, Decryptable {
  private static final long serialVersionUID = 1L;

  private SecretKeySpec key;

  public static void main(String[] args) {
    // test the class
    StandardPassword pass = new StandardPassword("TestPassword1");

    // should print "TestPassword1"
    System.out.println(pass.getPassword());

    // change the password
    pass.setPassword("TestPassword2");

    // should print "TestPassword2"
    System.out.println(pass.getPassword());

    // generate new password
    pass.generatePass(15);

    // print new password
    System.out.println(pass.getPassword());
  } // end main

  public StandardPassword(String pass) {
    // create random salt
    SecureRandom rand = new SecureRandom();
    this.salt = new byte[32];
    rand.nextBytes(this.salt);

    // create key
    boolean keyCreated = false;
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(128);
      byte[] keyBytes = keyGen.generateKey().getEncoded();
      this.key = new SecretKeySpec(keyBytes, "AES");

      keyCreated = true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      // TODO write to error log
    } // end try catch

    if (keyCreated) {
      // set dateSet to current date
      this.dateSet = new Date();

      // salt and encrypt the password
      this.password = this.encrypt(pass);
    } else {
      System.out.println("Something went wrong while creating this account");
    } // end if else
  } // end constructor

  public Date getDate() {
    return this.dateSet;
  } // end getDate

  public void setPassword(String newPass) {
    // set dateSet to current date
    this.dateSet = new Date();

    // salt and hash the password
    this.password = this.encrypt(newPass);
  } // end setPassword

  public String getPassword() {
    return this.decrypt(this.password);
  } // end getPassword

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
        keepGoing = false;
      } else if (response.toUpperCase().equals("N")) {
        System.out.println("\nPassword change cancelled");
        keepGoing = false;
      } else {
        System.out.println("\nError: Please enter either 'y' or 'n'");
      } // end if else
    } // end while
  } // end generatePass

  public byte[] encrypt(String pass) {
    // create byte array
    byte[] passBytes = pass.getBytes();
    int length = passBytes.length + this.salt.length;
    byte[] newPass = new byte[length];

    // fill byte array with salt and password
    int count = 0;
    for (int i = 0; i < this.salt.length; i++) {
      newPass[i] = this.salt[i];
      count++;
    } // end for
    for (int i = 0; i < passBytes.length; i++) {
      newPass[count++] = passBytes[i];
    } // end for

    // encrypt the password
    byte[] encBytes;
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, this.key);
      encBytes = cipher.doFinal(newPass);
    } catch (Exception e) {
      System.out.println("Error: Something went wrong while encrypting the password");
      System.out.println(e.getMessage());
      // TODO write to error log
      encBytes = null;
    } // end try catch

    return encBytes;
  } // end encrypt

  public String decrypt(byte[] encBytes) {
    String decPass;

    // decrypt the password
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, this.key);
      byte[] decBytes = cipher.doFinal(encBytes);

      String saltPass = new String(decBytes);
      String salt = new String(this.salt);
      decPass = "";
      for (int i = salt.length(); i < saltPass.length(); i++) {
        decPass += saltPass.charAt(i);
      } // end for

    } catch (Exception e) {
      System.out.println("Error: Something went wrong while decrypting the password");
      System.out.println(e.getMessage());
      // TODO write to error log
      decPass = null;
    }

    return decPass;
  } // end decrypt
} // end class