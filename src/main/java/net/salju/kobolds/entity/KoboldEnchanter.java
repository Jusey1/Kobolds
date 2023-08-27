package net.salju.kobolds.entity;

import net.salju.kobolds.item.KoboldPotionUtils;
import net.salju.kobolds.init.KoboldsModSounds;
import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.KoboldsMod;

import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class KoboldEnchanter extends AbstractKoboldEntity {
	public KoboldEnchanter(PlayMessages.SpawnEntity packet, Level world) {
		this(KoboldsMobs.KOBOLD_ENCHANTER.get(), world);
	}

	public KoboldEnchanter(EntityType<KoboldEnchanter> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new KoboldEnchanter.KoboldTradeGoal(this));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand) {
		if (drop.getItem() instanceof ArmorItem) {
			if (EnchantmentHelper.hasBindingCurse(hand)) {
				return false;
			} else if (hand.isEmpty() || hand.getItem() instanceof BlockItem) {
				return true;
			} else if (hand.getItem() instanceof ArmorItem) {
				ArmorItem newbie = (ArmorItem) drop.getItem();
				ArmorItem worn = (ArmorItem) hand.getItem();
				if (newbie.getDefense() != worn.getDefense()) {
					return newbie.getDefense() > worn.getDefense();
				} else if (newbie.getToughness() != worn.getToughness()) {
					return newbie.getToughness() > worn.getToughness();
				} else {
					return this.canReplaceEqualItem(drop, hand);
				}
			} else {
				return false;
			}
		} else if (drop.getItem() == Items.EMERALD && hand.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	class KoboldTradeGoal extends Goal {
		public final AbstractKoboldEntity kobold;

		public KoboldTradeGoal(AbstractKoboldEntity kobold) {
			this.kobold = kobold;
		}

		@Override
		public boolean canUse() {
			return (checkHand() && !(this.kobold.hasEffect(MobEffects.MOVEMENT_SPEED)));
		}

		@Override
		public void start() {
			this.kobold.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, -10, false, false));
			ItemStack potion = KoboldPotionUtils.sellPotion(new ItemStack(KoboldsItems.KOBOLD_POTION.get()), Mth.nextInt(RandomSource.create(), 0, 100));
			KoboldsMod.queueServerWork(100, () -> {
				if (this.kobold.isAlive()) {
					this.kobold.swing(InteractionHand.MAIN_HAND, true);
					this.kobold.playSound(KoboldsModSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					LevelAccessor world = this.kobold.level();
					double x = this.kobold.getX();
					double y = this.kobold.getY();
					double z = this.kobold.getZ();
					if (world instanceof ServerLevel lvl) {
						if (Math.random() >= 0.1) {
							Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 0);
							Player target = lvl.getNearestPlayer(this.kobold, 7);
							if (target != null) {
								pos = target.position();
							}
							BehaviorUtils.throwItem(this.kobold, potion, pos);
						} else {
							List<ItemStack> list = this.kobold.getTradeItems(this.kobold, "kobolds:gameplay/enchanter_loot");
							Vec3 pos = LandRandomPos.getPos(this.kobold, 2, 0);
							Player target = lvl.getNearestPlayer(this.kobold, 7);
							if (target != null) {
								pos = target.position();
							}
							for (ItemStack stack : list) {
								BehaviorUtils.throwItem(this.kobold, stack, pos);
							}
						}
					}
					KoboldsMod.queueServerWork(20, () -> {
						this.kobold.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					});
				}
			});
		}

		protected boolean checkHand() {
			return (this.kobold.getOffhandItem().getItem() == (Items.EMERALD));
		}
	}
}