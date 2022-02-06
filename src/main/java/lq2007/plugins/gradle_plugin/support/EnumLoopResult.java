package lq2007.plugins.gradle_plugin.support;

public enum EnumLoopResult {

    /**
     * This plugin is finished and exit,
     */
    FINISHED,

    /**
     * This plugin need a new loop
     */
    CONTINUE,

    /**
     * This plugin is finished, and all other plugins need interrupted.
     */
    STOP_ALL
}
