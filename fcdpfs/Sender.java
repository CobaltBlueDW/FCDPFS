/**	Sender:  The thread that handles individual xml file requests.
*
*	@author David Wipperfurth
*	@dated	4/28/10
*	@copyright	Public Domain
*/

//Use whatever package you want here, all needed imports are listed.
//Just make sure the package you choose is properly reflected in your properties file
//AND that the associated FlashCrossDomainPolicyFileService and Listener files reflect the change as well.
package simpleremoteconnection.fcdpfs;

import simpleremoteconnection.fcdpfs.*;

import java.io.*;
import java.net.Socket;
//import java.lang.InterruptedException;

import java.util.logging.Logger;

public class Sender extends Thread{

	private static final Logger logger = Logger.getLogger(Sender.class.getName()); // Used for all logging from this class

	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	private String trigger;
	private String response;

	public Sender(Socket socket, String trigger, String response){
		this.socket = socket;
		this.trigger = trigger;
		this.response = response;
	}

	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			socket.setSoTimeout(10000);
			if(trigger.equals(read())){
				out.print(response+"\0");
				out.flush();
				try{ sleep(500); }catch(InterruptedException e){}	//To fix Flash's timing issues
			}

			socket.close();
			out.close();
			in.close();
		}catch(IOException e){
			logger.severe(e.getMessage());
			return;
		}
	}

	/**	Read:  Grabs the policy request string.
	 *
	 * @return String
	 * @throws IOException
	 * @throws EOFException
	 * @throws InterruptedIOException
	 */
	private String read() throws IOException, EOFException, InterruptedIOException{
		StringBuffer buffer = new StringBuffer();
		int character;

		while(buffer.length()<100){
			character=in.read();
			if(character==0){
				return buffer.toString();
			}else{
				buffer.appendCodePoint(character);
			}
		}
		
		return buffer.toString();
	}
}