package backend.academy.flame.transform;

import backend.academy.flame.model.Point;

public record WeightedTransform(Transform transform, double weight) implements Transform {
    @Override
    public Point apply(Point point) {
        Point p = transform.apply(point);
        return new Point(weight * p.x(), weight * p.y());
    }
}
