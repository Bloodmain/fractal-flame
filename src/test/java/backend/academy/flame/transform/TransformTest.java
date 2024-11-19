package backend.academy.flame.transform;

import backend.academy.flame.model.Point;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;

public class TransformTest {
    @Test
    public void weightedTransform() {
        Transform inner = Mockito.mock(Transform.class);
        doReturn(new Point(40, 40)).when(inner).apply(any(Point.class));
        Transform transform = new WeightedTransform(inner, 0.2);

        Point point = transform.apply(new Point(40, 40));

        assertThat(point).isEqualTo(new Point(8, 8));
    }

    @Test
    public void sumTransform() {
        Transform inner1 = Mockito.mock(Transform.class);
        doReturn(new Point(40, 60)).doReturn(new Point(15, 25))
            .when(inner1).apply(any(Point.class));
        Transform transform1 = new WeightedTransform(inner1, 0.2);
        Transform transform2 = new WeightedTransform(inner1, 0.3);

        Transform inner = Mockito.mock(Transform.class);
        doReturn(new Point(40, 40)).when(inner).apply(any(Point.class));
        Transform sumTransform = new SumTransform(inner, List.of(transform1, transform2));

        Point point = sumTransform.apply(new Point(0, 0));

        assertThat(point).isEqualTo(new Point(12.5, 19.5));
    }

    @Test
    public void affineTransform() {
        Random rnd = Mockito.mock(Random.class);
        doReturn(0.1).doReturn(0.2).doReturn(0.3)
            .doReturn(0.4).doReturn(0.5).doReturn(0.6)
            .when(rnd).nextDouble(anyDouble(), anyDouble());
        Transform affine = Affine.create(rnd);

        Point point = affine.apply(new Point(1, 2));

        assertThat(point.x()).isEqualTo(1);
        assertThat(point.y()).isCloseTo(1.7, offset(1e-6));
    }
}
