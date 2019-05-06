package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientReceiveThread extends Thread implements Observable {
	private Socket clientSocket;
	private List<Observer> observers;


	public ClientReceiveThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		observers = new ArrayList<>();
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String receiveString;
			while (true) {
				receiveString = in.readLine();
				if (receiveString.startsWith("[START]")) {
					notifyObserver("[START]");
				}

				if (receiveString.startsWith("[EVENT]")) {
					//gameEventProcedure();
					notifyObserver("[EVENT]");
				}

				if (receiveString.startsWith("[CHAT]")) {
					//chatProcedure();
					notifyObserver("[CHAT]");
				}

				if (receiveString.startsWith("[NAME]")) {
					//getNameProcedure();
					notifyObserver("[NAME]");
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
