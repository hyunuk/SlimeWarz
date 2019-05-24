package viewModel;

import model.ServerModel;

import java.io.IOException;

public class ConnectionManager {
	ServerModel serverModel;

	public void closeServer() {
		final String MSG_CLOSING_SERVER = "Closing Server";

		if (serverModel != null) {
			try {
				serverModel.close();
				System.out.println(MSG_CLOSING_SERVER);
			} catch (IOException err) {
				System.out.println(err.getMessage());
			}
		}
	}

	public void createServer(int portNum) {
		try {
			serverModel = new ServerModel(portNum);
			serverModel.start();
			serverModel.addObserver(this);
		} catch (IOException e) {
			String errorMSG = "PORT #" + portNum + " is already opened.\r\n";
			printLog(errorMSG);
		}
	}
}
