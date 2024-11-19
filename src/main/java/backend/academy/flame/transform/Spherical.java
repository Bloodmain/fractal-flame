package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Spherical extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double r2 = r * r;
        return new Point(point.x() / r2, point.y() / r2);
    }
}
