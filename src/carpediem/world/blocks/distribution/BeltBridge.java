package carpediem.world.blocks.distribution;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.input.*;
import carpediem.world.blocks.distribution.Belt.*;
import carpediem.world.draw.DrawBeltUnder.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

// what the hell
public class BeltBridge extends DuctBridge {
    private static BuildPlan otherReq;
    private int otherDst = 0;
    private boolean interrupted;

    public DrawBlock drawer;
    public TextureRegion bridgeRegion1, bridgeRegion2;

    public BeltBridge(String name) {
        super(name);
        configurable = true;
        config(Boolean.class, (BeltBridgeBuild bridge, Boolean out) -> {
            bridge.out = out;
            bridge.configged = true;
        });
    }

    @Override
    public void setStats() {
        super.setStats();

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
        boolean out = false;
        int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);

        for (int i = 1; i <= range; i++) {
            Tile other = Vars.world.tile(x + dx * i, y + dy * i);

            if (other != null && other.build instanceof BeltBridgeBuild build && build.rotation == rotation && build.block == this && build.team == Vars.player.team()) {
                if (build.out) {
                    length = i;
                    found = other.build;
                }
                break;
            }
        }
        // this is stupid
        for (int i = 1; i <= range; i++) {
            Tile other = Vars.world.tile(x + dx * -i, y + dy * -i);

            if (other != null && other.build instanceof BeltBridgeBuild build && build.rotation == rotation && build.block == this && build.team == Vars.player.team()) {
                if (!build.out) {
                    length = -i;
                    found = other.build;
                    out = true;
                }
                break;
            }
        }

        Color color = out ? Pal.place : Pal.placing;
        if (line || found != null) {
            // anuke what are these color names?
            Drawf.dashLine(color,
                    x * Vars.tilesize + dx * Mathf.sign(!out) * (Vars.tilesize / 2f + 2),
                    y * Vars.tilesize + dy * Mathf.sign(!out) * (Vars.tilesize / 2f + 2),
                    x * Vars.tilesize + dx * (length) * Vars.tilesize,
                    y * Vars.tilesize + dy * (length) * Vars.tilesize
            );
        }

        if (found != null) {
            if (line) {
                Drawf.square(found.x, found.y, found.block.size * Vars.tilesize / 2f + 2.5f, 0f, color);
            } else {
                Drawf.square(found.x, found.y, 2f, color);
            }
        }
    }

    @Override
    public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
        otherReq = null;
        otherDst = range;
        interrupted = false;
        Point2 d = Geometry.d4(plan.rotation);
        list.each(other -> {
            if (!interrupted && other.block == this && plan != other && Mathf.clamp(other.x - plan.x, -1, 1) == d.x && Mathf.clamp(other.y - plan.y, -1, 1) == d.y) {
                if (other.config != Boolean.TRUE) {
                    interrupted = true;
                    return;
                }

                int dst = Math.max(Math.abs(other.x - plan.x), Math.abs(other.y - plan.y));
                if (dst <= otherDst) {
                    otherReq = other;
                    otherDst = dst;
                }
            }
        });

        if (otherReq != null) {
            drawBridge(plan.rotation, plan.drawx(), plan.drawy(), otherReq.drawx(), otherReq.drawy(), null);
        }
    }

    @Override
    public void drawBridge(int rotation, float x1, float y1, float x2, float y2, Color liquidColor) {
        // good god
        bridgeRegion = rotation == 0 || rotation == 3 ? bridgeRegion1 : bridgeRegion2;
        super.drawBridge(rotation, x1, y1, x2, y2, liquidColor);
    }

    @Override
    public void changePlacementPath(Seq<Point2> points, int rotation) {
        BeltPlacement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= range);
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans) {
        if (plans.size <= 1) return;

        boolean nextOut = false;
        for (BuildPlan plan : plans) {
            plan.config = nextOut;
            nextOut = !nextOut;
        }
    }

    public class BeltBridgeBuild extends DuctBridgeBuild implements BeltUnderBlending {
        public boolean out, configged;
        public int inputDir = -1;
        public boolean[] input = new boolean[4], inputBelt = new boolean[4];
        public int[] blendInputs = new int[4], blendOutputs = new int[4];

        @Override
        public void placed() {
            super.placed();

            if (Vars.net.client()) return;

            if (!configged) {
                for (int i = 1; i <= range; i++) {
                    // backwards this time
                    Tile other = tile.nearby(Geometry.d4x(rotation) * -i, Geometry.d4y(rotation) * -i);
                    if (other != null && other.build instanceof BeltBridgeBuild build && !build.out && build.rotation == rotation && build.block == BeltBridge.this && build.team == team) {
                        configureAny(true);
                        break;
                    }
                }
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
        public void drawSelect() {
            if (!out) {
                drawPlace(tile.x, tile.y, rotation, true, false);
            }

            for (int dir = 0; dir < 4; dir++) {
                if (dir != rotation) {
                    int dx = Geometry.d4x(dir), dy = Geometry.d4y(dir);
                    Building found = occupied[(dir + 2) % 4];

                    if (found != null) {
                        int length = Math.max(Math.abs(found.tileX() - tileX()), Math.abs(found.tileY() - tileY()));
                        Drawf.dashLine(Pal.place,
                                found.x - dx * (Vars.tilesize / 2f + 2),
                                found.y - dy * (Vars.tilesize / 2f + 2),
                                found.x - dx * (length) * Vars.tilesize,
                                found.y - dy * (length) * Vars.tilesize
                        );

                        Drawf.square(found.x, found.y, 2f, 45f, Pal.place);
                    }
                }
            }
        }

        @Override
        public boolean drawInput() {
            // god
            return !out && (input[inputDir] || inputBelt[inputDir]);
        }

        @Override
        public boolean drawOutput() {
            return out;
        }

        @Override
        public int[] blendInputs() {
            return blendInputs;
        }

        @Override
        public int[] blendOutputs() {
            return blendOutputs;
        }

        @Override
        public void buildBlending(Building build) {
            buildBlending(this, inputDir, rotation);
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            for (int i = 0; i < 4; i++) {
                if (i == rotation) continue;
                Building other = nearby(i);

                if (other != null && belt().input(tile, rotation, other.tile, other.rotation, other.block)) {
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

            buildBlending(this);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return tile.relativeTo(Edges.getFacingEdge(source.tile, tile)) == inputDir && super.acceptItem(source, item);
        }

        @Override
        public boolean configTapped() {
            configure(!out);
            Sounds.click.at(this);
            return false;
        }

        @Override
        public Object config() {
            return out;
        }

        @Override
        public DirectionBridgeBuild findLink() {
            if (out) return null;

            for (int i = 1; i <= range; i++) {
                Tile other = tile.nearby(Geometry.d4x(rotation) * i, Geometry.d4y(rotation) * i);
                if (other != null && other.build instanceof BeltBridgeBuild build && build.rotation == rotation && build.block == BeltBridge.this && build.team == team) {
                    return build.out ? build : null;
                }
            }
            return null;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(out);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            out = read.bool();
        }
    }
}
