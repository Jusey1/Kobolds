package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import javax.annotation.Nullable;
import java.util.List;

public class KoboldBreedGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0F).ignoreLineOfSight();
    private final AbstractKoboldEntity kobold;
    private final ServerLevel lvl;
    private final double speed;
    @Nullable
    private AbstractKoboldEntity partner;
    private int love;

	public KoboldBreedGoal(AbstractKoboldEntity kobold, double s) {
		this.kobold = kobold;
        this.lvl = getServerLevel(kobold);
        this.speed = s;
	}

	@Override
	public boolean canUse() {
        if (!this.isInLove(this.kobold)) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
	}

	@Override
	public boolean canContinueToUse() {
		return this.partner != null && this.isInLove(this.partner) && this.love < 60;
	}

    @Override
    public void stop() {
        this.partner = null;
        this.love = 0;
    }

    @Override
    public void tick() {
        if (this.kobold.level() instanceof ServerLevel lvl && this.partner != null) {
            this.kobold.getLookControl().setLookAt(this.partner, 10.0F, this.kobold.getMaxHeadXRot());
            this.kobold.getNavigation().moveTo(this.partner, this.speed);
            ++this.love;
            if (this.love >= this.adjustedTickDelay(60) && this.kobold.distanceToSqr(this.partner) < 9.0F) {
                this.kobold.spawnChild(lvl, this.partner);
            }
        }
    }

    @Nullable
    private AbstractKoboldEntity getFreePartner() {
        List<? extends AbstractKoboldEntity> list = this.lvl.getNearbyEntities(this.kobold.getClass(), PARTNER_TARGETING, this.kobold, this.kobold.getBoundingBox().inflate(8.0F));
        double d = Double.MAX_VALUE;
        AbstractKoboldEntity target = null;
        for(AbstractKoboldEntity kobolds : list) {
            if (this.isInLove(this.kobold) && this.isInLove(kobolds) && this.kobold != kobolds && this.kobold.distanceToSqr(kobolds) < d) {
                target = kobolds;
                d = this.kobold.distanceToSqr(kobolds);
            }
        }
        return target;
    }

    public boolean isInLove(AbstractKoboldEntity target) {
        return target.canBreed() && target.isAlive() && !target.isAggressive() && !target.isPanicking();
    }
}