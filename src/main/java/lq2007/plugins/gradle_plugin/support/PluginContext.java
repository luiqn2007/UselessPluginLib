package lq2007.plugins.gradle_plugin.support;

import java.nio.file.Path;

/**
 * Plugin context
 *
 * @param times      loop times, begin from 0
 * @param root       loop root
 * @param exceptions exceptions, always empty in {@link ISourcePlugin#begin(PluginContext, PluginHelper)} and {@link  ISourcePlugin#getLoopRoot(PluginHelper)}
 */
public record PluginContext(int times, Path root,
                            PluginExceptions exceptions) {
}
