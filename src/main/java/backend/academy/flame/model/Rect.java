package backend.academy.flame.model;

public record Rect(double x, double y, double width, double height) {
    public boolean contains(Point p) {
        return p.x() >= x && p.x() <= x + width && p.y() >= y && p.y() <= y + height;
    }

    public double xRight() {
        return x + width;
    }

    public double yTop() {
        return y + height;
    }
}
