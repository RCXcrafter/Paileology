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
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		for (BucketInfos bucketInfo : BucketRegistry.bucketList) {
			String model1 = "paileology:bucket";
			if(bucketInfo.color == -1) {
				model1 = "paileology:bucket_" + bucketInfo.materialName;
			}
			final String model = model1;

			// items
			ModelLoader.setCustomModelResourceLocation(bucketInfo.bucketItem, 0, new ModelResourceLocation(model, "inventory"));

			ModelLoader.setCustomMeshDefinition(bucketInfo.bucketItem, new ItemMeshDefinition() {
				@Nonnull
				@Override
				public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
					return new ModelResourceLocation(model, "inventory");
				}
			});
			// loop through the special bucket types
			for(SpecialFluid fluid : SpecialFluid.values()) {
				if(fluid != SpecialFluid.EMPTY) {
					ModelLoader.setCustomModelResourceLocation(bucketInfo.bucketItem, fluid.getMeta(), new ModelResourceLocation(model, fluid.getName()));
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
