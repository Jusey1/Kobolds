package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class KoboldChild extends AbstractKoboldEntity {
	private int grow;

	public KoboldChild(PlayMessages.SpawnEntity packet, Level world) {
		this(KoboldsMobs.KOBOLD_CHILD.get(), world);
	}

	public KoboldChild(EntityType<KoboldChild> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
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
		if (tag.contains("Grow")) {
			int saved = tag.getInt("Grow");
			this.grow = saved;
		}
	}

	public boolean isBaby() {
		return true;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return this.isBaby() ? 0.66F : 1.26F;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		LevelAccessor world = this.level();
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (!world.isClientSide()) {
			if (this.grow < 24000 && (this.getDisplayName().getString()).equals(Component.translatable("entity.kobolds.kobold_child").getString())) {
				++this.grow;
			} else if (this.grow >= 24000) {
				BlockPos pos = BlockPos.containing(x, y, z);
				this.discard();
				if (world instanceof ServerLevel lvl) {
					if (world.getBiome(BlockPos.containing(x, y, z)).is(BiomeTags.IS_JUNGLE)) {
						if (Math.random() < 0.06) {
							KoboldCaptain kobold = KoboldsMobs.KOBOLD_CAPTAIN.get().spawn(lvl, pos, MobSpawnType.BREEDING);
						} else {
							KoboldPirate kobold = KoboldsMobs.KOBOLD_PIRATE.get().spawn(lvl, pos, MobSpawnType.BREEDING);
						}
					} else if (Math.random() > 0.95) {
						KoboldEngineer kobold = KoboldsMobs.KOBOLD_ENGINEER.get().spawn(lvl, pos, MobSpawnType.BREEDING);
					} else if (Math.random() < 0.1) {
						KoboldEnchanter kobold = KoboldsMobs.KOBOLD_ENCHANTER.get().spawn(lvl, pos, MobSpawnType.BREEDING);
					} else {
						Kobold kobold = KoboldsMobs.KOBOLD.get().spawn(lvl, pos, MobSpawnType.BREEDING);
					}
				}
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		super.mobInteract(player, hand);
		LevelAccessor world = this.level();
		ItemStack gem = player.getItemInHand(hand);
		if (gem.is(ItemTags.create(new ResourceLocation("kobolds:kobold_breed_items")))) {
			if (!player.getAbilities().instabuild) {
				gem.shrink(1);
			}
			player.swing(hand, true);
			this.grow = this.grow + 1256;
		}
		return InteractionResult.FAIL;
	}
}