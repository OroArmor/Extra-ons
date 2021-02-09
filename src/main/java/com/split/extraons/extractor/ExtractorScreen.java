package com.split.extraons.extractor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ExtractorScreen extends AbstractFurnaceScreen<ExtractorScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final Identifier BACKGROUND = new Identifier("textures/gui/container/furnace.png");

    public ExtractorScreen(ExtractorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new FurnaceRecipeBookScreen(), inventory, title, BACKGROUND);
    }
}
