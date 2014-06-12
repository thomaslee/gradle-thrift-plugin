# gradle-thrift-plugin

    apply plugin: 'java'
    apply plugin: 'idea'
    apply plugin: 'thrift'

    ext {
        thriftOutDir = file("src/main/gen-java")
    }

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-thrift-plugin:0.0.1'
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

    sourceSets {
        main {
            java {
                srcDir thriftOutDir
            }
        }
    }

    task compileThrift(type: ThriftCompile) {
        inputs.file file("src/main/thrift/example.thrift")

        out thriftOutdir

        generators {
            java {
                option 'hashcode'
            }
        }
    }
    compile.dependsOn 'compileThrift'

