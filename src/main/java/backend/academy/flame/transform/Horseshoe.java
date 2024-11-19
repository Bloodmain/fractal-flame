package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Horseshoe extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        return new Point(
            (point.x() - point.y()) * (point.x() + point.y()) / r,
            2 * point.x() * point.y() / r
        );
    }
}
