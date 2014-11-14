package co.tomlee.gradle.plugins.thrift;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import javax.inject.Inject;
import java.io.File;

public class ThriftPlugin implements Plugin<Project> {
    private final FileResolver fileResolver;

    @Inject
    public ThriftPlugin(final FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JavaPlugin.class);

        configureConfigurations(project);
        configureSourceSets(project);
    }

    private void configureConfigurations(final Project project) {
        final Configuration thriftConfiguration = project.getConfigurations().create("thrift").setVisible(false);
        project.getConfigurations().getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(thriftConfiguration);
    }

    private void configureSourceSets(final Project project) {
        project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(new Action<SourceSet>() {
            @Override
            public void execute(SourceSet sourceSet) {
                //
                // This logic borrowed from the antlr plugin.
                // 1. Add a new 'thrift' virtual directory mapping
                //
                final ThriftSourceVirtualDirectoryImpl thriftSourceSet =
                        new ThriftSourceVirtualDirectoryImpl(((DefaultSourceSet) sourceSet).getDisplayName(), fileResolver);
                new DslObject(sourceSet).getConvention().getPlugins().put("thrift", thriftSourceSet);
                final String srcDir = String.format("src/%s/thrift", sourceSet.getName());
                thriftSourceSet.getThrift().srcDir(srcDir);
                sourceSet.getAllSource().source(thriftSourceSet.getThrift());

                //
                // 2. Create a ThriftTask for this sourceSet
                //
                final String taskName = sourceSet.getTaskName("generate", "ThriftSource");
                final ThriftTask thriftTask = project.getTasks().create(taskName, ThriftTask.class);
                thriftTask.setDescription(String.format("Processes the %s Thrift IDLs.", sourceSet.getName()));

                //
                // 3. Set up convention mapping for default sources (allows user to not have to specify)
                //
                thriftTask.setSource(thriftSourceSet.getThrift());

                //
                // 4. Set up the thrift output directory (adding to javac inputs)
                //
                final String outputDirectoryName =
                        String.format("%s/generated-src/thrift/%s", project.getBuildDir(), sourceSet.getName());
                final File outputDirectory = new File(outputDirectoryName);
                thriftTask.out(outputDirectory);
                sourceSet.getJava().srcDir(outputDirectory);

                //
                // 5. Register the fact that thrit should run before compiling.
                //
                project.getTasks().getByName(sourceSet.getCompileJavaTaskName()).dependsOn(taskName);
            }
        });
    }
}
