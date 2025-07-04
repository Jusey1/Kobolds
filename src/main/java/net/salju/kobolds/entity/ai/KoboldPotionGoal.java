package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.InteractionHand;

public class KoboldPotionGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldPotionGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return this.isHoldingPotion();
	}

	@Override
	public boolean canContinueToUse() {
		if (!kobold.isUsingItem() && this.isHoldingPotion()) {
			InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.POTION));
			if (kobold.hasEffect(MobEffects.SLOWNESS)) {
				kobold.removeEffect(MobEffects.SLOWNESS);
			}
			if (hand.equals(InteractionHand.MAIN_HAND)) {
				kobold.setItemInHand(hand, (kobold.getOffhandItem().getItem() instanceof TridentItem ? ItemStack.EMPTY : kobold.getPrimary()));
			} else {
				kobold.setItemInHand(hand, (kobold.getSecondary().getItem() instanceof TridentItem ? ItemStack.EMPTY : kobold.getSecondary()));
			}
		}
		return super.canContinueToUse();
	}

	@Override
	public void start() {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.POTION));
		kobold.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, kobold.getItemInHand(hand).getUseDuration(kobold), 10, false, false));
		kobold.startUsingItem(hand);
	}

	private boolean isHoldingPotion() {
		return kobold.isHolding(stack -> stack.is(Items.POTION));
	}
}