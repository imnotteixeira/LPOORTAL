package networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;



public class Server implements Runnable{

	private ServerSocket socket;
	private SocketCommunicator socketCommunicator;
	
	public Server(ServerSocket socket) {
		this.socket = socket;
		
	}

	@Override
	public void run() {
		try {
			while(true) {
		        Socket clientSocket = socket.accept();
		        
		        socketCommunicator = new SocketCommunicator(clientSocket);
		        
		        new Thread(socketCommunicator).start();
		        
			}
		}
		catch (IOException e) {
			System.out.println(e);
	    }
		
	}
	


}
