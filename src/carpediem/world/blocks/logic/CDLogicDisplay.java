package carpediem.world.blocks.logic;

import arc.graphics.gl.*;
import carpediem.graphics.*;
import mindustry.graphics.*;
import mindustry.world.blocks.logic.*;

public class CDLogicDisplay extends LogicDisplay {
    public CDLogicDisplay(String name) {
        super(name);
    }

    public class CDLogicDisplayBuild extends LogicDisplayBuild {
        @Override
        public void ensureBuffer() {
            if (buffer == null) {
                buffer = new FrameBuffer(displaySize, displaySize);
                buffer.begin(CDColors.outline);
                buffer.end();
            }
        }
    }
}
