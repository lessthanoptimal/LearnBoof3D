package lesson03;

import boofcv.simulation.SimulatePlanarWorld;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.image.GrayF32;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.se.SpecialEuclideanOps_F64;
import georegression.struct.so.Rodrigues_F64;

import static org.boofcv.GenerateSimulatedMarkers.renderSquare;

/**
 * @author Peter Abeles
 */
public class BoilderPlate03 {



    // Don't look at this code




































































































    /**
     * Hey I intentionally put the code here so that you wouldn't look and cheat!
     */
    public static GrayF32 renderTwoMarkers(double markerLength, CameraPinholeRadial pinhole) {
        Se3_F64 a_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.5,Math.PI,0,-0.2,0,0.2,null);
        Se3_F64 b_to_world = SpecialEuclideanOps_F64.eulerXYZ(0,Math.PI,0.3,0.3,0,0.25,null);

        // The white border that was added to the marker needs to be taken in account
        SimulatePlanarWorld sim = new SimulatePlanarWorld();
        sim.setCamera(pinhole);
        sim.addSurface(a_to_world,markerLength,renderSquare("dog",0.25,200));
        sim.addSurface(b_to_world,markerLength,renderSquare("h2o",0.25,200));
        sim.setBackground(125);

        Se3_F64 world_to_Camera = SpecialEuclideanOps_F64.eulerXYZ(0.1,-0.05,0.2,0,0,0.1,null);
        sim.setWorldToCamera(world_to_Camera);

        sim.render();
        return sim.getOutput();
    }

    public static GrayF32 renderViewInSequence( double markerLength , CameraPinholeRadial pinhole , int frame )
    {
        Se3_F64 a_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.3,Math.PI-0.3,0,-0.3,0,0.05,null);
        Se3_F64 b_to_world = SpecialEuclideanOps_F64.eulerXYZ(0,Math.PI,0.3,0.5,0,0,null);
        Se3_F64 c_to_world = SpecialEuclideanOps_F64.eulerXYZ(0.1,Math.PI,0.0,1.1,0,0.28,null);

        // The white border that was added to the marker needs to be taken in account
        SimulatePlanarWorld sim = new SimulatePlanarWorld();
        sim.setCamera(pinhole);
        sim.addSurface(a_to_world,markerLength,renderSquare("dog",0.25,200));
        sim.addSurface(b_to_world,markerLength,renderSquare("h2o",0.25,200));
        sim.addSurface(c_to_world,markerLength,renderSquare("ke",0.25,200));
        sim.setBackground(125);

        Se3_F64 world_to_camera = computeWorldToCamera(frame);

        sim.setWorldToCamera(world_to_camera);

        sim.render();
        return sim.getOutput();
    }

    private static Se3_F64 computeWorldToCamera(int frame) {
        // move around the world's center
        Se3_F64 world_to_a = SpecialEuclideanOps_F64.eulerXYZ(0,frame*0.005,0.15*Math.cos(frame*0.1),
                0,0,0,null);
        // move away from origin
        Se3_F64 a_to_b = SpecialEuclideanOps_F64.eulerXYZ(0,0,0,0.05 + -0.012*frame,0,0.4+0.002*frame,null);

        return world_to_a.concat(a_to_b,null);
    }

    public static void checkSolution( Se3_F64 found_0_to_frame , int frame ) {
        Se3_F64 world_to_camera0 = computeWorldToCamera(0);
        Se3_F64 world_to_camera1 = computeWorldToCamera(frame);

        Se3_F64 expected = world_to_camera0.invert(null).concat(world_to_camera1,null);

        Se3_F64 difference = expected.invert(null).concat(found_0_to_frame,null);

        boolean good = true;
        if( difference.T.norm() > 0.05 )
            good = false;
        Rodrigues_F64 rod = ConvertRotation3D_F64.matrixToRodrigues(difference.R,null);
        if( Math.abs(rod.theta) > 0.05 )
            good = false;
        if( good )
            System.out.println("within error tolerance");
        else
            System.out.println("bad solution");
    }
}
