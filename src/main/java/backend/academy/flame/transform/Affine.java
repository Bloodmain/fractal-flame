package backend.academy.flame.transform;

import backend.academy.flame.model.Point;
import java.util.Random;

public class Affine implements Transform {
    private static final double BOUND = 1.5;
    private final Coefficients coef;

    private Affine(Coefficients coefficients) {
        this.coef = coefficients;
    }

    public static Affine create(Random random) {
        Coefficients coef = getCoefficients(random);
        while (!check(coef)) {
            coef = getCoefficients(random);
        }
        return new Affine(coef);
    }

    @Override
    public Point apply(Point point) {
        return new Point(
            coef.a * point.x() + coef.b * point.y() + coef.c,
            coef.d * point.x() + coef.e * point.y() + coef.f
        );
    }

    private static boolean check(Coefficients coef) {
        double a = coef.a;
        double b = coef.a;
        double d = coef.a;
        double e = coef.a;

        return a * a + d * d < 1
            && b * b + e * e < 1
            && a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d);
    }

    private static double getCoef(Random rnd) {
        return rnd.nextDouble(-BOUND, BOUND);
    }

    private static Coefficients getCoefficients(Random rnd) {
        return new Coefficients(getCoef(rnd), getCoef(rnd), getCoef(rnd), getCoef(rnd), getCoef(rnd), getCoef(rnd));
    }

    private record Coefficients(
        double a, double b, double d, double e,
        double c, double f
    ) {
    }
}
