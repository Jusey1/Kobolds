package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.init.KoboldsSounds;
import net.salju.kobolds.entity.ai.KoboldRevengeGoal;
import net.salju.kobolds.entity.ai.KoboldPotionGoal;
import net.salju.kobolds.entity.ai.KoboldMeleeGoal;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import javax.annotation.Nullable;
import java.util.*;

public class KoboldRascal extends AbstractKoboldEntity {
	public KoboldRascal(EntityType<KoboldRascal> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new KoboldRevengeGoal(this));
		this.goalSelector.addGoal(1, new KoboldPotionGoal(this));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal(this, 1.2D, false));
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.hasEffect(MobEffects.INVISIBILITY) && !this.getEffect(MobEffects.INVISIBILITY).endsWithin(9600)) {
			if (!this.level().isClientSide()) {
				player.swing(hand);
				this.removeEffect(MobEffects.INVISIBILITY);
				this.swing(InteractionHand.MAIN_HAND, true);
				if (this.level() instanceof ServerLevel lvl) {
					this.playSound(KoboldsSounds.KOBOLD_TRADE.get(), 1.0F, 1.0F);
					ItemStack stack = new ItemStack(Items.BUNDLE);
					if (Math.random() >= 0.85) {
						stack = new ItemStack(KoboldsItems.KOBOLD_IRON_PICKAXE.get());
						stack.setDamageValue(Mth.nextInt(this.getRandom(), 52, 716));
						EnchantmentHelper.enchantItem(this.getRandom(), stack, Mth.nextInt(this.getRandom(), 18, 32), this.level().registryAccess(), Optional.empty());
					} else {
						List<ItemStack> data = new ArrayList<>();
						for (int i = 0; i < Mth.nextInt(this.getRandom(), 7, 38); ++i) {
							int g = Mth.nextInt(this.getRandom(), 0, 7);
							ItemStack gem = this.getStack(g);
							if (Math.random() >= 0.98) {
								gem = new ItemStack(Items.DIAMOND);
							}
							int e = this.getMatchingItem(gem, data);
							if (e != -1) {
								ItemStack copy = data.remove(e);
								copy.grow(gem.getCount());
								data.addFirst(copy);
							} else {
								data.addFirst(gem);
							}
						}
						stack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(data));
					}
					ItemEntity reward = new ItemEntity(lvl, this.getX(), this.getY(), this.getZ(), stack);
					reward.setPickUpDelay(10);
					lvl.addFreshEntity(reward);
				}
			}
		}
		for (AbstractKoboldEntity kobold : this.level().getEntitiesOfClass(AbstractKoboldEntity.class, this.getBoundingBox().inflate(128.0D))) {
			if (kobold != null && !(kobold.is(this))) {
				this.getNavigation().moveTo(kobold, 1.2);
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 24000, 0));
		return super.finalizeSpawn(world, difficulty, reason, data);
	}

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
        super.populateDefaultEquipmentSlots(randy, difficulty);
    }

	@Override
	protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
		if (drop.is(ItemTags.SWORDS)) {
			return super.canReplaceCurrentItem(drop, hand, slot);
		}
        return false;
	}

	public int getMatchingItem(ItemStack stack, List<ItemStack> data) {
		if (!stack.isStackable()) {
			return -1;
		} else {
			for (int i = 0; i < data.size(); i++) {
				if (ItemStack.isSameItemSameComponents(data.get(i), stack)) {
					return i;
				}
			}
			return -1;
		}
	}

	public ItemStack getStack(int i) {
		Map<Integer, Item> stackMap = new HashMap<>();
		stackMap.put(0, Items.COAL);
		stackMap.put(1, Items.AMETHYST_SHARD);
		stackMap.put(2, Items.EMERALD);
		stackMap.put(3, Items.RAW_COPPER);
		stackMap.put(4, Items.RAW_IRON);
		stackMap.put(5, Items.RAW_GOLD);
		stackMap.put(6, Items.LAPIS_LAZULI);
		stackMap.put(7, Items.REDSTONE);
		return new ItemStack(stackMap.getOrDefault(i, Items.EMERALD));
	}
}