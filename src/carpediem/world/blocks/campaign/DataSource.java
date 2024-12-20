package carpediem.world.blocks.campaign;

import carpediem.content.*;
import carpediem.world.blocks.campaign.data.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class DataSource extends Block implements DataBlock {
    public DataSource(String name) {
        super(name);
        update = true;
        solid = true;
        noUpdateDisabled = true;
        envEnabled = Env.any;
    }

    public class DataSourceBuild extends Building implements DataBuild {
        public DataType data = new ArchiveDataType(CDArchives.automation);

        @Override
        public void updateTile() {
            dumpData();
        }

        @Override
        public DataType data() {
            return data;
        }

        @Override
        public boolean acceptData(Building source, DataType data) {
            return false;
        }

        @Override
        public void handleData(Building source, DataType data) {

        }

        public void dumpData() {
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
    }
}
