package carpediem.world.blocks.campaign;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import carpediem.world.blocks.campaign.ArchiveVault.*;
import carpediem.world.blocks.campaign.data.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class Scanner extends Block implements DataBlock {
    public float warmupSpeed = 0.019f;

    public DrawBlock drawer;

    public Scanner(String name) {
        super(name);
        update = true;
        solid = true;
        rotate = true;
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public boolean rotatedOutput(int x, int y) {
        return false;
    }

    public class ScannerBuild extends Building implements DataBuild {
        public ArchiveDataType data;
        public float warmup, totalProgress;

        @Override
        public void updateTile() {
            if (efficiency > 0f) {
                if (front() instanceof ArchiveVaultBuild vault && vault.archive != null) {
                    if (data == null || data.archive != vault.archive) {
                        data = new ArchiveDataType(vault.archive);
                    }

                    outputData();
                }

                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
            } else {
                data = null;
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;
        }

        @Override
        public boolean shouldConsume() {
            return front() instanceof ArchiveVaultBuild vault && vault.archive != null;
        }

        public void outputData() {
            if (proximity.size == 0) return;

            int dump = this.cdump;

            for (int i = 0; i < proximity.size; i++) {
                Building build = proximity.get((i + dump) % proximity.size);

                if (build instanceof DataBuild other && other.acceptData(this, data)) {
                    other.handleData(this, data);
                    incrementDump(proximity.size);
                    return;
                }

                incrementDump(proximity.size);
            }
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public DataType data() {
            return data;
        }
    }
}
