Welcome to the BoofCV 3D computer vision tutorial. This will step you through several exercises which will start
very simple and build up in complexity. These lessons take a hands on approach to 3D computer vision and attempt
to replicate how problems are tackled in the real world. Visualization and observation based error metrics are 
emphasized.

# Overview

1) **Lesson 1:** Image Formation Pinhole camera
2) **Lesson 2:** Camera Calibration and Removing Lens Distortion
3) **Lesson 3:** Pose Estimation using PNP and Fiducials/Markers
4) **Lesson 4:** (IN PROGRESS) Stereo Vision: Calibrated Cameras
5) **Lesson 5:** (FUTURE) Removing Perspective Distortion
5) **Lesson 6:** (FUTURE) Image Stitching
6) **Lesson 7:** (FUTURE) Visual Odometry
7) **Lesson 8:** (FUTURE) Projective Reconstruction
8) **Lesson 9:** (FUTURE) Self Calibration
9) **Lesson 10:** (FUTURE) 3-View Scene Reconstruction


# Getting Started

All the code here will run without modification on Linux, Windows and Mac OS X. 
[IntelliJ](https://www.jetbrains.com/idea/) is the preferred IDE and everything will load and run just fine
inside of it.

Quick Start Instructions:

1) Setup your development environment
3) Clone the repo locally on your computer
4) Open in IntelliJ
5) Start Lesson01

If you want to save your work in progress remotely you can fork this project and checkout your fork of it.

Are you a beginner and need help? Checkout the beginner documentation at [docs/beginner.md](docs/beginner.md).

Examples and demonstrations included with BoofCV are referenced throughout. You will also need to checkout
[BoofCV](http://boofcv.org) on [Github](https://github.com/lessthanoptimal/BoofCV) then follow the instructions 
found in the "Quick Start Examples and Demonstrations" in the 
[readme.md](https://github.com/lessthanoptimal/BoofCV/blob/SNAPSHOT/README.md).

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

* Access [Oracle's website](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Read and accept the license
* Download the Java SE Development Kit (JDK) jdk-8u191-windows-i586.exe or jdk-8u191-windows-x64.exe depending on 
your Window's architecture (if you don't know which is, download the first one).
* Install the JDK

### Mac OS X

## Installing IntelliJ

IntelliJ is recommended. All you need to use if the Community edition which is free. Open "LearnBoof3D" inside of it
by clicking File->Open then navigate to where you checked out this project. Select build.gradle. Click OK a couple
of times.

* To run any example with a main function, right click it and select run.

# Reading Material

1) [Multview Geometry in Computer Vision 2nd](http://www.robots.ox.ac.uk/~vgg/hzbook/)
2) [Stanford CS231A Fall 2014](http://cvgl.stanford.edu/teaching/cs231a_winter1415/schedule.html)
