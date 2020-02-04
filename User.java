import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private HashPassword password;
  private Vector<Account> accounts;
  private Account currentAcct;

  public static void main(String[] args) {
    User user = new User("testPassword1");

    // should print true
    System.out.println(user.checkPassword("testPassword1"));

    // should print false
    System.out.println(user.checkPassword("test"));

    // change password and test, should print true
    user.setPassword();
    System.out.println(user.checkPassword("newPassword"));

    // create new account
    user.createAccount();

    // set current account
    user.setCurrentAcct();

    // print account name
    System.out.println(user.getAccountName());

    // change name and print
    user.setAccountName();
    System.out.println(user.getAccountName());

    // print account url
    System.out.println(user.getAccountURL());

    // change url and print
    user.setAccountURL();
    System.out.println(user.getAccountURL());

    // print account password
    System.out.println(user.getAccountPass());

    // change account password and print
    user.setAccountPass();
    System.out.println(user.getAccountPass());

    // generate account password and print
    user.generateAccountPass();
    System.out.println(user.getAccountPass());

    // test checkAccountPassAge
    user.checkAccountPassAge();

    // test checkPassAge
    user.checkPassAge();

    // delete user
    user.deleteAccount();

    // check deletion worked
    user.listAccounts();
  } // end main

  public User(String pass) {
    this.password = new HashPassword(pass);
    this.accounts = new Vector<Account>();
  } // end constructor

  public int getTotalAccounts() {
    return this.accounts.size();
  } // end getTotalAccounts

  public void setPassword() {
    boolean keepGoing = true;
    while (keepGoing) {
      char[] firstEntry = System.console().readPassword("What would you like the new password to be?\n");

      while (firstEntry.length == 0) {
        System.out.println("Error: password cannot be empty");
        firstEntry = System.console().readPassword("\nWhat would you like the new password to be?\n");
      } // end while

      char[] secondEntry = System.console().readPassword("Retype new password:\n");

      if (Arrays.equals(firstEntry, secondEntry)) {
        String newPass = new String(firstEntry);
        this.password.setPassword(newPass);
        keepGoing = false;
      } else {
        System.out.println("Error: Passwords do not match\n");
      } // end if else
    } // end while
  }// end setPassword

  public boolean checkPassword(String pass) {
    return this.password.checkPassword(pass);
  } // end checkPassword

  public void listAccounts() {
    int length = this.accounts.size();

    System.out.println("Accounts:\n");

    for (int i = 0; i < length; i++) {
      System.out.println(Integer.toString(i) + ") " + this.accounts.get(i).getName());
    } // end for
    System.out.println();
  } // end listAccounts

  public void setCurrentAcct() {
    // select account
    System.out.println("Which account would you like to view?\n");
    int index = this.selectAccount();

    // assign chosen account to currentAcct
    this.currentAcct = this.accounts.get(index);

    // check how old the password is
    this.checkAccountPassAge();
  } // end setCurrentAcct

  public int selectAccount() {
    this.listAccounts();

    int length = this.accounts.size();
    Vector<String> validResponses = new Vector<String>();
    for (int i = 0; i < length; i++) {
      validResponses.add(Integer.toString(i));
    } // end for

    Scanner scanner = new Scanner(System.in);
    boolean keepGoing = true;
    int index = -1;

    while (keepGoing) {
      System.out.println("Enter a number to select an account");

      String response = scanner.nextLine();

      if (response.equals("-1")) {
        this.listAccounts();
      } else if (validResponses.contains(response)) {
        // convert response to int
        index = Integer.parseInt(response);
        keepGoing = false;
      } else {
        System.out.println("Error: Invalid input\n");
      } // end if else
    } // end while

    return index;
  } // end selectAccount

  public void setAccountName() {
    Scanner scanner = new Scanner(System.in);
    String name = "";

    while (name.length() == 0) {
      System.out.println("Current name: " + this.getAccountName());
      System.out.println("Enter new name:");

      name = scanner.nextLine();

      if (name.equals("")) {
        name = this.getAccountName();
      } // end if
    } // end while

    this.currentAcct.setName(name);
  } // end setAccountName

  public void setAccountURL() {
    Scanner scanner = new Scanner(System.in);
    String url = "";

    while (url.length() == 0) {
      System.out.println("Current url: " + this.getAccountURL());
      System.out.println("Enter new url:");

      url = scanner.nextLine();

      if (url.equals("")) {
        url = this.getAccountURL();
      } // end if
    } // end while

    this.currentAcct.setURL(url);
  } // end setAccountURL

  public void setAccountPass() {
    Scanner scanner = new Scanner(System.in);
    String pass = "";

    while (pass.length() == 0) {
      System.out.println("Current password: " + this.getAccountPass());
      System.out.println("Enter new password:");

      pass = scanner.nextLine();
    } // end while

    this.currentAcct.setPassword(pass);
  } // end setAccountPass

  public String getAccountName() {
    return this.currentAcct.getName();
  } // end getAccountName

  public String getAccountURL() {
    return this.currentAcct.getURL();
  } // end getAccountURL

  public String getAccountPass() {
    return this.currentAcct.getPassword();
  } // end getAccountPass

  public void generateAccountPass() {
    Scanner scanner = new Scanner(System.in);
    String response = "";
    int length = 0;

    while (response.length() == 0) {
      System.out.println("Enter the length of password you want: (12-50)");

      response = scanner.nextLine();

      try {
        length = Integer.parseInt(response);

        if (length < 12 || length > 50) {
          System.out.println("\nError: Please enter a number between 12-50");
          response = "";
        } // end if
      } catch (Exception e) {
        System.out.println("\nError: Please enter an integer");
        response = "";
      } // end try catch
    } // end while

    this.currentAcct.generatePass(length);
  } // end generateAccountPass

  public void checkAccountPassAge() {
    this.currentAcct.checkPassAge();
  } // end checkAccountPassAge

  public void generatePass() {
    Scanner scanner = new Scanner(System.in);
    String response = "";
    int length = 0;

    while (response.length() == 0) {
      System.out.println("\nEnter the length of password you want: (12-50)");

      response = scanner.nextLine();

      try {
        length = Integer.parseInt(response);

        if (length < 12 || length > 50) {
          System.out.println("\nError: Please enter a number between 12-50");
          response = "";
        } // end if
      } catch (Exception e) {
        System.out.println("\nError: Please enter an integer");
        response = "";
      } // end try catch
    } // end while

    this.password.generatePass(length);
  } // end generatePass

  public void createAccount() {
    Scanner scanner = new Scanner(System.in);
    String response = "";

    // get account name from user
    while (response.length() == 0) {
      System.out.println("Enter name for the account:");

      response = scanner.nextLine();
    } // end while
    String name = response;

    // get account url
    response = "";
    while (response.length() == 0) {
      System.out.println("\nEnter url for the account:");

      response = scanner.nextLine();
    } // end while
    String url = response;

    // get account password
    response = "";
    while (response.length() == 0) {
      System.out.println("\nEnter password for the account:");

      response = scanner.nextLine();
    } // end while
    String password = response;

    Account newAcct = new Account(name, url, password);

    this.accounts.add(newAcct);
  } // end createAccount

  public void deleteAccount() {
    // select an account
    System.out.println("Which account would you like to delete?");
    int index = this.selectAccount();

    Account selectedAcct = this.accounts.get(index);
    Scanner scanner = new Scanner(System.in);

    System.out.println("\nWarning:");
    System.out.println("You are about to delete the following account:");
    System.out.println("Account Name: " + selectedAcct.getName());
    System.out.println("Account url: " + selectedAcct.getURL());

    System.out.println("\nThis action cannot be undone");
    System.out.println("Please enter the name of the account to confirm deletion");
    System.out.println("To cancel press enter");

    String response = scanner.nextLine();

    if (response.equals(selectedAcct.getName())) {
      this.accounts.remove(index);
      System.out.println("\nAccount deleted");
    } else {
      System.out.println("\nDelete cancelled\n");
    } // end if else
  } // end deleteAccount

  public void checkPassAge() {
    Calendar dateSet = new GregorianCalendar();
    dateSet.setTime(this.password.getDate());

    Calendar today = new GregorianCalendar();
    today.setTime(new Date());

    int yearsDiff = today.get(Calendar.YEAR) - dateSet.get(Calendar.YEAR);
    int monthsDiff = today.get(Calendar.MONTH) - dateSet.get(Calendar.MONTH);
    long diffInMonths = yearsDiff * 12 + monthsDiff;

    if (diffInMonths >= 6) {
      System.out.println("The master password is over 6 months old");
      System.out.println("It is recommended to change your password every 6 months");
    } // end if
  } // end checkPassAge
} // end class