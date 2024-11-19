package backend.academy.flame.util;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.Pixel;
import backend.academy.flame.model.Rect;
import backend.academy.flame.model.ThreadSafePixel;
import backend.academy.flame.render.RenderConfig;
import backend.academy.flame.render.Renderer;
import backend.academy.flame.transform.ColoredTransform;
import backend.academy.flame.transform.Sinusoidal;
import backend.academy.flame.transform.Spherical;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ParallelTest {
    @Test
    public void ensureThreadSafePixel() {
        List<ColoredTransform> transforms = List.of(
            new ColoredTransform(new Spherical(), new Color(125, 125, 125)),
            new ColoredTransform(new Sinusoidal(), new Color(255, 1, 147))
        );
        RenderConfig cfg = new RenderConfig(
            RendererRunner.createCanvas(2, 1920, 1080),
            new Rect(-2, -2, 4, 4),
            transforms, 1000000, 100, 1
        );

        assertThat(cfg.canvas().pixel(0, 0)).isInstanceOf(ThreadSafePixel.class);
    }

    @Test
    public void ensureNonThreadSafePixelWithOneThread() {
        List<ColoredTransform> transforms = List.of(
            new ColoredTransform(new Spherical(), new Color(125, 125, 125)),
            new ColoredTransform(new Sinusoidal(), new Color(255, 1, 147))
        );
        RenderConfig cfg = new RenderConfig(
            RendererRunner.createCanvas(1, 1920, 1080),
            new Rect(-2, -2, 4, 4),
            transforms, 1000000, 100, 1
        );

        assertThat(cfg.canvas().pixel(0, 0)).isExactlyInstanceOf(Pixel.class);
    }

    @Test
    public void ensureSampleDivisionOnThreads() {
        List<ColoredTransform> transforms = List.of(
            new ColoredTransform(new Spherical(), new Color(125, 125, 125)),
            new ColoredTransform(new Sinusoidal(), new Color(255, 1, 147))
        );
        int threads = 7;
        RenderConfig cfg = new RenderConfig(
            RendererRunner.createCanvas(threads, 1920, 1080),
            new Rect(-2, -2, 4, 4),
            transforms, 998, 100, 1
        );
        Renderer renderer = Mockito.mock(Renderer.class);

        RendererRunner.run(threads, renderer, cfg);

        verify(renderer, times(4)).render(cfg.withSamplesNum(143));
        verify(renderer, times(3)).render(cfg.withSamplesNum(142));
    }

    @Test
    public void parallelPerformance() {
        Random rnd = new Random(24);
        Renderer renderer = new Renderer(rnd);
        List<ColoredTransform> transforms = List.of(
            new ColoredTransform(new Spherical(), new Color(125, 125, 125)),
            new ColoredTransform(new Sinusoidal(), new Color(255, 1, 147))
        );

        long seqEstimatedTime = measureTime(1, renderer, transforms);
        long parEstimatedTime = measureTime(8, renderer, transforms);

        assertThat(parEstimatedTime).isLessThan(seqEstimatedTime);
    }

    private long measureTime(int threads, Renderer renderer, List<ColoredTransform> transforms) {
        long parStartTime = System.nanoTime();
        RenderConfig cfg = new RenderConfig(
            RendererRunner.createCanvas(threads, 1920, 1080),
            new Rect(-2, -2, 4, 4),
            transforms, 1000000, 100, 1
        );
        RendererRunner.run(threads, renderer, cfg);
        return System.nanoTime() - parStartTime;
    }
}
