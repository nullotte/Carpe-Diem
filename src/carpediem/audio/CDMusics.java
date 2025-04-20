package carpediem.audio;

import arc.*;
import arc.audio.*;
import arc.struct.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.blocks.storage.*;

public class CDMusics {
    public static Music
    // cutscene
    reboot, land, launch,
    // ambient
    asAbove, terawatt;

    public static void load() {
        reboot = Vars.tree.loadMusic("reboot");
        land = Vars.tree.loadMusic("cd-land");
        launch = Vars.tree.loadMusic("cd-launch");

        asAbove = Vars.tree.loadMusic("as-above");
        terawatt = Vars.tree.loadMusic("terawatt");

        for (CoreBlock block : new CoreBlock[]{CDStorage.landingPodT0, CDStorage.landingPodT1}) {
            block.landMusic = land;
        }

        Seq<Music> vanillaAmbient = Vars.control.sound.ambientMusic.copy();
        Seq<Music> vanillaDark = Vars.control.sound.darkMusic.copy();
        Seq<Music> asphodelAmbient = Seq.with(asAbove, terawatt, Musics.fine);

        // kinda ass implementation but theres not really a good way to do custom music ok?
        Events.on(WorldLoadEvent.class, e -> {
            if (Vars.state.rules.planet == CDPlanets.asphodel) {
                Vars.control.sound.ambientMusic = asphodelAmbient;
                Vars.control.sound.darkMusic = asphodelAmbient;
            } else {
                Vars.control.sound.ambientMusic = vanillaAmbient;
                Vars.control.sound.darkMusic = vanillaDark;
            }
        });
    }
}
