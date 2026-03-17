package carpediem.graphics;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import carpediem.content.*;
import carpediem.world.blocks.campaign.*;
import carpediem.world.blocks.campaign.RocketControlCenter.*;
import carpediem.world.blocks.campaign.RocketLaunchPad.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
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
    rocketMerge = new Effect(60f, e -> {
        if (!(e.data instanceof RocketControlCenterBuild build)) return;
        alpha(e.fout());
        mixcol(Pal.accent, 1f);
        rect(((RocketControlCenter) build.block).rocketRegion, e.x, e.y);
    }),
    countdownNumber = new Effect(50f, e -> {
        if (!(e.data instanceof RocketControlCenterBuild build)) return;
        color(Pal.accent, e.foutpowdown());
        rect(((RocketControlCenter) build.block).countdownNumberRegions[(int) e.rotation], e.x, e.y);
    }),
    launcherSelect = new Effect(5f, e -> {
        if (!(e.data instanceof Vec2 origin)) return;

        // bro what
        Pal.gray.a(e.fout());
        Pal.accent.a(e.fout());

        Drawf.limitLine(origin, Tmp.v3.set(e.x, e.y), e.rotation, 3f);
        Drawf.square(e.x, e.y, 3f);
        Drawf.circles(origin.x, origin.y, e.rotation);

        Pal.gray.a(1f);
        Pal.accent.a(1f);
    }).layer(Layer.overlayUI),
    lemonSplat = new Effect(45f, e -> {
        color(CDItems.lemon.color);
        Angles.randLenVectors(e.id, 6, 7f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2f);
        });
    }).layer(Layer.effect + 0.1f);
}
