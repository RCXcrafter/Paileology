package com.rcx.paileology;

import java.util.ArrayList;
import java.util.List;

import com.rcx.paileology.items.ItemCustomBucket;
import com.rcx.paileology.items.ItemCustomBucket.SpecialFluid;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BucketRegistry {

	public static List<BucketInfos> bucketList = new ArrayList<BucketInfos>();

	public static void registerBucket(String name, String modID, String itemID, int meta, boolean canCarryHot, boolean canBreak, int color, String oredict) {
		if (!(Loader.isModLoaded(modID) || modID.equals("minecraft") || OreDictionary.doesOreNameExist(oredict)))
			return;

		Item bucketItem = registerItem(new ItemCustomBucket(name, new ResourceLocation(modID, itemID), canCarryHot, canBreak, color), name.toLowerCase());

		OreDictionary.registerOre("bucket" + name.substring(0, 1).toUpperCase() + name.substring(1), new ItemStack(bucketItem));
		OreDictionary.registerOre("bucketMilk", new ItemStack(bucketItem, 1, SpecialFluid.MILK.getMeta()));
		OreDictionary.registerOre("listAllmilk", new ItemStack(bucketItem, 1, SpecialFluid.MILK.getMeta()));

		bucketList.add(new BucketInfos(name, modID, itemID, meta, bucketItem, canCarryHot, canBreak, color, oredict));
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

			Paileology.emptyBuckets.add(new ItemStack(bucket));
			// add all fluids that the bucket can be filled with
			for(Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
				// skip milk if registered since we add it manually whether it is a
				// fluid or not
				if(!fluid.getName().equals("milk")) {
					if(!bucketInfo.canBucketCarryHot && fluid.getTemperature() >= 450){
						continue;
					}
					FluidStack fs = new FluidStack(fluid, ((ItemCustomBucket) bucket).getCapacity());
					ItemStack stack = new ItemStack(bucket);
					if(((ItemCustomBucket) bucket).fill(stack, fs, true) == fs.amount) {
						Paileology.filledBuckets.add(stack);
						//FluidContainerRegistry.registerFluidContainer(fs, stack);
					}
				}
			}
			// special fluids
			for(SpecialFluid fluid : SpecialFluid.values()) {
				if(fluid.show()) {
					Paileology.filledBuckets.add(new ItemStack(bucket, 1, fluid.getMeta()));
				}
			}
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.CAKE), "MMM", "SES", "WWW", 'M', "bucketMilk", 'S', Items.SUGAR, 'E', "egg", 'W', "cropWheat"));

		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if(fuel != null && fuel.getItem() instanceof ItemCustomBucket) {
					FluidStack fluid = ((ItemCustomBucket) fuel.getItem()).getFluid(fuel);
					if(fluid != null && fluid.getFluid() == FluidRegistry.LAVA) {
						return 20000;
					}
				}
				return 0;
			}
		});
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
		public int color;
		public String materialOredict;

		public BucketInfos(String name, String mod, String item, int meta, Item bucket, boolean canCarryHot, boolean canBreak, int materialColor, String oredict) {
			materialName = name;
			modID = mod;
			itemID = item;
			itemMeta = meta;
			bucketItem = bucket;
			canBucketCarryHot = canCarryHot;
			canBucketBreak = canBreak;
			color = materialColor;
			materialOredict = oredict;
		}
	}
}
