package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public abstract class Variation implements Transform {
    protected double r(Point point) {
        return Math.sqrt(point.x() * point.x() + point.y() * point.y());
    }

    protected double theta(Point point) {
        return Math.atan2(point.x(), point.y());
    }
}
