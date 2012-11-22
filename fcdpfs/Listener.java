/**	Listener:  The listening server for the Flash cross-domain policy file server/service.
*
*	@author David Wipperfurth
*	@dated	4/28/10
*	@copyright	Public Domain
*/

//Use whatever package you want here, all needed imports are listed.
//Just make sure the package you choose is properly reflected in your properties file
//AND that the associated FlashCrossDomainPolicyFileService and Sender files reflect the change as well.
package simpleremoteconnection.fcdpfs;

import simpleremoteconnection.fcdpfs.*;

//import java.lang.NullPointerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Logger;

public class Listener extends Thread {

	private static final Logger logger = Logger.getLogger(Listener.class.getName()); // Used for all logging from this class

	private boolean running;
	private int port;
	private String response;
	private String trigger;

	private ServerSocket portServer;

	public Listener(int port, String trigger, String response) throws NullPointerException{
		if(response==null) throw new NullPointerException("Missing Required response File.");
		this.port = port;
		this.response = response;
		this.trigger = trigger;
		this.running=true;
		System.out.print("Creating Listener\n");
	}

	public void run(){
		try{
			portServer = new ServerSocket(port);
		}catch(IOException e){
			logger.severe(e.getMessage());
			return;
		}
		while(running){
			try{
				Socket socket = portServer.accept();

				(new Sender(socket, trigger, response)).start();
			}catch(IOException e){
				logger.warning(e.getMessage());
				try{ sleep(500); }catch(InterruptedException ie){}	//if whole-sale communication fails wait a bit before trying again
			}
		}
	}

	public void halt(){
		this.running = false;
		this.interrupt();
	}
}