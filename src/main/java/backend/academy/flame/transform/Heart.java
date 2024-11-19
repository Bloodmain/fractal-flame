package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Heart extends Variation {
    @Override
    public Point apply(Point point) {
        double r = r(point);
        double theta = theta(point);
        return new Point(r * Math.sin(theta * r), -r * Math.cos(theta * r));
    }
}
