package backend.academy.flame.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pixel {
    private Color color;

    @Setter
    private int hitCount;

    public Pixel(Color color, int hitCount) {
        this.color = color;
        this.hitCount = hitCount;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void mixColor(Color color) {
        if (this.hitCount == 0) {
            this.color = color;
        } else {
            this.color = this.color.mix(color);
        }
    }

    public void incHitCount() {
        this.hitCount++;
    }
}
