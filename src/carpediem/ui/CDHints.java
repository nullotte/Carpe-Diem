package carpediem.ui;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.ui.fragments.HintsFragment.*;
import mindustry.world.*;

// it's just one hint im gonna cry
public class CDHints {
    public ObjectSet<Block> placedBlocks = new ObjectSet<>();

    public CDHints() {
        Vars.ui.hints.hints.addAll(Seq.with(CDHint.values()).select(h -> !h.finished()));

        Log.info(CDHint.cdBlockInfo.finished());
        Log.info(Vars.ui.hints.hints.contains(CDHint.cdBlockInfo));

        Events.on(BlockBuildEndEvent.class, event -> {
            if (!event.breaking && event.unit == Vars.player.unit()) {
                placedBlocks.add(event.tile.block());
            }
        });
    }

    public enum CDHint implements Hint {
        cdBlockInfo(() -> CarpeDiem.hints.placedBlocks.contains(CDCrafting.smelterT0), () -> Vars.ui.content.isShown() || Vars.ui.database.isShown());

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
                if (!Vars.mobile) text = text.replace("tap", "click").replace("Tap", "Click");
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
