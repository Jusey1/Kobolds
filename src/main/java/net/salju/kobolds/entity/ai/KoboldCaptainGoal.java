package net.salju.kobolds.entity.ai;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.init.KoboldsSounds;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;

public class KoboldCaptainGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldCaptainGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return this.isHoldingGem();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.kobold.hasEffect(MobEffects.SLOWNESS);
	}

	@Override
	public void start() {
		kobold.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 120, 10, false, false));
	}

	@Override
	public void stop() {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.EMERALD));
		kobold.setItemInHand(hand, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		if (kobold.isAlive()) {
			if (this.hasEffect(20)) {
				kobold.swing(InteractionHand.MAIN_HAND, true);
				kobold.playSound(KoboldsSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
				if (kobold.level() instanceof ServerLevel lvl) {
					ItemStack stack = new ItemStack(Items.EMERALD);
					Vec3 pos = LandRandomPos.getPos(kobold, 2, 1);
					Player target = lvl.getNearestPlayer(kobold, 7);
					if (target != null) {
						pos = target.position();
					} else if (pos == null) {
						pos = kobold.position();
					}
					InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.EMERALD));
					stack.setCount(Mth.randomBetweenInclusive(kobold.getRandom(), kobold.getItemInHand(hand).is(KoboldsTags.VALUABLE) ? 21 : 2, kobold.getItemInHand(hand).is(KoboldsTags.VALUABLE) ? 36 : 7));
					BehaviorUtils.throwItem(kobold, stack, pos);
				}
			}
		}
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private boolean hasEffect(int i) {
		return this.kobold.hasEffect(MobEffects.SLOWNESS) && this.kobold.getEffect(MobEffects.SLOWNESS).getDuration() == i;
	}

	private boolean isHoldingGem() {
		return kobold.isHolding(stack -> stack.is(KoboldsTags.CAPTAIN));
	}
}