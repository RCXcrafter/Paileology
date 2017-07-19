package com.rcx.paileology.utils;

import javax.annotation.Nullable;

import com.rcx.paileology.Paileology;
import com.rcx.paileology.items.ItemCustomBucket;
import com.rcx.paileology.proxy.CommonProxy;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
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

	@Override
	protected void setFluid(Fluid fluid) {
		if(fluid == null) {
			bucketItem.drain(container, 1000, true);

		}
		else if(FluidRegistry.getBucketFluids().contains(fluid) || fluid == FluidRegistry.LAVA
				|| fluid == FluidRegistry.WATER || fluid.getName().equals("milk")) {
			bucketItem.fill(container, new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (container.stackSize != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || bucketItem.hasFluid(container) || !canFillFluidType(resource)) {
			return 0;
		}

		if (doFill) {
			setFluid(resource.getFluid());
		}

		return Fluid.BUCKET_VOLUME;
	}
}
