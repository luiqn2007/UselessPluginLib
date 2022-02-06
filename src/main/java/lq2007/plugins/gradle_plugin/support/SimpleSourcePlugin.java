package lq2007.plugins.gradle_plugin.support;

import java.nio.file.Path;

public class SimpleSourcePlugin implements ISourcePlugin {

    @Override
    public void begin(PluginContext context, PluginHelper helper) {

    }

    @Override
    public void each(Path file, PluginContext context, PluginHelper helper) throws Exception {

    }

    @Override
    public EnumLoopResult finished(PluginContext context, PluginHelper helper) {
        return EnumLoopResult.FINISHED;
    }

    @Override
    public Path getLoopRoot(PluginHelper helper) {
        return helper.srcPath();
    }
}
