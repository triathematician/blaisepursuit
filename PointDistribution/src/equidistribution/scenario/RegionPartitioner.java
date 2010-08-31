/*
 * RegionPartitioner.java
 * Created Aug 18, 2010
 */

package equidistribution.scenario;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bm.blaise.scio.algorithm.PolygonIntersectionUtils;
import org.bm.blaise.scio.algorithm.Tesselation.Polygon;
import org.bm.blaise.scio.algorithm.voronoi.VoronoiFrontier;

/**
 * Provides an algorithm that divides a region into subregions based on the positions of points.
 * Also tracks the neighbor structure of the regions.
 * 
 * @author Elisha Peterson
 */
public final class RegionPartitioner {

    /**
     * Computes an assignment of entities on a team to polygonal areas in a specified region.
     * @param border the region border
     * @param t the team
     * @param paritition pointer to map that will store subregions assigned to agents
     * @param network pointer to map that will store network structure of the subregion (i.e. region agencies)
     * @param boundaryAgents pointer to set that will store the agents along the border of the region
     */
    public void networkPartition(Point2D.Double[] border, List<EquiTeam.Agent> agents,
            LinkedHashMap<EquiTeam.Agent, Point2D.Double[]> partition,
            Map<EquiTeam.Agent, Set> network,
            Set<EquiTeam.Agent> borderAgents) {

        VoronoiFrontier v = new VoronoiFrontier(agents);

        partition.clear();
        network.clear();
        borderAgents.clear();

        Map<Point2D.Double,Polygon> polyMap = v.getPolygonMap();
        
        // important here to keep agents in same order
        for (EquiTeam.Agent a : agents) {
            Point2D.Double[] poly = polyMap.get(a).getVertices();
            Point2D.Double[] clipped = PolygonIntersectionUtils.intersectionOfConvexPolygons(border, poly);
            partition.put(a, clipped);
            if (poly != clipped)
                borderAgents.add(a);
        }

        for (Entry<Point2D.Double, Set<Point2D.Double>> en : v.getAdjacencyMap().entrySet())
            network.put((EquiTeam.Agent) en.getKey(), en.getValue());

    }

}
