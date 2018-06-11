package server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.IOException;

public class ServerActivator implements BundleActivator {

  private ClientConnectionThreadSpawner clientConnectionThreadSpawner;
  private Thread connectionListenThread;
  private final int maxUsers = 10;
  private final int maxMessages = 9999;
  private final int port = 8080;

  public void start(BundleContext bundleContext) throws IOException {
    MessageDispatcher messageDispatcher = new DefaultMessageDispatcher(maxUsers);
    clientConnectionThreadSpawner = new ClientConnectionThreadSpawner(messageDispatcher, port);
    connectionListenThread = new Thread(clientConnectionThreadSpawner);
    connectionListenThread.start();
  }

  public void stop(BundleContext bundleContext) throws Exception {
    clientConnectionThreadSpawner.exit();
    connectionListenThread.join();
  }
}
