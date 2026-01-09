package net.mathias.teleportmod.networking;

import net.mathias.teleportmod.TeleportMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TeleportPayload(double x, double y, double z, int energy) implements CustomPayload {

    public static final CustomPayload.Id<TeleportPayload> ID =
            new CustomPayload.Id<>(Identifier.of(TeleportMod.MOD_ID, "teleport"));

    public static final PacketCodec<RegistryByteBuf, TeleportPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE, TeleportPayload::x,
            PacketCodecs.DOUBLE, TeleportPayload::y,
            PacketCodecs.DOUBLE, TeleportPayload::z,
            PacketCodecs.INTEGER, TeleportPayload::energy,
            TeleportPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}