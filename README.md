# gradle-thrift-plugin

    apply plugin: 'java'
    apply plugin: 'idea'
    apply plugin: 'thrift'

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-thrift-plugin:0.0.1'
        }
    }

    task compileThrift(type: ThriftCompile) {
        inputs.file file("src/main/thrift/example.thrift")

        generators {
            java {
                option 'hashcode'
            }
        }
    }
    compile.dependsOn 'compileThrift'

