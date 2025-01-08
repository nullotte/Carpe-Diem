package carpediem.world.blocks.campaign.data;

import mindustry.gen.*;

// surprisingly simple ?
public interface DataBuild {
    int baseDataStrength = 64;

    DataType data();

    default int dataStrength() {
        return baseDataStrength;
    }

    default float dataStrengthFrac() {
        return ((float) dataStrength()) / baseDataStrength;
    }

    default boolean acceptData(Building source, DataType data) {
        return data() == null || data() == data;
    }
    void handleData(Building source, DataType data);
}
