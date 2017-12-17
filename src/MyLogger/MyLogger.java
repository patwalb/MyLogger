package MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PW
 */
public class MyLogger
{
    private static Logger s_lLg = null;

    private MyLogger() //private constructor according to singleton design pattern
    {
    }

    public static Logger getLogger()
    {
        if (s_lLg == null)
        {
            s_lLg = Logger.getLogger("myLogger");
            initLogger();
        }
        return s_lLg;
    }

    private static void initLogger()
    {
        Properties pProp = new Properties();
        String strProjectPath = System.getProperty("user.dir");
        InputStream isInputStream = null;
        boolean bDisableDefault = false;
        Handler[] tFileHandler = null;

        try // open config file
        {
            isInputStream = new FileInputStream(strProjectPath + 
                                     File.separator + "src" + 
                                     File.separator + "MyLogger" + 
                                     File.separator + "config" + 
                                     File.separator + "Logger.properties");
        } catch (FileNotFoundException ex)
        {
            System.out.println("Logger: " + ex.toString());
        }

        try // reading config file
        {
            pProp.load(isInputStream);
        } catch (IOException ex)
        {
            System.out.println("Logger: " + ex.toString());
        }

        setLogLevel(pProp.getProperty("logLevel"));
        bDisableDefault = Boolean.parseBoolean(pProp.getProperty("disableDefaultHandler"));
        s_lLg.setUseParentHandlers(!bDisableDefault); // disable default handler

        Formatter tMyFormatter = new MyFormatter();
        tFileHandler = setLoggerTyp(pProp.getProperty("logType"),
                pProp.getProperty("logFileName"),
                Boolean.parseBoolean(pProp.getProperty("appendText")),
                tMyFormatter
        );

        try
        {
            for (Handler s : tFileHandler)
            {
                s_lLg.addHandler(s); // add own handler
                s.setFormatter(tMyFormatter);
            }
        } catch (NullPointerException e)
        {
            System.out.println("Logger: No Handler found. Maybe wrong options logType in Logger.properties?");
        }
    }

    static void setLogLevel(String strLogLevel)
    {
        Level lLogLevel = null;

        if (null != strLogLevel)
        {
            switch (strLogLevel)
            {
                case "INFO":
                    lLogLevel = Level.INFO;
                    break;
                case "WARNING":
                    lLogLevel = Level.WARNING;
                    break;
                case "SEVERE":
                    lLogLevel = Level.SEVERE;
                    break;
                default:
                    System.out.println("Enter a suitable level. Choose INFO, WARNING or SEVERE!");
                    break;
            }
        }
        s_lLg.setLevel(lLogLevel);
    }

    static Handler[] setLoggerTyp(String strLogType, String strLogFileName, boolean bAppend, Formatter tMyFormatter)
    {
        Handler[] tFilehandler = null;

        if ("console".equals(strLogType))
        {
            tFilehandler = new Handler[1];
            tFilehandler[0] = new ConsoleHandler();
        } else if ("textfile".equals(strLogType))
        {
            try
            {
                tFilehandler = new Handler[1];
                tFilehandler[0] = new FileHandler(strLogFileName, bAppend);
            } catch (IOException | SecurityException ex)
            {
                System.out.println("Logger: " + ex.toString());
            }
        } else if ("both".equals(strLogType))
        {
            tFilehandler = new Handler[2];

            tFilehandler[0] = new ConsoleHandler();
            try
            {
                tFilehandler[1] = new FileHandler(strLogFileName, bAppend);
            } catch (IOException | SecurityException ex)
            {
                System.out.println("Logger: " + ex.toString());
            }
        }
        return tFilehandler;
    }
}
