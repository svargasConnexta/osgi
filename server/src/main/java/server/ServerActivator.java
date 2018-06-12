package server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import java.io.IOException;

public class ServerActivator implements BundleActivator, BundleListener {

  private ClientConnectionThreadSpawner clientConnectionThreadSpawner;
  private Thread connectionListenThread;
  //private PluginListener pluginListener;
  private final int maxUsers = 10;
  private final int port = 8080;

  @Override
  public void start(BundleContext bundleContext) throws IOException {
    MessageDispatcher messageDispatcher = new DefaultMessageDispatcher(maxUsers);
    clientConnectionThreadSpawner = new ClientConnectionThreadSpawner(messageDispatcher, port);
    connectionListenThread = new Thread(clientConnectionThreadSpawner);
    connectionListenThread.start();
    //pluginListener = new PluginListener();
    //bundleContext.addBundleListener(pluginListener);
    //bundleContext.getServiceReferences()
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception {
    clientConnectionThreadSpawner.exit();
    connectionListenThread.join();
    //bundleContext.removeBundleListener(pluginListener);
  }

  @Override
  public void bundleChanged(BundleEvent event) {
  }
}
