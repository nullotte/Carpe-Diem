package carpediem.entities.bullet;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

// it casts a shadow
public class CastShadowBulletType extends BasicBulletType {
    public TextureRegion softShadowRegion;

    public CastShadowBulletType(float speed, float damage) {
        super(speed, damage);
    }

    @Override
    public void load() {
        super.load();
        softShadowRegion = Core.atlas.find("particle");
    }

    @Override
    public void draw(Bullet b) {
        Draw.color(0, 0, 0, 0.4f);
        float rad = 1.6f;
        float size = Math.max(frontRegion.width, frontRegion.height) * frontRegion.scl();
        Draw.rect(softShadowRegion, b.x, b.y, size * rad * Draw.xscl, size * rad * Draw.yscl, b.rotation() - 90f);
        Draw.color();

        super.draw(b);
    }
}
