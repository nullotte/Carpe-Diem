package carpediem.ui;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.ui.*;

// it really doesnt need to extend warningbar but im just lazy like that
public class MovingWarningBar extends WarningBar {
    public float speed;
    public float barOffset;

    @Override
    public void act(float delta) {
        super.act(delta);

        barOffset = Mathf.mod(barOffset + delta * speed, spacing);
    }

    @Override
    public void draw() {
        Draw.color(color);
        Draw.alpha(parentAlpha);

        int amount = (int) (width / spacing) + 2;

        for (int i = 0; i < amount; i++) {
            float rx = x + barOffset + (i - 1) * spacing;
            Fill.quad(
                    rx, y,
                    rx + skew, y + height,
                    rx + skew + barWidth, y + height,
                    rx + barWidth, y
            );
        }
        Lines.stroke(Scl.scl(3f));
        Lines.line(x, y, x + width, y);
        Lines.line(x, y + height, x + width, y + height);

        Draw.reset();
    }
}
