package SSHTunnelingFYP;

//Logger v1.0 
//- Logging all session using a single file
//Pending Logger v1.1 TO DO
//- Log session into individual files with unique name
import java.io.*;

//this is an entity class that will interact with the logging of information to files
public class Logger 
{
    
	//**import this method - might need to remove static if u are using with object** TO BE REMOVED 
	//constructor
        //String logMessage;
        //String [] lm_arr;
        public Logger()
        {
        //default constructor
        }
	//both methods overloaded , one to take in String array , other to take in just String 
	public boolean Txt_logger(String [] input) //static removed
	{
		try 
		{
			//pass in a string
			//create file or append
			FileWriter output_writer = new FileWriter("logfile.txt",true);
			output_writer.write("------START OF LOG------" + "\n");
				//interate through array and write in line by line
				for( int i = 0; i < input.length; i++)
				{
					output_writer.write(input[i] + "\n");
				}
			output_writer.write("------END OF LOG------" + "\n");
			output_writer.close();
		} 
		catch (IOException e) 
		{
			System.out.println("file writing error occurred!");
			e.printStackTrace();
			return false;
		}
		catch (NullPointerException e) 
		{
            System.out.print("file writing error occurred!");
			e.printStackTrace();
			return false;
                }
	return true; //assertTrue
	}
        
	public boolean Txt_logger(String input) //Static removed
	{
		
		try 
		{
			//pass in a string
			//create file or append
			FileWriter output_writer = new FileWriter("logfile.txt",true);
			output_writer.write("------START OF LOG------" + "\n");
			output_writer.write(input + "\n");
			output_writer.write("------END OF LOG------" + "\n");
			output_writer.close();
		} 
		catch (IOException e) 
		{
			System.out.println("file writing error occurred!");
			e.printStackTrace();
			return false;
		}
	return true; //assertTrue
	}
}

