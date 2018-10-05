package com.rcx.paileology;

import com.rcx.paileology.BucketRegistry.BucketInfos;
import com.rcx.paileology.proxy.CommonProxy;
import com.rcx.paileology.utils.CreativeTab;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, dependencies = ModInformation.DEPEND)
public class Paileology {

	@SidedProxy(clientSide = ModInformation.CLIENTPROXY, serverSide = ModInformation.COMMONPROXY)
	public static CommonProxy proxy;

	public static CreativeTab modTab= new CreativeTab(ModInformation.ID + ".creativeTab", new ItemStack(Items.BUCKET));

	public static NonNullList<ItemStack> emptyBuckets = NonNullList.create();
	public static NonNullList<ItemStack> filledBuckets = NonNullList.create();
	public static NonNullList<ItemStack> allBuckets = NonNullList.create();

	public static IForgeRegistry<Item> itemRegistry = null;

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
	
	@SubscribeEvent
	public static void itemRegistry(RegistryEvent.Register<Item> event) {
		itemRegistry = event.getRegistry();
		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			
			
			final ResourceLocation location = bucketInfo.bucketItem.getRegistryName();
			
			
			
			itemRegistry.register(bucketInfo.bucketItem);
		}
	}
}
