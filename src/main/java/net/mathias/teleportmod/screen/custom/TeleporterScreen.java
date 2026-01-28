package net.mathias.teleportmod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.component.ModDataComponentTypes;
import net.mathias.teleportmod.item.custom.TeleporterItem;
import net.mathias.teleportmod.networking.TeleportPayload;
import net.mathias.teleportmod.util.ModTags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TeleporterScreen extends HandledScreen<TeleporterScreenHandler> {

    // Constants to avoid "Magic Numbers"
    public static final Identifier GUI_TEXTURE = Identifier.of(TeleportMod.MOD_ID, "textures/gui/teleporter/teleporter_gui.png");
    private static final int TEXT_COLOR = 4210752;
    private static final float DEFAULT_ENERGY_DIVISOR = 30.0f;
    private static final int CROSS_DIMENSION_COST = 20;
    private static final int MIN_DIVISOR_FOR_DIMENSION_TRAVEL = 240;

    private TextFieldWidget coordinateInput;
    private ButtonWidget teleportButton;
    private CyclingButtonWidget<Dimension> dimensionButton;

    private Dimension currentSelectedDimension = Dimension.OVERWORLD;
    private Text distanceInfoText = Text.empty();
    private Text energyInfoText = Text.empty();

    public TeleporterScreen(TeleporterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 92;
    }

    @Override
    protected void init() {
        super.init();
        this.currentSelectedDimension = determineInitialDimension();

        int centerX = (width - backgroundWidth) / 2;
        int centerY = (height - backgroundHeight) / 2;

        setupCoordinateInput(centerX, centerY);
        setupDimensionButton(centerX, centerY);
        setupTeleportButton(centerX, centerY);

        // Initial validation
        this.updateStatus(this.coordinateInput.getText());
        this.setInitialFocus(coordinateInput);
    }

    private void setupCoordinateInput(int x, int y) {
        this.coordinateInput = new TextFieldWidget(textRenderer, x + 7, y + 17, 162, 20, Text.translatable("gui.teleportmod.teleporter.coords.name"));

        PlayerEntity player = this.client.player;
        if (player != null) {
            this.coordinateInput.setText(String.format("%d, %d, %d", player.getBlockX(), player.getBlockY(), player.getBlockZ()));
        }

        this.coordinateInput.setPlaceholder(Text.translatable("gui.teleportmod.teleporter.coords.placeholder"));
        this.coordinateInput.setChangedListener(this::updateStatus);
        this.addDrawableChild(coordinateInput);
    }

    private void setupDimensionButton(int x, int y) {
        this.dimensionButton = CyclingButtonWidget.builder(Dimension::asText)
                .values(Dimension.values())
                .initially(this.currentSelectedDimension)
                .build(x + 7, y + 41, 162, 20, Text.translatable("gui.teleportmod.teleporter.dimension_button.text"), (button, value) -> {
                    this.currentSelectedDimension = value;
                    this.updateStatus(this.coordinateInput.getText());
                });

        this.dimensionButton.active = getEnergyDivisor() >= MIN_DIVISOR_FOR_DIMENSION_TRAVEL;
        this.addDrawableChild(dimensionButton);
    }

    private void setupTeleportButton(int x, int y) {
        this.teleportButton = ButtonWidget.builder(Text.translatable("gui.teleportmod.teleporter.tp_button.text"), button -> executeTeleport())
                .dimensions(x + 7, y + 65, 50, 20)
                .build();
        this.addDrawableChild(teleportButton);
    }

    /**
     * Updates the UI text and button state based on the input.
     */
    private void updateStatus(String text) {
        if (text.isEmpty()) {
            setInfoText("dist.empty", "energy.empty");
            this.teleportButton.active = false;
            return;
        }

        Optional<Vec3d> targetCoords = parseCoordinates(text);

        if (targetCoords.isPresent() && this.client != null && this.client.player != null) {
            Vec3d target = targetCoords.get();
            double distance = Math.sqrt(this.client.player.squaredDistanceTo(target.x, target.y, target.z));
            double requiredEnergy = calculateRequiredEnergy(distance);
            int availableEnergy = calculateAvailableEnergyInInventory();

            // Update UI Texts
            if (availableEnergy >= Math.round(requiredEnergy)) {
                this.distanceInfoText = Text.translatable("gui.teleportmod.teleporter.info.dist", Math.round(distance));
                this.energyInfoText = Text.translatable("gui.teleportmod.teleporter.info.energy", Math.round(requiredEnergy));
                this.teleportButton.active = true;
            } else {
                this.distanceInfoText = Text.translatable("gui.teleportmod.teleporter.info.dist.low_energy");
                this.energyInfoText = Text.translatable("gui.teleportmod.teleporter.info.energy", Math.round(requiredEnergy));
                this.teleportButton.active = false;
            }
        } else {
            setInfoText("dist.invalid", "energy.empty");
            this.teleportButton.active = false;
        }
    }

    private void executeTeleport() {
        Optional<Vec3d> targetCoords = parseCoordinates(this.coordinateInput.getText());

        if (targetCoords.isPresent() && this.client != null && this.client.player != null) {
            Vec3d target = targetCoords.get();
            double distance = Math.sqrt(this.client.player.squaredDistanceTo(target.x, target.y, target.z));
            int energyCost = (int) Math.round(calculateRequiredEnergy(distance));

            ClientPlayNetworking.send(new TeleportPayload(target.x, target.y, target.z, energyCost, dimensionButton.getValue()));
            this.close();
        }
    }

    // --- Helper Methods ---

    private Optional<Vec3d> parseCoordinates(String text) {
        try {
            String[] parts = text.replace(" ", "").split(",");
            if (parts.length == 3) {
                return Optional.of(new Vec3d(
                        Double.parseDouble(parts[0]),
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2])
                ));
            }
        } catch (NumberFormatException ignored) {
            // Invalid format
        }
        return Optional.empty();
    }

    private double calculateRequiredEnergy(double distance) {
        double energy = distance / getEnergyDivisor();
        if (isCrossDimension()) {
            energy += CROSS_DIMENSION_COST;
        }
        return energy;
    }

    private boolean isCrossDimension() {
        if (this.client == null || this.client.world == null) return false;

        RegistryKey<World> currentWorldKey = this.client.world.getRegistryKey();
        RegistryKey<World> selectedWorldKey = getRegistryKeyFromDimension(this.currentSelectedDimension);

        return currentWorldKey != selectedWorldKey;
    }

    private int calculateAvailableEnergyInInventory() {
        if (this.client == null || this.client.player == null) return 0;

        int totalEnergy = 0;
        PlayerInventory inventory = this.client.player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && stack.isIn(ModTags.Items.ENERGY_SCOURS_ITEMS)) {
                totalEnergy += stack.getCount();
            }
        }
        return totalEnergy;
    }

    private float getEnergyDivisor() {
        if (this.client == null || this.client.player == null) return DEFAULT_ENERGY_DIVISOR;

        ItemStack stack = this.client.player.getMainHandStack();
        if (!(stack.getItem() instanceof TeleporterItem)) {
            stack = this.client.player.getOffHandStack();
        }
        return stack.getOrDefault(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, DEFAULT_ENERGY_DIVISOR);
    }

    private Dimension determineInitialDimension() {
        if (this.client == null || this.client.world == null) return Dimension.OVERWORLD;

        RegistryKey<World> currentDim = this.client.world.getRegistryKey();
        if (currentDim == World.NETHER) return Dimension.NETHER;
        if (currentDim == World.END) return Dimension.END;
        return Dimension.OVERWORLD;
    }

    private RegistryKey<World> getRegistryKeyFromDimension(Dimension dim) {
        if (dim == Dimension.NETHER) return World.NETHER;
        if (dim == Dimension.END) return World.END;
        return World.OVERWORLD;
    }

    private void setInfoText(String distKeySuffix, String energyKeySuffix) {
        this.distanceInfoText = Text.translatable("gui.teleportmod.teleporter.info." + distKeySuffix);
        this.energyInfoText = Text.translatable("gui.teleportmod.teleporter.info." + energyKeySuffix);
    }

    // --- Rendering ---

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
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, TEXT_COLOR, false);
        context.drawText(this.textRenderer, this.distanceInfoText, 61, 67, TEXT_COLOR, false);
        context.drawText(this.textRenderer, this.energyInfoText, 61, 76, TEXT_COLOR, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}