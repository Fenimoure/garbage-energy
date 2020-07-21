package net.jmorg.garbageenergy.common.items.craftitems;

import net.jmorg.garbageenergy.GarbageEnergy;
import net.minecraft.item.Item;

public class ScannerMatrix extends Item {
    public ScannerMatrix() {
        setMaxStackSize(3);
        setCreativeTab(GarbageEnergy.garbageEnergyTab);
        setUnlocalizedName(GarbageEnergy.MODID + ".scmatrix");
    }
}
