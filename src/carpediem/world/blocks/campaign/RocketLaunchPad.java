package carpediem.world.blocks.campaign;

import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import carpediem.world.blocks.campaign.RocketControlCenter.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

public class RocketLaunchPad extends PayloadBlock {
    public Block requiredBlockEdge, requiredBlockCorner;

    public RocketLaunchPad(String name) {
        super(name);
        acceptsPayload = true;
        conductivePower = true;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(CDStat.inputEdge, StatValues.content(requiredBlockEdge));
        stats.add(CDStat.inputCorner, StatValues.content(requiredBlockCorner));
    }

    public class RocketLaunchPadBuild extends PayloadBlockBuild<BuildPayload> {
        public RocketControlCenterBuild controlCenter;
        public boolean corner;

        @Override
        public void updateTile() {
            super.updateTile();

            moveInPayload();

            if (controlCenter != null && !controlCenter.isValid()) {
                controlCenter = null;
            }
        }

        @Override
        public void onProximityUpdate() {
            if (controlCenter != null) {
                controlCenter.updatePads();
            }
        }

        @Override
        public boolean shouldConsume() {
            return payload != null;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return super.acceptPayload(source, payload) && controlCenter != null && payload.content() == (corner ? requiredBlockCorner : requiredBlockEdge);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            //draw input
            for (int i = 0; i < 4; i++) {
                if (blends(i)) {
                    Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                }
            }

            Draw.z(Layer.blockOver);
            drawPayload();
        }

        @Override
        public void drawSelect() {
            if (controlCenter != null) {
                controlCenter.drawSelect();
            }
        }

        @Override
        public void displayConsumption(Table table) {
            super.displayConsumption(table);
            if (controlCenter != null) {
                table.table(t -> {
                    t.add(new ReqImage((corner ? requiredBlockCorner : requiredBlockEdge).uiIcon, () -> payload != null)).size(Vars.iconMed);
                });
            }
        }
    }
}
