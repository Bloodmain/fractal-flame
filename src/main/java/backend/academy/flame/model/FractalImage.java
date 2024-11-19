package backend.academy.flame.model;

import java.util.Arrays;
import java.util.function.Supplier;

public record FractalImage(Pixel[] data, int width, int height) {
    public static FractalImage create(int width, int height, Supplier<? extends Pixel> pixelCtor) {
        Pixel[] pixels = new Pixel[width * height];
        Arrays.setAll(pixels, i -> pixelCtor.get());
        return new FractalImage(pixels, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        return data[flatCoords(x, y)];
    }

    private int flatCoords(int x, int y) {
        return y * width + x;
    }
}
