/**
 * TaskImplementer.java
 * Created on Jul 17, 2009
 */

package sim.tasks;

import java.awt.geom.Point2D;
import scio.coordinate.utils.PlanarMathUtils;

/**
 * <p>
 *   <code>TaskImplementer</code> takes in a task and returns a proposal for a control
 *   variable setting.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class Router {


    /**
     * Implements a task by returning an object representing the appropriate control variable.
     * @param task a task to implement
     * @return direction of travel
     */
    abstract public Point2D.Double getDirectionFor(Task task);



    //============================================//
    //                                            //
    //               FACTORY METHODS              //
    //                                            //
    //============================================//

    /** An enum encoding possible implementer values */
    public enum RouterEnum { STRAIGHT, LEADING, PLUCKER_LEADING, CONSTANT_BEARING, NOISY };

    /** Factory method to retrieve an instance of the router according to a given enum value. */
    public static Router getInstance(RouterEnum value) {
        switch (value) {
            case LEADING: return new Leading();
            case PLUCKER_LEADING: return new PluckerLeading();
            case CONSTANT_BEARING: return new ConstantBearing();
            case NOISY: return new Noisy(DEFAULT_INSTANCE);
            default: return DEFAULT_INSTANCE;
        }
    }



    //============================================//
    //                                            //
    //               INNER CLASSES                //
    //                                            //
    //============================================//

    private static Point2D.Double unitVectorTo(Point2D.Double owner, Point2D.Double target, double m) {
        double x = m * (target.x - owner.x);
        double y = m * (target.y - owner.y);
        double magn = Math.sqrt(x*x + y*y);
        return new Point2D.Double(x / magn, y / magn);
    }

    /** Routes owner straight to target */
    public static final Router DEFAULT_INSTANCE = new Router() {
        @Override public String toString() { return "Router - Straight"; }
        public Point2D.Double getDirectionFor(Task task) {
            return unitVectorTo(task.ownerLoc, task.targetLoc, task.type.multiplier);
        }
    };

    /**
     * In this algorithm, the capture point is assumed to be where the pursuer would first be able to overtake the evader if the evader kept heading
     *      in its current direction... this is where the ratio of speeds is equal to the ratio of distances to this hypothetical point.
     *      The evader's distance to this point is dE, and the pursuer heads toward the evader's position plus its heading times dE*leadFactor.
     *      If the pursuer is not moving faster, this hypothetical point will never be reached, and the pursuer just heads straight to the evader.
     */
    public static class Leading extends Router {
        double leadFactor = 1;
        public Leading() { }
        public Leading(double lf) { setLeadFactor(lf); }
        public double getLeadFactor() { return leadFactor; }
        public void setLeadFactor(double leadFactor) { this.leadFactor = leadFactor; }
        @Override public String toString() { return String.format("Router - Leading [%.2f]", this.leadFactor); }

        public Point2D.Double getDirectionFor(Task task) {
            Point2D.Double loc = task.ownerLoc;
            Point2D.Double tLoc = task.targetLoc;
            Point2D.Double tVel = task.target.getVelocity();
            double tSpeed = tVel == null ? 0 : tVel.distance(0, 0);
            Point2D.Double diff = new Point2D.Double(tLoc.x - loc.x, tLoc.y - loc.y);

            if (tSpeed == 0 || task.owner.par.topSpeed < tSpeed * 1.0001)
                return PlanarMathUtils.normalize(diff);

            // In this algorithm, the capture point is assumed to be where the pursuer would first be able to overtake the evader if the evader kept heading
            // in its current direction... this is where the ratio of speeds is equal to the ratio of distances to this hypothetical point.
            // The evader's distance to this point is dE, and the pursuer heads toward the evader's position plus its heading times dE*leadFactor.
            double mu = task.owner.par.topSpeed / tSpeed;                                     // ratio of speeds is used several times in the formula
            double dcosth = (tVel.x * diff.x + tVel.y * diff.y) / tSpeed;   // basic dot product formula for cosine... provides ||diff|| * cos of angle between diff and tVel
            double dE = (Math.abs(dcosth) - Math.sqrt(dcosth * dcosth - (1 - mu * mu) * diff.distanceSq(0,0))) / (1 - mu * mu);

            return unitVectorTo(loc,
                    new Point2D.Double(
                    tLoc.x + tVel.x * leadFactor * dE / tSpeed,
                    tLoc.y + tVel.y * leadFactor * dE / tSpeed),
                    task.type.multiplier);
        }
    }

    /**
     * In this algorithm, the capture point is assumed to be where the pursuer would overtake the evader the evader headed straight away
     *   from him...
     * The evader's distance to this point is dE, and the pursuer heads toward the evader's position plus its heading times dE*leadFactor.
     *      If the pursuer is not moving faster, this hypothetical point will never be reached, and the pursuer just heads straight to the evader.
     */
    public static class PluckerLeading extends Router {
        double leadFactor = 1;
        public PluckerLeading() { }
        public double getLeadFactor() { return leadFactor; }
        public void setLeadFactor(double leadFactor) { this.leadFactor = leadFactor; }
        @Override public String toString() { return String.format("Router - Plucker Leading [%.2f]", this.leadFactor); }

        public Point2D.Double getDirectionFor(Task task) {
            Point2D.Double loc = task.ownerLoc;
            Point2D.Double tLoc = task.targetLoc;
            Point2D.Double tVel = task.target.getVelocity();
            double tSpeed = tVel == null ? 0 : tVel.distance(0, 0);
            Point2D.Double diff = new Point2D.Double(tLoc.x - loc.x, tLoc.y - loc.y);

            if (tSpeed == 0 || task.owner.par.topSpeed < tSpeed * 1.0001)
                return PlanarMathUtils.normalize(diff);

            double dE = diff.distance(0, 0) * task.owner.par.topSpeed / tSpeed;
            return unitVectorTo(loc, new Point2D.Double(
                    tLoc.x + tVel.x*leadFactor*dE/tSpeed,
                    tLoc.y + tVel.y*leadFactor*dE/tSpeed),
                    task.type.multiplier);
        }
    }

    /**
     * In this algorithm, the capture point is assumed to be where the pursuer would overtake the evader the evader headed straight away
     *   from him...
     * The evader's distance to this point is dE, and the pursuer heads toward the evader's position plus its heading times dE*leadFactor.
     *      If the pursuer is not moving faster, this hypothetical point will never be reached, and the pursuer just heads straight to the evader.
     */
    public static class ConstantBearing extends Router {
        double angle = Math.PI/6;
        public ConstantBearing() { }
        public double getAngle() { return angle; }
        public void setAngle(double angle) { this.angle = Math.abs(angle); }
        @Override public String toString() { return String.format("Router - Constant Bearing [%.2f]", angle); }

        public Point2D.Double getDirectionFor(Task task) {
            Point2D.Double loc = task.ownerLoc;
            Point2D.Double tLoc = task.targetLoc;
            Point2D.Double diff = PlanarMathUtils.normalize(new Point2D.Double(tLoc.x - loc.x, tLoc.y - loc.y));
            if (PlanarMathUtils.crossProduct(diff, task.target.getVelocity()) > 0)
                return PlanarMathUtils.rotate(diff, angle);
            else
                return PlanarMathUtils.rotate(diff, -angle);
        }
    }

    /** RouterNoisy wraps another router, returning a direction w/ error */
    public static class Noisy extends Router {
        double maxAngleError = 0.1;
        Router router;
        public Noisy(Router router) { this.router = router; }
        public Noisy(Router router, double angleError) { this.router = router; this.maxAngleError = angleError; }
        /** @return the underlying implementer used to determine direction */
        public Router getRouter() { return router; }
        public void setRouter(Router implementer) { this.router = implementer; }
        /** @return the maximum amount of error made in translating the implemented heading */
        public double getMaxAngleError() { return maxAngleError; }
        public void setMaxAngleError(double percentError) { this.maxAngleError = percentError; }
        @Override public String toString() { return String.format("Router - " + router + " [error = %.2f]", maxAngleError); }
        
        public Point2D.Double getDirectionFor(Task task) {
            Point2D.Double result = router.getDirectionFor(task);
            return PlanarMathUtils.rotate(result, maxAngleError*(Math.random()*2-1));
        }
    }

}
