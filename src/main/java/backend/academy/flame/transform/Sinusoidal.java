package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public class Sinusoidal extends Variation {
    @Override
    public Point apply(Point point) {
        return new Point(Math.sin(point.x()), Math.sin(point.y()));
    }
}
