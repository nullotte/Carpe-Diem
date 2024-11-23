package carpediem.world.blocks.distribution;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;

// what the hell
public class DrawerBridge extends DuctBridge {
    public DrawBlock drawer;
    public TextureRegion bridgeRegion1, bridgeRegion2;

    public DrawerBridge(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);

        bridgeRegion1 = Core.atlas.find(name + "-bridge1");
        bridgeRegion2 = Core.atlas.find(name + "-bridge2");
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public void drawBridge(int rotation, float x1, float y1, float x2, float y2, Color liquidColor) {
        // good god
        bridgeRegion = rotation == 0 || rotation == 3 ? bridgeRegion1 : bridgeRegion2;
        super.drawBridge(rotation, x1, y1, x2, y2, liquidColor);
    }

    public class DrawerBridgeBuild extends DuctBridgeBuild {
        @Override
        public void draw() {
            drawer.draw(this);

            DirectionBridgeBuild link = findLink();
            if (link != null) {
                Draw.z(Layer.power - 1);
                drawBridge(rotation, x, y, link.x, link.y, null);
            }
        }
    }
}
