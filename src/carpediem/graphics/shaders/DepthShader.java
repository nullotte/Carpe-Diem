package carpediem.graphics.shaders;

import arc.graphics.g3d.*;
import arc.graphics.gl.*;
import carpediem.graphics.*;

public class DepthShader extends Shader {
    public Camera3D camera;

    public DepthShader() {
        super(CDShaders.file("depth.vert"), CDShaders.file("depth.frag"));
    }

    @Override
    public void apply() {
        setUniformf("u_camPos", camera.position);
        setUniformf("u_camRange", camera.near, camera.far - camera.near);
    }
}