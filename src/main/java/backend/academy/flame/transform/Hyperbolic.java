package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Hyperbolic extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double theta = theta(point);
        return new Point(Math.sin(theta) / r, r * Math.cos(theta));
    }
}
