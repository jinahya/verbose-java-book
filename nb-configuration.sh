#!/bin/sh
find src/main/asciidoc/codes -mindepth 2 -name pom.xml -execdir pwd \; -execdir cp -v ../../../../../nb-configuration.xml . \;
cp ./nb-configuration.xml ../verbose-java-benchmarks/
