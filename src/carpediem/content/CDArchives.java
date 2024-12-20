package carpediem.content;

import arc.struct.*;
import carpediem.type.*;
import mindustry.type.*;

import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.CDItems.*;

public class CDArchives {
    public static Archive
    automation, logistics;

    public static void load() {
        automation = new Archive("automation",
                ItemStack.with(controlCircuit, 20),
                Seq.with(smelterT1, pressT1, rollingMillT1, assemblerT1)
        );
    }
}
