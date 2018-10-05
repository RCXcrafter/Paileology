package com.rcx.paileology.proxy;

import javax.annotation.Nonnull;

import com.rcx.paileology.BucketRegistry;
import com.rcx.paileology.Paileology;
import com.rcx.paileology.BucketRegistry.BucketInfos;
import com.rcx.paileology.items.ItemCustomBucket.SpecialFluid;
import com.rcx.paileology.utils.ItemBucketColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			String model = "paileology:bucket";
			if(bucketInfo.color == -1) {
				model = "paileology:bucket_" + bucketInfo.materialName;
			}
			// items
			ModelLoader.setCustomModelResourceLocation(bucketInfo.bucketItem, 0, new ModelResourceLocation(bucketInfo.bucketItem.getRegistryName(), "inventory"));
			
			ModelLoader.setCustomMeshDefinition(bucketInfo.bucketItem, new ItemMeshDefinition() {
				@Nonnull
				@Override
				public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
					return new ModelResourceLocation(bucketInfo.bucketItem.getRegistryName(), "inventory");
				}
			});
			// loop through the special bucket types
			for(SpecialFluid fluid : SpecialFluid.values()) {
				if(fluid != SpecialFluid.EMPTY) {
					ModelLoader.setCustomModelResourceLocation(bucketInfo.bucketItem, fluid.getMeta(), new ModelResourceLocation(bucketInfo.bucketItem.getRegistryName(), fluid.getName()));
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

		if(Paileology.filledBuckets.size() > 2)
			Paileology.modTab.icon = Paileology.filledBuckets.get(Paileology.filledBuckets.size() - 2);
	}
}
