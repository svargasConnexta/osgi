package scrap;

import server.ClientConnection;
import server.ConnectionManager;
import server.DefaultClientConnection;
import server.MessageDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultConnectionManager implements ConnectionManager {

  private final ArrayList<ClientConnection> clientList;
  private final HashSet<ClientConnection> clientSet;
  private final int PORT = 8080;
  private long USER_ID = 0;
  private final MessageDispatcher messageDispatcher;
  private AtomicBoolean shouldExit;

  public DefaultConnectionManager(final MessageDispatcher messageDispatcher) {
    clientList = new ArrayList<>();
    clientSet = new HashSet<>();
    shouldExit.set(false);
    this.messageDispatcher = messageDispatcher;
  }

  public void addClient(ClientConnection name) {
    if (clientSet.contains(name)) {
      throw new RuntimeException("Duplicate user!");
    }

    clientList.add(name);
    clientSet.add(name);
  }

  public boolean removeClient(ClientConnection name) {
    if (!clientSet.contains(name)) {
      return false;
    }
    clientSet.remove(name);
    clientList.remove(name);
    return true;
  }

  public void exit() {
      shouldExit.set(true);
  }

  public void run() {
      try (ServerSocket serverSocket = new ServerSocket(PORT)) {
          while (!shouldExit.get()) {
              final ClientConnection clientConnection;
              try {
                  Socket clientSocket = serverSocket.accept();
                  clientConnection = new DefaultClientConnection(clientSocket, USER_ID++, messageDispatcher);
                  clientList.add(clientConnection);
                  clientSet.add(clientConnection);

                  /* Spawn each Client Thread */
              } catch (IOException e) {
                  // Failure to connect a client.
                  e.printStackTrace();
              }

          }

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
