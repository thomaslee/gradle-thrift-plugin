package co.tomlee.gradle.plugins.thrift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Generator implements Serializable {
    private final String name;
    private final List<String> options = new ArrayList<>();
    private Object out;

    public Generator() {
        this.name = null;
    }

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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Generator) {
            final Generator otherGenerator = (Generator) other;
            return name.equals(((Generator) other).name) &&
                    options.equals(((Generator) other).options) &&
                    Objects.equals(out, ((Generator) other).out);
        }
        return false;
    }
}
