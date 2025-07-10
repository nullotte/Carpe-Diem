package carpediem.content;

import mindustry.graphics.*;
import mindustry.type.*;

public class CDStatusEffects {
    public static StatusEffect unpowered;

    public static void load() {
        unpowered = new StatusEffect("unpowered") {{
            color = Pal.remove;
            speedMultiplier = 1f / 5f;
        }};
    }
}
