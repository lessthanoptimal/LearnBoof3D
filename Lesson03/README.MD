# 3D Pose Estimation with Known Markers

BoofCV Demonstrations
* https://www.youtube.com/watch?v=qJWDK_FrgHE&feature=youtu.be
* FiducialTrackerDemoApp

BoofCV Examples
* https://boofcv.org/index.php?title=Tutorial_Fiducials
* https://boofcv.org/index.php?title=Example_Fiducial_Square_Image
* https://boofcv.org/index.php?title=Example_Fiducial_Square_Binary

This lesson is the start exploring the mathematical side of 3D computer vision. To start we will use known
fiducial markers. Because these are known many tasks are greatly simplified. There are simple and reliable methods
for detecting them in the images and then estimating their 3D pose.

After we detect a marker there pose will be estimated using solutions to the Pose-N-Point problem (PNP). PNP estimate
the 6-DOF pose (XYZ and orientation) of a camera by using the known 3D location of features observed in the view. 
There are various sub-problems in PNP, such as P3P and P4P, in which 3 and 4 points are considered. Also different
degenerate geometries which can mess up linear solutions. Tons of paperes on this subjects, but most of the complexity
is hidden from you.

BoofCV contains several solutions [1,2,3] to these different PNP problems.


Required Reading



Algorithms Implemented in BoofCV

[1] Haralick, Robert M. and Lee, Chung-Nan and Ottenberg, Karsten and Nolle, Michael, "Review and analysis of
solutions of the three point perspective pose estimation problem"  Int. J. Comput. Vision, 1994 vol 13, no. 13,
pages 331-356

[2]  Vincent Lepetit, Francesc Moreno-Noguer, and Pascal Fua, "EPnP: An Accurate O(n) Solution to the PnP Problem"
Int. J. Comput. Visionm, vol 81, issue 2, 2009
 
[3] Collins, Toby, and Adrien Bartoli. "Infinitesimal plane-based pose estimation." International journal
of computer vision 109.3 (2014): 252-286.</p>