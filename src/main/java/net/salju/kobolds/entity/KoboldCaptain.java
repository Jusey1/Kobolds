package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.entity.ai.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.LivingEntity;
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
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(1, new KoboldShieldGoal(this));
		this.goalSelector.addGoal(1, new KoboldTridentGoal(this, 1.0D, 40, 12.0F));
		this.goalSelector.addGoal(1, new KoboldCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(1, new KoboldBowGoal<>(this, 1.0D, 20, 15.0F));
		this.goalSelector.addGoal(1, new KoboldCaptainTradeGoal(this));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new KoboldAttackSelector(this)));
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