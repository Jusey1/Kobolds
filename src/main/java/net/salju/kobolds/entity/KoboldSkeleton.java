package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;

public class KoboldSkeleton extends Skeleton implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(KoboldSkeleton.class, EntityDataSerializers.BOOLEAN);

	public KoboldSkeleton(EntityType<KoboldSkeleton> type, Level world) {
		super(type, world);
		getEyePosition(0.5F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RangedCrossbowAttackGoal<>(this, 1.0D, 12.0F));
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.225D;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		super.performRangedAttack(target, f);
		if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 6.0F);
		}
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void shootCrossbowProjectile(LivingEntity arg0, ItemStack arg1, Projectile arg2, float arg3) {
		this.shootCrossbowProjectile(this, arg0, arg2, arg3, 1.6F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CHARGING_STATE, false);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.25F);
		this.populateDefaultEquipmentEnchantments(randy, souls);
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return this.isBaby() ? 0.66F : 1.26F;
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	public void setChargingCrossbow(boolean charging) {
		this.entityData.set(DATA_CHARGING_STATE, charging);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 18);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}

	public static void init() {
		SpawnPlacements.register(KoboldsMobs.KOBOLD_SKELETON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}
}