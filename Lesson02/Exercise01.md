We will learn how to calibrate a camera using a previously developed tool. Instead of using
real camera images we will create simulated ones with known lens distortion and see how well
those parameters are recovered.

Did you checkout BoofCV's source code? You will need to do so now to complete this lesson.

1) lesson02/Exercise01.java
    * Note the camera parameters
    * Read the JavaDoc and understand the scenes its creating
    * Run this exercise
    * Look at the images it generated 
2) Switch to BoofCV's source code then build and run the applications.jar
    1) cd boofcv
    2) ./gradlew applicationsJar
    3) java -jar applications/applications.jar CameraCalibration --GUI
3) A window should have oppened up
4) Make sure the correct calibration target is selected
5) Remove tangential distortion
6) File->Open Directory
7) Navigate to LearnBoof3D and open the calibration/good directory containing the simulated images

After it process the directory there will be a GUI which let's you view the results. Did it do a good
job recovering the original parameters? Play with the options on the right. Undistort the image.

A good set of calibration images will have features that uniformly cover the entire image all
the way up the the edge. You also want a nice distribution along all 3 axises of different
orientations. Not too extreme of an angle since the feature detector returns worse results
when you do that.

Let's see what happens if you ignore this advice. I wasn't actually sure what the results would
be since I never attempted to calibrate what should be a pathological case before! Compare
the results between the good and bad image sets.

For more information on calibration go here

https://boofcv.org/index.php?title=Tutorial_Camera_Calibration

You also might want to watch the video below where I demonstrate an assisted calibration and discuss
how to properly calibrate a camera. That assisted calibration mode is actually available with the
application you just built.

```bash
java -jar applications/applications.jar CameraCalibration
```


Additional Reading:
Zhang, Zhengyou. "Flexible camera calibration by viewing a plane from unknown orientations." ICCV 1999