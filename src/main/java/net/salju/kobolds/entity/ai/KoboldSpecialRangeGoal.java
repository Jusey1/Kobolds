package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;

public class KoboldSpecialRangeGoal<T extends AbstractKoboldEntity & RangedAttackMob> extends Goal {
	private final T kobold;
	private final double speedModifier;
	private final float attackRadiusSqr;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public KoboldSpecialRangeGoal(T t, double d, float f) {
		this.kobold = t;
		this.speedModifier = d;
		this.attackRadiusSqr = f * f;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return kobold.getTarget() != null && kobold.getTarget().isAlive() && this.isHoldingWeapon();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		super.start();
		kobold.setAggressive(true);
		kobold.startUsingItem(ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(KoboldsTags.RANGED)));
	}

	@Override
	public void stop() {
		super.stop();
		kobold.setAggressive(false);
		kobold.getMoveControl().strafe(0.0F, 0.0F);
		kobold.getNavigation().stop();
		kobold.stopUsingItem();
	}

	@Override
	public void tick() {
		super.tick();
		if (kobold.getTarget() != null) {
			double d = kobold.distanceToSqr(kobold.getTarget().getX(), kobold.getTarget().getY(), kobold.getTarget().getZ());
			boolean flag = kobold.getSensing().hasLineOfSight(kobold.getTarget());
			if (flag != this.seeTime > 0) {
				this.seeTime = 0;
			}
			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}
			if (!(d > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
				kobold.getNavigation().stop();
				++this.strafingTime;
			} else {
				kobold.getNavigation().moveTo(kobold.getTarget(), this.speedModifier);
				this.strafingTime = -1;
			}
			if (this.strafingTime >= 20) {
				if ((double) kobold.getRandom().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}
				if ((double) kobold.getRandom().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}
				this.strafingTime = 0;
			}
			if (this.strafingTime > -1) {
				if (d > (double) (this.attackRadiusSqr * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d < (double) (this.attackRadiusSqr * 0.25F)) {
					this.strafingBackwards = true;
				}
				kobold.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				Entity veh = kobold.getControlledVehicle();
				if (veh instanceof Mob target) {
					target.lookAt(kobold.getTarget(), 30.0F, 30.0F);
				}
				kobold.lookAt(kobold.getTarget(), 30.0F, 30.0F);
				kobold.getLookControl().setLookAt(kobold.getTarget().getX(), kobold.getTarget().getEyeY(), kobold.getTarget().getZ());
			} else {
				kobold.getLookControl().setLookAt(kobold.getTarget(), 30.0F, 30.0F);
				kobold.getLookControl().setLookAt(kobold.getTarget().getX(), kobold.getTarget().getEyeY(), kobold.getTarget().getZ());
			}
		}
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	protected boolean isHoldingWeapon() {
		return kobold.isHolding(stack -> stack.is(KoboldsTags.RANGED));
	}
}