package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Fisheye extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        return new Point(2 * point.y() / (r + 1), 2 * point.x() / (r + 1));
    }
}
