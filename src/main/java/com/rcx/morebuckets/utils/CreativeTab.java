package com.rcx.morebuckets.utils;

import java.util.List;

import com.rcx.morebuckets.MoreBuckets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {

	public ItemStack icon;

	public CreativeTab(String label, ItemStack icon) {
		super(label);
		this.icon = icon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return icon;
	}

	@Override
	public Item getTabIconItem() {
		return icon.getItem();
	}

	@Override
	public int getIconItemDamage() {
		return icon.getItemDamage();
	}

	@Override
	public void displayAllRelevantItems(List<ItemStack> items) {
		//items = MoreBuckets.allBuckets;
		super.displayAllRelevantItems(items);
	}

}
