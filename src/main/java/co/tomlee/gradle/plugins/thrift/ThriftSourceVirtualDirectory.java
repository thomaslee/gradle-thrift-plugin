package co.tomlee.gradle.plugins.thrift;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;

public interface ThriftSourceVirtualDirectory {
    SourceDirectorySet getThrift();
    ThriftSourceVirtualDirectory thrift(Closure closure);
}
