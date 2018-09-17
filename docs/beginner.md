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