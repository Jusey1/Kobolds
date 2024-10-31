package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.TimeUtil;
import java.util.EnumSet;

public class KoboldCrossbowGoal<T extends AbstractKoboldEntity & RangedAttackMob & CrossbowAttackMob> extends Goal {
	public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
	private final T kobold;
	private KoboldCrossbowGoal.CrossbowState crossbowState = KoboldCrossbowGoal.CrossbowState.UNCHARGED;
	private final double speedModifier;
	private final float attackRadiusSqr;
	private int seeTime;
	private int attackDelay;
	private int updatePathDelay;

	public KoboldCrossbowGoal(T t, double d1, float f1) {
		this.kobold = t;
		this.speedModifier = d1;
		this.attackRadiusSqr = f1 * f1;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		return kobold.getTarget() != null && kobold.getTarget().isAlive() && this.isHoldingCrossbow();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		super.start();
		kobold.setAggressive(true);
	}

	@Override
	public void stop() {
		super.stop();
		kobold.setAggressive(false);
		kobold.setTarget(null);
		this.seeTime = 0;
		if (kobold.isUsingItem()) {
			kobold.stopUsingItem();
			kobold.setChargingCrossbow(false);
		}
	}

	@Override
	public void tick() {
		LivingEntity target = kobold.getTarget();
		if (target != null) {
			boolean flag = kobold.getSensing().hasLineOfSight(target);
			boolean flag1 = this.seeTime > 0;
			if (flag != flag1) {
				this.seeTime = 0;
			}
			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}
			double d0 = kobold.distanceToSqr(target);
			boolean flag2 = (d0 > (double) this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0;
			if (flag2) {
				--this.updatePathDelay;
				if (this.updatePathDelay <= 0) {
					kobold.getNavigation().moveTo(target, this.canRun() ? this.speedModifier : this.speedModifier * 0.5D);
					this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(kobold.getRandom());
				}
			} else {
				this.updatePathDelay = 0;
				kobold.getNavigation().stop();
			}
			kobold.getLookControl().setLookAt(target, 30.0F, 30.0F);
			if (this.crossbowState == KoboldCrossbowGoal.CrossbowState.UNCHARGED) {
				if (!flag2) {
					kobold.startUsingItem(ProjectileUtil.getWeaponHoldingHand(kobold, item -> item instanceof CrossbowItem));
					this.crossbowState = KoboldCrossbowGoal.CrossbowState.CHARGING;
					kobold.setChargingCrossbow(true);
				}
			} else if (this.crossbowState == KoboldCrossbowGoal.CrossbowState.CHARGING) {
				if (!kobold.isUsingItem()) {
					this.crossbowState = KoboldCrossbowGoal.CrossbowState.UNCHARGED;
				}
				int i = kobold.getTicksUsingItem();
				ItemStack stack = kobold.getUseItem();
				if (i >= CrossbowItem.getChargeDuration(stack, kobold)) {
					kobold.releaseUsingItem();
					this.crossbowState = KoboldCrossbowGoal.CrossbowState.CHARGED;
					this.attackDelay = 20 + kobold.getRandom().nextInt(20);
					kobold.setChargingCrossbow(false);
				}
			} else if (this.crossbowState == KoboldCrossbowGoal.CrossbowState.CHARGED) {
				--this.attackDelay;
				if (this.attackDelay == 0) {
					this.crossbowState = KoboldCrossbowGoal.CrossbowState.READY_TO_ATTACK;
				}
			} else if (this.crossbowState == KoboldCrossbowGoal.CrossbowState.READY_TO_ATTACK && flag) {
				kobold.performRangedAttack(target, 1.0F);
				this.crossbowState = KoboldCrossbowGoal.CrossbowState.UNCHARGED;
			}
		}
	}

	private boolean isHoldingCrossbow() {
		return kobold.isHolding(stack -> stack.getItem() instanceof CrossbowItem);
	}

	private boolean canRun() {
		return this.crossbowState == KoboldCrossbowGoal.CrossbowState.UNCHARGED;
	}

	static enum CrossbowState {
		UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK;
	}
}
