package backend.academy.flame.util;

import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageUtils {
    public backend.academy.flame.model.Color decodeHex(String hex) {
        Color color = Color.decode(hex);
        return new backend.academy.flame.model.Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void save(FractalImage image, Path filename, String format) throws IOException {
        BufferedImage toSave = toImage(image);

        try (OutputStream out = Files.newOutputStream(filename)) {
            ImageIO.write(toSave, format, out);
        }
    }

    private BufferedImage toImage(FractalImage image) {
        BufferedImage res = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);

        int[] encodedData = Arrays.stream(image.data())
            .mapToInt(ImageUtils::encodePixel)
            .toArray();

        res.setRGB(0, 0, image.width(), image.height(), encodedData, 0, image.width());
        return res;
    }

    private int encodePixel(Pixel pixel) {
        return new Color(pixel.color().r(), pixel.color().g(), pixel.color().b()).getRGB();
    }
}
