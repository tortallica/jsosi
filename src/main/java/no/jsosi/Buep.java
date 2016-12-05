package no.jsosi;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class Buep {

    public static Geometry create(GeometryFactory gf, Coordinate[] coordinates) {
        if (coordinates.length != 3) {
            throw new IllegalArgumentException("BUEP should have 3 coordinates, not " + coordinates.length);
        }

        Coordinate c0 = coordinates[0];
        Coordinate c1 = coordinates[1];
        Coordinate c2 = coordinates[2];

        Coordinate center = circleCenter(c0, c1, c2);
        double radius = center.distance(c0);

        double a0 = Angle.angle(center, c0);
        double a1 = Angle.angle(center, c2);
        if (a1 < a0) {
            a1 = a1 + Math.PI + Math.PI;
        }

        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(center);
        gsf.setSize(radius * 2.0);
        return gsf.createArc(a1, a1 - a0);
    }

    static Coordinate circleCenter(Coordinate A, Coordinate B, Coordinate C) {

        // http://stackoverflow.com/questions/4103405/what-is-the-algorithm-for-finding-the-center-of-a-circle-from-three-points
        double yDelta_a = B.y - A.y;
        double xDelta_a = B.x - A.x;
        double yDelta_b = C.y - B.y;
        double xDelta_b = C.x - B.x;
        Coordinate center = new Coordinate(0, 0);

        double aSlope = yDelta_a / xDelta_a;
        double bSlope = yDelta_b / xDelta_b;
        center.x = (aSlope * bSlope * (A.y - C.y) + bSlope * (A.x + B.x) - aSlope * (B.x + C.x))
                / (2 * (bSlope - aSlope));
        center.y = -1 * (center.x - (A.x + B.x) / 2) / aSlope + (A.y + B.y) / 2;

        return center;
    }

}