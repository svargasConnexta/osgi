import junit.framework.AssertionFailedError;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ClientConnectionThreadSpawnerTest {

  // Shared components
  private final int portNumber = 8080;

  // Server components
  private final String url = "127.0.0.1";
  private MessageDispatcher messageDispatcher;
  private ClientConnectionThreadSpawner connectionThreadSpawner;
  private Thread serverThread;
  private final int maxUsers = 10;
  private final int maxMessages = 9999;

  @Before
  public void setUp() throws Exception {
    messageDispatcher = new DefaultMessageDispatcher(maxUsers);
    connectionThreadSpawner = new ClientConnectionThreadSpawner(messageDispatcher, portNumber);
    serverThread = new Thread(connectionThreadSpawner);
    serverThread.setName("Connection Thread Spawner");
    serverThread.start();
  }

  @After
  public void tearDown() throws Exception {
    // Shutdown the server.
    try {
      connectionThreadSpawner.exit();
      serverThread.join();
    } catch (InterruptedException e) {
      Logger logger = Logger.getLogger(ClientConnectionThreadSpawnerTest.class.getName());
      logger.log(Level.WARNING, e.getMessage());
    }
  }

  @Test
  public void singleUserReceivesEchoedMessage() {
    // Client
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
  }

  @Test
  public void multipleUsersRecieveEchoedMessages() throws IOException {
    final List<Socket> clientSockets = new ArrayList<>();
    final List<PrintWriter> outputs = new ArrayList<>();
    final List<BufferedReader> inputs = new ArrayList<>();

    final int numberOfClients = 3;

    // Create 'numberOfClients' clients.
    for (int i = 0; i < numberOfClients; ++i) {
      final Socket client = new Socket(url, portNumber);
      clientSockets.add(client);
      outputs.add(new PrintWriter(client.getOutputStream(), true));
      inputs.add(new BufferedReader(new InputStreamReader(client.getInputStream())));
    }

    // Send a message with a number that corresponds to each client
    for (int i = 0; i < numberOfClients; ++i) {
      outputs.get(i).println(i);
    }

    // TODO: There is no synchronization and no guaranteed order
    //       in this OSGI chat demo. Instead we just test that all clients
    //       did infact receive 'numberOfClient' messages for now.

    for (int i =0; i < numberOfClients; ++i) {
      for (int j = 0; j < numberOfClients; ++j) {
          int actualNumber = Integer.parseInt(inputs.get(i).readLine());
          assertTrue(actualNumber >= 0 && actualNumber < numberOfClients);
      }
    }

  }
}
