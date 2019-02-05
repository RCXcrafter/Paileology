package com.rcx.paileology.items;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.rcx.paileology.Paileology;
import com.rcx.paileology.utils.FluidCustomBucketWrapper;
//import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.EntityFluidCow;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemCustomBucket extends UniversalBucket {

	public static final String TAG_FLUIDS = "fluids";
	public static ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);
	public String materialName;
	public ResourceLocation baseItem;
	public Boolean heatProof;
	public Boolean breakable;
	public int color;

	public ItemCustomBucket(String name, ResourceLocation item, boolean canCarryHot, boolean canBreak, int materialColor) {
		materialName = name;
		baseItem = item;
		heatProof = canCarryHot;
		breakable = canBreak;
		setCreativeTab(Paileology.modTab);
		hasSubtypes = true;
		color = materialColor;

		MinecraftForge.EVENT_BUS.register(this);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
	}

	// allow empty buckets to stack to 16
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if(!hasFluid(stack)) {
			return 16;
		}

		return 1;
	}

	@Nonnull
	public ItemStack getEmpty() {
		return new ItemStack(this);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluidStack = getFluid(stack);
		String fluid;
		String material = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);

		if(I18n.canTranslate("material." + materialName.toLowerCase() + ".name")) {
			material = I18n.translateToLocal("material." + materialName.toLowerCase() + ".name");
		} else {
			String[] looseWords = StringUtils.splitByCharacterTypeCamelCase(material);
			material = looseWords[0];
			for(String word : looseWords) {
				if(word.equals(looseWords[0]))
					continue;
				material = material + " " + word;
			}
		}

		if(fluidStack == null && !hasSpecialFluid(stack)) {
			return I18n.translateToLocal("item.paileology.bucket_empty.name").replace("@material", material);
		}

		if(hasSpecialFluid(stack)) {
			return I18n.translateToLocal("item.paileology.bucket.name").replace("@material", material).replace("@liquid", I18n.translateToLocal("liquid." + getSpecialFluid(stack).getName() + ".name"));
		} else {
			fluid = fluidStack.getLocalizedName();
		}

		if(I18n.canTranslate("item.paileology.bucket." + materialName + "." + fluidStack.getFluid().getName() + ".name")) {
			return I18n.translateToLocal("item.paileology.bucket." + materialName + "." + fluidStack.getFluid().getName() + ".name");
		}

		return I18n.translateToLocal("item.paileology.bucket.name").replace("@material", material).replace("@liquid", fluid);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(getSpecialFluid(stack) == SpecialFluid.MILK) {
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, this.rayTrace(world, player, !hasFluid(stack)));
		if(ret != null) {
			return ret;
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onBucketEvent(FillBucketEvent event) {
		if(event.getResult() != Event.Result.DEFAULT) {
			return;
		}

		ItemStack stack = event.getEmptyBucket();
		if(stack == null || !stack.getItem().equals(this)) {
			return;
		}

		RayTraceResult target = event.getTarget();
		if(target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) {
			return;
		}

		World world = event.getWorld();
		BlockPos pos = target.getBlockPos();
		EntityPlayer player = event.getEntityPlayer();
		if(!world.isBlockModifiable(player, pos)) {
			event.setCanceled(true);
			return;
		}

		IBlockState state = world.getBlockState(pos);
		ItemStack result = null;
		if(state.getBlock() == Blocks.CAULDRON && !player.isSneaking()) {
			result = interactWithCauldron(event, player, world, pos, state, stack);

			if(event.getResult() == Result.DENY) {
				return;
			}
		}

		if (result == null) {
			if (hasFluid(stack)) {
				if (!player.canPlayerEdit(pos, target.sideHit, stack)) {
					event.setCanceled(true);
					return;
				}
				BlockPos targetPos = pos.offset(target.sideHit);

				result = tryPlaceFluid(stack, player, world, targetPos);
			} else {
				result = tryFillBucket(stack, player, world, pos, state, target.sideHit);
			}
		}
		if(result != null) {
			event.setResult(Result.ALLOW);
			event.setFilledBucket(result);
		} else {
			event.setResult(Result.DENY);
		}
	}

	private ItemStack tryFillBucket(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state, EnumFacing side) {
		ItemStack single = stack.copy();
		single.setCount(1);
		FluidActionResult result = FluidUtil.tryPickUpFluid(single, player, world, pos, side);

		if(result.isSuccess()) {
			return result.getResult();
		}
		return null;
	}

	private ItemStack tryPlaceFluid(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
		stack = stack.copy();
		FluidStack fluidStack = getFluid(stack);
		FluidActionResult result = FluidUtil.tryPlaceFluid(player, player.getEntityWorld(), pos, stack, fluidStack);
		if(result.isSuccess()) {
			if(fluidStack.getFluid() == FluidRegistry.WATER || fluidStack.getFluid() == FluidRegistry.LAVA) {
				IBlockState state = world.getBlockState(pos);
				world.neighborChanged(pos, state.getBlock(), pos);
			}
			return result.getResult();
		}
		return null;
	}

	private ItemStack interactWithCauldron(FillBucketEvent event, EntityPlayer player, World world, BlockPos pos, IBlockState state, ItemStack stack) {
		int level = state.getValue(BlockCauldron.LEVEL);
		if (!hasFluid(stack)) {
			if(level == 3) {
				if(fill(stack, new FluidStack(FluidRegistry.WATER, getCapacity()), true) == getCapacity()) {
					if(player != null) {
						player.addStat(StatList.CAULDRON_USED);
					}
					if(!world.isRemote) {
						Blocks.CAULDRON.setWaterLevel(world, pos, state, 0);
					}
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					return stack.copy();
				}
			}
			event.setResult(Result.DENY);
		} else if(getFluid(stack).getFluid() == FluidRegistry.WATER) {
			if(level < 3) {
				if(player != null) {
					player.addStat(StatList.CAULDRON_FILLED);
				}
				if(!world.isRemote) {
					Blocks.CAULDRON.setWaterLevel(world, pos, state, 3);
				}
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

				return new ItemStack(this);
			}
			event.setResult(Result.DENY);
		}
		return null;
	}

	@SubscribeEvent
	public void onItemDestroyed(PlayerDestroyItemEvent event) {
		if(!event.getOriginal().isEmpty() && event.getOriginal().getItem() == this) {
			event.getEntityPlayer().renderBrokenItemStack(new ItemStack(Item.REGISTRY.getObject(baseItem)));
		}
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		if (doesBreak(stack)) {
			return ItemStack.EMPTY;
		}
		return getEmpty().copy();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !doesBreak(stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if(!hasFluid(stack) && target instanceof EntityCow && !player.capabilities.isCreativeMode) {
			if(Loader.isModLoaded("moofluids") && target instanceof EntityFluidCow) {
				return ((EntityFluidCow) target).processInteract(player, hand);
			} else {
				if(stack.getCount() > 1) {
					stack.shrink(1);
					ItemHandlerHelper.giveItemToPlayer(player, setSpecialFluid(new ItemStack(this), SpecialFluid.MILK));
				} else {
					setSpecialFluid(stack, SpecialFluid.MILK);
				}
			}
			return true;
		}
		return false;
	}

	public boolean doesBreak(ItemStack stack) {
		if(hasSpecialFluid(stack)) {
			return false;
		}

		FluidStack fluid = getFluid(stack);
		if(fluid != null && breakable && fluid.getFluid().getTemperature() >= 450) {
			return true;
		}

		return false;
	}

	public boolean hasSpecialFluid(ItemStack stack) {
		return stack.getItemDamage() != 0;
	}

	public SpecialFluid getSpecialFluid(ItemStack stack) {
		return SpecialFluid.fromMeta(stack.getItemDamage());
	}

	public ItemStack setSpecialFluid(ItemStack stack, SpecialFluid fluid) {
		stack.setItemDamage(fluid.getMeta());
		return stack;
	}

	@Override
	public FluidStack getFluid(ItemStack container) {
		if(getSpecialFluid(container) == SpecialFluid.MILK) {
			return FluidRegistry.getFluidStack("milk", Fluid.BUCKET_VOLUME);
		}
		NBTTagCompound tags = container.getTagCompound();
		if(tags != null) {
			return FluidStack.loadFluidStackFromNBT(tags.getCompoundTag(TAG_FLUIDS));
		}

		return null;
	}

	public boolean hasFluid(ItemStack container) {
		if(hasSpecialFluid(container)) {
			return true;
		}
		return getFluid(container) != null;
	}

	public int getCapacity() {
		return Fluid.BUCKET_VOLUME;
	}

	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		if(container.getCount() != 1) {
			return 0;
		}

		if(resource == null || resource.amount < getCapacity()) {
			return 0;
		}

		if(!heatProof && resource.getFluid().getTemperature() >= 450) {
			return 0;
		}

		if(hasFluid(container)) {
			return 0;
		}

		if(resource.getFluid().getName().equals("milk")) {
			if(doFill) {
				setSpecialFluid(container, SpecialFluid.MILK);
			}
			return getCapacity();
		} else if(FluidRegistry.getBucketFluids().contains(resource.getFluid()) || resource.getFluid() == FluidRegistry.WATER || resource.getFluid() == FluidRegistry.LAVA) {
			if(doFill) {
				NBTTagCompound tag = container.getTagCompound();
				if(tag == null) {
					tag = new NBTTagCompound();
				}
				tag.setTag(TAG_FLUIDS, resource.writeToNBT(new NBTTagCompound()));
				container.setTagCompound(tag);
			}
			return getCapacity();
		}

		return 0;
	}

	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		if(container.getCount() != 1) {
			return null;
		}

		if(maxDrain < getCapacity()) {
			return null;
		}

		FluidStack fluidStack = getFluid(container);
		if(doDrain && hasFluid(container)) {
			if(doesBreak(container)) {
				container.setCount(0);
			}
			else {
				if(getSpecialFluid(container) == SpecialFluid.MILK) {
					setSpecialFluid(container, SpecialFluid.EMPTY);
				} else {
					NBTTagCompound tag = container.getTagCompound();
					if(tag != null) {
						tag.removeTag(TAG_FLUIDS);
					}
					if(tag.hasNoTags()) {
						container.setTagCompound(null);
					}
				}
			}
		}

		return fluidStack;
	}

	@Override
	@Nullable
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(getSpecialFluid(stack) != SpecialFluid.MILK) {
			return stack;
		}

		if(entityLiving instanceof EntityPlayer && !((EntityPlayer) entityLiving).capabilities.isCreativeMode) {
			setSpecialFluid(stack, SpecialFluid.EMPTY);
		}

		if(!worldIn.isRemote) {
			entityLiving.curePotionEffects(MILK_BUCKET);
		}

		if(entityLiving instanceof EntityPlayer) {
			((EntityPlayer) entityLiving).addStat(StatList.getObjectUseStats(this));
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return getSpecialFluid(stack) == SpecialFluid.MILK ? 32 : 0;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return getSpecialFluid(stack) == SpecialFluid.MILK ? EnumAction.DRINK : EnumAction.NONE;
	}

	@Override
	public int getItemBurnTime(ItemStack stack) {
		FluidStack fluid = ((ItemCustomBucket) stack.getItem()).getFluid(stack);
		if(fluid != null && fluid.getFluid() == FluidRegistry.LAVA) {
			return 20000;
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!this.isInCreativeTab(tab))
			return;

		subItems.add(new ItemStack(this));

		for(Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			if(!fluid.getName().equals("milk")) {
				if(!heatProof && fluid.getTemperature() >= 450){
					continue;
				}
				FluidStack fs = new FluidStack(fluid, getCapacity());
				ItemStack stack = new ItemStack(this);
				if(fill(stack, fs, true) == fs.amount) {
					subItems.add(stack);
				}
			}
		}
		for(SpecialFluid fluid : SpecialFluid.values()) {
			if(fluid.show()) {
				subItems.add(new ItemStack(this, 1, fluid.getMeta()));
			}
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidCustomBucketWrapper(stack, this, heatProof);
	}

	public enum SpecialFluid {
		EMPTY,
		MILK;

		private int meta;

		SpecialFluid() {
			this.meta = ordinal();
		}

		public String getName() {
			return this.toString().toLowerCase(Locale.US);
		}

		public int getMeta() {
			return meta;
		}

		public static SpecialFluid fromMeta(int meta) {
			if(meta < 0 || meta > values().length) {
				meta = 0;
			}
			return values()[meta];
		}

		public boolean show() {
			return this != EMPTY;
		}
	}
}