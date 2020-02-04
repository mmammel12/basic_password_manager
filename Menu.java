import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class Menu {
  private boolean auth = false;
  private User user;
  private SecretKeySpec key;

  public static void main(String[] args) {
    Menu menu = new Menu();

    clearScreen();
    clearScreen();
    while (!menu.auth) {
      menu.auth = menu.authenticate();

      if (!menu.auth) {
        clearScreen();
        System.out.println("Invalid Password\n");
      }
    } // end while

    clearScreen();
    menu.user.checkPassAge();
    menu.mainMenu();
    menu.writeData();
  } // end main

  public Menu() {
    try {
      Cipher cipher = Cipher.getInstance("AES");

      // get the key
      FileInputStream keyFile = new FileInputStream("key.bin");
      ObjectInputStream keyObj = new ObjectInputStream(keyFile);

      this.key = (SecretKeySpec) keyObj.readObject();

      keyFile.close();
      keyObj.close();

      // unencrypt user file
      cipher.init(Cipher.DECRYPT_MODE, this.key);
      FileInputStream userFile = new FileInputStream("user.enc");
      CipherInputStream cis = new CipherInputStream(userFile, cipher);
      ObjectInputStream userObj = new ObjectInputStream(cis);
      SealedObject sealedObject = (SealedObject) userObj.readObject();
      this.user = (User) sealedObject.getObject(cipher);

      userFile.close();
      cis.close();
      userObj.close();
    } catch (Exception e) {
      // no data, create key and new user
      this.createUser();

      // create key
      try {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        byte[] keyBytes = keyGen.generateKey().getEncoded();
        this.key = new SecretKeySpec(keyBytes, "AES");

        FileOutputStream keyFileOut = new FileOutputStream("key.bin");
        ObjectOutputStream keyObjOut = new ObjectOutputStream(keyFileOut);

        keyObjOut.writeObject(this.key);

        keyObjOut.close();
        keyFileOut.close();

      } catch (Exception ex) {
        System.out.println(ex.getMessage());
        // TODO write to error log
      } // end try catch
    } // end try catch
  }// end constructor

  public boolean authenticate() {
    char[] responseArr = System.console().readPassword("Enter Master Password:\n");

    String response = new String(responseArr);

    return this.user.checkPassword(response);
  } // end authenticate

  public void writeData() {
    try {
      Cipher cipher = Cipher.getInstance("AES");

      cipher.init(Cipher.ENCRYPT_MODE, this.key);

      SealedObject sealedObject = new SealedObject(this.user, cipher);
      FileOutputStream outFile = new FileOutputStream("user.enc");
      CipherOutputStream cos = new CipherOutputStream(outFile, cipher);
      ObjectOutputStream outObj = new ObjectOutputStream(cos);

      outObj.writeObject(sealedObject);

      outObj.close();
      cos.close();
      outFile.close();
    } catch (Exception e) {
      System.out.println("Error: Something went wrong while saving data");
      // TODO write to error log
    } // end try catch
  } // end writeData

  public void mainMenu() {
    Scanner scanner = new Scanner(System.in);
    boolean keepGoing = true;

    while (keepGoing) {
      this.printMainMenu();

      System.out.println("\nWhat would you like to do");

      String response = scanner.nextLine();

      if (response.equals("1")) {
        clearScreen();
        if (this.user.getTotalAccounts() > 0) {
          this.user.listAccounts();
        } else {
          System.out.println("You do not have any accounts\n");
        }
      } else if (response.equals("2")) {
        clearScreen();
        if (this.user.getTotalAccounts() > 0) {
          this.user.setCurrentAcct();
          clearScreen();
          this.AcctMenu();
          clearScreen();
        } else {
          System.out.println("You do not have any accounts\n");
        }
      } else if (response.equals("3")) {
        clearScreen();
        this.user.createAccount();
        clearScreen();
      } else if (response.equals("4")) {
        clearScreen();
        if (this.user.getTotalAccounts() > 0) {
          this.user.deleteAccount();
          clearScreen();
        } else {
          System.out.println("You do not have any accounts\n");
        }
      } else if (response.equals("5")) {
        clearScreen();
        this.user.setPassword();
        clearScreen();
      } else if (response.equals("6")) {
        clearScreen();
        this.user.generatePass();
        clearScreen();
      } else if (response.equals("0")) {
        clearScreen();
        keepGoing = false;
      } else {
        clearScreen();
        System.out.println("Error: Invalid Input\n");
      } // end if else
    } // end while
  } // end mainMenu

  public void printMainMenu() {
    String mainMenu = "Main Menu\n";
    mainMenu += "\n1) List Accounts";
    mainMenu += "\n2) Select Account";
    mainMenu += "\n3) Create Account";
    mainMenu += "\n4) Delete Account";
    mainMenu += "\n5) Change Master Password";
    mainMenu += "\n6) Generate New Master Password";

    mainMenu += "\n\n0) Exit";

    System.out.println(mainMenu);
  } // end printMainMenu

  public void AcctMenu() {
    Scanner scanner = new Scanner(System.in);
    boolean keepGoing = true;

    while (keepGoing) {
      this.printAcctMenu();

      System.out.println("\nWhat would you like to do");

      String response = scanner.nextLine();

      if (response.equals("1")) {
        clearScreen();
        System.out.println("Password: " + this.user.getAccountPass());
      } else if (response.equals("2")) {
        clearScreen();
        this.user.setAccountPass();
        clearScreen();
      } else if (response.equals("3")) {
        clearScreen();
        this.user.generateAccountPass();
        clearScreen();
      } else if (response.equals("4")) {
        clearScreen();
        this.user.setAccountName();
        clearScreen();
      } else if (response.equals("5")) {
        clearScreen();
        this.user.setAccountURL();
        clearScreen();
      } else if (response.equals("0")) {
        clearScreen();
        keepGoing = false;
      } else {
        clearScreen();
        System.out.println("Error: Invalid Input\n");
      } // end if else
    } // end while
  } // end AcctMenu

  public void printAcctMenu() {
    System.out.println("Account Name: " + this.user.getAccountName());
    System.out.println("Account URL: " + this.user.getAccountURL());

    String acctMenu = "\nAccount Menu\n";
    acctMenu += "\n1) View Password";
    acctMenu += "\n2) Change Password";
    acctMenu += "\n3) Generate New Password";
    acctMenu += "\n4) Change Account Name";
    acctMenu += "\n5) Change Account URL";

    acctMenu += "\n\n0) Return to Main Menu";

    System.out.println(acctMenu);
  } // end printAcctMenu

  public void createUser() {
    clearScreen();
    String pass = "";

    System.out.println("Creating new account");

    boolean keepGoing = true;
    while (keepGoing) {
      char[] firstEntry = System.console().readPassword("\nEnter Password:\n");

      char[] secondEntry = System.console().readPassword("Retype Password:\n");

      if (Arrays.equals(firstEntry, secondEntry)) {
        pass = new String(firstEntry);
        keepGoing = false;
      } else {
        System.out.println("\nError: Passwords do not match");
      } // end if else
    } // end while

    this.user = new User(pass);
    this.auth = true;
  } // end createUser

  public static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  } // end clearScreen
} // end menu