package net.jmorg.garbageenergy.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.jmorg.garbageenergy.GarbageEnergy;
import net.jmorg.garbageenergy.common.items.ItemAugment;
import net.jmorg.garbageenergy.common.items.ItemDataCard;
import net.jmorg.garbageenergy.common.items.craftitems.ItemBaseMachineBody;
import net.jmorg.garbageenergy.common.items.craftitems.ItemMachineBody;
import net.jmorg.garbageenergy.common.items.craftitems.ScElement;
import net.jmorg.garbageenergy.common.items.craftitems.ScannerMatrix;
import net.jmorg.garbageenergy.common.items.tool.Wrench;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GarbageEnergyItem
{
    public static final ItemAugment augment = new ItemAugment();
    public static final ItemDataCard dataCard = new ItemDataCard();
    public static final Item wrench = new Wrench();
    public static final Item scmat = new ScannerMatrix();
    public static final ItemMachineBody machineBody = new ItemMachineBody();
    public static final ItemBaseMachineBody bmachineBody = new ItemBaseMachineBody();
    public static final Item scel = new ScElement();

    public static void registerItems()
    {
        augment.registerAugments();
        dataCard.registerCards();
        GameRegistry.registerItem(wrench, "wrench");
        GameRegistry.registerItem(scmat, "ScannerMatrix");
        machineBody.registerIMB();
        GameRegistry.registerItem(bmachineBody, "BaseMachineBody");
        GameRegistry.registerItem(scel, "scanningelement");

        GarbageEnergy.log.info(GarbageEnergy.MODNAME + ": Items are registered.");
    }

    public static void postInit()
    {
        // Cards
        GameRegistry.addRecipe(new ShapedOreRecipe(dataCard.common, "BRB", "BDB", "BBB", 'B', "dyeBlack", 'R', "dustRedstone", 'D', "gemDiamond"));
        GameRegistry.addRecipe(dataCard.uncommon, "III", "DCD", "III", 'I', Items.iron_ingot, 'C', dataCard.common, 'D', Items.diamond);
        GameRegistry.addRecipe(dataCard.rare, " D ", "DCD", " D ", 'C', dataCard.uncommon, 'D', Items.diamond);
        GameRegistry.addRecipe(dataCard.epic, "IDI", "DCD", "IDI", 'I', Items.iron_ingot, 'C', dataCard.rare, 'D', Items.diamond);

        // Augments
        GameRegistry.addRecipe(new ShapedOreRecipe(augment.energyAmplifiers[0], " N ", "NXN", " N ", 'N', "ingotIron", 'X', Items.blaze_powder));
        GameRegistry.addRecipe(new ShapedOreRecipe(augment.energyAmplifiers[1], "YNY", "BXB", "YNY", 'N', "ingotIron", 'X', Items.blaze_powder, 'B', Items.blaze_rod, 'Y', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(augment.energyAmplifiers[2], "DND", "NXN", "DND", 'N', "ingotIron", 'X', Items.blaze_powder, 'D', "gemDiamond"));

        GameRegistry.addRecipe(new ShapedOreRecipe(augment.attenuateModifiers[0], " N ", "NXN", " N ", 'N', "ingotIron", 'X', Items.gunpowder));
        GameRegistry.addRecipe(new ShapedOreRecipe(augment.attenuateModifiers[1], "YNY", "NXN", "YNY", 'N', "ingotGold", 'X', Items.gunpowder, 'Y', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(augment.attenuateModifiers[2], "DND", "NXN", "DND", 'N', "ingotGold", 'X', Items.gunpowder, 'D', "gemDiamond"));

        // scanner
        GameRegistry.addShapedRecipe(new ItemStack(scmat), "GGG", "ATA", "###", 'G', Blocks.stained_glass_pane, 'A', Blocks.redstone_lamp, 'T', Blocks.unpowered_repeater, '#', Blocks.daylight_detector);

        GameRegistry.addShapedRecipe(new ItemStack(bmachineBody), "III", "I I", "III", 'I', Items.iron_ingot);
        GameRegistry.addShapedRecipe(machineBody.standard, "GIG", "I I", "G#G", 'G', Items.gold_ingot, 'I', Items.iron_ingot, '#', GarbageEnergyItem.bmachineBody);
        GameRegistry.addShapedRecipe(machineBody.improved, "GIG", "R#R", "GIG", 'G', Items.gold_ingot, 'I', Items.iron_ingot, 'R', Items.redstone, '#', machineBody.standard);
        GameRegistry.addShapedRecipe(machineBody.advanced, "GDG", "R#R", "GDG", 'G', Items.gold_ingot, 'D', Items.diamond, 'R', Blocks.redstone_block, '#', machineBody.improved);
        GameRegistry.addShapedRecipe(machineBody.industrial, "D#D", "#D#", "D#D", 'D', Items.diamond, '#', machineBody.advanced);
        GameRegistry.addShapedRecipe(new ItemStack(scel), "ISI", "IBI", "RCR", 'I', Items.iron_ingot, 'S', GarbageEnergyItem.scmat, 'B', machineBody.standard, 'R', Items.redstone, 'C', Items.comparator);
    }
}
