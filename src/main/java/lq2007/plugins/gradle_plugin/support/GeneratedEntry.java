package lq2007.plugins.gradle_plugin.support;

import java.util.List;
import java.util.Map;

/**
 * @param generator     generator, usually the generator's class name
 * @param version       generator version
 * @param parameterList other generated parameters
 * @param parameters    other generated parameters, >parse params like "a=b"
 */
public record GeneratedEntry(String generator, int version, List<String> parameterList,
                             Map<String, String> parameters) {

    public GeneratedEntry(String generator, int version, List<String> parameterList, Map<String, String> parameters) {
        this.generator = generator;
        this.version = version;
        this.parameterList = List.copyOf(parameterList);
        this.parameters = Map.copyOf(parameters);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        return parameters.getOrDefault(name, defaultValue);
    }
}
