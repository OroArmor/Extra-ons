package com.spjoes.extraons.proxies;

import com.spjoes.extraons.Constants;
import com.spjoes.extraons.client.TESRCentralUnit;
import com.spjoes.extraons.items.ItemHandler;
import com.spjoes.extraons.tileentities.TileEntityCentralUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerModels() {
		for(EnumDyeColor col : EnumDyeColor.values()) {
			this.registerModel(ItemHandler.MICROPHONE, col.getMetadata(), new ResourceLocation(Constants.MODID, "mic/" + col.getName()));
			this.registerModel(ItemHandler.HEADPHONES, col.getMetadata(), new ResourceLocation(Constants.MODID, "headphones/" + col.getName()));
		}
		this.registerModel(ItemHandler.MICROPHONE, 16, new ResourceLocation(Constants.MODID, "mic/white"));
		this.registerModel(ItemHandler.HEADPHONES, 16, new ResourceLocation(Constants.MODID, "headphones/white"));
		this.registerModel(ItemHandler.MONITOR_ITEM, 0, new ResourceLocation(Constants.MODID, "monitor"));
		this.registerModel(ItemHandler.MOUSE_ITEM, 0, new ResourceLocation(Constants.MODID, "mouse"));
		this.registerModel(ItemHandler.CONSOLE, 0, new ResourceLocation(Constants.MODID, "console_creeper"));
		this.registerModel(ItemHandler.CONSOLE, 1, new ResourceLocation(Constants.MODID, "console_pig"));
		this.registerModel(ItemHandler.CONSOLE, 2, new ResourceLocation(Constants.MODID, "console_cow"));
		this.registerModel(ItemHandler.CENTRAL_UNIT_ITEM, 0, new ResourceLocation(Constants.MODID, "central_unit"));
		this.registerModel(ItemHandler.HDMI_CABLE, 0, new ResourceLocation(Constants.MODID, "hdmi_cable"));
		this.registerModel(ItemHandler.TAB_ICON, 0, new ResourceLocation(Constants.MODID, "tab_icon"));
		this.registerModel(ItemHandler.PHONE, 0, new ResourceLocation(Constants.MODID, "phone"));
		this.registerModel(ItemHandler.TV_ITEM, 0, new ResourceLocation(Constants.MODID, "tv"));
		this.registerModel(ItemHandler.CONTROLLER, 0, new ResourceLocation(Constants.MODID, "controller"));
	}
	
	@Override
	public void registerItemColors() {
		IItemColor jeb = new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				int time = (int) (Minecraft.getSystemTime()%6000);
				int r = 0, g = 0, b = 0;
				if(time < 1000) {
					r = 255;
					g = (int) ((time/1000.0)*255);
				} else if(time < 2000) {
					g = 255;
					r = (int) ((1.0-((time-1000)/1000.0))*255);
				} else if(time < 3000) {
					g = 255;
					b = (int) (((time-2000)/1000.0)*255);
				} else if(time < 4000) {
					b = 255;
					g = (int) ((1.0-((time-3000)/1000.0))*255);
				} else if(time < 5000) {
					b = 255;
					r = (int) (((time-4000)/1000.0)*255);
				} else {
					r = 255;
					b = (int) ((1.0-((time-5000)/1000.0))*255);
				}
				return tintIndex == 1 && stack.getItemDamage() == 16 ? ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0) : 0xFFFFFF;
			}
		};
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(jeb, ItemHandler.HEADPHONES, ItemHandler.MICROPHONE);
	}
	
	private void registerModel(Item item, int damage, ResourceLocation rl) {
		ModelLoader.setCustomModelResourceLocation(item, damage, new ModelResourceLocation(rl, "inventory"));
	}
	
	@Override
	public void registerTERenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCentralUnit.class, new TESRCentralUnit());
	}
	
}
