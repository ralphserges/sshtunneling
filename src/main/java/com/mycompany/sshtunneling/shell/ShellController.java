/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sshtunneling.shell;
    
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import static java.lang.Compiler.command;

/**
 *
 * @author gpdat
 */
public class ShellController {
    
    public static Channel getChannel(Session session) throws JSchException, IOException
    {
        if (!session.isConnected())
            throw new RuntimeException("Not connected to an open session.  Call open() first!");

        ChannelExec channelexec = null;
        channelexec = (ChannelExec) session.openChannel("exec");

        channelexec.setCommand(command);
        channelexec.setInputStream(null);

        //PrintStream out = new PrintStream(channelexec.getOutputStream());
        //InputStream in = channelexec.getInputStream(); // channel.getInputStream();

        channelexec.connect();
        
        return channelexec;
    }
    
}
