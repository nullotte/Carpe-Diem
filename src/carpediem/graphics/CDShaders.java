package carpediem.graphics;

import arc.files.*;
import carpediem.graphics.shaders.*;
import mindustry.*;

public class CDShaders {
    public static DepthShader depth;
    public static DepthAtmosphereShader depthAtmosphere;

    public static void load() {
        depth = new DepthShader();
        depthAtmosphere = new DepthAtmosphereShader();
    }

    public static Fi file(String name) {
        return Vars.tree.get("shaders/" + name);
    }
}
