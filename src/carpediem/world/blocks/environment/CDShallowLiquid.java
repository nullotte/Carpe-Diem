package carpediem.world.blocks.environment;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class CDShallowLiquid extends Floor {
    public Floor liquidBase, floorBase;
    public float liquidOpacity = 0.35f;

    public CDShallowLiquid(String name) {
        super(name);
    }

    public void set(Block liquid, Block floor) {
        this.liquidBase = liquid.asFloor();
        this.floorBase = floor.asFloor();

        isLiquid = true;
        variants = floorBase.variants;
        status = liquidBase.status;
        liquidDrop = liquidBase.liquidDrop;
        cacheLayer = liquidBase.cacheLayer;
        shallow = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        if (liquidBase != null && floorBase != null) {
            PixmapRegion overlay = Core.atlas.getPixmap(liquidBase.region);
            int index = 0;
            for (TextureRegion region : floorBase.variantRegions()) {
                Pixmap res = Core.atlas.getPixmap(region).crop();
                for (int x = 0; x < res.width; x++) {
                    for (int y = 0; y < res.height; y++) {
                        res.setRaw(x, y, Pixmap.blend((overlay.getRaw(x, y) & 0xffffff00) | (int) (liquidOpacity * 255), res.getRaw(x, y)));
                    }
                }

                String baseName = this.name + (++index);
                packer.add(PageType.environment, baseName, res);
                packer.add(PageType.editor, "editor-" + baseName, res);

                res.dispose();
            }
        }

        super.createIcons(packer);

        // aight dawg
        PixmapRegion image = packer.get(this.name + 1);
        mapColor.set(image.get(image.width / 2, image.height / 2));
    }
}
