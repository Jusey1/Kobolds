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
		return kobold.isHoldingPotion();
	}

	@Override
	public boolean canContinueToUse() {
		if (!kobold.isUsingItem() && kobold.isHoldingPotion()) {
			InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.POTION) || new ItemStack(item).is(Items.GLASS_BOTTLE));
			if (kobold.hasEffect(MobEffects.SLOWNESS)) {
				kobold.removeEffect(MobEffects.SLOWNESS);
			}
            kobold.setItemInHand(hand, ItemStack.EMPTY);
		}
		return super.canContinueToUse();
	}

	@Override
	public void start() {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.POTION));
		kobold.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, kobold.getItemInHand(hand).getUseDuration(kobold), 10, false, false));
		kobold.startUsingItem(hand);
	}
}