package co.tomlee.gradle.plugins.thrift;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.util.ConfigureUtil;

public class ThriftSourceVirtualDirectoryImpl implements ThriftSourceVirtualDirectory {
    private static final String[] filters = { "**/*.thrift" };

    private final SourceDirectorySet thrift;

    public ThriftSourceVirtualDirectoryImpl(String parentDisplayName, FileResolver fileResolver) {
        final String displayName = String.format("%s Thrift source", parentDisplayName);
        this.thrift = new DefaultSourceDirectorySet(displayName, fileResolver);
        this.thrift.getFilter().include(filters);
    }

    @Override
    public SourceDirectorySet getThrift() {
        return thrift;
    }

    @Override
    public ThriftSourceVirtualDirectory thrift(Closure closure) {
        ConfigureUtil.configure(closure, getThrift());
        return this;
    }
}
