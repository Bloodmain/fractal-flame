package backend.academy.flame;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Rect;
import backend.academy.flame.post.ImageProcessor;
import backend.academy.flame.post.LogarithmicGammaCorrection;
import backend.academy.flame.render.RenderConfig;
import backend.academy.flame.render.Renderer;
import backend.academy.flame.transform.Affine;
import backend.academy.flame.transform.Bent;
import backend.academy.flame.transform.ColoredTransform;
import backend.academy.flame.transform.Diamond;
import backend.academy.flame.transform.Disc;
import backend.academy.flame.transform.Ex;
import backend.academy.flame.transform.Fisheye;
import backend.academy.flame.transform.Handkerchief;
import backend.academy.flame.transform.Heart;
import backend.academy.flame.transform.Horseshoe;
import backend.academy.flame.transform.Hyperbolic;
import backend.academy.flame.transform.Sinusoidal;
import backend.academy.flame.transform.Spherical;
import backend.academy.flame.transform.Spiral;
import backend.academy.flame.transform.SumTransform;
import backend.academy.flame.transform.Swirl;
import backend.academy.flame.transform.Transform;
import backend.academy.flame.transform.WeightedTransform;
import backend.academy.flame.util.ImageUtils;
import backend.academy.flame.util.RendererRunner;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    private static final Map<String, Transform> TRANSFORMS_NAME = Map.ofEntries(
        Map.entry("Bent", new Bent()),
        Map.entry("Diamond", new Diamond()),
        Map.entry("Disc", new Disc()),
        Map.entry("Ex", new Ex()),
        Map.entry("Fisheye", new Fisheye()),
        Map.entry("Handkerchief", new Handkerchief()),
        Map.entry("Heart", new Heart()),
        Map.entry("Horseshoe", new Horseshoe()),
        Map.entry("Hyperbolic", new Hyperbolic()),
        Map.entry("Sinusoidal", new Sinusoidal()),
        Map.entry("Spherical", new Spherical()),
        Map.entry("Spiral", new Spiral()),
        Map.entry("Swirl", new Swirl())
    );

    @SuppressWarnings("ReturnCount")
    @SuppressFBWarnings(
        value = {"PATH_TRAVERSAL_IN", "PREDICTABLE_RANDOM"},
        justification =
            "1) config path should be specified by user"
                + "2) random should be predictable to be able to configure seed"
    )
    public static void main(String[] args) {
        Config config = new Config();

        if (args.length > 0) {
            try (InputStream stream = Files.newInputStream(Paths.get(args[0]))) {
                ObjectMapper mapper = new ObjectMapper();
                config = mapper.readValue(stream, Config.class);
            } catch (DatabindException e) {
                System.err.printf("Can't parse configuration file \"%s\": %s%n", args[0], e.getMessage());
                return;
            } catch (IOException e) {
                System.err.printf("Can't read configuration file \"%s\": %s%n", args[0], e.getMessage());
                return;
            }
        }

        for (Config.VariationName v : config.variations) {
            if (!TRANSFORMS_NAME.containsKey(v.name)) {
                System.err.println("Unknown variation: " + v.name);
                return;
            }
        }

        Random rnd = new Random(config.seed);
        List<Transform> variations = config.variations.stream().map(v -> new WeightedTransform(
            TRANSFORMS_NAME.get(v.name),
            v.weight
        )).collect(Collectors.toList());

        List<Color> colors = new ArrayList<>(config.colors.size());
        for (String colorHex : config.colors) {
            try {
                colors.add(ImageUtils.decodeHex(colorHex));
            } catch (NumberFormatException e) {
                System.err.printf("Invalid color \"%s\": %s%n", colorHex, e.getMessage());
                return;
            }
        }

        List<ColoredTransform> transforms = colors.stream()
            .map(color -> new ColoredTransform(
                new SumTransform(Affine.create(rnd), variations),
                color
            ))
            .toList();

        Renderer renderer = new Renderer(rnd);

        RenderConfig cfg = new RenderConfig(
            RendererRunner.createCanvas(config.threads, config.width, config.height),
            new Rect(-config.xBound, -config.yBound, config.xBound * 2, config.yBound * 2),
            transforms, config.samplesNum, config.iterPerSample, config.symmetry
        );

        FractalImage image = RendererRunner.run(config.threads, renderer, cfg);

        ImageProcessor postTransform = new LogarithmicGammaCorrection(config.gamma);
        postTransform.process(image);

        try {
            Path path = Path.of(config.output);
            String extension = com.google.common.io.Files.getFileExtension(config.output).toLowerCase();
            ImageUtils.save(image, path, extension);
        } catch (InvalidPathException e) {
            System.err.println("Bad output path: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Can't write the result: " + e.getMessage());
            return;
        }
    }
}
