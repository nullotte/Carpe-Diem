package carpediem.graphics;

import arc.files.*;
import arc.graphics.gl.*;
import carpediem.graphics.shaders.*;
import mindustry.*;

public class CDShaders {
    public static DepthShader depth;
    public static DepthAtmosphereShader depthAtmosphere;

    public static void load() {
        String prevVert = Shader.prependVertexCode, prevFrag = Shader.prependFragmentCode;
        Shader.prependVertexCode = Shader.prependFragmentCode = "";

        depth = new DepthShader();
        depthAtmosphere = new DepthAtmosphereShader();

        Shader.prependVertexCode = prevVert;
        Shader.prependFragmentCode = prevFrag;
    }

    public static Fi file(String name) {
        return Vars.tree.get("shaders/" + name);
    }
}
