/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sshtunneling.shell;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author gpdat
 */
public class Shell 
{

    public static String getResponse(ChannelExec channelexec, InputStream in ,String command ) throws IOException {
        String ret = getChannelOutput(channelexec, in);
        return ret;
    }
    
    private static String getChannelOutput(Channel channel, InputStream in) throws IOException{

        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();

        String line = "";
        while (true){
            while (in.available() > 0) {
                int i = in.read(buffer, 0, 1024);
                if (i < 0) {
                    break;
                }
                strBuilder.append(new String(buffer, 0, i));
                System.out.println(line);
            }

            if(line.contains("logout")){
                break;
            }

            if (channel.isClosed()){
                break;
            }
        }
        return strBuilder.toString();   
    }
    
}
