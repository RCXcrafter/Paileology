package com.rcx.morebuckets.proxy;

import javax.annotation.Nonnull;

import com.rcx.morebuckets.MoreBuckets;
import com.rcx.morebuckets.BucketRegistry;
import com.rcx.morebuckets.BucketRegistry.BucketInfos;
import com.rcx.morebuckets.items.ItemCustomBucket.SpecialFluid;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			// items
			registerItemModel(bucketInfo.bucketItem, 0, "inventory");

			// loop through the special bucket types
			for(SpecialFluid fluid : SpecialFluid.values()) {
				if(fluid != SpecialFluid.EMPTY) {
					registerItemModel(bucketInfo.bucketItem, fluid.getMeta(), fluid.getName());
				}
			}
		}

	}

	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);

		if(MoreBuckets.filledBuckets.size() > 2)
			MoreBuckets.modTab.icon = MoreBuckets.filledBuckets.get(MoreBuckets.filledBuckets.size() - 2);
	}

	private void registerItemModel(Item item, int meta, String name) {
		if(item != null) {
			// tell the game which model to use for this item-meta combination
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("morebuckets:bucket", name));
		}
	}
}
