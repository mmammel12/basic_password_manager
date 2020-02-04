import java.io.Serializable;
import java.util.Date;

public abstract class Password implements Serializable {
  private static final long serialVersionUID = 1L;

  protected byte[] password;
  protected byte[] salt;
  protected Date dateSet;

  abstract Date getDate();

  abstract void setPassword(String newPass);
} // end Password