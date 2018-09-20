Let's calibrate a webcam! Assuming you have a webcam. Your cellphone works just fine too. No cell phone
with a camera? How about a point and shoot camera? Yeah I stopped buying those years ago too...

Besides obtaining a camera, you now need to print a calibration target. Everyone knows about chessboard
patterns. They are everywhere. So let's not use those. Instead we will use a square grid pattern.

Square grid patterns have an advantage being able to detect more features in the same area than
a chessboard and being able to go to the very edge of the image easily. The first chessboard detectors
didn't let you go to the edge of the image, but most of the uses in use these days too. A disadvantage
of square grid patterns is that its harder to get precise corners and over exposure, poor focus, and 
other adverse conditions will affect it more than a chessboard. Chessboard patterns have symmetry
and which can partially compensate for the problems just mentioned.

Circle patterns are another option that some people like. I've experimented with them and I don't
recommend their use. They don't work well under significant lens distortion because lens distortion
breaks the invariants that they rely on. Also the relative size of a circle affects your calibration.
The smaller a circle is the less of an error is induced by lens distortion.

Back to that application you created before.

```bash
java -jar applications/applications.jar CreateCalibrationTarget --GUI
```

Select SQUARE_GRID under "Target Type". Default settings work just fine. Print the document. Apply it
to a flat surface that doesn't warp easily.

You have a few options for collecting the images. If it's a webcam you can use the assisted calibration
app. See BoofCV's website for instructions on launching that. You can collect the images and
save them to disk, then use the calibration application from before to load and process the images.
You could even write code to load and process the images. There's an example for doing just that
in BoofCV's example directory.

Just after calibrating your camera look at the output from the application. The residual error should be
at most 0.6 pixels. You should also double check the images and remove any images that
are blurred or out of focus.

If you are happy with the results, save the images and save the generated yaml file. You will
need it later on.

As a side comment, you can save calibration file in a format OpenCV understands if you wish
to use this tool but also want to work with OpenCV. See command line help for that.
