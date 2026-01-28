package net.mathias.teleportmod.screen.custom;

import net.minecraft.util.Identifier;
import net.minecraft.text.Text;

public enum Dimension{
    OVERWORLD("overworld", "gui.teleportmod.teleporter.dimension_button.text_overworld"),
    NETHER("the_nether", "gui.teleportmod.teleporter.dimension_button.text_the_nether"),
    END("the_end", "gui.teleportmod.teleporter.dimension_button.text_the_end");

    private final Identifier identifier;
    private final String displayName;

    Dimension(String registryName, String displayName) {
        this.identifier = Identifier.of(registryName);
        this.displayName = displayName;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Text asText() {
        return Text.translatable(this.displayName);
    }
}