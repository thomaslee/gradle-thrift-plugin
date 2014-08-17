# gradle-thrift-plugin

    apply plugin: 'java'
    apply plugin: 'idea'
    apply plugin: 'thrift'

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-thrift-plugin:0.0.4'
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        //
        // adjust for your Thrift version ...
        //
        compile 'org.apache.thrift:libthrift:0.9.1'
    }

    generateThriftSource {
        generators {
            java {
                option 'hashcode'
                option 'beans'
            }
        }
    }

    //
    // optional: add generated sources as a source directory in IDEA
    //
    idea.module.excludeDirs = []
    idea.module.sourceDirs += file('build/generated-src/thrift/main')
    idea.module.jdkName = '1.8'

