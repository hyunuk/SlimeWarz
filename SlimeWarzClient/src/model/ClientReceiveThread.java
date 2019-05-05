package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiveThread extends Thread {
	private Socket clientSocket;



	public ClientReceiveThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}


	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String receiveString;
			while (true) {
				receiveString = in.readLine();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
