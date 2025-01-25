package carpediem.type.ammo;

import arc.graphics.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

// peaceful campaign mod
public class VanityAmmoType implements AmmoType {
    @Override
    public String icon() {
        return Iconc.units + "";
    }

    @Override
    public Color color() {
        return Pal.health;
    }

    @Override
    public Color barColor() {
        return Pal.health;
    }

    @Override
    public void resupply(Unit unit) {
        // no
    }
}