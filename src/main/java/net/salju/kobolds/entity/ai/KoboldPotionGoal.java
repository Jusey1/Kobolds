package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.Kobolds;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
			this.start();
		}
		return super.canContinueToUse();
	}

	@Override
	public void start() {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.POTION));
		kobold.startUsingItem(hand);
		Kobolds.queueServerWork(kobold.getItemInHand(hand).getItem().getUseDuration(kobold.getItemInHand(hand), kobold) + 2, () -> {
			kobold.setItemInHand(hand, ItemStack.EMPTY);
		});
	}

	private boolean isHoldingPotion() {
		return kobold.isHolding(stack -> stack.is(Items.POTION));
	}
}