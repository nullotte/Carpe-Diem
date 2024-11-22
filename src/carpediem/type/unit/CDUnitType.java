package carpediem.type.unit;

import arc.graphics.*;
import arc.graphics.g2d.*;
import carpediem.graphics.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.type.*;

public class CDUnitType extends UnitType {
    public CDUnitType(String name) {
        super(name);
        outlineColor = CDColors.outline;
    }

    // dusted lands and melancholy ran so carpe diem could walk
    @Override
    public void createIcons(MultiPacker packer) {
        super.createIcons(packer);
        PixmapRegion base = new PixmapRegion(packer.get(name).crop());

        // it finally works !!!!
        Pixmap cell = packer.get(name + "-cell").crop();
        cell.replace(in -> switch (in) {
            case 0xffffffff -> 0xffa664ff;
            case 0xdcc6c6ff -> 0xd06b53ff;
            case 0x9d7f7fff -> 0xb0463cff;
            default -> 0;
        });
        base.pixmap.draw(cell, true);

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
