package net.mathias.teleportmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeProvider extends FabricDynamicRegistryProvider {

    public ModDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        entries.add(
                RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("teleportmod", "teleport_collision")),
                new DamageType(
                        "teleport_collision",
                        DamageScaling.NEVER,
                        0.1f,
                        DamageEffects.HURT,
                        DeathMessageType.DEFAULT
                )
        );
    }

    @Override
    public String getName() {
        return "Mod Damage Types";
    }
}