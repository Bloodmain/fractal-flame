package backend.academy.flame.render;

import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Rect;
import backend.academy.flame.transform.ColoredTransform;
import java.util.List;
import lombok.With;

@With
public record RenderConfig(
    FractalImage canvas,
    Rect world,
    List<ColoredTransform> transforms,
    int samplesNum,
    int iterPerSample,
    int symmetry
) {
}
