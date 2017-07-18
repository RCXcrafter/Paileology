package com.rcx.morebuckets.proxy;

import java.util.ArrayList;
import java.util.List;

import com.rcx.morebuckets.BucketRegistry;
import com.rcx.morebuckets.ConfigHandler;
import com.rcx.morebuckets.MoreBuckets;
import com.rcx.morebuckets.items.ItemCustomBucket;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		ConfigHandler.init(event.getSuggestedConfigurationFile());




		for (String entry : ConfigHandler.customBucketWhitelist) {
			String[] entries = entry.split(":");
			if(entries.length == 6) {
				BucketRegistry.registerBucket(entries[0], entries[1], entries[2], Integer.parseInt(entries[3]), Boolean.parseBoolean(entries[4]), Boolean.parseBoolean(entries[5]), null);
			} else {
				BucketRegistry.registerBucket(entries[0], entries[1], entries[2], Integer.parseInt(entries[3]), Boolean.parseBoolean(entries[4]), Boolean.parseBoolean(entries[5]), entries[6]);
			}
		}
		
	}

	public void init(FMLInitializationEvent event) {
		BucketRegistry.addRecipes();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}
}
