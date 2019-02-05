/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.registry.*;
import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		// String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
		// TO-DO: Bind to RMIServer
		System.out.println("before try");
			try{
				System.out.println("lookup attempted..");
				//iRMIServer = (RMIServerI)Naming.lookup("RMIServer");
				Registry registry = LocateRegistry.getRegistry(args[0]);
				iRMIServer = (RMIServerI) registry.lookup("RMIServer");
				System.out.println("lookup succeeded");
				// TO-DO: Attempt to send messages the specified number of times
				for(int i = 0; i < numMessages; i++){
					MessageInfo messageIn = new MessageInfo(numMessages, i);
					iRMIServer.receiveMessage(messageIn);
					System.out.println("Message: " + i);
				}
				System.out.println("Messages Sent");
			}
		catch (NotBoundException e) {
				System.out.println("Error: NotBoundException.");
		}
		catch (RemoteException e) {
				System.out.println("Error: Remote Exception.");
		}
		// catch (MalformedURLException e) {
		// 		System.out.println("Error: Malformed URL.");
		// }
	}
}
