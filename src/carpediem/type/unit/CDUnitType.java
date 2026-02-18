package carpediem.type.unit;

import arc.graphics.*;
import arc.graphics.g2d.*;
import carpediem.content.*;
import carpediem.graphics.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class CDUnitType extends UnitType {
    public CDUnitType(String name) {
        super(name);
        outlineColor = CDColors.outline;

        fogRadius = 64f; // should every unit have the same fog radius?
        targetPriority = -2;
        targetable = false;
        hittable = false;
    }

    @Override
    public void setStats() {
        super.setStats();
        // we dont do that shit here
        stats.remove(Stat.weapons);
        stats.remove(Stat.range);
        stats.remove(Stat.abilities);
    }

    // dusted lands and melancholy ran so carpe diem could walk
    @Override
    public void createIcons(MultiPacker packer) {
        super.createIcons(packer);
        PixmapRegion region = packer.get(name);
        if (region == null) return;
        PixmapRegion base = new PixmapRegion(region.crop());

        // it finally works !!!!
        // all that effort trying to get this shit to work just to end up using the team color for the icon anyway im crying
        Pixmap cell = packer.get(name + "-cell").crop();
        cell.replace(in -> switch (in) {
            // this uses the coalition's colors because the vanilla icon cell looks like triage . if you want vanilla colors check vanilla
            case 0xffffffff -> CDTeams.coalition.palettei[0];
            case 0xdcc6c6ff -> CDTeams.coalition.palettei[1];
            case 0x9d7f7fff -> CDTeams.coalition.palettei[2];
            default -> 0;
        });
        base.pixmap.draw(cell, true);

        // ALL THIS SHIT JUST FOR ONE SINGLE UNIT TO ACTUALLY USE WEAPONS
        // AND NOT EVEN FANCY WEAPONS OR ANYTHING ITS LITERALLY JUST TWO BITS OF METAL SLAPPED ON THE FRONT ISTG
        // PEACEFUL FACTORY BUILDING CAMPAIGN MOD WOOHOOOO
        // anyways unfortunately this code doesnt support parts . tho if you're copying this code you probably know how to make it work with parts
        for (Weapon weapon : weapons) {
            if (!weapon.name.isEmpty()) {
                Pixmap over = base.crop();
                Pixmap weaponRegion = packer.get(weapon.name).crop();
                Pixmap weaponOutlineRegion = weapon.top ? weaponRegion.copy() : packer.get(weapon.name + "-outline").crop();
                base.pixmap.draw(weaponOutlineRegion,
                        (int) (weapon.x * 4 + base.width / 2f - weaponOutlineRegion.width / 2f),
                        (int) (-weapon.y * 4 + base.height / 2f - weaponOutlineRegion.height / 2f),
                        true);

                if (!weapon.top) {
                    base.pixmap.draw(over, true);
                    base.pixmap.draw(weaponRegion,
                            (int) (weapon.x * 4 + base.width / 2f - weaponRegion.width / 2f),
                            (int) (-weapon.y * 4 + base.height / 2f - weaponRegion.height / 2f),
                            true);
                }

                if (weapon.mirror) {
                    Pixmap overFlip = base.crop();
                    Pixmap flipRegion = weaponRegion.flipX();
                    Pixmap flipOutlineRegion = weaponOutlineRegion.flipX();
                    base.pixmap.draw(flipOutlineRegion,
                            (int) (-weapon.x * 4 + base.width / 2f - weaponOutlineRegion.width / 2f),
                            (int) (-weapon.y * 4 + base.height / 2f - weaponOutlineRegion.height / 2f),
                            true);
                    if (!weapon.top) {
                        base.pixmap.draw(overFlip, true);
                        base.pixmap.draw(flipRegion,
                                (int) (-weapon.x * 4 + base.width / 2f - weaponRegion.width / 2f),
                                (int) (-weapon.y * 4 + base.height / 2f - weaponRegion.height / 2f),
                                true);
                    }
                }

                if (weapon.layerOffset < 0f) {
                    base.pixmap.draw(over, true);
                }
            }
        }

        packer.add(PageType.main, name + "-preview", base);
        packer.add(PageType.main, name + "-full", base);
    }
}
