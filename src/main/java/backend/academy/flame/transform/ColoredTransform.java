package backend.academy.flame.transform;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.Point;

public record ColoredTransform(Transform transform, Color color) implements Transform {
    @Override
    public Point apply(Point point) {
        return transform.apply(point);
    }
}
