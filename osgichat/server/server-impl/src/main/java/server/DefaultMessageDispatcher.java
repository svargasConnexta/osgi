package server;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class DefaultMessageDispatcher implements MessageDispatcher {

  private ArrayBlockingQueue<ClientConnection> clients;

  public DefaultMessageDispatcher(int maxUsers) {
    clients = new ArrayBlockingQueue<>(maxUsers); // TODO: Magic Number
  }

  @Override
  public void addClient(ClientConnection clientConnection) {
    // TODO: Duplicated clients?
    clients.add(clientConnection);
  }

  @Override
  public void dispatchMessage(ClientConnection origin, String message) {
    for (final ClientConnection c : clients) {
      //c.sendMessage(origin.getID() + " " + message);
      c.sendMessage(message);
    }
  }
}
