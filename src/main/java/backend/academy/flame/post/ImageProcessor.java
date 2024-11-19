package backend.academy.flame.post;

import backend.academy.flame.model.FractalImage;

@FunctionalInterface
public interface ImageProcessor {
    void process(FractalImage image);
}
