
package com.mycompany.sshtunneling.shell;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class ShellController {
    
    public static String executeCommand(Session session, String command) throws JSchException, IOException {
        String ret = "";

        if (!session.isConnected())
            throw new RuntimeException("Not connected to an open session.  Call open() first!");

        ChannelExec channelexec = null;
        channelexec = (ChannelExec) session.openChannel("exec");

        channelexec.setCommand(command);
        channelexec.setInputStream(null);

        PrintStream out = new PrintStream(channelexec.getOutputStream());
        InputStream in = channelexec.getInputStream(); // channel.getInputStream();

        channelexec.connect();

        ret = Shell.getResponse(channelexec, in, command);
        channelexec.disconnect();

        System.out.println("Finished sending commands!");
        return ret;
    }
    
    
}
