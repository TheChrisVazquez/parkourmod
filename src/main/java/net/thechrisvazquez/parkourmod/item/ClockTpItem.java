package net.thechrisvazquez.parkourmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClockTpItem extends Item {
    public ClockTpItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer serverPlayer) {
                BlockPos spawnPos = serverPlayer.getRespawnPosition();

                if (spawnPos != null) {
                    serverPlayer.teleportTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    Component fancyMessage = Component.literal("¡Has usado el teleport!")
                            .withStyle(style -> style.withBold(true).withColor(ChatFormatting.LIGHT_PURPLE));
                    serverPlayer.sendSystemMessage(fancyMessage);
                } else {
                    serverPlayer.sendSystemMessage(Component.literal("No tienes un punto de reaparición guardado."));
                }

                level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
