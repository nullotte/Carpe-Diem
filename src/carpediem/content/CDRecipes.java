package carpediem.content;

import arc.struct.*;
import carpediem.world.blocks.crafting.*;
import carpediem.world.consumers.*;
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

    public static Recipe simpleRecipe(String sector, Item in, Item out) {
        return new Recipe(sector).consumeItem(in).outputItem(out);
    }

    public static void load() {
        basicSmelterRecipes = Seq.with(
                simpleRecipe("the-reserve", rawAluminum, aluminum),
                simpleRecipe("the-reserve", rawNickel, nickel)
        );

        advancedSmelterRecipes = Seq.with(
                new Recipe("forward-outpost").consumeItems(ItemStack.with(rawSilver, 1, sand, 1)).outputItem(silver),
                new Recipe("forward-outpost").consumeItem(rawSilver).outputItem(silver).outputLiquid(slag, 0.1f),
                new Recipe(null).consumeItem(rawPlatinum).outputItem(platinum).outputLiquid(slag, 0.1f)
        );

        pressRecipes = Seq.with(
                simpleRecipe("the-reserve", aluminum, aluminumPlate),
                simpleRecipe("the-reserve", nickel, nickelPlate),
                simpleRecipe("forward-outpost", silver, silverPlate),
                simpleRecipe(null, platinum, platinumPlate),
                simpleRecipe("forward-outpost", silicon, siliconSheet),
                simpleRecipe(null, plastanium, plastaniumSheet),
                simpleRecipe(null, sturdyAlloy, alloyPlate)
        );

        rollingMillRecipes = Seq.with(
                simpleRecipe("the-reserve", aluminum, aluminumRod),
                simpleRecipe("the-reserve", nickel, nickelRod),
                simpleRecipe("forward-outpost", silver, silverRod),
                simpleRecipe(null, platinum, platinumRod),
                simpleRecipe(null, sturdyAlloy, alloyRod),
                new Recipe("the-reserve").consumeItem(aluminumRod).outputItem(aluminumWire, 2),
                new Recipe("the-reserve").consumeItem(nickelRod).outputItem(nickelWire, 2)
        );

        refineryRecipes = Seq.with(
                // oil
                new Recipe("forward-outpost")
                        .consumeLiquid(petroleum, 0.2f)
                        .outputLiquid(oil, 0.2f).outputItem(tar, 2),
                // silicon
                new Recipe("forward-outpost")
                        .consumeItem(sand, 2).consumeLiquid(oil, 0.2f)
                        .outputItem(silicon, 4),
                // pyratite
                new Recipe(null)
                        .consumeItems(ItemStack.with(sand, 1, sulfur, 2, tar, 1))
                        .outputItem(pyratite, 4),
                // plastanium
                new Recipe(null)
                        .consumeItem(aluminum, 2).consumeLiquid(oil, 0.2f).consume(new ConsumePressure())
                        .outputItem(plastanium, 3)
        );
        
        assemblerRecipes = Seq.with(
                // cogwheels
                new Recipe("the-reserve")
                        .consumeItems(ItemStack.with(aluminum, 1, aluminumPlate, 2))
                        .outputItem(aluminumCogwheel, 4),
                new Recipe("forward-outpost")
                        .consumeItems(ItemStack.with(silver, 1, silverPlate, 2))
                        .outputItem(silverCogwheel, 4),
                new Recipe(null)
                        .consumeItems(ItemStack.with(sturdyAlloy, 1, alloyPlate, 2))
                        .outputItem(alloyCogwheel, 4),
                // circuits
                new Recipe("the-reserve")
                        .consumeItems(ItemStack.with(aluminumPlate, 1, nickelWire, 4))
                        .outputItem(controlCircuit),
                new Recipe("forward-outpost")
                        .consumeItems(ItemStack.with(controlCircuit, 2, siliconSheet, 2, nickelWire, 2))
                        .outputItem(calculationCircuit),
                new Recipe(null)
                        .consumeItems(ItemStack.with(calculationCircuit, 4, platinumPlate, 2, plastanium, 2))
                        .outputItem(processingUnit),
                // other
                new Recipe("the-reserve")
                        .consumeItems(ItemStack.with(aluminum, 2, nickelPlate, 2, sulfur, 1))
                        .outputItem(powerCell),
                new Recipe("forward-outpost")
                        .consumeItems(ItemStack.with(silverPlate, 2, aluminumRod, 1))
                        .outputItem(fluidCell),
                // research cards
                new Recipe("the-reserve")
                        .consumeItems(ItemStack.with(aluminumCogwheel, 2, nickelPlate, 1))
                        .outputItem(card1),
                new Recipe("forward-outpost")
                        .consumeItems(ItemStack.with(aluminumCogwheel, 4, silverRod, 2, silicon, 2))
                        .outputItem(card2),
                new Recipe(null)
                        .consumeItems(ItemStack.with(plastanium, 2, nickelRod, 2))
                        .outputItem(card3),
                new Recipe(null)
                        .consumeItems(ItemStack.with(platinumRod, 4, plastaniumSheet, 2))
                        .outputItem(card4)
        );
    }
}
