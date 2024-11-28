package carpediem.graphics;

import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.blocks.payloads.*;

import static arc.graphics.g2d.Draw.*;

// TODO i need like a vfx artist or something
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
    });
}
