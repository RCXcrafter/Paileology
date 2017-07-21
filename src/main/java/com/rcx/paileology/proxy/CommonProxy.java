package com.rcx.paileology.proxy;

import com.rcx.paileology.BucketRegistry;
import com.rcx.paileology.ConfigHandler;
import com.rcx.paileology.BucketRegistry.BucketInfos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		for (String entry : ConfigHandler.customBucketWhitelist) {
			String[] entries = entry.split(":");
			if(entries[6].equals("custom"))
				entries[6] = "-1";
			if(entries.length == 7) {
				BucketRegistry.registerBucket(entries[0], entries[1], entries[2], Integer.parseInt(entries[3]), Boolean.parseBoolean(entries[4]), Boolean.parseBoolean(entries[5]), Integer.parseInt(entries[6], 16), null);
			} else {
				BucketRegistry.registerBucket(entries[0], entries[1], entries[2], Integer.parseInt(entries[3]), Boolean.parseBoolean(entries[4]), Boolean.parseBoolean(entries[5]), Integer.parseInt(entries[6], 16), entries[7]);
			}
		}
	}

	public void init(FMLInitializationEvent event) {
		BucketRegistry.addRecipes();
	}

	public void postInit(FMLPostInitializationEvent event) {
		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			MinecraftForge.EVENT_BUS.register(bucketInfo.bucketItem);
		}
	}
}
