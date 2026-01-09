package net.mathias.teleportmod.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mathias.teleportmod.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModMessages {

    public static void registerNetworking() {
        PayloadTypeRegistry.playC2S().register(TeleportPayload.ID, TeleportPayload.CODEC);

        final RegistryKey<DamageType> TELEPORT_COLLISION_KEY = RegistryKey.of(
                RegistryKeys.DAMAGE_TYPE,
                Identifier.of("teleportmod", "teleport_collision")
        );

        ServerPlayNetworking.registerGlobalReceiver(TeleportPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                Identifier first_teleport_id = Identifier.of("teleportmod", "teleport/first_teleport");
                Identifier materialized_inside_id = Identifier.of("teleportmod", "teleport/materialized_inside");

                BlockPos targetPos = new BlockPos((int) payload.x(), (int) payload.y(), (int) payload.z());
                var world = player.getServerWorld();

                BlockState stateFeet = world.getBlockState(targetPos);
                BlockState stateHead = world.getBlockState(targetPos.up());

                boolean isObstructed = !stateFeet.getCollisionShape(world, targetPos).isEmpty() ||
                        !stateHead.getCollisionShape(world, targetPos.up()).isEmpty();

                player.getInventory().remove(
                        stack -> stack.isIn(ModTags.Items.ENERGY_SCOURS_ITEMS),
                        payload.energy(),
                        player.getInventory()
                );
                player.teleport(
                        player.getServerWorld(),
                        payload.x() + 0.5,
                        payload.y(),
                        payload.z() + 0.5,
                        player.getYaw(),
                        player.getPitch()
                );
                if (player.getServer() != null) {
                    AdvancementEntry entry = player.getServer().getAdvancementLoader().get(first_teleport_id);
                    if (entry != null) {
                        player.getAdvancementTracker().grantCriterion(entry, "first_teleport");
                    }
                }
                if (isObstructed) {
                    DamageSource damageSource = new DamageSource(
                            world.getRegistryManager()
                                    .get(RegistryKeys.DAMAGE_TYPE)
                                    .entryOf(TELEPORT_COLLISION_KEY)
                    );

                    player.damage(damageSource, Float.MAX_VALUE);

                    if (player.getServer() != null) {
                        AdvancementEntry entry = player.getServer().getAdvancementLoader().get(materialized_inside_id);
                        if (entry != null) {
                            player.getAdvancementTracker().grantCriterion(entry, "materialized_inside");
                        }
                    }
                }
            });
        });
    }
}