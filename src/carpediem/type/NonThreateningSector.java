package carpediem.type;

import arc.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.type.*;

// it is not threatening . threat level None .
public class NonThreateningSector extends Sector {
    public NonThreateningSector(Planet planet, Ptile tile) {
        super(planet, tile);
    }

    @Override
    public String displayThreat() {
        return "[white]" + Core.bundle.get("threat.none");
    }
}
