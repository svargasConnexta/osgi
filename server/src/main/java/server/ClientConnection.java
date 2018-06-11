package server;

import java.net.Socket;

public interface ClientConnection extends Runnable {
  Socket getSocket();
  long getID();
  void sendMessage(String message);
  void disconnect();
}
