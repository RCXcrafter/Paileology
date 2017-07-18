package com.rcx.morebuckets;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ConfigHandler {

	public static Configuration config;

	// Categories
	public static String buckets = "Buckets";

	public static String[] customBucketWhitelist;
	private static String[] customBucketWhitelistDefaults = {
			"wood:minecraft:log:0:false:true:custom:logWood",
			"leather:minecraft:leather:0:false:true:C65C35:leather",
			"gold:minecraft:gold_ingot:0:true:false:custom:ingotGold"
	};

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void syncConfig() {
		config.setCategoryComment(buckets, "Here you can add your own custom buckets or remove default ones");

		customBucketWhitelist = config.getStringList("oredictChiselWhitelist", buckets, customBucketWhitelistDefaults, "Whitelist for buckets created by this mod."
				+ "\nSyntax is:"
				+ "\nname:modID:itemID:metadata:heatproof:breakable:color:oredict"
				+ "\nname: The name of the material."
				+ "\nmodID: The ID of the mod that adds this item."
				+ "\nitemID: The ID of the item."
				+ "\nmetadata: The metadata value of the item, usually 0."
				+ "\nheatproof: Weather the bucket can carry hot liquids."
				+ "\nbreakable: Weather the bucket breaks upon placing a hot liquid."
				+ "\ncolor: The color of the material, use a hexadecimal color code."
				+ "\noredict: The ore dictionary name of the material (optional)");

		config.save();
	}
}
