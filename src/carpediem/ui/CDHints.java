package carpediem.ui;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.type.*;
import mindustry.ui.fragments.HintsFragment.*;
import mindustry.world.*;

public class CDHints {
    public ObjectSet<String> events = new ObjectSet<>();
    public ObjectSet<Block> placedBlocks = new ObjectSet<>();
    public static UnitType[] droneTypes = {};

    public CDHints() {
        // idk if this is necessary or not lol
        droneTypes = new UnitType[]{CDUnitTypes.carver, CDUnitTypes.heap, CDUnitTypes.myriad};

        Vars.ui.hints.hints.addAll(Seq.with(CDHint.values()).select(h -> !h.finished()));

        Events.on(BlockBuildEndEvent.class, e -> {
            if (!e.breaking && e.unit == Vars.player.unit()) {
                placedBlocks.add(e.tile.block());
            }
        });

        Events.on(ConfigEvent.class, e -> {
            if (e.player == Vars.player) {
                if (e.tile.block == CDCrafting.rollingMillT1 || e.tile.block == CDCrafting.assemblerT1) {
                    events.add("crafterconfig");
                }
                if (e.tile.block == CDDistribution.beltBridge) {
                    events.add("beltbridgeconfig");
                }
            }
        });
    }

    public enum CDHint implements Hint {
        beltBridgeConfig(() -> CarpeDiem.hints.placedBlocks.contains(CDDistribution.beltBridge), () -> CarpeDiem.hints.events.contains("beltbridgeconfig")),
        cdBlockInfo(() -> CarpeDiem.hints.placedBlocks.contains(CDCrafting.smelterT0), () -> Vars.ui.content.isShown() || Vars.ui.database.isShown()),
        crafterConfig(() -> CarpeDiem.hints.placedBlocks.contains(CDCrafting.rollingMillT1) || CarpeDiem.hints.placedBlocks.contains(CDCrafting.assemblerT1), () -> CarpeDiem.hints.events.contains("crafterconfig")),
        valves(() -> CarpeDiem.hints.placedBlocks.contains(CDCrafting.refineryT1), () -> CarpeDiem.hints.placedBlocks.contains(CDLiquidBlocks.valve)),
        smeltingSilver(() -> CDItems.rawSilver.unlockedNow(), () -> false),
        droneCharging(() -> {
            for (UnitType unitType : droneTypes) {
                if (Vars.state.rules.defaultTeam.data().countType(unitType) > 0) return true;
            }
            return false;
        }, () -> false);

        CDHint(Boolp shown, Boolp complete) {
            this.shown = shown;
            this.complete = complete;
        }

        public String text;
        public boolean finished, cached;
        final Boolp complete, shown;

        @Override
        public boolean finished() {
            if (!cached) {
                cached = true;
                finished = Core.settings.getBool(name() + "-hint-done", false);
            }
            return finished;
        }

        @Override
        public void finish() {
            Core.settings.put(name() + "-hint-done", finished = true);
        }

        @Override
        public String text() {
            if (text == null) {
                text = Vars.mobile && Core.bundle.has("hint." + name() + ".mobile") ? Core.bundle.get("hint." + name() + ".mobile") : Core.bundle.get("hint." + name());
            }
            return text;
        }

        @Override
        public boolean complete() {
            return complete.get();
        }

        @Override
        public boolean show() {
            return shown.get();
        }

        @Override
        public int order() {
            return ordinal();
        }

        @Override
        public boolean valid() {
            return true;
        }
    }
}
