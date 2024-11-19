package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Diamond extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double theta = theta(point);
        return new Point(Math.sin(theta) * Math.cos(r), Math.cos(theta) * Math.sin(r));
    }
}
