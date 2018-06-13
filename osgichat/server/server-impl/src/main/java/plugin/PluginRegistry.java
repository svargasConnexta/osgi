package plugin;

import interfaces.ConversionPlugin;
import org.osgi.framework.*;

import java.util.HashSet;
import java.util.Set;

public class PluginRegistry implements BundleListener {

  private final Set<ConversionPlugin> plugins;

  public PluginRegistry(BundleContext bundleContext) throws InvalidSyntaxException {
    plugins = new HashSet<>();

    ServiceReference<?>[] services = bundleContext.getAllServiceReferences(null, "(conversionPlugin=*)");
    if (services == null) {
      return;
    }

    for (final ServiceReference<?> service : services) {
      Object maybePlugin = bundleContext.getService(service);
      if (ConversionPlugin.class.isAssignableFrom(maybePlugin.getClass())) {
        plugins.add((ConversionPlugin) maybePlugin);
      }
    }

  }

  @Override
  public void bundleChanged(BundleEvent event) {
    // We only care about add / remove events.
    // If the bundle has been added
  }
}
