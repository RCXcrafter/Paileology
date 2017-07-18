package com.rcx.morebuckets;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ConfigHandler {

	public static Configuration config;

	// Categories
	public static String buckets = "Buckets";

	public static String[] customBucketWhitelist;
	private static String[] customBucketWhitelistDefaults = {
			"wood:minecraft:log:0:false:true:logWood",
			"birch:minecraft:log:2:false:true",
			"leather:minecraft:leather:0:false:true:leather",
			"gold:minecraft:gold_ingot:0:true:false:ingotGold"
	};

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		config.setCategoryComment(buckets, "Here you can add your own custom buckets or remove default ones");

		customBucketWhitelist = config.getStringList("oredictChiselWhitelist", buckets, customBucketWhitelistDefaults, "Whitelist for buckets created by this mod.\nSyntax is:\nname:modID:itemID:itemMetadata:heatproof:breakable:optionalOredict");

		config.save();
	}
}
