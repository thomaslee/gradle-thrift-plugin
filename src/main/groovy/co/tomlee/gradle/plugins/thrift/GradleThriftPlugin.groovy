package co.tomlee.gradle.plugins.thrift

import co.tomlee.gradle.plugins.thrift.tasks.ThriftCompile
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleThriftPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.extensions.create("thrift", GradleThriftPluginExtension, project)
		project.ext.ThriftCompile = ThriftCompile
	}
}
