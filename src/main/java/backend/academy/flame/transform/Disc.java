package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Disc extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double theta = theta(point);
        return new Point(theta * Math.sin(Math.PI * r) / Math.PI, theta * Math.cos(Math.PI * r) / Math.PI);
    }
}
