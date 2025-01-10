package carpediem.world.blocks.environment;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;

// I HATE
public class FlatOreBlock extends OreBlock {
    public FlatOreBlock(String name, Item ore) {
        super(name, ore);
    }

    @Override
    public void createIcons(MultiPacker packer) {
        // GOD FUCKING DAMMIT
        for (int i = 0; i < variants; i++) {
            PixmapRegion shadow = Core.atlas.has(name + (i + 1)) ?
                    Core.atlas.getPixmap(name + (i + 1)) :
                    Core.atlas.getPixmap(itemDrop.name + (i + 1));

            Pixmap image = shadow.crop();

            packer.add(PageType.environment, name + (i + 1), image);
            packer.add(PageType.editor, "editor-" + name + (i + 1), image);

            if (i == 0) {
                packer.add(PageType.editor, "editor-block-" + name + "-full", image);
                packer.add(PageType.main, "block-" + name + "-full", image);
            }
        }
    }
}
