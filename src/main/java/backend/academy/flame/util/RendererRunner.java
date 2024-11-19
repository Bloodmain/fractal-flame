package backend.academy.flame.util;

import backend.academy.flame.model.Color;
import backend.academy.flame.model.FractalImage;
import backend.academy.flame.model.Pixel;
import backend.academy.flame.model.ThreadSafePixel;
import backend.academy.flame.render.RenderConfig;
import backend.academy.flame.render.Renderer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RendererRunner {
    private static final Color DEFAULT_COLOR = new Color(0, 0, 0);

    /**
     * Runs rendering sequentially or in parallel depending on the {@code threads} given.
     * {@code threads == 1} equals to sequential running without any additional expenses.
     *
     * @param threads  number of threads
     * @param renderer the renderer
     * @param cfg      config for rendering
     * @return the rendered image
     */
    public FractalImage run(int threads, Renderer renderer, RenderConfig cfg) {
        checkPositive(threads);

        if (threads == 1) {
            return renderer.render(cfg);
        }
        return runParallel(threads, renderer, cfg);
    }

    /**
     * Run rendering in parallel. Even with {@code threads == 1} it creates a thread to execute the task.
     *
     * @param threads  number of threads
     * @param renderer the renderer
     * @param cfg      config for rendering
     * @return the rendered image
     */
    public FractalImage runParallel(int threads, Renderer renderer, RenderConfig cfg) {
        checkPositive(threads);

        /* We're trying to give all the threads equal number of samples.
         That is, {rest} would be divided into threads by 1 as 0 <= rest < threads.
         For example, if we have samplesNum == 23 and thread == 3,
         the thread get 8, 8, 7 samples respectively.
        */
        int samplesPerThread = cfg.samplesNum() / threads;
        int rest = cfg.samplesNum() % threads;

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("flame-renderer-%d").build();
        try (ExecutorService executor = Executors.newFixedThreadPool(threads, namedThreadFactory)) {
            for (int i = 0; i < threads; i++) {
                // we need only [rest] threads to divide [rest]
                int samplesNum = i < rest ? samplesPerThread + 1 : samplesPerThread;

                executor.execute(() -> renderer.render(cfg.withSamplesNum(samplesNum)));
            }

            executor.shutdown(); // awaits for all tasks to complete
        }

        return cfg.canvas();
    }

    public FractalImage createCanvas(
        int threads,
        int width,
        int height
    ) {
        checkPositive(threads);

        return FractalImage.create(
            width,
            height,
            threads == 1
                ? () -> new Pixel(DEFAULT_COLOR, 0)
                : () -> new ThreadSafePixel(DEFAULT_COLOR, 0)
        );
    }

    private static void checkPositive(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("Threads must be greater than 0");
        }
    }
}
