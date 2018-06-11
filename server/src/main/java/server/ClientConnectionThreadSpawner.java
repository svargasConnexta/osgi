package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnectionThreadSpawner implements Runnable {

  private long connectionsSpawned = 0;
  private AtomicBoolean shouldExit;
  private final MessageDispatcher messageDispatcher;
  private final int port;
  private final ServerSocket serverSocket;

  public ClientConnectionThreadSpawner(final MessageDispatcher messageDispatcher, final int port)
      throws IOException {
    serverSocket = new ServerSocket(port);
    shouldExit = new AtomicBoolean(false);
    this.messageDispatcher = messageDispatcher;
    this.port = port;
  }

  public void exit() throws IOException {
    shouldExit.set(true);
    if (!serverSocket.isClosed()) {
      serverSocket.close();
    }
  }

  private void acceptClientConnection(ServerSocket serverSocket) throws IOException {
    Socket clientSocket = serverSocket.accept();
    ClientConnection clientConnection =
        new DefaultClientConnection(clientSocket, connectionsSpawned++, messageDispatcher);
    messageDispatcher.addClient(clientConnection);
    final Thread clientThread = new Thread(clientConnection);
    clientThread.setName("Client " + clientConnection.getID());
    clientThread.start();
  }

  public void run() {
    while (true) {
      try {
        acceptClientConnection(serverSocket);
      } catch (IOException e) {
        Logger logger = Logger.getLogger(ClientConnectionThreadSpawner.class.getName());
        logger.log(Level.INFO, e.getMessage());
        logger.log(Level.INFO, "Server shutting down!");
        return;
      }
    }
  }
}
