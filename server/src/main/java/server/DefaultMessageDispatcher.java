package server;

import java.util.ArrayList;

public class DefaultMessageDispatcher implements MessageDispatcher {

  private ArrayList<ClientConnection> clients;

  public DefaultMessageDispatcher() {
    clients = new ArrayList<>();
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
