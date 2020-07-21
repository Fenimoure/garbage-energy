package net.jmorg.garbageenergy.common.blocks.generator;

import cofh.api.tileentity.ISidedTexture;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jmorg.garbageenergy.GarbageEnergy;
import net.jmorg.garbageenergy.common.GarbageEnergyItem;
import net.jmorg.garbageenergy.common.blocks.BaseBlock;
import net.jmorg.garbageenergy.common.items.ItemBlockGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGenerator extends BaseBlock
{
    public static final String NAME = GarbageEnergy.MODID + ".Generator";

    public static IIcon[] face = new IIcon[Types.values().length];
    public static IIcon[] faceActive = new IIcon[Types.values().length];
    public static IIcon oppositeSide;
    public static IIcon activeOppositeSide;

    public static ItemStack itemRf;
    public static ItemStack receiver;
    public static ItemStack transmitter;

    public BlockGenerator()
    {
        super(Material.rock);
        setBlockName(NAME);
        setHardness(15.0F);
        setResistance(25.0F);

        basicGui = true;
    }

    @Override
    public boolean initialize()
    {
        TileGeneratorBase.configure();
        TileItemRFGenerator.initialize();

        itemRf = registerItemStack(NAME + ".ItemRf", Types.ITEM_RF);

        return true;
    }

    private ItemStack registerItemStack(String name, Types type)
    {
        ItemStack itemStack = ItemBlockGenerator.setDefaultTag(new ItemStack(this, 1, type.ordinal()));
        GameRegistry.registerCustomItemStack(name, itemStack);
        return itemStack;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ISidedTexture tile = (ISidedTexture) world.getTileEntity(x, y, z);
        return tile == null ? null : tile.getTexture(side, renderPass);
    }

    @Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 2) {
            return oppositeSide;
        }
        return side != 3 ? super.getIcon(side, metadata) : face[metadata % Types.values().length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegistry)
    {
        super.registerBlockIcons(iconRegistry);

        oppositeSide = iconRegistry.registerIcon(GarbageEnergy.MODID + ":generator/opposite_side");
        activeOppositeSide = iconRegistry.registerIcon(GarbageEnergy.MODID + ":generator/active_opposite_side");

        for (int i = 0; i < Types.values().length; i++) {
            face[i] = iconRegistry.registerIcon(GarbageEnergy.MODID + ":generator/face_" + NAMES[i]);
            faceActive[i] = iconRegistry.registerIcon(GarbageEnergy.MODID + ":generator/active_face_" + NAMES[i]);
        }
    }

    @Override
    public boolean postInit()
    {
        GameRegistry.addRecipe(itemRf, "I#I", "IBF", "RCR", 'C', Blocks.chest, 'F', Blocks.furnace, 'B', GarbageEnergyItem.machineBody.improved, 'R', Items.redstone, 'I', Items.iron_ingot, '#', Blocks.iron_bars);

        return true;
    }

    public static void refreshItemStacks()
    {
        ItemBlockGenerator.setDefaultTag(itemRf);
    }

    public static String getTileName(String tileName)
    {
        return BaseBlock.getTileName("generator." + tileName);
    }

//    @Override
//    public void getSubBlocks(Item item, CreativeTabs tab, List list)
//    {
//        for (int i = 0; i < Types.values().length; i++) {
//            list.add(ItemBlockGenerator.setDefaultTag(new ItemStack(item, 1, i)));
//        }
//    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (metadata >= Types.values().length) return null;

        switch (Types.values()[metadata]) {
            case ITEM_RF:
                return new TileItemRFGenerator();
            default:
                return null;
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        renderPass = pass;
        return pass < 1;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }

    @Override
    public NBTTagCompound getItemStackTag(World world, int x, int y, int z)
    {
        NBTTagCompound tag = super.getItemStackTag(world, x, y, z);
        TileGeneratorBase tile = (TileGeneratorBase) getTile(world, x, y, z);

        if (tag != null && tile != null) {
            tag.setInteger("Energy", tile.getEnergyStored(ForgeDirection.UNKNOWN));
        }

        return tag;
    }

    public static final String[] NAMES = {"itemRf"};

    public enum Types
    {
        ITEM_RF,
    }
}
