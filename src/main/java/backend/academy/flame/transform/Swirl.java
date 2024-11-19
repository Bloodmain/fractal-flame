package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Swirl extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double r2 = r * r;
        return new Point(
            point.x() * Math.sin(r2) - point.y() * Math.cos(r2),
            point.x() * Math.cos(r2) + point.y() * Math.sin(r2)
        );
    }
}
