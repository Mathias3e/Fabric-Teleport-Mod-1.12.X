package net.mathias.teleportmod.component;

import com.mojang.serialization.Codec;
import net.mathias.teleportmod.TeleportMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<Float> ENERGY_EFFICIENCY = register("energy_efficiency", builder -> builder
            .codec(Codec.FLOAT)
            .packetCodec(PacketCodecs.FLOAT));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of("teleportmod", name), (builderOperator.apply(ComponentType.builder())).build());
    }

    public static void registerDataComponentTypes() {
        TeleportMod.LOGGER.info("Registering Mod NBT Data Component for " + TeleportMod.MOD_ID);
    }
}