package com.rcx.paileology.proxy;

import com.rcx.paileology.BucketRegistry;
import com.rcx.paileology.Paileology;
import com.rcx.paileology.BucketRegistry.BucketInfos;
import com.rcx.paileology.items.ItemCustomBucket.SpecialFluid;
import com.rcx.paileology.utils.ItemBucketColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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

		if(Paileology.filledBuckets.size() > 2)
			Paileology.modTab.icon = Paileology.filledBuckets.get(Paileology.filledBuckets.size() - 2);
	}

	private void registerItemModel(Item item, int meta, String model, String name) {
		if(item != null) {
			// tell the game which model to use for this item-meta combination
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(model, name));
		}
	}
}
