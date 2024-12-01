package carpediem.world.blocks.distribution;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.content.blocks.*;
import carpediem.input.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.*;

// like the belts from shapez.io
public class Belt extends Block implements Autotiler {
    public float moveTime;
    public TextureRegion[][] regions;
    public Block mergerReplacement, bridgeReplacement;

    public Belt(String name) {
        super(name);

        group = BlockGroup.transportation;
        update = true;
        solid = false;
        hasItems = true;
        conveyorPlacement = true;
        unloadable = false;
        noUpdateDisabled = true;
        underBullets = true;
        rotate = true;
        noSideBlend = true;
        priority = TargetPriority.transport;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.itemsMoved, 60f / moveTime, StatUnit.itemsSecond);
    }

    @Override
    public void load() {
        super.load();

        regions = new TextureRegion[2][4];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                regions[i][j] = Core.atlas.find(name + "-" + i + "-" + j);
            }
        }
    }

    @Override
    public void init() {
        super.init();

        if (mergerReplacement == null) mergerReplacement = CDDistribution.beltMerger;
        if (bridgeReplacement == null) bridgeReplacement = CDDistribution.beltBridge;
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{regions[0][0]};
    }

    @Override
    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
        if (mergerReplacement == null) return this;

        if (req.tile() != null) {
            if (req.tile().build != null && req.tile().build.rotation == req.rotation && (req.tile().block() == mergerReplacement || req.block == mergerReplacement))
                return this;

            Boolf<Point2> cont = p -> {
                if (plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && o.tile() != null && input(req.tile(), req.rotation, o.tile(), o.rotation, o.block))) {
                    return true;
                }

                Tile near = req.tile().nearby(p.x, p.y);
                return near != null && near.build != null && input(req.tile(), req.rotation, near, near.build.rotation, near.build.block);
            };

            int in = 0;
            for (int i = 1; i < 4; i++) {
                if (cont.get(Geometry.d4(req.rotation + i))) {
                    in++;

                    if (in > 1) {
                        return mergerReplacement;
                    }
                }
            }
        }

        return this;
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans) {
        BeltPlacement.calculateBridges(plans, (BeltBridge) bridgeReplacement, b -> b instanceof Belt);
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
        return false; // djkghgfkhjgfhlgdfjhgfjklhfglhjgflhjkgl
    }

    // i cannot escape
    public boolean input(Tile tile, int rotation, Tile other, int otherRot, Block otherBlock) {
        return otherBlock.outputsItems() && tile.relativeTo(other) != rotation && ((!otherBlock.rotate || !otherBlock.rotatedOutput(other.x, other.y)) || other.relativeTo(tile) == otherRot);
    }

    public class BeltBuild extends Building implements ChainedBuilding {
        public float clogHeat;
        public boolean moved;
        // starts at array length and goes down to 0
        public Item[] ids = new Item[itemCapacity];
        public float[] progresses = new float[itemCapacity];
        // cannot accept from any direction other than this one
        public int inputDir = -1;

        public int regionIndex;
        public float regionScl = 1f;
        public boolean blendIn, blendOut;

        public Building next;
        public BeltBuild queuedPush, nextc;
        public boolean[] input = new boolean[4], inputBelt = new boolean[4];

        @Override
        public void updateTile() {
            if (items.total() > 0) {
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i] != null) {
                        if (progresses[i] < 1f) {
                            progresses[i] += getProgressIncrease(moveTime);
                        }

                        if (progresses[i] >= 1f) {
                            tryPush(i);

                            if (i == ids.length - 1 && queuedPush != null) {
                                queuedPush.tryPush(0);
                                queuedPush = null;
                            }
                        }
                    }
                }
            } else {
                clogHeat = 0f;
                sleep();
            }

            if (!moved) {
                clogHeat = Mathf.approachDelta(clogHeat, 1f, 1f / 60f);
            } else {
                clogHeat = 0f;
            }
        }

        public void tryPush(int index) {
            if (ids[index] == null) return;
            moved = false;

            if (index == 0) {
                if (moveForward(ids[0])) {
                    items.remove(ids[0], 1);
                    ids[0] = null;
                    moved = true;
                } else if (nextc != null) {
                    nextc.queuedPush = this;
                }
            } else {
                if (ids[index - 1] == null) {
                    // push forward
                    ids[index - 1] = ids[index];
                    ids[index] = null;
                    moved = true;
                }
            }

            if (moved) {
                clogHeat = 0f;
                if (index == 0) {
                    if (nextc != null) {
                        nextc.progresses[itemCapacity - 1] = progresses[0] % 1f;
                    }
                } else {
                    progresses[index - 1] = progresses[index] % 1f;
                }
                progresses[index] = 0f;
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return ids[itemCapacity - 1] == null && tile.relativeTo(Edges.getFacingEdge(source.tile(), tile)) == inputDir;
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (ids[itemCapacity - 1] == null) {
                ids[itemCapacity - 1] = item;
                items.add(item, 1);
                noSleep();
            }
        }

        @Override
        public int acceptStack(Item item, int amount, Teamc source) {
            int accepted = 0;

            // yea ok intellij
            for (Item id : ids) {
                if (id == null) accepted++;
            }

            return accepted;
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source) {
            for (int i = ids.length - 1; i >= 0; i--) {
                if (ids[i] == null) {
                    ids[i] = item;
                    items.add(item, 1);
                } else {
                    break;
                }
            }

            noSleep();
        }

        @Override
        public int removeStack(Item item, int amount) {
            int removed = 0;

            for (int i = ids.length - 1; i >= 0 && removed < amount; i--) {
                if (ids[i] == item) {
                    ids[i] = null;
                    removed++;
                }
            }

            items.remove(item, removed);
            return removed;
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            next = front();
            nextc = next instanceof BeltBuild b ? b : null;

            // im not a good coder ok? pls bear with me
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
            regionIndex = inputDir == (rotation + 2) % 4 ? 0 : 1;

            Building in = nearby(inputDir);

            blendIn = in != null && input(tile, rotation, in.tile, in.rotation, in.block) && !in.block.squareSprite;
            blendOut = next != null && !next.block.squareSprite;

            if (inputDir == (rotation + 3) % 4) {
                regionScl = -1f;
            } else {
                regionScl = 1f;
            }
        }

        @Override
        public Building next() {
            return nextc;
        }

        @Override
        public void draw() {
            int frame = enabled && clogHeat <= 0.5f ? (int) (((Time.time / (moveTime * itemCapacity) * 8f * timeScale * efficiency)) % 4) : 0;

            Draw.z(Layer.blockUnder);
            // why is sliced in autotiler
            if (blendIn) {
                Draw.rect(sliced(regions[0][frame], SliceMode.bottom), x + Geometry.d4x(inputDir) * Vars.tilesize * 0.75f, y + Geometry.d4y(inputDir) * Vars.tilesize * 0.75f, inputDir * 90f);
            }
            if (blendOut) {
                Draw.rect(sliced(regions[0][frame], SliceMode.top), x + Geometry.d4x(rotation) * Vars.tilesize * 0.75f, y + Geometry.d4y(rotation) * Vars.tilesize * 0.75f, rotdeg());
            }

            Draw.z(Layer.block - 0.2f);
            Draw.rect(regions[regionIndex][frame], x, y, Vars.tilesize, Vars.tilesize * regionScl, rotdeg());

            float layer = Layer.block - 0.1f, wwidth = Vars.world.unitWidth(), wheight = Vars.world.unitHeight(), scaling = 0.01f;
            for (int i = 0; i < ids.length; i++) {
                Item item = ids[i];

                if (item != null) {
                    float scl = 1f / ids.length;

                    Tmp.v1.set(Geometry.d4x(inputDir) * Vars.tilesize / 2f, Geometry.d4y(inputDir) * Vars.tilesize / 2f)
                            .lerp(Geometry.d4x(rotation) * Vars.tilesize / 2f, Geometry.d4y(rotation) * Vars.tilesize / 2f,
                                    (Mathf.clamp(progresses[i]) * scl) + (0.75f - (scl * i)));

                    Draw.z(layer + ((x + Tmp.v1.x) / wwidth + (y + Tmp.v1.y) / wheight) * scaling);
                    Draw.rect(item.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, Vars.itemSize, Vars.itemSize);
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            // this might be bad
            for (int i = 0; i < ids.length; i++) {
                write.i(ids[i] != null ? ids[i].id : -1);
                write.f(progresses[i]);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            for (int i = 0; i < ids.length; i++) {
                int id = read.i();

                if (id >= 0) {
                    ids[i] = Vars.content.item(id);
                }

                progresses[i] = read.f();
            }
        }
    }
}
