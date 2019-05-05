package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSendThread extends Thread {
	private Socket clientSocket;
	private String userName;
	private BufferedReader in;
	PrintWriter out;

	public ClientSendThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	// client send data when players do something. Usually mouse click event.
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			String sendString;
			while (true) {
				//list of events

				if (clientSocket.isConnected()) {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
