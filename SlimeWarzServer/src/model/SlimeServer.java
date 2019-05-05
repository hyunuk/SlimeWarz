package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlimeServer extends Thread implements CallBack, Observable {
	private ServerSocket serverSocket;
	private PrintWriter out;
	private ArrayList<PrintWriter> toSend;
	private List<Observer> observers;
	private int port;

	public SlimeServer(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
		observers = new ArrayList<>();
	}

	public void run() {
		final String SOCKET_BOUND = "ServerSocket #" + port + " is bound. " + (new Date()).toString() + "\r\n";
		toSend = new ArrayList<>();
		Socket connectionSocket;
		boolean proceed = true;
		int userID = 0;
		Thread callBackThread;
		CallBackHandler callbackHandler;

		if (serverSocket.isBound()) {
			notifyObserver(SOCKET_BOUND);
		}

		while (proceed) {
			try {
				connectionSocket = serverSocket.accept();
				callbackHandler = new CallBackHandler(connectionSocket, userID, this);
				out = new PrintWriter(connectionSocket.getOutputStream(), true);
				out.println("Welcome client # " + userID);
				userID++;
				toSend.add(out);
				callBackThread = new Thread(callbackHandler);
				callBackThread.start();
			} catch (IOException e) {
				proceed = false;
			}
		}
	}

	@Override
	public void executeCallBack(String user, String msg) {
		ArrayList<Player> players = new ArrayList<>();
		int userIndex = Integer.parseInt(user);
		final String THREAD_END = user + " is quit. Thread ends.";
		final String READY = "Player" + players.get(userIndex).name + "is ready!";

		if (msg == null) {
			notifyObserver(THREAD_END);
			out.println(THREAD_END);
			return;
		}

		if (msg.startsWith("[JOIN]")) {
			String name = msg.replaceAll("[JOIN]", "");
			Player player = new Player(userIndex, name);
			players.add(player);
			toSend.get(userIndex).println();
		}

		if (msg.startsWith("[READY]")) {
			players.get(userIndex).isReady = true;
			broadCast(READY);
			if (players.get(0).isReady && players.get(1).isReady) {
				broadCast("Let's start.");
			}
		}

		if (msg.startsWith("[EVENT]")) {
			if (userIndex == 0) {
				toSend.get(1).println(msg);
			} else if (userIndex == 1) {
				toSend.get(0).println(msg);
			}
		}

		if (msg.startsWith("[CHAT]")) {
			broadCast(msg + ":" + players.get(userIndex).name);
		}

	}

	private void broadCast(String str) {
		for (PrintWriter printWriter : toSend) {
			printWriter.println(str);
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

	public void close() throws IOException {
		serverSocket.close();
	}
}
