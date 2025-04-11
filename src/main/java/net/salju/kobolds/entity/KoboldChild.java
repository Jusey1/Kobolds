package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.BiomeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class KoboldChild extends AbstractKoboldEntity {
	private int grow;

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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Grow", this.grow);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.grow = tag.getInt("Grow").orElse(0);
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide()) {
			if (this.grow < 24000 && this.getDisplayName().getString().equals(Component.translatable("entity.kobolds.kobold_child").getString())) {
				++this.grow;
			} else if (this.grow >= 24000) {
				BlockPos pos = this.blockPosition();
				this.discard();
				if (this.level() instanceof ServerLevel lvl) {
					if (this.level().getBiome(pos).is(BiomeTags.IS_JUNGLE)) {
						if (Math.random() < 0.06) {
							KoboldsMobs.KOBOLD_CAPTAIN.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
						} else {
							KoboldsMobs.KOBOLD_PIRATE.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
						}
					} else if (Math.random() > 0.95) {
						KoboldsMobs.KOBOLD_ENGINEER.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
					} else if (Math.random() < 0.1) {
						KoboldsMobs.KOBOLD_ENCHANTER.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
					} else {
						KoboldsMobs.KOBOLD.get().spawn(lvl, pos, EntitySpawnReason.BREEDING);
					}
				}
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack gem = player.getItemInHand(hand);
		if (gem.is(Items.AMETHYST_SHARD)) {
			if (!player.isCreative()) {
				gem.shrink(1);
			}
			this.grow = this.grow + 1256;
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}
}