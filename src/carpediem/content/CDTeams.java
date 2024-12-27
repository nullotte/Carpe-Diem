package carpediem.content;

import arc.graphics.*;
import mindustry.game.*;

public class CDTeams {
    public static Team coalition, triage;

    public static void load() {
        coalition = modifyTeam(47, "carpe-diem-coalition", Color.valueOf("79aded"),
                Color.valueOf("b2daff"),
                Color.valueOf("79aded"),
                Color.valueOf("4475c8")
        );

        triage = modifyTeam(48, "carpe-diem-triage", Color.valueOf("000000"),
                Color.valueOf("000000"),
                Color.valueOf("000000"),
                Color.valueOf("000000")
        );
    }

    private static Team modifyTeam(int id, String name, Color color, Color pal1, Color pal2, Color pal3) {
        Team team = Team.get(id);
        team.name = name;
        team.color.set(color);
        team.hasPalette = true;

        team.palette[0] = pal1;
        team.palette[1] = pal2;
        team.palette[2] = pal3;

        for (int i = 0; i < 3; i++) {
            team.palettei[i] = team.palette[i].rgba();
        }

        return team;
    }
}
