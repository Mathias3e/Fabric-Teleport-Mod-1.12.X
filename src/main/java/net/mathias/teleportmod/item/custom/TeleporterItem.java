package net.mathias.teleportmod.item.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.mathias.teleportmod.component.ModDataComponentTypes;
import net.mathias.teleportmod.screen.custom.TeleporterScreenHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleporterItem extends Item implements ExtendedScreenHandlerFactory<ItemStack> {
    public TeleporterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (!world.isClient) {
            player.openHandledScreen(this);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        float efficiency = stack.getOrDefault(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, 30.0f);
        int efficiencyDisplay = (int) efficiency;

        int tier;
        switch (efficiencyDisplay) {
            case 30:
                tier = 1;
                break;
            case 60:
                tier = 2;
                break;
            case 120:
                tier = 3;
                break;
            case 240:
                tier = 4;
                break;
            default:
                tier = 1;
                break;
        }

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line1"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line2"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line3"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line4"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line5"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line6"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line7",
                    Text.literal(String.valueOf(efficiencyDisplay)).formatted(Formatting.YELLOW)
            ));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line8"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line9"));
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter.shift_down.line10",
                    Text.literal(String.valueOf(tier)).formatted(Formatting.YELLOW)
            ));
        } else {
            tooltip.add(Text.translatable("tooltip.teleportmod.teleporter"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ItemStack getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return serverPlayerEntity.getMainHandStack();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.teleportmod.teleporter.name");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TeleporterScreenHandler(syncId, playerInventory, player.getMainHandStack());
    }
}