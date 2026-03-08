package carpediem.graphics.g3d;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;

import java.util.*;

// this isn't actually anything i want to use in the mod, but i'm leaving it here in case anyone else wants to yoink it
public class RingMeshBuilder {
    private static final Rand rand = new Rand();
    private static final boolean packNormals = Core.gl30 != null && (Core.app.isMobile() || Core.graphics.getGLVersion().atLeast(3, 3));
    private static final Vec2 tmp = new Vec2();
    private static final Vec2[] d6 = {
            new Vec2(0f, 1f),
            new Vec2(Mathf.sqrt3 / 2f, 0.5f),
            new Vec2(Mathf.sqrt3 / 2f, -0.5f),
            new Vec2(0f, -1f),
            new Vec2(-Mathf.sqrt3 / 2f, -0.5f),
            new Vec2(-Mathf.sqrt3 / 2f, 0.5f)
    };
    private static final Vec3[] array = new Vec3[6];

    static {
        for (int i = 0; i < 6; i++) {
            array[i] = new Vec3();
        }
    }

    public static Mesh buildRing(int seed, int innerRadius, int outerRadius, float radius) {
        rand.setSeed(seed);

        float scale = radius / outerRadius;
        int range = (int) (outerRadius / 1.5f) + 2;
        Mesh mesh = begin((range * range * 4) * 12, 0, true, false);

        float[] floats = new float[3 + (packNormals ? 1 : 3) + 1];
        Vec3 nor = new Vec3();
        Color tmpCol = new Color();
        Color color1 = Color.valueOf("ebfffd"), color2 = Color.valueOf("ade4ff");

        for (int x = -range; x < range; x++) {
            for (int y = -range; y < range; y++) {
                hexToSquare(x, y);
                if (tmp.len() > innerRadius && tmp.len() < outerRadius) {
                    tmpCol.set(color1).lerp(color2, Math.abs((float) Noise.rawNoise(60f + Mathf.curve(tmp.len() / outerRadius, (float) innerRadius / outerRadius, 1f) * 5f)));
                    tmpCol.a(rand.random(0.8f, 1f));
                    tmpCol.a *= (float) Math.abs(Noise.rawNoise(20f + Mathf.curve(tmp.len() / outerRadius, (float) innerRadius / outerRadius, 1f) * 7f)) * 0.6f + 0.4f;
                    float col = tmpCol.toFloatBits();

                    tmp.scl(scale);

                    for (int i = 0; i < 6; i++) {
                        array[i].set(tmp.x + d6[i].x * scale, 0f, tmp.y + d6[i].y * scale).rotate(Vec3.X, 20f);
                    }

                    normal(array[0], array[2], array[4], nor);

                    verts(mesh, floats, array[0], array[1], array[2], nor, col, 0f);
                    verts(mesh, floats, array[0], array[2], array[3], nor, col, 0f);
                    verts(mesh, floats, array[0], array[3], array[4], nor, col, 0f);
                    verts(mesh, floats, array[0], array[4], array[5], nor, col, 0f);
                }
            }
        }

        return end(mesh);
    }

    private static void verts(Mesh mesh, float[] floats, Vec3 a, Vec3 b, Vec3 c, Vec3 normal, float color, float emissive) {
        vert(mesh, floats, a.x, a.y, a.z, normal, color, emissive);
        vert(mesh, floats, b.x, b.y, b.z, normal, color, emissive);
        vert(mesh, floats, c.x, c.y, c.z, normal, color, emissive);
    }

    private static void vert(Mesh mesh, float[] floats, float x, float y, float z, Vec3 normal, float color, float emissive) {
        floats[0] = x;
        floats[1] = y;
        floats[2] = z;

        if (packNormals) {
            floats[3] = packNormals(normal.x, normal.y, normal.z);

            floats[4] = color;
            if (floats.length > 5) floats[5] = emissive;
        } else {
            floats[3] = normal.x;
            floats[4] = normal.y;
            floats[5] = normal.z;

            floats[6] = color;
            if (floats.length > 7) floats[7] = emissive;
        }

        mesh.getVerticesBuffer().put(floats);
    }

    private static Mesh begin(int vertices, int indices, boolean normal, boolean emissive) {
        return begin(vertices, indices, normal, emissive, true);
    }

    private static Mesh begin(int vertices, int indices, boolean normal, boolean emissive, boolean color) {
        Seq<VertexAttribute> attributes = Seq.with(
                VertexAttribute.position3
        );

        if (normal) {
            //only GL30 supports GL_INT_2_10_10_10_REV
            attributes.add(packNormals ? VertexAttribute.packedNormal : VertexAttribute.normal);
        }

        if (color) {
            attributes.add(VertexAttribute.color);
        }

        if (emissive) {
            attributes.add(new VertexAttribute(4, GL20.GL_UNSIGNED_BYTE, true, "a_emissive"));
        }

        Mesh mesh = new Mesh(true, vertices, indices, attributes.toArray(VertexAttribute.class));

        mesh.getVerticesBuffer().limit(mesh.getVerticesBuffer().capacity());
        mesh.getVerticesBuffer().position(0);

        if (indices > 0) {
            mesh.getIndicesBuffer().limit(mesh.getIndicesBuffer().capacity());
            mesh.getIndicesBuffer().position(0);
        }

        return mesh;
    }

    private static Mesh end(Mesh mesh) {
        mesh.getVerticesBuffer().limit(mesh.getVerticesBuffer().position());
        if (mesh.getNumIndices() > 0) {
            mesh.getIndicesBuffer().limit(mesh.getIndicesBuffer().position());
        }

        return mesh;
    }

    private static void normal(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 out) {
        out.set(v2).sub(v1).crs(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z).nor();
    }

    private static float packNormals(float x, float y, float z) {
        int xs = x < -1f / 512f ? 1 : 0;
        int ys = y < -1f / 512f ? 1 : 0;
        int zs = z < -1f / 512f ? 1 : 0;

        int vi =
                zs << 29 | ((int) (z * 511 + (zs << 9)) & 511) << 20 |
                        ys << 19 | ((int) (y * 511 + (ys << 9)) & 511) << 10 |
                        xs << 9 | ((int) (x * 511 + (xs << 9)) & 511);
        return Float.intBitsToFloat(vi);
    }

    private static void hexToSquare(int x, int y) {
        tmp.set(
                Mathf.sqrt3 * (x + 0.5f * (y % 2)),
                1.5f * y
        );
    }
}
