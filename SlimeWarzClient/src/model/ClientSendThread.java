package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSendThread extends Thread implements Observable {
	private Socket clientSocket;
	private String userName;
	private BufferedReader in;
	private PrintWriter out;
	private List<Observer> observers;


	public ClientSendThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		observers = new ArrayList<>();
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

	@Override
	public void addObserver(Observer o) {
		if (!observers.contains(o)) {
			observers.add(o);
		}
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);

	}

	@Override
	public void notifyObserver(String message) {
		for (Observer o : observers) {
			o.update(message);
		}
	}
}
