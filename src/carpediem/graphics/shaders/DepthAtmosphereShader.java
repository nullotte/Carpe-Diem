package carpediem.graphics.shaders;

import arc.*;
import arc.graphics.g3d.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.util.*;
import carpediem.graphics.*;
import carpediem.type.*;

public class DepthAtmosphereShader extends Shader {
    private static final Mat3D mat = new Mat3D();

    public Camera3D camera;
    public AtmospherePlanet planet;

    public DepthAtmosphereShader() {
        super(CDShaders.file("depth-atmosphere.vert"), CDShaders.file("depth-atmosphere.frag"));
    }

    @Override
    public void apply() {
        setUniformMatrix4("u_projView", camera.combined.val);
        setUniformMatrix4("u_invProj", mat.set(camera.projection).inv().val);
        setUniformMatrix4("u_trans", planet.getTransform(mat).val);

        setUniformf("u_camPos", camera.position);
        setUniformf("u_relCamPos", Tmp.v31.set(camera.position).sub(planet.position));
        setUniformf("u_depthRange", camera.near, camera.far);
        setUniformf("u_center", planet.position);
        setUniformf("u_light", planet.getLightNormal());
        setUniformf("u_color", planet.atmosphereColor.r, planet.atmosphereColor.g, planet.atmosphereColor.b);

        setUniformf("u_innerRadius", planet.radius + planet.atmosphereRadIn);
        setUniformf("u_outerRadius", planet.radius + planet.atmosphereRadOut);

        planet.depthBuffer.getTexture().bind(0);
        setUniformi("u_topology", 0);
        setUniformf("u_viewport", Core.graphics.getWidth(), Core.graphics.getHeight());
    }
}
