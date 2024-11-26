package carpediem.content;

import carpediem.content.blocks.*;
import carpediem.type.*;

public class CDArchives {
    public static Archive basicLogistics;

    public static void load() {
        basicLogistics = new Archive("basic-logistics") {{
            contents.addAll(
                    CDDistribution.belt,
                    CDDistribution.beltMerger,
                    CDDistribution.beltSplitter,
                    CDDistribution.beltBridge
            );
        }};
    }
}
