package backend.academy.flame.post;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LogarithmicGammaCorrectionTest {
    @Test
    public void zeroPixels() {
        FractalImage image = FractalImage.create(100, 100, () -> new Pixel(new Color(0, 0, 0), 0));
        ImageProcessor processor = new LogarithmicGammaCorrection(2);

        processor.process(image);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                assertThat(image.pixel(j, i).color()).isEqualTo(new Color(0, 0, 0));
            }
        }
    }

    @Test
    public void correctness() {
        Pixel[] data = new Pixel[] {
            new Pixel(new Color(100, 100, 100), 10),
            new Pixel(new Color(200, 200, 200), 100),
            new Pixel(new Color(50, 50, 50), 0),
            new Pixel(new Color(10, 10, 10), 1000)
        };
        FractalImage image = new FractalImage(data, 2, 2);
        ImageProcessor processor = new LogarithmicGammaCorrection(0.25);

        processor.process(image);

        assertThat(image.data()).usingRecursiveComparison().isEqualTo(new Pixel[] {
            new Pixel(new Color(1, 1, 1), 10),
            new Pixel(new Color(40, 40, 40), 100),
            new Pixel(new Color(50, 50, 50), 0),
            new Pixel(new Color(10, 10, 10), 1000)
        });
    }
}
