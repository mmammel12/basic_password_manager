import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class Account implements Serializable {
  private static final long serialVersionUID = 1L;

  private String name;
  private String url;
  private StandardPassword password;

  public static void main(String[] args) {
    // test class
    Account acct = new Account("testAcct", "www.google.com", "testPassword1");

    // should print "testPassword1"
    System.out.println(acct.getPassword());

    // should print "testAcct"
    System.out.println(acct.getName());

    // should print "www.google.com"
    System.out.println(acct.getURL());

    // test generatePassword
    acct.generatePass(15);
    System.out.println(acct.getPassword());

    // set name to "testName" then print
    acct.setName("testName");
    System.out.println(acct.getName());

    // set password to "testPass" then print
    acct.setPassword("testPass");
    System.out.println(acct.getPassword());

    // set url to "www.test.com" then print
    acct.setURL("www.test.com");
    System.out.println(acct.getURL());

    // test checkPassAge
    acct.checkPassAge();
  } // end main

  public Account(String name, String url, String pass) {
    this.name = name;
    this.url = url;
    this.password = new StandardPassword(pass);
  } // end constructor

  public void setName(String name) {
    this.name = name;
  } // end setName

  public void setURL(String url) {
    this.url = url;
  } // end setURL

  public void setPassword(String newPass) {
    this.password.setPassword(newPass);
  } // end setPassword

  public String getName() {
    return this.name;
  } // end getName

  public String getURL() {
    return this.url;
  } // end getURL

  public String getPassword() {
    return this.password.getPassword();
  } // end getPassword

  public void generatePass(int length) {
    this.password.generatePass(length);
  } // end generatePass

  public void checkPassAge() {
    Calendar dateSet = new GregorianCalendar();
    dateSet.setTime(this.password.getDate());

    Calendar today = new GregorianCalendar();
    today.setTime(new Date());

    int yearsDiff = today.get(Calendar.YEAR) - dateSet.get(Calendar.YEAR);
    int monthsDiff = today.get(Calendar.MONTH) - dateSet.get(Calendar.MONTH);
    long diffInMonths = yearsDiff * 12 + monthsDiff;

    if (diffInMonths >= 6) {
      System.out.println("The password for " + this.name + " is over 6 months old");
      System.out.println("It is recommended to change your password every 6 months");
    } // end if
  } // end checkPassAge
} // end class