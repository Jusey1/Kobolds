package net.salju.kobolds.entity.ai;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsSounds;
import net.salju.kobolds.events.KoboldsManager;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import java.util.List;

public class KoboldTradeGoal extends Goal {
	public final AbstractKoboldEntity kobold;
	public final String loc;

	public KoboldTradeGoal(AbstractKoboldEntity kobold, String str) {
		this.kobold = kobold;
		this.loc = str;
	}

	@Override
	public boolean canUse() {
		return this.isHoldingGem();
	}

	@Override
	public void start() {
		kobold.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 120, 10, false, false));
		Kobolds.queueServerWork(100, () -> {
			if (kobold.isAlive()) {
				InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(kobold, item -> new ItemStack(item).is(Items.EMERALD));
				kobold.swing(hand.equals(InteractionHand.OFF_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, true);
				kobold.playSound(KoboldsSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
				if (kobold.level() instanceof ServerLevel lvl) {
					List<ItemStack> list = KoboldsManager.getTradeItems(kobold, loc);
					Vec3 pos = LandRandomPos.getPos(kobold, 2, 1);
					Player target = lvl.getNearestPlayer(kobold, 7);
					if (target != null) {
						pos = target.position();
					} else if (pos == null) {
						pos = kobold.position();
					}
					for (ItemStack stack : list) {
						BehaviorUtils.throwItem(kobold, stack, pos);
					}
				}
				Kobolds.queueServerWork(20, () -> {
					kobold.setItemInHand(hand, ItemStack.EMPTY);
				});
			}
		});
	}

	private boolean isHoldingGem() {
		return kobold.isHolding(Items.EMERALD);
	}
}