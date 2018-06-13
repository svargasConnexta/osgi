package server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import plugin.PluginRegistry;

import interfaces.ConversionPlugin;

import java.io.IOException;

public class ServerActivator implements BundleActivator {

  private ClientConnectionThreadSpawner clientConnectionThreadSpawner;
  private Thread connectionListenThread;
  private PluginRegistry pluginRegistry;
  private static final int MAX_USERS = 10;
  private static final int PORT = 8081;

  @Override
  public void start(BundleContext bundleContext) throws IOException, InvalidSyntaxException {

    // Spaghetti: bundleContext.getAllServiceReferences(null, "(conversionPlugin=*)")[0].getBundle();
    //bundleContext.getAllServiceReferences(null, "(conversionPlugin=*)")[0].getBundle().loadClass(ConversionPlugin.class.getName())






    MessageDispatcher messageDispatcher = new DefaultMessageDispatcher(MAX_USERS);
    clientConnectionThreadSpawner = new ClientConnectionThreadSpawner(messageDispatcher, PORT);
    connectionListenThread = new Thread(clientConnectionThreadSpawner);
    connectionListenThread.start();
    pluginRegistry = new PluginRegistry(bundleContext);
    bundleContext.addBundleListener(pluginRegistry);
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception {
    clientConnectionThreadSpawner.exit();
    connectionListenThread.join();
    bundleContext.removeBundleListener(pluginRegistry);
  }
}
