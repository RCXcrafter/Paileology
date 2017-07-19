package com.rcx.paileology;

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
			"glass:minecraft:glass_pane:0:true:true:custom:paneGlassColorless",
			"gold:minecraft:gold_ingot:0:true:false:custom:ingotGold",
			"copper:thermalfoundation:material:128:true:false:FF9E49:ingotCopper",
			"tin:thermalfoundation:material:129:true:false:B1CAD3:ingotTin",
			"silver:thermalfoundation:material:130:true:false:D6F4FF:ingotSilver",
			"lead:thermalfoundation:material:131:true:false:71567F:ingotLead",
			"aluminum:thermalfoundation:material:132:true:false:FFFFFF:ingotAluminum",
			"nickel:thermalfoundation:material:133:true:false:FFF9BA:ingotNickel",
			"rubber:IC2:crafting:0:false:true:333333:itemRubber",
			"plastic:Mekanism:Polyethene:2:false:true:custom:itemPlastic",
			"steel:thermalfoundation:material:160:true:false:C6C6C6:ingotSteel",
			"electrum:thermalfoundation:material:161:true:false:FFEC60:ingotElectrum",
			"invar:thermalfoundation:material:162:true:false:EEFFCC:ingotInvar",
			"bronze:thermalfoundation:material:163:true:false:FFBB3D:ingotBronze",
			"constantan:immersiveengineering:metal:6:true:false:FFA468:ingotConstantan",
			"aluminumBrass:tconstruct:ingots:5:true:false:FFE15E:ingotAlubrass"
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
				+ "\noredict: The ore dictionary name of the material. (optional)");

		config.save();
	}
}
