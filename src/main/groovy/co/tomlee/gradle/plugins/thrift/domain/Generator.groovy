package co.tomlee.gradle.plugins.thrift.domain

class Generator {
	final String name
	List<String> options = new ArrayList<>()

	public Generator(String name) {
		this.name = name
	}

	def option(String option) {
		options << option
	}

	def options(List<String> options) {
		this.options.addAll(options)
	}
}
