package lq2007.plugins.gradle_plugin.support;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An exception map
 */
public class PluginExceptions extends RuntimeException implements Map<Path, Exception> {

    private final Map<Path, Exception> exceptions = new HashMap<>();

    @Override
    public void printStackTrace(PrintStream s) {
        values().forEach(e -> e.printStackTrace(s));
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        values().forEach(e -> e.printStackTrace(s));
    }

    @Override
    public void printStackTrace() {
        values().forEach(Throwable::printStackTrace);
    }

    @Override
    public int size() {
        return exceptions.size();
    }

    @Override
    public boolean isEmpty() {
        return exceptions.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return exceptions.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return exceptions.containsValue(value);
    }

    @Override
    public Exception get(Object key) {
        return exceptions.get(key);
    }

    @Override
    public Exception put(Path key, Exception value) {
        return exceptions.put(key, value);
    }

    @Override
    public Exception remove(Object key) {
        return exceptions.remove(key);
    }

    @Override
    public void putAll(Map<? extends Path, ? extends Exception> m) {
        exceptions.putAll(m);
    }

    @Override
    public void clear() {
        exceptions.clear();
    }

    @Override
    public Set<Path> keySet() {
        return exceptions.keySet();
    }

    @Override
    public Collection<Exception> values() {
        return exceptions.values();
    }

    @Override
    public Set<Entry<Path, Exception>> entrySet() {
        return exceptions.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return exceptions.equals(o);
    }

    @Override
    public int hashCode() {
        return exceptions.hashCode();
    }

    @Override
    public Exception getOrDefault(Object key, Exception defaultValue) {
        return exceptions.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super Path, ? super Exception> action) {
        exceptions.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super Path, ? super Exception, ? extends Exception> function) {
        exceptions.replaceAll(function);
    }

    @Override
    public Exception putIfAbsent(Path key, Exception value) {
        return exceptions.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return exceptions.remove(key, value);
    }

    @Override
    public boolean replace(Path key, Exception oldValue, Exception newValue) {
        return exceptions.replace(key, oldValue, newValue);
    }

    @Override
    public Exception replace(Path key, Exception value) {
        return exceptions.replace(key, value);
    }

    @Override
    public Exception computeIfAbsent(Path key, Function<? super Path, ? extends Exception> mappingFunction) {
        return exceptions.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Exception computeIfPresent(Path key, BiFunction<? super Path, ? super Exception, ? extends Exception> remappingFunction) {
        return exceptions.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Exception compute(Path key, BiFunction<? super Path, ? super Exception, ? extends Exception> remappingFunction) {
        return exceptions.compute(key, remappingFunction);
    }

    @Override
    public Exception merge(Path key, Exception value, BiFunction<? super Exception, ? super Exception, ? extends Exception> remappingFunction) {
        return exceptions.merge(key, value, remappingFunction);
    }
}
