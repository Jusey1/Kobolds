package net.salju.kobolds.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;

public abstract class AbstractKoboldSkeleton extends AbstractSkeleton implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(AbstractKoboldSkeleton.class, EntityDataSerializers.BOOLEAN);

	public AbstractKoboldSkeleton(EntityType<? extends AbstractKoboldSkeleton> type, Level world) {
		super(type, world);
		this.getEyePosition(0.5F);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 6.0F);
		} else {
			super.performRangedAttack(target, f);
		}
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void setChargingCrossbow(boolean check) {
		this.entityData.set(DATA_CHARGING_STATE, check);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHARGING_STATE, false);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor lvl, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		this.populateDefaultEquipmentSlots(lvl.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(lvl, lvl.getRandom(), difficulty);
		return super.finalizeSpawn(lvl, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.15F);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}
}