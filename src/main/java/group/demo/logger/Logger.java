package group.demo.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public void logToConsole(String message){
        System.out.println(message);
    }

    public void errorToConsole(String message){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms");
        Date date = new Date();
        System.out.println(dateFormat.format(date) + " : " + message);
    }
}
