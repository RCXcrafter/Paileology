package com.rcx.paileology;

import java.util.ArrayList;
import java.util.List;

import com.rcx.paileology.proxy.CommonProxy;
import com.rcx.paileology.utils.CreativeTab;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, dependencies = ModInformation.DEPEND)
public class Paileology {

	@SidedProxy(clientSide = ModInformation.CLIENTPROXY, serverSide = ModInformation.COMMONPROXY)
	public static CommonProxy proxy;

	public static CreativeTab modTab= new CreativeTab(ModInformation.ID + ".creativeTab", new ItemStack(Items.BUCKET));

	public static List<ItemStack> emptyBuckets = new ArrayList<ItemStack>();
	public static List<ItemStack> filledBuckets = new ArrayList<ItemStack>();
	public static List<ItemStack> allBuckets = new ArrayList<ItemStack>();

	@Mod.Instance
	public static Paileology instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);

		for(ItemStack bucket : Paileology.emptyBuckets){
			allBuckets.add(bucket);
		}

		for(ItemStack bucket : Paileology.filledBuckets){
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
