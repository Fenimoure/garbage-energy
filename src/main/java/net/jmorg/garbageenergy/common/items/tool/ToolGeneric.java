package net.jmorg.garbageenergy.common.items.tool;

import net.jmorg.garbageenergy.common.items.ItemGeneric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ToolGeneric extends ItemGeneric
{
    public ToolGeneric()
    {
        setMaxStackSize(1);
    }

    @Override
    public String getName()
    {
        return "generic_tool";
    }

    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int hitSide, float hitX, float hitY, float hitZ)
    {
        return false;
    }
}