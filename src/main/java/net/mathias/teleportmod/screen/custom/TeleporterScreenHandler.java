package net.mathias.teleportmod.screen.custom;

import net.mathias.teleportmod.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory; // Added Import
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class TeleporterScreenHandler extends ScreenHandler {
    private final ItemStack itemStack;

    // Client-side constructor (Called by the Factory/Packet)
    // The signature here MUST match what you defined in ModScreenHandlers
    public TeleporterScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandlers.TELEPORTER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;

        // You usually add slots here (addSlot, addPlayerHotbar, etc.)
        // See Video #57 at [06:00] for how to add player inventory slots
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY; // Changed from null to ItemStack.EMPTY to prevent crashes
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true; // FIX: Must be true, or the GUI closes immediately
    }
}