package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.minecraftforge.event.ForgeEventFactory;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.tags.BiomeTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class KoboldZombie extends Zombie {
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING = SynchedEntityData.defineId(KoboldZombie.class, EntityDataSerializers.BOOLEAN);
	private int convert;

	public KoboldZombie(EntityType<KoboldZombie> type, Level world) {
		super(type, world);
		getEyePosition(0.5F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CONVERTING, false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Convert", this.convert);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Convert")) {
			int saved = tag.getInt("Convert");
			this.convert = saved;
		}
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.225D;
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return this.isBaby() ? 0.66F : 1.26F;
	}

	protected boolean convertsInWater() {
		return false;
	}

	public boolean isBaby() {
		return false;
	}

	public boolean isConvert() {
		return this.getEntityData().get(DATA_CONVERTING);
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ZOMBIE_VILLAGER_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_VILLAGER_DEATH;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		LevelAccessor world = this.level();
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (!world.isClientSide() && this.isAlive() && !this.isNoAi()) {
			if (this.convert > 1) {
				--this.convert;
				if (!this.isConvert()) {
					this.getEntityData().set(DATA_CONVERTING, true);
				}
			} else if (this.convert == 1) {
				if (this.isAlive()) {
					this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
					ItemStack weapon = this.getMainHandItem();
					ItemStack off = this.getOffhandItem();
					if (world instanceof ServerLevel lvl) {
						if (world.getBiome(BlockPos.containing(x, y, z)).is(BiomeTags.IS_JUNGLE)) {
							if (weapon.getItem() instanceof SwordItem && weapon.isEnchanted()) {
								KoboldCaptain capty = this.convertTo(KoboldsMobs.KOBOLD_CAPTAIN.get(), true);
								ForgeEventFactory.onLivingConvert(this, capty);
							} else {
								KoboldPirate pirate = this.convertTo(KoboldsMobs.KOBOLD_PIRATE.get(), true);
								ForgeEventFactory.onLivingConvert(this, pirate);
							}
						} else if (world.getBiome(BlockPos.containing(x, y, z)).is(BiomeTags.IS_MOUNTAIN) && weapon.getItem() == Items.CROSSBOW && off.isEmpty()) {
							KoboldEngineer engi = this.convertTo(KoboldsMobs.KOBOLD_ENGINEER.get(), true);
							ForgeEventFactory.onLivingConvert(this, engi);
						} else if (off.getItem() instanceof ShieldItem) {
							KoboldWarrior war = this.convertTo(KoboldsMobs.KOBOLD_WARRIOR.get(), true);
							ForgeEventFactory.onLivingConvert(this, war);
						} else {
							Kobold basic = this.convertTo(KoboldsMobs.KOBOLD.get(), true);
							ForgeEventFactory.onLivingConvert(this, basic);
						}
					}
				}
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack apple = player.getItemInHand(hand);
		LevelAccessor world = this.level();
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		int waitTicks = 0;
		int potionLevel = 0;
		if (!world.isClientSide() && apple.getItem() == Items.GOLDEN_APPLE && this.hasEffect(MobEffects.WEAKNESS)) {
			if (world.getDifficulty() == Difficulty.EASY) {
				potionLevel = 0;
				waitTicks = 1200;
			} else if (world.getDifficulty() == Difficulty.NORMAL) {
				potionLevel = 0;
				waitTicks = 2400;
			} else if (world.getDifficulty() == Difficulty.HARD) {
				potionLevel = 1;
				waitTicks = 4800;
			}
			if (!player.getAbilities().instabuild) {
				apple.shrink(1);
			}
			player.swing(hand, true);
			this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
			this.removeEffect(MobEffects.WEAKNESS);
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, waitTicks, potionLevel));
			this.convert = waitTicks;
			this.getEntityData().set(DATA_CONVERTING, true);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		this.setCanPickUpLoot(true);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
		return super.finalizeSpawn(world, difficulty, reason, data, tag);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
		builder = builder.add(Attributes.MAX_HEALTH, 18);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
		return builder;
	}

	public static void init() {
		SpawnPlacements.register(KoboldsMobs.KOBOLD_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}
}