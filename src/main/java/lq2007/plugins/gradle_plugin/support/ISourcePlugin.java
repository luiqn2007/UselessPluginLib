package lq2007.plugins.gradle_plugin.support;

import java.nio.file.Path;

public interface ISourcePlugin {

    /**
     * Begin loop
     *
     * @param context context, includes times, root path and exception(always empty)
     * @param helper plugin helper, includes some paths
     */
    void begin(PluginContext context, PluginHelper helper);

    /**
     * Parse all files, not include directories
     *
     * @param file all files
     * @param context plugin context
     * @param helper plugin helper, includes methods to build ast tree, create or save java files
     * @throws Exception any exceptions, but can't stop the plugin, it can be found in context.
     */
    void each(Path file, PluginContext context, PluginHelper helper) throws Exception;

    /**
     * Loop finished.
     *
     * @param context context, includes exceptions.
     * @param helper plugin helpers
     * @return loop result, decide whether the plugin is finished.
     */
    EnumLoopResult finished(PluginContext context, PluginHelper helper);

    /**
     * provide thr root directory or file to next loop.
     *
     * @param helper plugin helper
     * @return root directory or file
     */
    Path getLoopRoot(PluginHelper helper);
}
