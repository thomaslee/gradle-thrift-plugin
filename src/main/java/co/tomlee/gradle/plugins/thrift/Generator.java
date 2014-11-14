package co.tomlee.gradle.plugins.thrift;

import java.util.ArrayList;
import java.util.List;

public final class Generator {
    private final String name;
    private final List<String> options = new ArrayList<>();
    private Object out;

    public Generator(String name) {
        this.name = name;
    }

    public void option(String option) {
        options.add(option);
    }

    public void options(List<String> options) {
        this.options.addAll(options);
    }

    public void out(Object out) {
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public List<String> getOptions() {
        return options;
    }

    public Object getOut() {
        return out;
    }
}
