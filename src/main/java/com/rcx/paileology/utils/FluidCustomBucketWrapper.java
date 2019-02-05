package com.rcx.paileology.utils;

import javax.annotation.Nullable;

import com.rcx.paileology.items.ItemCustomBucket;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class FluidCustomBucketWrapper extends FluidBucketWrapper {

	public Boolean heatProof;
	ItemCustomBucket bucketItem;

	public FluidCustomBucketWrapper(ItemStack container, ItemCustomBucket bucket, Boolean canCarryHot) {
		super(container);
		bucketItem = bucket;
		heatProof = canCarryHot;
	}

	@Override
	@Nullable
	public FluidStack getFluid() {
		return bucketItem.getFluid(container);
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		if (fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == FluidRegistry.LAVA || fluid.getFluid().getName().equals("milk") || FluidRegistry.getBucketFluids().contains(fluid.getFluid())) {
			if(heatProof || fluid.getFluid().getTemperature() < 450){
				return true;
			}
		}
		return false;
	}

	protected void setFluid(@Nullable FluidStack fluidStack) {
		if (fluidStack != null) {
			container = bucketItem.getEmpty().copy();
			bucketItem.fill(container, fluidStack, true);
		} else {
			if(bucketItem.doesBreak(container)) {
				container.setCount(0);
			} else {
				container = bucketItem.getEmpty().copy();
			}
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || bucketItem.hasFluid(container) || !canFillFluidType(resource)) {
			return 0;
		}
		if (doFill) {
			setFluid(resource);
		}
		return Fluid.BUCKET_VOLUME;
	}
}
