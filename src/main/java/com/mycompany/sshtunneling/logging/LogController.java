
package com.mycompany.sshtunneling.logging;

//LogController v1.0 
//- Logging all session using a single file
//Pending LogController v1.1 TO DO
//- Log session into individual files with unique name

//this is a controller class for the Logger 
public class LogController {
    //String logMessage;
    public LogController()
    {
    
    }
    //public LogController(String logMessage)
    //{
    //    this.logMessage  = logMessage;
    //}
    public boolean logWriterFunc(String logMessage) 
            // create an object of the Logger class 
    {
        Logger loggerObject = new Logger();
        boolean status;
        status = loggerObject.Txt_logger(logMessage);
        
        return status;
    }
    public boolean logWriterFunc(String [] logMessage)
    {
        Logger loggerObject = new Logger();
        return loggerObject.Txt_logger(logMessage);
        
    }
}
