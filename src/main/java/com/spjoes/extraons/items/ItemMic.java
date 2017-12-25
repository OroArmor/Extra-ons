package com.spjoes.extraons.items;

import java.lang.reflect.Field;
import java.util.List;

import com.mrcrayfish.device.init.DeviceBlocks;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemMic extends Item {

	public ItemMic() {
		this.setRegistryName("microphone");
		this.setUnlocalizedName("microphone");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote && playerIn.isSneaking()) {
			ItemStack stack = playerIn.getHeldItem(handIn);
			if(stack.hasTagCompound()) {
				stack.setTagCompound(null);
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && player.isSneaking()) {
			if(stack.hasTagCompound()) {
				stack.setTagCompound(null);
			}
			return EnumActionResult.SUCCESS;
		}
		if(world.getBlockState(pos).getBlock() == DeviceBlocks.LAPTOP) {
			if(isCorrectMic(stack, pos)) {
				return EnumActionResult.PASS;
			}
			if(!world.isRemote) {
				if(!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				NBTTagCompound nbt = stack.getTagCompound();
				nbt.setIntArray("devicePos", new int[] {pos.getX(), pos.getY(), pos.getZ()});
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	private static boolean hasDevicePos(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("devicePos", Constants.NBT.TAG_INT_ARRAY);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return hasDevicePos(stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String colText = "Color: ";
		try {
			EnumDyeColor col = EnumDyeColor.byMetadata(stack.getItemDamage());
			Field f = EnumDyeColor.class.getDeclaredField("chatColor");
			f.setAccessible(true);
			TextFormatting tf = (TextFormatting) f.get(col);
			colText = colText + tf + TextFormatting.BOLD + col.getDyeColorName().toUpperCase().replaceAll("_", " ");
		} catch(Exception e) {
			e.printStackTrace();
			colText = colText + TextFormatting.OBFUSCATED + TextFormatting.BOLD + "unknown";
		}
		tooltip.add(colText);
		if(hasDevicePos(stack)) {
			int[] pos = stack.getTagCompound().getIntArray("devicePos");
			tooltip.add(TextFormatting.AQUA + "Is linked to device at pos");
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.RED);
			sb.append(TextFormatting.BOLD);
			sb.append("X: ");
			sb.append(TextFormatting.RESET);
			sb.append(TextFormatting.GRAY);
			sb.append(pos[0]);
			sb.append(", ");
			sb.append(TextFormatting.GREEN);
			sb.append(TextFormatting.BOLD);
			sb.append("Y: ");
			sb.append(TextFormatting.RESET);
			sb.append(TextFormatting.GRAY);
			sb.append(pos[1]);
			sb.append(", ");
			sb.append(TextFormatting.BLUE);
			sb.append(TextFormatting.BOLD);
			sb.append("Z: ");
			sb.append(TextFormatting.RESET);
			sb.append(TextFormatting.GRAY);
			sb.append(pos[2]);
			tooltip.add(sb.toString());
		} else {
			tooltip.add("Right-click a computer to link it");
		}
	}
	
	public static boolean isCorrectMic(ItemStack stack, BlockPos pos) {
		if(stack.getItem() == ItemHandler.MICROPHONE) {
			if(hasDevicePos(stack)) {
				int[] linkpos = stack.getTagCompound().getIntArray("devicePos");
				if(linkpos[0] == pos.getX() && linkpos[1] == pos.getY() && linkpos[2] == pos.getZ()) {
					return true;
				}
			}
		}
		return false;
	}
	
}
