package carpediem.world.blocks.payloads;

import arc.struct.*;
import mindustry.world.blocks.payloads.*;
import carpediem.world.blocks.payloads.PayloadManufacturingPlant.*;

public class PayloadManufacturingComponent extends PayloadBlock {
    private static final Seq<ManufacturingComponentBuild> checkSeq = new Seq<>();

    public PayloadManufacturingComponent(String name) {
        super(name);
        rotate = true;
        acceptsPayload = true;
    }

    public class ManufacturingComponentBuild extends PayloadBlockBuild<Payload> {
        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            checkSeq.clear();
            updateChainForward();
        }

        // this is so, so, so terrible
        public void updateChainForward() {
            if (front() instanceof ManufacturingComponentBuild other && !checkSeq.contains(other)) {
                checkSeq.add(this);
                other.updateChainForward();
            } else if (front() instanceof ManufacturingPlantBuild other) {
                // found the root, tell it to update
                checkSeq.clear();
                other.updateChained();
            }
        }

        public boolean filled() {
            return payload != null && hasArrived();
        }

        @Override
        public void updateTile() {
            super.updateTile();

            moveInPayload();
        }

        @Override
        public void draw() {
            super.draw();

            if (payload != null) payload.draw();
        }
    }
}
