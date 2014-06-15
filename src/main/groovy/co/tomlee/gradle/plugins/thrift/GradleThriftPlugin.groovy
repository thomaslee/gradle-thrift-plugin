package co.tomlee.gradle.plugins.thrift

import co.tomlee.gradle.plugins.thrift.tasks.ThriftCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet

class GradleThriftPlugin implements Plugin<Project> {
	public static final String COMPILE_THRIFT_TASK = "compileThrift"
	public static final String CLEAN_THRIFT_TASK = "cleanThrift"
	private static final String THRIFT_SOURCE = "src/main/thrift"
	private static final String GEN_JAVA_SOURCE = "src/main/gen-java"

	@Override
	void apply(Project project) {
		project.extensions.create("thrift", GradleThriftPluginExtension, project)
		project.ext.ThriftCompile = ThriftCompile

		project.tasks.create(COMPILE_THRIFT_TASK, ThriftCompile) {
			path project.file(THRIFT_SOURCE)
		}

		def javaPlugin = project.plugins.findPlugin("java")
		if (javaPlugin) {
			project.getTasksByName(COMPILE_THRIFT_TASK, false).each {
				installTaskDependencies(project, it, JavaPlugin.COMPILE_JAVA_TASK_NAME)
			}
		}
		else {
			project.plugins.whenPluginAdded {
				if (it.id.equals("java")) {
					project.getTasksByName(COMPILE_THRIFT_TASK, false).each {
						installTaskDependencies(project, it, JavaPlugin.COMPILE_JAVA_TASK_NAME)
					}
				}
			}
		}
	}

	private void installTaskDependencies(Project project, Task compileThrift, String taskName) {
		def destination = project.file(GEN_JAVA_SOURCE)
		project.getTasksByName(taskName, false).each {
			project.sourceSets.main.java.srcDir destination.path
			compileThrift.out destination
			compileThrift.generators.create("java").option "hashcode"
			it.dependsOn compileThrift

			project.getTasksByName(BasePlugin.CLEAN_TASK_NAME, false).each {
				def cleanThrift = project.tasks.maybeCreate(CLEAN_THRIFT_TASK) << {
					project.delete compileThrift.out
				}
				it.dependsOn cleanThrift
			}
		}
	}
}
