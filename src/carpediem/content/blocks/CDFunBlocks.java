package carpediem.content.blocks;

import arc.graphics.*;
import carpediem.content.*;
import carpediem.entities.bullet.*;
import carpediem.graphics.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;

public class CDFunBlocks {
    public static Block lemonCannon;

    public static void load() {
        lemonCannon = new ItemTurret("lemon-cannon") {{
            requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 3;
            alwaysUnlocked = true;
            range = 320f;
            reload = 30f;

            ammo(CDItems.lemon, new CastShadowBulletType(8f, 1000f) {{
                width = height = 8f;
                shrinkY = 0f;
                sprite = "carpe-diem-lemon";
                backSprite = "clear";
                frontColor = Color.white;
                layer = Layer.bullet - 0.1f;
                spin = 5f;

                hitEffect = despawnEffect = CDFx.lemonSplat;

                hitSound = despawnSound = Sounds.plantBreak;
            }});

            shootSound = Sounds.shootArtillerySmall;
        }};
    }
}
