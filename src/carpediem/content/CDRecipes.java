package carpediem.content;

import arc.struct.*;
import carpediem.world.blocks.crafting.*;
import mindustry.type.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.CDLiquids.*;
import static mindustry.content.Items.*;
import static mindustry.content.Liquids.*;

public class CDRecipes {
    public static Seq<Recipe>
    basicSmelterRecipes, advancedSmelterRecipes,
    pressRecipes, rollingMillRecipes, refineryRecipes,
    assemblerRecipes;

    public static void load() {
        basicSmelterRecipes = Seq.with(
                new Recipe().consumeItem(rawAluminum).outputItem(aluminum),
                new Recipe().consumeItem(rawNickel).outputItem(nickel)
        );

        advancedSmelterRecipes = Seq.with(
                new Recipe().consumeItem(rawSilver).outputItem(silver),
                new Recipe().consumeItem(rawPlatinum).outputItem(platinum)
        );

        pressRecipes = new Seq<>();
        OrderedMap.<Item, Item>of(
                aluminum, aluminumPlate,
                nickel, nickelPlate,
                silver, silverPlate,
                platinum, platinumPlate,
                silicon, siliconSheet,
                plastanium, plastaniumSheet,
                sturdyAlloy, alloyPlate
        ).each((in, out) -> {
            pressRecipes.add(new Recipe().consumeItem(in).outputItem(out));
        });

        rollingMillRecipes = new Seq<>();
        OrderedMap.<Item, Item>of(
                aluminum, aluminumRod,
                nickel, nickelRod,
                silver, silverRod,
                platinum, platinumRod,
                sturdyAlloy, alloyRod,
                aluminumRod, aluminumWire,
                nickelRod, nickelWire
        ).each((in, out) -> {
            rollingMillRecipes.add(new Recipe().consumeItem(in).outputItem(out));
        });

        refineryRecipes = Seq.with(
                // water
                new Recipe()
                        .consumeItem(waterIce, 3)
                        .outputLiquid(water, 0.2f),
                // oil
                new Recipe()
                        .consumeLiquid(petroleum, 0.2f)
                        .outputItem(tar, 2).outputLiquid(oil, 0.2f),
                // silicon
                new Recipe()
                        .consumeItem(sand, 2).consumeLiquid(oil, 0.2f)
                        .outputItem(silicon, 4),
                // pyratite
                new Recipe()
                        .consumeItems(ItemStack.with(sand, 1, sulfur, 2, tar, 1))
                        .outputItem(pyratite, 4),
                // plastanium
                new Recipe()
                        .consumeItem(aluminum, 2).consumeLiquid(oil, 0.2f)
                        .outputItem(plastanium, 3)
        );
        // alloy should be moved to t2 smelter. recipe is 2 aluminum, 2 silicon, 1 pyratite, 0.1 water, outputs 4 alloy
        
        assemblerRecipes = Seq.with(
                // cogwheels
                new Recipe()
                        .consumeItems(ItemStack.with(aluminum, 1, aluminumPlate, 2))
                        .outputItem(aluminumCogwheel, 4),
                new Recipe()
                        .consumeItems(ItemStack.with(silver, 1, silverPlate, 2))
                        .outputItem(silverCogwheel, 4),
                new Recipe()
                        .consumeItems(ItemStack.with(sturdyAlloy, 1, alloyPlate, 2))
                        .outputItem(alloyCogwheel, 4),
                // circuits
                new Recipe()
                        .consumeItems(ItemStack.with(aluminumPlate, 1, nickelWire, 4))
                        .outputItem(controlCircuit),
                new Recipe()
                        .consumeItems(ItemStack.with(lemon, 39))
                        .outputItem(calculationCircuit),
                new Recipe()
                        .consumeItems(ItemStack.with(lemon, 39))
                        .outputItem(processingUnit),
                // other
                new Recipe()
                        .consumeItems(ItemStack.with(aluminum, 2, nickelPlate, 2, sulfur, 1))
                        .outputItem(powerCell),
                new Recipe().consumeItems(ItemStack.with(lemon, 39))
                        .outputItem(liquidCell)
        );
    }
}
