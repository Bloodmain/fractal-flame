package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Ex extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double theta = theta(point);
        double p0 = Math.sin(theta + r);
        double p1 = Math.cos(theta - r);
        double p03 = p0 * p0 * p0;
        double p13 = p1 * p1 * p1;
        return new Point(r * (p03 + p13), r * (p03 - p13));
    }
}
