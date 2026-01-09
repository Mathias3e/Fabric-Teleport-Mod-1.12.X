package net.mathias.teleportmod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.component.ModDataComponentTypes;
import net.mathias.teleportmod.item.ModItems;
import net.mathias.teleportmod.util.ModTags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mathias.teleportmod.networking.TeleportPayload;

public class TeleporterScreen extends HandledScreen<TeleporterScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(TeleportMod.MOD_ID, "textures/gui/teleporter/teleporter_gui.png");

    private TextFieldWidget textInput;
    private ButtonWidget teleport_button;

    private Text infoText_distance = Text.empty();
    private Text infoText_energy_usage = Text.empty();

    public TeleporterScreen(TeleporterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 68;
    }

    private float getEnergyDivisor() {
        if (this.client == null || this.client.player == null) return 30.0f;
        ItemStack stack = this.client.player.getMainHandStack();
        if (!(stack.getItem() instanceof net.mathias.teleportmod.item.custom.TeleporterItem)) {
            stack = this.client.player.getOffHandStack();
        }
        return stack.getOrDefault(ModDataComponentTypes.ENERGY_EFFICIENCY, 30.0f);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        this.textInput = new TextFieldWidget(textRenderer, x + 7, y + 17, 162, 20, Text.translatable("gui.teleportmod.teleporter.coords.name"));

        this.teleport_button = ButtonWidget.builder(Text.translatable("gui.teleportmod.teleporter.button.text"), (button) -> { this.onTeleportButtonClick(); })
                .dimensions(x + 7, y + 41, 50, 20)
                .build();

        this.textInput.setChangedListener(text -> {
            this.updateStatusCheck(text);
        });

        this.updateStatusCheck(this.textInput.getText());

        int px = this.client.player.getBlockX();
        int py = this.client.player.getBlockY();
        int pz = this.client.player.getBlockZ();

        this.textInput.setDrawsBackground(true);
        this.textInput.setText(px + ", " + py + ", " + pz);
        this.textInput.setPlaceholder(Text.translatable("gui.teleportmod.teleporter.coords.placeholder"));

        this.addDrawableChild(textInput);
        this.addDrawableChild(teleport_button);

        this.setInitialFocus(textInput);
    }

    private void updateStatusCheck(String text) {
        if (text.isEmpty()) {
            this.infoText_distance = Text.translatable("gui.teleportmod.teleporter.info.dist.empty");
            this.infoText_energy_usage = Text.translatable("gui.teleportmod.teleporter.info.energy.empty");
            this.teleport_button.active = false;
            return;
        }

        try {
            String[] parts = text.replace(" ", "").split(",");

            if (parts.length == 3) {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);

                double dist = Math.sqrt(this.client.player.squaredDistanceTo(x, y, z));

                double divisor = getEnergyDivisor();
                double energy = dist / divisor;

                int amountEnergyOrb = 0;
                PlayerInventory inventory = this.client.player.getInventory();

                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack stack = inventory.getStack(i);

                    if (!stack.isEmpty() && stack.isIn(ModTags.Items.ENERGY_SCOURS_ITEMS)) {
                        amountEnergyOrb += stack.getCount();
                    }
                }

                if (amountEnergyOrb >= (int) Math.round(energy)) {
                    this.infoText_distance = Text.translatable("gui.teleportmod.teleporter.info.dist", (int) Math.round(dist));
                    this.infoText_energy_usage = Text.translatable("gui.teleportmod.teleporter.info.energy", (int) Math.round(energy));
                    this.teleport_button.active = true;
                } else {
                    this.infoText_distance = Text.translatable("gui.teleportmod.teleporter.info.dist.low_energy");
                    this.infoText_energy_usage = Text.translatable("gui.teleportmod.teleporter.info.energy", (int) Math.round(energy));
                    this.teleport_button.active = false;
                }
            } else {
                this.infoText_distance = Text.translatable("gui.teleportmod.teleporter.info.dist.empty");
                this.infoText_energy_usage = Text.translatable("gui.teleportmod.teleporter.info.energy.empty");
                this.teleport_button.active = false;
            }
        } catch (NumberFormatException e) {
            this.infoText_distance = Text.translatable("gui.teleportmod.teleporter.info.dist.invalid");
            this.infoText_energy_usage = Text.translatable("gui.teleportmod.teleporter.info.energy.empty");
            this.teleport_button.active = false;
        }
    }

    private void onTeleportButtonClick() {
        String text = this.textInput.getText();
        String[] coords = text.replace(" ", "").split(",");

        if (coords.length == 3) {
            try {
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                double z = Double.parseDouble(coords[2]);

                double dist = Math.sqrt(this.client.player.squaredDistanceTo(x, y, z));
                double energy = dist/30; //replace 30 with nbt value

                ClientPlayNetworking.send(new TeleportPayload(x, y, z, (int) Math.round(energy)));

                this.close();

            } catch (NumberFormatException e) {
                //System.out.println("Keine gültigen Zahlen!");
            }
        } else {
            //System.out.println("Falsches Format!");
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
        context.drawText(this.textRenderer, this.infoText_distance, 61, 43, 4210752, false);
        context.drawText(this.textRenderer, this.infoText_energy_usage, 61, 52, 4210752, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}