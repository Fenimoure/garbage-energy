package net.jmorg.garbageenergy.common.blocks.scanner;

import cofh.api.tileentity.ISidedTexture;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jmorg.garbageenergy.GarbageEnergy;
import net.jmorg.garbageenergy.common.GarbageEnergyItem;
import net.jmorg.garbageenergy.common.blocks.BaseBlock;
import net.jmorg.garbageenergy.common.blocks.generator.BlockGenerator;
import net.jmorg.garbageenergy.common.items.ItemBlockGenerator;
import net.jmorg.garbageenergy.common.items.ItemBlockScanner;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.jmorg.garbageenergy.common.GarbageEnergyItem.machineBody;

public class BlockScanner extends BaseBlock
{
    public static final String NAME = GarbageEnergy.MODID + ".Scanner";

    public static IIcon[] face = new IIcon[Types.values().length];
    public static IIcon[] faceActive = new IIcon[Types.values().length];
    public static IIcon[] faceFinished = new IIcon[BlockGenerator.Types.values().length];

    public static ItemStack item;

    public BlockScanner()
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
        TileScanner.configure();
        TileItemScanner.initialize();

        item = registerItemStack(NAME + ".Item", Types.ITEM);

        return false;
    }

    private ItemStack registerItemStack(String name, Types type)
    {
        ItemStack itemStack = ItemBlockScanner.setDefaultTag(new ItemStack(this, 1, type.ordinal()));
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
        return side != 3 ? super.getIcon(side, metadata) : face[metadata % BlockGenerator.Types.values().length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegistry)
    {
        super.registerBlockIcons(iconRegistry);

        for (int i = 0; i < Types.values().length; i++) {
            face[i] = iconRegistry.registerIcon(GarbageEnergy.MODID + ":scanner/face_" + NAMES[i]);
            faceActive[i] = iconRegistry.registerIcon(GarbageEnergy.MODID + ":scanner/active_face_" + NAMES[i]);
            faceFinished[i] = iconRegistry.registerIcon(GarbageEnergy.MODID + ":scanner/finished_face_" + NAMES[i]);
        }
    }

    @Override
    public boolean postInit()
    {
        GameRegistry.addRecipe(item, "ISI", "URU", "BUB", 'I', Blocks.iron_block, 'S', GarbageEnergyItem.scel, 'U', machineBody.improved, 'R', machineBody.industrial, 'B', Blocks.redstone_block);

        return false;
    }

    public static void refreshItemStacks()
    {
        ItemBlockGenerator.setDefaultTag(item);
    }

    public static String getTileName(String tileName)
    {
        return BaseBlock.getTileName("scanner." + tileName);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (metadata >= Types.values().length) return null;

        switch (Types.values()[metadata]) {
            case ITEM:
                return new TileItemScanner();
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

    public static final String[] NAMES = {"item"};

    public enum Types
    {
        ITEM
    }
}
