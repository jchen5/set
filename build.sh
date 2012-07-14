#!/bin/sh
set -e
rm -f *.class
javac *.java
jar cfm setGame.jar manifest *.class cards projectiveCards
