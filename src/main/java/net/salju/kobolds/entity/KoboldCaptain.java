package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.KoboldCaptainTradeGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class KoboldCaptain extends AbstractKoboldEntity {
	public KoboldCaptain(EntityType<KoboldCaptain> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldCaptainTradeGoal(this));
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack gem = player.getItemInHand(hand).copy();
		if (!this.level().isClientSide() && this.isAlive()) {
			if (this.getOffhandItem().isEmpty()) {
				if (gem.is(KoboldsTags.CAPTAIN_ONE) || gem.is(KoboldsTags.CAPTAIN_TWO) || gem.is(KoboldsTags.CAPTAIN_THREE)) {
					this.setItemInHand(InteractionHand.OFF_HAND, gem);
					if (!player.getAbilities().instabuild) {
						(player.getItemInHand(hand)).shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.mobInteract(player, hand);
	}
}