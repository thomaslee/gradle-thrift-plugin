package co.tomlee.gradle.plugins.thrift;

import groovy.lang.Closure;
import org.gradle.api.GradleException;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThriftTask extends SourceTask {
    private String thrift;
    private NamedDomainObjectContainer<Generator> generators = getProject().container(Generator.class);
    private final ArrayList<File> include = new ArrayList<>();
    private SourceDirectorySet source;
    private File out = getProject().file("build/generated-src/thrift");
    private boolean recurse = true;
    private boolean verbose = false;
    private boolean strict = false;
    private boolean debug = false;


    @TaskAction
    public void invokeThrift() throws Exception {
        for (final File file : getSource().getFiles()) {
            final List<String> command = buildCommand(file.getAbsolutePath());
            getProject().getLogger().info("Running thrift: " + command);
            if (!out.isDirectory()) {
                if (!out.mkdirs()) {
                    throw new GradleException("Could not create thrift output directory: " + out);
                }
            }
            final CountDownLatch latch = new CountDownLatch(2);
            final Process p = new ProcessBuilder(command)
                    .start();
            new SlurpThread(latch, p.getInputStream(), System.out).start();
            new SlurpThread(latch, p.getErrorStream(), System.err).start();

            if (p.waitFor() != 0) {
                latch.countDown();
                throw new GradleException(thriftExecutable() + " command failed");
            }
            latch.await();
        }
    }

    public void setOutputDirectory(final File outputDirectory) {
        this.out = outputDirectory;
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return out;
    }

    public void setSource(final SourceDirectorySet sourceDirectorySet) {
        this.source = sourceDirectorySet;
    }

    public SourceDirectorySet getSource() {
        return source;
    }

    public void out(File dir) {
        this.out = dir;
    }

    public void recurse(boolean recurse) {
        this.recurse = recurse;
    }

    public void verbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void strict(boolean strict) {
        this.strict = strict;
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }

    public void path(File file) {
        include.add(file);
    }

    public void generators(Closure c) {
        generators.configure(c);
    }

    public String thriftExecutable() {
        return this.thrift != null ? this.thrift : "thrift";
    }

    public List<String> buildCommand(String fileName) {
        final String thrift = thriftExecutable();
        final List<String> command = new ArrayList<>(Arrays.asList(thrift, "-out", out.getAbsolutePath()));
        for (final Generator generator : generators) {
            command.add("--gen");
            command.add(generator.getName() + ":" + join(",", generator.getOptions()));
        }
        for (final File include : this.include) {
            command.add("-I");
            command.add(include.getAbsolutePath());
        }
        if (recurse) command.add("-recurse");
        if (verbose) command.add("-verbose");
        if (strict) command.add("-strict");
        if (debug) command.add("-debug");
        command.add(fileName);
        return command;
    }

    private static String join(final String sep, final List<String> arg) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg.size(); i++) {
            sb.append(arg.get(i));
            if (i < arg.size()-1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    private final class SlurpThread extends Thread {
        private final CountDownLatch latch;
        private final InputStream in;
        private final PrintStream out;

        public SlurpThread(final CountDownLatch latch, final InputStream in, final PrintStream out) {
            this.latch = latch;
            this.in = in;
            this.out = out;
        }

        public void run() {
            try {
                final InputStreamReader reader = new InputStreamReader(in);
                final char[] buf = new char[8 * 1024];
                for (; ; ) {
                    try {
                        if (reader.read(buf) <= 0) {
                            break;
                        }
                        out.print(buf);
                    } catch (IOException e) {
                        getLogger().error("Failed to read from input stream", e);
                        break;
                    }
                }
            }
            finally {
                latch.countDown();
            }
        }
    }
}
