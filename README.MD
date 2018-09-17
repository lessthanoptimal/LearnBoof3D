Welcome to the BoofCV 3D computer vision tutorial. This will step you through several exercises which will start
very simple and build up in complexity. Each lession state required reading will assume that you have read through 
the reading list and are familiar with the math. 

Reading Material:
1) [Multview Geometry in Computer Vision 2nd](http://www.robots.ox.ac.uk/~vgg/hzbook/)
2) [Stanford CS231A Fall 2014](http://cvgl.stanford.edu/teaching/cs231a_winter1415/schedule.html)

All the code here will run without modification on Linux, Windows and Mac OS X. 
[IntelliJ](https://www.jetbrains.com/idea/) is the preferred IDE and everything will load and run just fine
inside of it.

Quick Start Instructions:

1) Setup your development environment
3) Clone the repo locall on your computer
4) Open in IntelliJ
5) Start Lesson01

If you want to save your work in progress remotely you can fork this project and checkout your fork of it.




# Newbie Start

This section is intended for beginners who aren't familiar all the tools and work flow. It can be skipped.

## New to GIT?

Git is a version control system and is used to keep track of your work in progress.

* [Tutorial](http://rogerdudler.github.io/git-guide/)

## New to Java?

To complete this tutorial you will need to know Java. If you are new to Java here are some resources.

* [Hello World]()

## New to Gradle?

[Gradle](https://gradle.org/) is the build tool used to create projects in this tutorial. If you're a C++ developer you can think of it like
cmake but with the ability to download dependencies automatically. In math intensive projects with lots of dependencies
I actually find that it's much easier to get a Java project built with Gradle up an running faster than anything
in Python or C++. 

You do NOT need to install Gradle. The Gradle wrapper is used in this project it will download the exact
version of Gradle that you need in the background the first time you build this source code.

* [Quick Start](https://docs.gradle.org/current/userguide/tutorial_java_projects.html)
* [Java Application](https://guides.gradle.org/building-java-applications/)

When looking at these tutorials, keep in mind that you should be using the 
[Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) for this project.
What that means is use ./gradlew instead of the gradle command.

# Development Environment

Summary:
* Install Git
* Install Java 8
* Install IntelliJ Community Edition


## Installing GIT

Atlasian has great [instructions](https://www.atlassian.com/git/tutorials/install-git) on their website.

## Installing Java 8

Install the latest version of Java 1.8. In Ubuntu 16.04 the following will install Oracle's JDK. I've had issues with 
the open source JDK missing encryption keys, which is supposed to be fixed in more recent version of Java. Java 1.8 
is the latest long term support. Java 11 will be out soon and is the new long term support and has several new
language features.

### Linux
```$bash
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```
### Windows

### Mac OS X

## Installing IntelliJ

IntelliJ is recommended. All you need to use if the Community edition which is free. Open "LearnBoof3D" inside of it
by clicking File->Open then navigate to where you checked out this project. Select build.gradle. Click OK a couple
of times.

* To run any example with a main function, right click it and select run.

# Reading Material