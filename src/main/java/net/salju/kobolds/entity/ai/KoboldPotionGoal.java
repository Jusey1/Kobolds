package net.salju.kobolds.entity.ai;

import net.salju.kobolds.item.KoboldPotionUtils;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.KoboldsMod;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;

public class KoboldPotionGoal extends Goal {
	public final AbstractKoboldEntity kobold;

	public KoboldPotionGoal(AbstractKoboldEntity kobold) {
		this.kobold = kobold;
	}

	@Override
	public boolean canUse() {
		return (kobold.isHolding(stack -> stack.getItem() == KoboldsItems.KOBOLD_POTION.get()));
	}

	@Override
	public void start() {
		kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 32, -4, false, false));
		kobold.playSound(SoundEvents.GENERIC_DRINK, 0.5F, 1.0F);
		KoboldsMod.queueServerWork(32, () -> {
			InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, stack -> stack == KoboldsItems.KOBOLD_POTION.get());
			for (MobEffectInstance effect : KoboldPotionUtils.getEffects(kobold.getItemInHand(hand))) {
				kobold.addEffect(effect);
			}
			if (kobold.getOffhandItem().getItem() instanceof ShieldItem) {
				kobold.setItemInHand(hand, kobold.getPrimaryWeapon());
			} else {
				kobold.setItemInHand(hand, ItemStack.EMPTY);
			}
		});
	}
}