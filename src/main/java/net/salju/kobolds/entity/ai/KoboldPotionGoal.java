package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.Kobolds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
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
		return (kobold.isHolding(stack -> stack.getItem() == Items.POTION));
	}

	@Override
	public void start() {
		kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 4, false, false));
		kobold.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, 1.0F);
		Kobolds.queueServerWork(32, () -> {
			InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, stack -> stack == Items.POTION);
			kobold.getItemInHand(hand).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).forEachEffect(effect -> {
				kobold.addEffect(effect);
			});
			kobold.setItemInHand(hand, ItemStack.EMPTY);
		});
	}
}