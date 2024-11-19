package backend.academy.flame;

import java.util.List;
import lombok.AllArgsConstructor;

@SuppressWarnings("MagicNumber")
public class Config {
    public int seed = 8082003;
    public int threads = 8;
    public int width = 1920;
    public int height = 1080;
    public double xBound = 4;
    public double yBound = 3;
    public int symmetry = 1;
    public int samplesNum = 1000000;
    public int iterPerSample = 100;
    public double gamma = 0.5;
    public String output = "EXAMPLE.png";

    public List<VariationName> variations = List.of(
        new VariationName("Spherical", 0.6),
        new VariationName("Hyperbolic", 0.4)
    );
    public List<String> colors = List.of("#ebf2fa", "#ddfff7", "#fbf5f3");

    @AllArgsConstructor
    public static class VariationName {
        String name;
        double weight;
    }
}

