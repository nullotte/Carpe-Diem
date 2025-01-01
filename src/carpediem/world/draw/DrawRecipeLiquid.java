package carpediem.world.draw;

import carpediem.world.blocks.crafting.*;
import carpediem.world.blocks.crafting.RecipeCrafter.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;

// jank!!
public class DrawRecipeLiquid extends DrawLiquidTile {
    @Override
    public void draw(Building build) {
        if (build instanceof RecipeCrafterBuild crafter) {
            Recipe recipe = crafter.getCurrentRecipe();
            if (recipe != null) {
                // pretty sure every single recipe in the refinery consumes some form of liquid
                for (Consume consume : recipe.consumes) {
                    if (consume instanceof ConsumeLiquids liquids) {
                        drawLiquid = liquids.liquids[0].liquid;
                        break;
                    }
                }
            }
        }
        super.draw(build);
    }
}
