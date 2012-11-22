/**	Flash Cross-Domain Policy File Service:  An RDS service wrapper around a stand-alone
*	flash cross-domain policy file server.
*
*	Make sure you set the properties for this class properly in your app.properties file.
*	Take a look at the accompanied 'app.properties' file for help with that.
*
*	NOTE: best to use Flash Player 9.0.124.0. or later
*
*	@author David Wipperfurth
*	@dated	4/28/10
*	@copyright	Public Domain
*/

//Use whatever package you want here, all needed imports are listed.
//Just make sure the package you choose is properly reflected in your properties file
//AND that the associated Listner and Sender files reflect the change as well.
package simpleremoteconnection.fcdpfs;

import simpleremoteconnection.fcdpfs.*;
import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.service.Service;
import com.sun.sgs.service.TransactionProxy;

import java.io.*;

//import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;


public class FlashCrossDomainPolicyFileService implements Service{

	private static final Logger logger = Logger.getLogger(FlashCrossDomainPolicyFileService.class.getName()); // Used for all logging from this class

    public static final String PROP_FILE_LOCATION = "FlashCrossDomainPolicyFileService.filepath";
	public static final String PROP_LISTENING_PORT = "FlashCrossDomainPolicyFileService.port";

	public static final String POLICY_REQUEST = "<policy-file-request/>";

    private String file;
	private int listeningPort;
	private Listener server;

	public FlashCrossDomainPolicyFileService(Properties properties, ComponentRegistry registry, TransactionProxy txnProxy) throws IOException{
		String curLine = null;
		file = "";
		BufferedReader myInput = new BufferedReader(new InputStreamReader(new FileInputStream(properties.getProperty(PROP_FILE_LOCATION, "/conf/crossdomain.xml"))));
		while((curLine = myInput.readLine()) != null){
			file += curLine;
		}
		myInput.close();

		listeningPort = new Integer(properties.getProperty(PROP_LISTENING_PORT, "843"));

		server = new Listener(listeningPort, POLICY_REQUEST, file);
		server.start();
	}

	public String getName(){
		return FlashCrossDomainPolicyFileService.class.getName();
	}

	public void ready() throws Exception{
		logger.info("FlashCrossDomainPolicyFile:\n"+file);
	}

	public void shutdown(){
		if(server != null) server.halt();
	}
}