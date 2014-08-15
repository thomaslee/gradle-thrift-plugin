package co.tomlee.gradle.plugins.thrift;

import java.util.ArrayList;
import java.util.List;

public final class Generator {
    private final String name;
    private final List<String> options = new ArrayList<>();

    public Generator(String name) {
        this.name = name;
    }

    public void option(String option) {
        options.add(option);
    }

    public void options(List<String> options) {
        this.options.addAll(options);
    }

    public String getName() {
        return name;
    }

    public List<String> getOptions() {
        return options;
    }
}
