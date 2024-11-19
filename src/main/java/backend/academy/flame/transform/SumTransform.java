package backend.academy.flame.transform;

import backend.academy.flame.model.Point;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SumTransform implements Transform {
    private final Transform innerTransform;
    private final List<Transform> transforms;

    @Override
    public Point apply(Point point) {
        Point p1 = innerTransform.apply(point);
        Point res = new Point(0, 0);
        for (Transform t : transforms) {
            Point p2 = t.apply(p1);
            res = new Point(
                res.x() + p2.x(),
                res.y() + p2.y()
            );
        }
        return res;
    }
}
