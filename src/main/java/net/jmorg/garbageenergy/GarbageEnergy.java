package net.jmorg.garbageenergy;

import cofh.core.CoFHProps;
import cofh.core.network.PacketCoFHBase;
import cofh.core.util.ConfigHandler;
import cofh.mod.BaseMod;
import cofh.mod.updater.UpdateManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.jmorg.garbageenergy.common.GarbageEnergyBlock;
import net.jmorg.garbageenergy.common.GarbageEnergyItem;
import net.jmorg.garbageenergy.common.blocks.generator.BlockGenerator;
import net.jmorg.garbageenergy.common.blocks.generator.TileGeneratorBase;
import net.jmorg.garbageenergy.common.blocks.scanner.BlockScanner;
import net.jmorg.garbageenergy.common.gui.GuiHandler;
import net.jmorg.garbageenergy.common.items.craftitems.ScannerMatrix;
import net.jmorg.garbageenergy.crafting.RecipeManager;
import net.jmorg.garbageenergy.network.FMLEventHandler;
import net.jmorg.garbageenergy.network.GarbageEnergyPacket;
import net.jmorg.garbageenergy.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = GarbageEnergy.MODID, name = GarbageEnergy.MODNAME, guiFactory = GarbageEnergy.MODGUIFACTORY, version = GarbageEnergy.VERSION, dependencies = GarbageEnergy.DEPENDENCIES, customProperties = @Mod.CustomProperty(k = "cofhversion", v = "true"))
public class GarbageEnergy extends BaseMod {
    public static final String MODID = "GarbageEnergy";
    public static final String MODNAME = "Garbage Energy";
    public static final String MODGUIFACTORY = "net.jmorg.garbageenergy.common.gui.GuiFactory";
    public static final String VERSION = "1.7.10R1.2.8";
    public static final String DEPENDENCIES = "required-after:CoFHCore@[1.7.10R3.1.4,1.7.10R3.2.0);";
    public static final String RELEASEURL = "https://raw.github.com/gromovjm/garbage-energy/master/version.txt";
    @Mod.Instance(MODID)
    public static GarbageEnergy instance;
    @SidedProxy(clientSide = "net.jmorg.garbageenergy.proxy.ClientProxy", serverSide = "net.jmorg.garbageenergy.proxy.ServerProxy")
    public static CommonProxy proxy;
    public static final Logger log = LogManager.getLogger(MODID);
    public static final ConfigHandler config = new ConfigHandler(VERSION);
    public static final ConfigHandler configClient = new ConfigHandler(VERSION);
    public static final GuiHandler guiHandler = new GuiHandler();

    public GarbageEnergy() {
        super(log);
    }

    public static final CreativeTabs garbageEnergyTab = new CreativeTabs("garbageEnergy") {
        @Override
        public Item getTabIconItem() {
            return BlockGenerator.itemRf.getItem();
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        UpdateManager.registerUpdater(new UpdateManager(this, RELEASEURL, CoFHProps.DOWNLOAD_URL));
        config.setConfiguration(new Configuration(new File(CoFHProps.configDir, "cofh/garbageenergy/common.cfg"), true));
        configClient.setConfiguration(new Configuration(new File(CoFHProps.configDir, "cofh/garbageenergy/client.cfg"), true));

        FMLEventHandler.initialize();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        // Common minecraft content.
        GarbageEnergyBlock.registerBlocks();
        GarbageEnergyItem.registerItems();

        // Gui
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);

        // Proxy
        MinecraftForge.EVENT_BUS.register(proxy);

        // Packets
        GarbageEnergyPacket.initialize();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GarbageEnergyBlock.postInit();
        GarbageEnergyItem.postInit();

        proxy.registerRenderInformation();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        // Clean up configs.
        cleanConfig();
        config.cleanUp(false, true);
        configClient.cleanUp(false, true);

        log.info(GarbageEnergy.MODNAME + ": Load Complete.");
    }

    @Mod.EventHandler
    public void serverStart(FMLServerAboutToStartEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartedEvent event) {
        handleIdMapping();
        RecipeManager.initialize();
    }

    public synchronized void handleIdMapping() {
        BlockGenerator.refreshItemStacks();
        BlockScanner.refreshItemStacks();
    }

    public void handleConfigSync(PacketCoFHBase payload) {
        handleIdMapping();

        TileGeneratorBase.enableSecurity = payload.getBool();

        log.info("Receiving Server Configuration...");
    }

    public PacketCoFHBase getConfigSync() {
        PacketCoFHBase payload = GarbageEnergyPacket.getPacket(GarbageEnergyPacket.PacketTypes.CONFIG_SYNC);

        payload.addBool(TileGeneratorBase.enableSecurity);

        return payload;
    }

    void cleanConfig() {
        // Common
        String[] categoryNames = config.getCategoryNames().toArray(new String[config.getCategoryNames().size()]);
        for (String name : categoryNames) {
            config.getCategory(name).setLanguageKey("config.garbageenergy." + name).setRequiresMcRestart(true);
        }

        // Client
        categoryNames = configClient.getCategoryNames().toArray(new String[configClient.getCategoryNames().size()]);
        for (String categoryName : categoryNames) {
            configClient.getCategory(categoryName).setLanguageKey("config.garbageenergy." + categoryName).setRequiresMcRestart(true);
        }
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public String getModName() {
        return MODNAME;
    }

    @Override
    public String getModVersion() {
        return VERSION;
    }
}
