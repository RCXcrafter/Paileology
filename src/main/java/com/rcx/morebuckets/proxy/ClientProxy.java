package com.rcx.morebuckets.proxy;

import javax.annotation.Nonnull;

import com.rcx.morebuckets.MoreBuckets;
import com.rcx.morebuckets.BucketRegistry;
import com.rcx.morebuckets.BucketRegistry.BucketInfos;
import com.rcx.morebuckets.items.ItemCustomBucket.SpecialFluid;
import com.rcx.morebuckets.utils.ItemBucketColor;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			String model = "morebuckets:bucket";
			if(bucketInfo.color == -1) {
				model = "morebuckets:bucket_" + bucketInfo.materialName;
			}
			// items
			registerItemModel(bucketInfo.bucketItem, 0, model, "inventory");

			// loop through the special bucket types
			for(SpecialFluid fluid : SpecialFluid.values()) {
				if(fluid != SpecialFluid.EMPTY) {
					registerItemModel(bucketInfo.bucketItem, fluid.getMeta(), model, fluid.getName());
				}
			}
		}

	}

	public void init(FMLInitializationEvent event) {
		super.init(event);

		ItemBucketColor bucketColorizer = new ItemBucketColor();

		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			if(bucketInfo.color == -1)
				continue;
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(bucketColorizer, bucketInfo.bucketItem);
		}
	}

	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);

		if(MoreBuckets.filledBuckets.size() > 2)
			MoreBuckets.modTab.icon = MoreBuckets.filledBuckets.get(MoreBuckets.filledBuckets.size() - 2);
	}

	private void registerItemModel(Item item, int meta, String model, String name) {
		if(item != null) {
			// tell the game which model to use for this item-meta combination
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(model, name));
		}
	}
}
