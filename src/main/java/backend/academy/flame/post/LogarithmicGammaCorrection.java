package backend.academy.flame.post;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import java.util.Arrays;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogarithmicGammaCorrection implements ImageProcessor {
    private final double gamma;

    @Override
    public void process(FractalImage image) {
        int maxHitCount = Arrays.stream(image.data())
            .max(Comparator.comparingDouble(Pixel::hitCount))
            .orElseThrow()
            .hitCount();

        if (maxHitCount == 0) {
            return;
        }

        double logMax = Math.log10(maxHitCount);

        for (int i = 0; i < image.height(); ++i) {
            for (int j = 0; j < image.width(); ++j) {
                Pixel pixel = image.pixel(j, i);
                if (pixel.hitCount() == 0) {
                    continue;
                }
                pixel.setColor(new Color(
                    logCorrection(pixel.color().r(), pixel.hitCount(), logMax),
                    logCorrection(pixel.color().g(), pixel.hitCount(), logMax),
                    logCorrection(pixel.color().b(), pixel.hitCount(), logMax)
                ));
            }
        }
    }

    private int logCorrection(double color, double hitCount, double logMax) {
        return (int) Math.round(color * Math.pow(Math.log10(hitCount) / logMax, 1 / gamma));
    }
}
