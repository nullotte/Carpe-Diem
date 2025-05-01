package carpediem.graphics;

import arc.graphics.g2d.*;
import arc.math.*;
import carpediem.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.blocks.payloads.*;

import static arc.graphics.g2d.Draw.*;

public class CDFx {
    public static Effect
    payloadManufacture = new Effect(60f, e -> {
        if (!(e.data instanceof Payload payload)) return;

        alpha(e.fout());
        mixcol(Pal.accent, 1f);
        rect(payload.icon(), payload.x(), payload.y(), e.rotation);
    }),
    payloadManufactureFail = new Effect(60f, e -> {
        if (!(e.data instanceof Payload payload)) return;

        alpha(e.fout());
        mixcol(Pal.remove, 1f);
        rect(payload.icon(), payload.x(), payload.y(), e.rotation);
    }),
    lemonSplat = new Effect(45f, e -> {
        color(CDItems.lemon.color);
        Angles.randLenVectors(e.id, 6, 7f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2f);
        });
    }).layer(Layer.effect + 0.1f);
}
