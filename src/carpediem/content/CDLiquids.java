package carpediem.content;

import mindustry.type.*;

public class CDLiquids {
    // one liquid. one. 1.
    // also i know oil might be the raw form but shhh shhhhhh
    // im not making the player turn "Oil" into "Fuel Oil"
    public static Liquid petroleum;

    public static void load() {
        petroleum = new Liquid("petroleum") {{

        }};
    }
}
