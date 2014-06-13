package co.tomlee.gradle.plugins.thrift.tasks

import co.tomlee.gradle.plugins.thrift.domain.Generator
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction

class ThriftCompile extends DefaultTask {
	String thrift
	NamedDomainObjectContainer<Generator> generators = project.container(Generator)
	List<File> include = []
	File out = project.file("build/thrift")
	boolean recurse = true
	boolean verbose = false
	boolean strict = false
	boolean debug = false

	@TaskAction
	def invokeThrift() {
		def command = buildCommand()
		project.logger.info(command.join(" "))
		if (!out.isDirectory()) {
			if (!out.mkdirs()) {
				throw new GradleException("Could not create thrift output directory: ${out}")
			}
		}
		def p = command.execute()
		if (project.logger.quietEnabled) {
			p.consumeProcessOutput()
			p.waitFor()
		}
		else {
			p.waitForProcessOutput(System.out, System.err)
		}
		if (p.exitValue() != 0) {
			throw new GradleException("${thriftExecutable()} command failed")
		}
	}

	@OutputDirectories
	def outputDirectories() {
		return [out]
	}

	def out(File dir) {
		out = dir
	}

	def recurse(boolean recurse) {
		this.recurse = recurse
	}

	def verbose(boolean verbose) {
		this.verbose = verbose
	}

	def strict(boolean strict) {
		this.strict = strict
	}

	def debug(boolean debug) {
		this.debug = debug
	}

	def path(File file) {
		include << file
	}

	def generators(Closure c) {
		generators.configure(c)
	}

	String thriftExecutable() {
		return (this.thrift != null ? this.thrift : project.thrift.executable)
	}

	List<String> buildCommand() {
		def thrift = thriftExecutable()
		def command = [thrift, "-out", out.absolutePath]
		generators.each { Generator generator ->
			command << "--gen"
			generator.options.each { String option ->
				command << "${generator.name}:${option}"
			}
		}
		include.each { File file ->
			command << "-I"
			command << file.absolutePath
		}
		if (recurse) command << "-recurse"
		if (verbose) command << "-verbose"
		if (strict) command << "-strict"
		if (debug) command << "-debug"
		command << inputs.files.getSingleFile().absolutePath
		command
	}
}
