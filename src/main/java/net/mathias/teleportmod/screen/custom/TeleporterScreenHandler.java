package net.mathias.teleportmod.screen.custom;

import net.mathias.teleportmod.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory; // Added Import
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class TeleporterScreenHandler extends ScreenHandler {
    private final ItemStack itemStack;

    public TeleporterScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandlers.TELEPORTER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}