import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.ClientConnectionThreadSpawner;
import server.DefaultMessageDispatcher;
import server.MessageDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class ClientConnectionThreadSpawnerTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void echoTest() throws IOException {
    // Shared components
    final int portNumber = 8080;

    // Server components
    final String url = "127.0.0.1";
    final MessageDispatcher messageDispatcher = new DefaultMessageDispatcher();
    final ClientConnectionThreadSpawner connectionThreadSpawner =
        new ClientConnectionThreadSpawner(messageDispatcher, portNumber);

    final Thread serverThread = new Thread(connectionThreadSpawner);
    serverThread.setName("Connection Thread Spawner");
    serverThread.start();

    // Client components
    final Socket client;
    final PrintWriter out;
    final BufferedReader in;

    final String echoString = "Hello World!";

    // Connect a client to the server.
    try {
      client = new Socket(url, portNumber);
      out = new PrintWriter(client.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      // Send a single message, receive it back.
      out.println(echoString);
      String answer = in.readLine();
      assertEquals(answer, echoString);
    } catch (IOException e) {
      Logger logger = Logger.getLogger(ClientConnectionThreadSpawnerTest.class.getName());
      logger.log(Level.WARNING, e.getMessage());
      Assert.fail("Connection to server was unsuccessful.");
    }

    // Shutdown the server.
    try {
      connectionThreadSpawner.exit();
      serverThread.join();
    } catch (InterruptedException e) {
      Logger logger = Logger.getLogger(ClientConnectionThreadSpawnerTest.class.getName());
      logger.log(Level.WARNING, e.getMessage());
    }
  }
}
