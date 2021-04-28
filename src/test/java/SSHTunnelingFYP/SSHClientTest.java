/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SSHTunnelingFYP;

import com.mycompany.sshtunneling.SSHClient;
import com.mycompany.sshtunneling.SSHClientGui;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jet
 */
public class SSHClientTest {
    
    public SSHClientTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getSSHSessionLPF method, of class SSHClient. (NO PORTFORWARDING)
     * PASS: Session returned by the function is not null
     * FAIL: Session returned by the function is null
     */
    @Test
    public void testGetSSHSession() {
        System.out.println("Test: getSSHSessionLPF()");
        String username = "jet";
        String password = "password";
        String ipAddress = "127.0.0.1";
        int serverPort = 0;
        int sshServerPort = 2200;
        SSHClientGui gui = new SSHClientGui();
        Session result = SSHClient.getSSHSessionLPF(username, password, ipAddress, serverPort, sshServerPort, gui);
        assertNotNull(result);
    }
    
    /**
     * Test of getSSHSessionLPF method, of class SSHClient. (WITH PORTFORWARDING)
     * PASS: Session returned by the function is not null
     * FAIL: Session returned by the function is null
     */
    @Test
    public void testGetSSHSessionLPF() {
        System.out.println("Test: getSSHSessionLPF()");
        String username = "jet";
        String password = "password";
        String ipAddress = "127.0.0.1";
        int serverPort = 727;
        int sshServerPort = 2200;
        SSHClientGui gui = new SSHClientGui();
        Session result = SSHClient.getSSHSessionLPF(username, password, ipAddress, serverPort, sshServerPort, gui);
        assertNotNull(result);
    }

//    /**
//     * Test of getSFTPChannel method, of class SSHClient.
//     */
//    @org.junit.Test
//    public void testGetSFTPChannel() {
//        System.out.println("getSFTPChannel");
//        Session session = null;
//        SSHClientGui gui = null;
//        ChannelSftp expResult = null;
//        ChannelSftp result = SSHClient.getSFTPChannel(session, gui);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of transferFile method, of class SSHClient.
//     */
//    @org.junit.Test
//    public void testTransferFile() {
//        System.out.println("transferFile");
//        ChannelSftp sftp = null;
//        String fileName = "";
//        String localDir = "";
//        String remoteDir = "";
//        SSHClientGui gui = null;
//        SSHClient.transferFile(sftp, fileName, localDir, remoteDir, gui);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveFile method, of class SSHClient.
//     */
//    @org.junit.Test
//    public void testRetrieveFile() {
//        System.out.println("retrieveFile");
//        ChannelSftp sftp = null;
//        String fileName = "";
//        String localDir = "";
//        String remoteDir = "";
//        SSHClientGui gui = null;
//        SSHClient.retrieveFile(sftp, fileName, localDir, remoteDir, gui);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of endSSHSession method, of class SSHClient.
//     */
//    @org.junit.Test
//    public void testEndSSHSession() {
//        System.out.println("endSSHSession");
//        Session session = null;
//        ChannelSftp sftp = null;
//        SSHClientGui gui = null;
//        SSHClient.endSSHSession(session, sftp, gui);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
