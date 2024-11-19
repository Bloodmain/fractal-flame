package backend.academy.flame.model;

public record Color(int r, int g, int b) {
    public Color mix(Color other) {
        return new Color(
            mixColors(r, other.r),
            mixColors(g, other.g),
            mixColors(b, other.b)
        );
    }

    private int mixColors(int c1, int c2) {
        return (c1 + c2) / 2;
    }
}
