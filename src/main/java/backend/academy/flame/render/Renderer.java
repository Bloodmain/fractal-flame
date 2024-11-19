package backend.academy.flame.render;

import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import backend.academy.flame.model.Point;
import backend.academy.flame.model.Rect;
import backend.academy.flame.transform.ColoredTransform;
import java.util.Random;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Renderer {
    private final static int NON_PAINT_ITERATIONS = 20;
    private final Random random;

    public FractalImage render(RenderConfig cfg) {
        FractalImage canvas = cfg.canvas();
        Rect world = cfg.world();

        for (int num = 0; num < cfg.samplesNum(); ++num) {
            Point point = new Point(
                random.nextDouble(world.x(), world.xRight()),
                random.nextDouble(world.y(), world.yTop())
            );

            for (int it = 0; it < cfg.iterPerSample(); ++it) {
                int transformIndex = random.nextInt(cfg.transforms().size());
                ColoredTransform transform = cfg.transforms().get(transformIndex);
                Point transformedPoint = transform.transform().apply(point);

                double theta = 0;
                double angle = Math.PI * 2 / cfg.symmetry();
                for (int s = 0; s < cfg.symmetry(); theta += angle, ++s) {
                    Point rotated = rotateAroundRectCenter(transformedPoint, world, theta);

                    if (it > NON_PAINT_ITERATIONS && world.contains(rotated)) {
                        Point realPoint = translateCoordinates(canvas, world, rotated);
                        int realX = (int) realPoint.x();
                        int realY = (int) realPoint.y();

                        if (canvas.contains(realX, realY)) {
                            Pixel pixel = canvas.pixel(realX, realY);
                            pixel.mixColor(transform.color());
                            pixel.incHitCount();
                        }
                    }
                }

            }
        }

        return canvas;
    }

    private Point rotateAroundRectCenter(Point point, Rect world, double theta) {
        Point center = new Point(world.x() + world.width() / 2, world.y() + world.height() / 2);
        double dx = point.x() - center.x();
        double dy = point.y() - center.y();
        return new Point(
            dx * Math.cos(theta) - dy * Math.sin(theta) + center.x(),
            dx * Math.sin(theta) + dy * Math.cos(theta) + center.y()
        );
    }

    private Point translateCoordinates(
        FractalImage canvas,
        Rect world,
        Point point
    ) {
        int x = (int) ((point.x() - world.x()) / world.width() * canvas.width());
        int y = (int) ((point.y() - world.y()) / world.height() * canvas.height());
        return new Point(x, y);
    }
}
