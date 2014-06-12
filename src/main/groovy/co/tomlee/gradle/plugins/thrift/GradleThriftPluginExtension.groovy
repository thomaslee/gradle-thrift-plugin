package co.tomlee.gradle.plugins.thrift

import org.gradle.api.Project

class GradleThriftPluginExtension {
	String executable = "thrift"
	final Project project

	public GradleThriftPluginExtension(final Project project) {
		this.project = project
	}

	def executable(String executable) {
		this.executable = executable
	}
}
