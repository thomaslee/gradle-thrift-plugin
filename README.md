# gradle-thrift-plugin

Put your thrift files in `src/main/thrift`.

Configuration example:

    apply plugin: 'java'
    apply plugin: 'idea'
    apply plugin: 'thrift'

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-thrift-plugin:0.0.6'
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
        //
        // The output directory (optional)
        //
        out file('build/generated-src/thrift/main')

        //
        // -verbose / -debug / -strict (all optional)
        //
        verbose false
        debug false
        strict false

        //
        // Modify the include path (optional)
        //
        path file('vendor/thrift')

        //
        // Set the thrift executable (optional)
        //
        executable '/usr/bin/thrift'

        generators {
            //
            // --gen java:hashcode,beans
            //
            java {
                //
                // Options passed to the `java` generator
                //
                option 'hashcode'
                option 'beans'
            }

            //
            // --gen go
            //
            go {
                //
                // Output directory can be set on a per-generator basis too
                //
                out file('build/generated-src/thrift/go-main')
            }

            //
            // --gen js
            //
            js {
                out file('build/generated-src/thrift/js')
            }

            //
            // --gen js:node
            //
            'js:node' {
                out file('build/generated-src/thrift/node')
            }
        }
    }

    //
    // optional: add generated sources as a source directory in IDEA
    //
    idea.module.excludeDirs = []
    idea.module.sourceDirs += file('build/generated-src/thrift/main')
    idea.module.jdkName = '1.8'

