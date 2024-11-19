package backend.academy.flame.render;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import backend.academy.flame.model.Rect;
import backend.academy.flame.transform.ColoredTransform;
import backend.academy.flame.transform.Diamond;
import backend.academy.flame.transform.Ex;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;

public class RendererTest {
    @Test
    public void correctRender() {
        Random rnd = new Random(42);
        FractalImage image = FractalImage.create(2,2, () -> new Pixel(new Color(0, 0, 0), 0));
        RenderConfig cfg = new RenderConfig(
            image,
            new Rect(-2, -2, 4, 4),
            List.of(
                new ColoredTransform(new Diamond(), new Color(255, 0, 0)),
                new ColoredTransform(new Ex(), new Color(0, 255, 0))
            ),
            5,
            25,
            1
        );
        Renderer renderer = new Renderer(rnd);

        renderer.render(cfg);

        assertThat(image.data()).usingRecursiveComparison().isEqualTo(new Pixel[] {
            new Pixel(new Color(255,0,0), 2),
            new Pixel(new Color(198, 56, 0), 10),
            new Pixel(new Color(0, 255, 0), 4),
            new Pixel(new Color(255, 0, 0), 4)
        });
    }

    @Test
    public void symmetry() {
        Random rnd = new Random(42);
        FractalImage image = FractalImage.create(2,2, () -> new Pixel(new Color(0, 0, 0), 0));
        RenderConfig cfg = new RenderConfig(
            image,
            new Rect(-2, -2, 4, 4),
            List.of(
                new ColoredTransform(new Diamond(), new Color(255, 0, 0)),
                new ColoredTransform(new Ex(), new Color(0, 255, 0))
            ),
            5,
            25,
            3
        );
        Renderer renderer = new Renderer(rnd);

        renderer.render(cfg);

        assertThat(image.data()).usingRecursiveComparison().isEqualTo(new Pixel[] {
            new Pixel(new Color(169,85,0), 20),
            new Pixel(new Color(232, 22, 0), 14),
            new Pixel(new Color(7, 247, 0), 14),
            new Pixel(new Color(173,81,0), 12)
        });
    }
}
