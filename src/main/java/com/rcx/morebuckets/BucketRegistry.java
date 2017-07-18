package com.rcx.morebuckets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.rcx.morebuckets.items.ItemCustomBucket;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BucketRegistry {

	public static List<BucketInfos> bucketList = new ArrayList<BucketInfos>();

	public static void registerBucket(String name, String modID, String itemID, int meta, boolean canCarryHot, boolean canBreak, String oredict) {
		if (!Loader.isModLoaded(modID) && !modID.equals("minecraft"))
			return;

		Block compressedBlock;
		List<Integer> existingLevels = new ArrayList<Integer>();
		
		Item bucketItem = registerItem(new ItemCustomBucket(name, new ResourceLocation(modID, itemID), canCarryHot, canBreak), name);

		bucketList.add(new BucketInfos(name, modID, itemID, meta, bucketItem, canCarryHot, canBreak, oredict));
	}

	public static void addRecipes() {
		for (BucketInfos bucketInfo : bucketList) {
			String oredict = bucketInfo.materialOredict;
			Item bucket = bucketInfo.bucketItem;
			Item baseItem = Item.REGISTRY.getObject(new ResourceLocation(bucketInfo.modID, bucketInfo.itemID));

			if (OreDictionary.doesOreNameExist(oredict))
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bucket, 1, 0), "X X", " X ", 'X', oredict));
			else
				GameRegistry.addRecipe(new ItemStack(bucket, 1, 0), "X X", " X ", 'X', new ItemStack(baseItem, 1, bucketInfo.itemMeta));
		}
	}

	private static <T extends Item> T registerItem(T item, String name) {
		item.setUnlocalizedName(name);
		item.setRegistryName(name);
		GameRegistry.register(item);

		return item;
	}

	public static class BucketInfos {
		public String materialName;
		public String modID;
		public String itemID;
		public int itemMeta;
		public Item bucketItem;
		public boolean canBucketCarryHot;
		public boolean canBucketBreak;
		public String materialOredict;

		public BucketInfos(String name, String mod, String item, int meta, Item bucket, boolean canCarryHot, boolean canBreak, String oredict) {
			materialName = name;
			modID = mod;
			itemID = item;
			itemMeta = meta;
			bucketItem = bucket;
			canBucketCarryHot = canCarryHot;
			canBucketBreak = canBreak;
			materialOredict = oredict;
		}
	}
}
