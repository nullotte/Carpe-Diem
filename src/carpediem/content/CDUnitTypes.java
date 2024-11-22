package carpediem.content;

import carpediem.type.unit.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;

public class CDUnitTypes {
    public static UnitType cache;

    public static void load() {
        cache = new CDUnitType("cache") {{
            constructor = LegsUnit::create;
            coreUnitDock = true;

            hitSize = 9f;
            accel = 0.2f;
            drag = 0.2f;
            speed = 2f;
            rotateSpeed = 4f;
            hovering = true;
            groundLayer = Layer.legUnit;

            legCount = 4;
            legLength = 10f;
            legLengthScl = 0.9f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = 5f;
            legBaseOffset = 4f;
            legSpeed = 0.01f;
            legForwardScl = 0.7f;
            legStraightness = 0.1f;
            legMoveSpace = 3f;
            legMaxLength = 1.1f;

            mineWalls = true; // sure why not
            mineSpeed = 6f;
            mineTier = 3;
            buildSpeed = 1f;
            itemCapacity = 200;

            fogRadius = 16f;
            targetPriority = -2;
            targetable = false;
            hittable = false;

            drawBuildBeam = false;
            weapons.add(new BuildWeapon("carpe-diem-cache-weapon") {{
                x = 5f;
                y = 5f;
                rotate = false;
                layerOffset = -0.001f;
                shootY = 3f;
            }});
        }};
    }
}
