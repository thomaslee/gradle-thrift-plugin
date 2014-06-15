# gradle-thrift-plugin

    apply plugin: 'java'
    apply plugin: 'idea'
    apply plugin: 'thrift'

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-thrift-plugin:0.0.2'
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

    compileThrift {
        inputs.file file("src/main/thrift/example.thrift")
    }

