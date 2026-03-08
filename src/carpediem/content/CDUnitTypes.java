package carpediem.content;

import carpediem.ai.types.*;
import carpediem.entities.abilities.*;
import carpediem.type.ammo.*;
import carpediem.type.unit.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.type.weapons.*;

public class CDUnitTypes {
    public static UnitType cache, carver, heap, myriad;

    public static void load() {
        cache = new CDUnitType("cache") {{
            constructor = LegsUnit::create;

            hitSize = 9f;
            accel = 0.2f;
            drag = 0.2f;
            speed = 3f;
            rotateSpeed = 6f;
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
            mineHardnessScaling = false;
            mineSpeed = (65f * 5f) / 60f;
            mineTier = 3;
            buildSpeed = 1f;
            buildRange = 20f * 8f;
            itemCapacity = 100;

            ammoType = new VanityAmmoType();

            drawBuildBeam = false;
            weapons.add(new BuildWeapon("carpe-diem-cache-weapon") {{
                x = 5f;
                y = 5f;
                rotate = false;
                layerOffset = -0.001f;
                shootY = 3f;
            }});
        }};

        carver = new CDUnitType("carver") {{
            constructor = UnitEntity::create;
            controller = u -> new ReloadingAI(() -> {
                BuilderAI ai = new BuilderAI();
                ai.onlyAssist = true;
                return ai;
            });
            playerControllable = false;

            hitSize = 20f;
            accel = 0.07f;
            drag = 0.05f;
            speed = 3.5f;
            rotateSpeed = 8f;
            flying = true;

            engineOffset = 6.5f;
            engineSize = 4f;

            abilities.add(new AmmoStatusAbility(CDStatusEffects.unpowered, 5f, 60f));
            ammoCapacity = 300;
            ammoType = new PowerAmmoType(10000f);

            buildSpeed = 2f;
            buildRange = 40f * 8f;
            itemCapacity = 0;

            drawBuildBeam = false;
            weapons.add(new BuildWeapon("carpe-diem-carver-weapon") {{
                x = 10f / 4f;
                y = 37f / 4f;
                rotate = false;
                layerOffset = -0.001f;
                shootY = 3f;
            }});
        }};

        heap = new CDUnitType("heap") {{
            constructor = UnitEntity::create;
            controller = u -> new ReloadingAI(CDCargoAI::new);
            playerControllable = false;

            hitSize = 22f;
            accel = 0.1f;
            drag = 0.1f;
            speed = 5f;
            rotateSpeed = 10f;
            flying = true;
            physics = false;

            engineOffset = 11f;
            engineSize = 5f;

            abilities.add(new AmmoStatusAbility(CDStatusEffects.unpowered, 5f, 60f));
            ammoCapacity = 300;
            ammoType = new PowerAmmoType(10000f);

            itemCapacity = 200;
            itemOffsetY = 3.5f;
        }};

        myriad = new CDUnitType("myriad") {{
            constructor = PayloadUnit::create;
            controller = u -> !playerControllable || (u.team.isAI() && !u.team.rules().rtsAi) ? aiController.get() : new ReloadingCommandAI();

            hitSize = 35f;
            accel = 0.03f;
            drag = 0.1f;
            speed = 6f;
            rotateSpeed = 4f;
            flying = true;
            physics = false;

            engineSize = 0f;
            setEnginesMirror(new UnitEngine(9f, -22f, 6f, 315f));

            abilities.add(new AmmoStatusAbility(CDStatusEffects.unpowered, 5f, 60f));
            ammoCapacity = 600;
            ammoType = new PowerAmmoType(10000f);

            itemCapacity = 0;
            payloadCapacity = 9f * 9f * Vars.tilePayload;
        }};
    }
}
