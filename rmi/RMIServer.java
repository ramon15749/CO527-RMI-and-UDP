/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.registry.Registry;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages; // this is a buffer

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialize the receive buffer
				if (receivedMessages == null) {
					totalMessages = msg.totalMessages;
					receivedMessages = new int[msg.totalMessages];
				}

				// TO-DO: Log receipt of the message
				receivedMessages[msg.messageNum] = 1;
				System.out.println(msg.messageNum);

				// TO-DO: If this is the last expected message, then identify any missing messages
				if (msg.messageNum == totalMessages-1) {
					System.out.println("Messages being totaled....");
					int count = 0;
					for (int i = 0; i < totalMessages; i++) {
						if (receivedMessages[i] != 1) {
							count++;
						}
					}


					System.out.println("Sent      : " + totalMessages);
					System.out.println("Received  : " + (totalMessages - count));
					System.out.println("Lost      : " + count);
					System.out.println("Test finished.");
					System.exit(0);
				}
			}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Instantiate the server class
		try{
			rmis =  new RMIServer();
			System.out.println("going to rebind");
			// TO-DO: Bind to RMI registry
			rebindServer("rmi://localhost/RMIServer",rmis);
		}
		//Exception Handling
		catch (RemoteException e) {
				System.out.println("Error: Remote Exception.");
	}
}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			// RMIServer stub =  (RMIServer) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(serverURL,server);
			System.out.println("RMIServer bound");
		}
		catch (RemoteException e) {
				System.out.println("Error: Remote Exception.");
	}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}
