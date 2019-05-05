package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class CallBackHandler implements Runnable {
	private Socket socket;
	private int userID;
	private CallBack callback;

	public CallBackHandler(Socket socket, int userID, CallBack callback) {
		this.socket = socket;
		this.userID = userID;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String message;
			while (true) {
				message = bufferedReader.readLine();
				callback.executeCallBack(userID + "", message);
			}
		} catch (IOException e) {
			callback.executeCallBack(userID + "", e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				callback.executeCallBack(userID + "", e.getMessage());
			}
		}
	}
}
