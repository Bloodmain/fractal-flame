package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Bent extends Variation {
    @Override
    public Point apply(Point point) {
        double newX = point.x() >= 0 ? point.x() : 2 * point.x();
        double newY = point.y() >= 0 ? point.y() : point.y() / 2;
        return new Point(newX, newY);
    }
}
