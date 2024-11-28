package carpediem.content;

import mindustry.graphics.*;
import mindustry.type.*;

// should this even exist
public class CDStatusEffects {
    public static StatusEffect powered;

    public static void load() {
        powered = new StatusEffect("powered") {{
            color = Pal.accent;
            speedMultiplier = 5f;
        }};
    }
}
