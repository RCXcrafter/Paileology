package com.rcx.morebuckets;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rcx.morebuckets.BucketRegistry.BucketInfos;
import com.rcx.morebuckets.items.ItemCustomBucket;
import com.rcx.morebuckets.proxy.CommonProxy;
import com.rcx.morebuckets.utils.CreativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, dependencies = ModInformation.DEPEND)//, guiFactory = ModInformation.GUIFACTORY)
public class MoreBuckets {

	@SidedProxy(clientSide = ModInformation.CLIENTPROXY, serverSide = ModInformation.COMMONPROXY)
	public static CommonProxy proxy;

	public static CreativeTab modTab= new CreativeTab(ModInformation.ID + ".creativeTab", new ItemStack(Items.BUCKET));
	public static Logger logger = LogManager.getLogger(ModInformation.NAME);

	public static List<ItemStack> emptyBuckets = new ArrayList<ItemStack>();
	public static List<ItemStack> filledBuckets = new ArrayList<ItemStack>();
	public static List<ItemStack> allBuckets = new ArrayList<ItemStack>();

	@Mod.Instance
	public static MoreBuckets instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);

		for(ItemStack bucket : MoreBuckets.emptyBuckets){
			allBuckets.add(bucket);
		}

		for(ItemStack bucket : MoreBuckets.filledBuckets){
			allBuckets.add(bucket);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
