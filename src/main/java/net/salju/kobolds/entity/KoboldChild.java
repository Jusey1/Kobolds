package net.salju.kobolds.entity;

import net.minecraft.world.entity.LivingEntity;
import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class KoboldChild extends AbstractKoboldEntity {
	public KoboldChild(EntityType<KoboldChild> type, Level world) {
		super(type, world);
		this.setCanPickUpLoot(false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide()) {
			if (!this.getDisplayName().getString().equals(Component.translatable("entity.kobolds.kobold_child").getString())) {
				this.setAge(-24000);
			} else if (this.getAge() >= 0) {
				BlockPos pos = this.blockPosition();
				this.discard();
				if (this.level() instanceof ServerLevel lvl) {
					if (this.level().getBiome(pos).is(BiomeTags.IS_JUNGLE)) {
						if (Math.random() < 0.06) {
							KoboldCaptain kobold = KoboldsMobs.KOBOLD_CAPTAIN.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
                            this.applyDragon(kobold, lvl);
						} else {
							Kobold kobold = KoboldsMobs.KOBOLD_PIRATE.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
                            this.applyDragon(kobold, lvl);
						}
					} else if (Math.random() > 0.95) {
						KoboldEngineer kobold = KoboldsMobs.KOBOLD_ENGINEER.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
                        this.applyDragon(kobold, lvl);
					} else if (Math.random() < 0.1) {
						KoboldEnchanter kobold = KoboldsMobs.KOBOLD_ENCHANTER.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
                        this.applyDragon(kobold, lvl);
					} else {
						Kobold kobold = KoboldsMobs.KOBOLD.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
                        this.applyDragon(kobold, lvl);
					}
				}
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack gem = player.getItemInHand(hand);
		int i = this.getAge();
		if (gem.is(Items.AMETHYST_SHARD)) {
			gem.consume(1, player);
			this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

    public void applyDragon(AbstractKoboldEntity kobold, ServerLevel lvl) {
        if (kobold != null) {
            kobold.setDragonColor(this.getDragonColor());
            if (this.getDragonReference() != null && lvl.getEntity(this.getDragonReference().getUUID()) instanceof LivingEntity dragon) {
                kobold.setDragonFriend(dragon);
            }
        }
    }
}