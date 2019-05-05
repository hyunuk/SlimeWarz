package model;

import java.io.IOException;
import java.net.Socket;

public class ConnectionManager {
	private ClientSendThread clientSendThread;
	private ClientReceiveThread clientReceiveThread;
	private Socket clientSocket;

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setThreads(String hostname, int port) throws IOException {
		clientSocket = new Socket(hostname, port);
		clientReceiveThread = new ClientReceiveThread(clientSocket);
		clientSendThread = new ClientSendThread(clientSocket);
	}
}
