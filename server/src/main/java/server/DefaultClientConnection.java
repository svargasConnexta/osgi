package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultClientConnection implements ClientConnection {

  private Socket socket;
  private long id;
  private PrintWriter output;
  private BufferedReader input;
  private AtomicBoolean shouldDisconnect;
  private final MessageDispatcher messageDispatcher;

  public DefaultClientConnection(Socket socket, long id, final MessageDispatcher messageDispatcher)
      throws IOException {
    this.socket = socket;
    this.id = id;
    this.messageDispatcher = messageDispatcher;
    output = new PrintWriter(this.socket.getOutputStream(), true);
    input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    shouldDisconnect = new AtomicBoolean(false);
  }

  public Socket getSocket() {
    return socket;
  }

  public long getID() {
    return id;
  }

  public synchronized void sendMessage(String message) {
      output.println(message);
  }

  public void disconnect() {
      shouldDisconnect.set(true);
  }

  public void run() {
      while (!shouldDisconnect.get()) {
        String userString = null;

        // Disconnect if an IOException is raised because
        // the user has disconnected.
        try {
          userString = input.readLine();
        } catch (IOException e) {
          Logger logger = Logger.getLogger(DefaultClientConnection.class.getName());
          logger.log(Level.WARNING, e.getMessage());
          shouldDisconnect.set(true);
        }

        // Record the message the user sent.
        if (userString != null) {
            messageDispatcher.dispatchMessage(this, userString);
        }
      }
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof ClientConnection)) {
      return false;
    }
    final ClientConnection other = (ClientConnection) obj;
    return other.getID() == id && other.getSocket() == socket;
  }
}
