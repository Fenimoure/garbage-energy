package net.jmorg.garbageenergy.common.items.craftitems;

import cofh.core.item.ItemBase;
import net.jmorg.garbageenergy.GarbageEnergy;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemMachineBody extends ItemBase
{
    public ItemStack standard;
    public ItemStack improved;
    public ItemStack advanced;
    public ItemStack industrial;

    public ItemMachineBody()
    {
        super(GarbageEnergy.MODID);
        setCreativeTab(GarbageEnergy.garbageEnergyTab);
        setUnlocalizedName("machineBody");
        setHasSubtypes(true);
    }

    public void registerIMB()
    {
        standard = makeInstance(EnumRarity.common);
        improved = makeInstance(EnumRarity.uncommon);
        advanced = makeInstance(EnumRarity.rare);
        industrial = makeInstance(EnumRarity.epic);
    }

    private ItemStack makeInstance(EnumRarity enumRarity)
    {
        return addItem(enumRarity.ordinal(), enumRarity.rarityName + "MachineBody", enumRarity.ordinal(), true);
    }

}
