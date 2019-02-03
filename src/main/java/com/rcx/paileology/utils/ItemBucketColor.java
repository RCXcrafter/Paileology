package com.rcx.paileology.utils;

import com.rcx.paileology.items.ItemCustomBucket;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemBucketColor implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(tintIndex != 1) {
			if(stack.getItem() instanceof ItemCustomBucket) {
				return ((ItemCustomBucket)stack.getItem()).color;
			}
		}
		return 0xFFFFFF;
	}
}
