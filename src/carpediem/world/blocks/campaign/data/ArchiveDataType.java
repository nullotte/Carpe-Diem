package carpediem.world.blocks.campaign.data;

import arc.*;
import arc.graphics.*;
import carpediem.type.*;
import mindustry.graphics.*;

public class ArchiveDataType extends DataType {
    public Archive archive;

    public ArchiveDataType(Archive archive) {
        this.archive = archive;
    }

    @Override
    public String name() {
        return "[#" + color() + "]" + Core.bundle.format("data.archive", archive.localizedName);
    }

    @Override
    public Color color() {
        return Pal.sapBullet;
    }
}
