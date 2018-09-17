Welcome to the BoofCV 3D computer vision tutorial. This will step you through several exercises which will start
very simple and build up in complexity. First projects involve introducing you to the math of 3D vision and how the camera
sees the world. At the end you will be creating 3D models using images from an uncalibrated camera.

# Newbie Start

Instructions for beginners to Java development.

## New to Java?

To complete this tutorial you will need to know Java. If you are new to Java

## New to Gradle?

[Gradle](https://gradle.org/) is the build tool used to create projects in this tutorial. If you're a C++ developer you can think of it like
cmake but with the ability to download dependencies automatically. In math intensive projects with lots of dependencies
I actually find that it's much easier to get a Java project built with Gradle up an running faster than anything
in Python or C++. 

* Hello World

# Development Environment

Install the latest version of Java 1.8. In Ubuntu 16.04 the following will install Oracle's JDK. I've had issues with 
the open source JDK missing encryption keys, which is supposed to be fixed in more recent version of Java.
```$bash
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
Java 1.8 is the latest long term support. Java 11 will be out soon and is the new long term support and has several new
language features.

IntelliJ is recommended. All you need to use if the Community edition which is free. Open "LearnBoof3D" inside of it
by clicking File->Open then navigate to where you checked out this project. Select build.gradle. Click OK a couple
of times.

* To run any example with a main function, right click it and select run.

# Reading Material