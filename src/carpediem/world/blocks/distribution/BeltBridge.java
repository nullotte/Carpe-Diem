package carpediem.world.blocks.distribution;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import carpediem.world.blocks.distribution.Belt.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

// what the hell
public class BeltBridge extends DuctBridge {
    public DrawBlock drawer;
    public TextureRegion bridgeRegion1, bridgeRegion2;

    public BeltBridge(String name) {
        super(name);
    }

    @Override
    public void setStats() {
        super.setStats();

        // ????? did anuke forget to set these for duct bridges
        stats.add(Stat.itemsMoved, 60f / speed, StatUnit.itemsSecond);
        stats.add(Stat.range, range, StatUnit.blocks);
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

    // vro
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, boolean line) {
        int length = range;
        Building found = null;
        int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);

        for (int i = 1; i <= range; i++) {
            Tile other = world.tile(x + dx * i, y + dy * i);

            if (other != null && other.build instanceof DirectionBridgeBuild build && build.rotation == rotation && build.block == this && build.team == player.team()) {
                length = i;
                found = other.build;
                break;
            }
        }

        if (line || found != null) {
            Drawf.dashLine(Pal.placing,
                    x * tilesize + dx * (tilesize / 2f + 2),
                    y * tilesize + dy * (tilesize / 2f + 2),
                    x * tilesize + dx * (length) * tilesize,
                    y * tilesize + dy * (length) * tilesize
            );
        }

        if (found != null) {
            if (line) {
                Drawf.square(found.x, found.y, found.block.size * tilesize / 2f + 2.5f, 0f);
            } else {
                Drawf.square(found.x, found.y, 2f);
            }
        }
    }

    @Override
    public void drawBridge(int rotation, float x1, float y1, float x2, float y2, Color liquidColor) {
        // good god
        bridgeRegion = rotation == 0 || rotation == 3 ? bridgeRegion1 : bridgeRegion2;
        super.drawBridge(rotation, x1, y1, x2, y2, liquidColor);
    }

    public boolean input(Tile tile, int rotation, Tile other, int otherRot, Block otherBlock) {
        return otherBlock.outputsItems() && tile.relativeTo(other) != rotation && ((!otherBlock.rotate || !otherBlock.rotatedOutput(other.x, other.y)) || other.relativeTo(tile) == otherRot);
    }

    public class BeltBridgeBuild extends DuctBridgeBuild {
        public BeltBridgeBuild in;
        // maybe i should just kill myself
        public int inputDir = -1;
        public boolean[] input = new boolean[4], inputBelt = new boolean[4];

        @Override
        public void updateTile() {
            super.updateTile();
            DirectionBridgeBuild link = findLink();

            if (link instanceof BeltBridgeBuild beltLink) {
                beltLink.in = this;
            }

            if (in == null || in.rotation != rotation || !in.isValid() || in.lastLink != this) {
                in = null;
            }
        }

        @Override
        public void draw() {
            drawer.draw(this);

            DirectionBridgeBuild link = findLink();
            if (link != null) {
                Draw.z(Layer.power - 1);
                drawBridge(rotation, x, y, link.x, link.y, null);
            }
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            for (int i = 0; i < 4; i++) {
                if (i == rotation) continue;
                Building other = nearby(i);

                if (other != null && input(tile, rotation, other.tile, other.rotation, other.block)) {
                    input[i] = true;
                    if (other instanceof BeltBuild) {
                        inputBelt[i] = true;
                    }
                } else {
                    input[i] = inputBelt[i] = false;
                }
            }

            if (inputDir == -1) {
                inputDir = (rotation + 2) % 4;
            }

            int prevDir = inputDir;
            inputDir = -1;

            for (int i = 3; i >= 0; i--) {
                int realDir = Mathf.mod(rotation + (i - 1), 4);
                if (realDir == rotation) continue;

                if (inputBelt[realDir]) {
                    inputDir = realDir;
                    break;
                }
            }

            if (inputDir == -1) {
                for (int i = 3; i >= 0; i--) {
                    int realDir = Mathf.mod(rotation + (i - 1), 4);
                    if (realDir == rotation) continue;

                    if (input[realDir]) {
                        inputDir = realDir;
                        break;
                    }
                }
            }

            if (inputDir == -1) inputDir = prevDir;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return tile.relativeTo(Edges.getFacingEdge(source.tile(), tile)) == inputDir && super.acceptItem(source, item);
        }

        @Override
        public DirectionBridgeBuild findLink() {
            if (in != null) return null;

            for (int i = 1; i <= range; i++) {
                Tile other = tile.nearby(Geometry.d4x(rotation) * i, Geometry.d4y(rotation) * i);
                if (other != null && other.build instanceof DirectionBridgeBuild build && build.rotation == rotation && build.block == BeltBridge.this && build.team == team) {
                    return build;
                }
            }
            return null;
        }
    }
}
