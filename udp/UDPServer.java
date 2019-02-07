/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;		//to stop receiving messages
	private boolean time_out = false;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		pacSize = 50;
		pacData = new byte [pacSize];
		pac = new DatagramPacket(pacData, pacSize);

		while (!close) {
			try {
			recvSoc.setSoTimeout(30000); //will throw socketexception after timeout (extension of IOexception)
			recvSoc.receive(pac);
			// System.out.println("Message received");
			
			}
			catch (IOException exc) {
				System.out.println("Error IO exception receiving packet, possibly timeout");
				// System.out.println("Closing server");
				// System.exit(-1);
				time_out = true;
				close = true;
			}

			processMessage(new String(pac.getData()));
		}

		
	}

	public void processMessage(String data) {
		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data);
		}
		catch (Exception exc) {
			System.out.println("Error creating MessageInfo");
			System.out.println("Closing server");
			System.exit(-1);
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null) {	//initially there will be no received messages
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1; //1 means received
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if ((msg.messageNum + 1 == msg.totalMessages) || time_out) {	//when last message is being received
			close = true;
			String lost = "Lost packets: ";
			int count = 0;
			for (int i=0; i < totalMessages; i++) {
				if (receivedMessages[i] != 1) {
					count++;
					lost = lost + (i+1) + ", ";
				}
			}

			if (count == 0) {
				lost = lost + "None";
			}

			System.out.println((msg.totalMessages - count) + " of " + msg.totalMessages + " received successfully");
			System.out.println(((count*100)/msg.totalMessages) + "% failure rate");
			System.out.println(count + " messages failed to transfer");
			System.out.println(lost);
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		//rp is receving port
		try {
			recvSoc = new DatagramSocket(rp);
		}
		catch (SocketException exc) {
			System.out.println("Couldn't create socket on port: " + rp);
			System.exit(-1);
		}
		close = false; //allows server to run
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer UDP_server = new UDPServer(recvPort);
		try {
			UDP_server.run();
		}
		catch (Exception exc) {}

	}

}
