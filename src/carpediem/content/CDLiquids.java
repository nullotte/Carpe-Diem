package carpediem.content;

import arc.graphics.*;
import mindustry.type.*;

public class CDLiquids {
    // add lemonade pls
    public static Liquid petroleum;

    public static void load() {
        // i know oil might be the raw form but shhh shhhhhh
        // im not making the player turn "Oil" into "Fuel Oil"
        petroleum = new Liquid("petroleum", Color.valueOf("4a483e")) {{

        }};
    }
}
