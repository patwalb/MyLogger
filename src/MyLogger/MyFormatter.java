package MyLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author PW
 */
public class MyFormatter extends SimpleFormatter
{
  public MyFormatter()
  {

  }
  
  public String format(LogRecord record)
  {
    long lTimestamp    = record.getMillis();
    Date tTime         = new Date(lTimestamp);
    Calendar tCalendar = new GregorianCalendar();
    
    tCalendar.setTime(tTime);
    
    int iYear   = tCalendar.get(Calendar.YEAR);
    int iMonth  = tCalendar.get(Calendar.MONTH) + 1;
    int iDay    = tCalendar.get(Calendar.DAY_OF_MONTH);
    int iHour   = tCalendar.get(Calendar.HOUR_OF_DAY);
    int iMinute = tCalendar.get(Calendar.MINUTE);
    int iSecond = tCalendar.get(Calendar.SECOND);
    
    Level tLevel        = record.getLevel();
    String strClassName = record.getSourceClassName();
    String strMessage   = record.getMessage();
    
    String strLogMessage = "| " + iYear + "/" 
                                + String.format("%02d", iMonth) + "/" 
                                + String.format("%02d", iDay) + " " 
                                + String.format("%02d", iHour) + ":" 
                                + String.format("%02d", iMinute) + ":" 
                                + String.format("%02d", iSecond) + " | " 
                                + String.format("%7s", tLevel.toString()) + " | " 
                                + strClassName + " | " 
                                + strMessage +"\n";
    
    return strLogMessage;
  }
}