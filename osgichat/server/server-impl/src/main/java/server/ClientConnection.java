package server;

public interface ClientConnection extends Runnable {
  long getID();
  void sendMessage(String message);
  void disconnect();
}
