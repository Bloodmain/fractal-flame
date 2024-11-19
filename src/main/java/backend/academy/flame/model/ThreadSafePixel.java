package backend.academy.flame.model;

public class ThreadSafePixel extends Pixel {
    public ThreadSafePixel(Color color, int hitCount) {
        super(color, hitCount);
    }

    @Override
    public synchronized void mixColor(Color color) {
        super.mixColor(color);
    }

    @Override
    public synchronized void incHitCount() {
        super.incHitCount();
    }

    @Override
    public synchronized void setColor(Color color) {
        super.setColor(color);
    }
}
