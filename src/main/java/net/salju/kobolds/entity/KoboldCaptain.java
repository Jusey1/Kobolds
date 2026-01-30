package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsTags;
import net.salju.kobolds.init.KoboldsItems;
import net.salju.kobolds.entity.ai.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

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
        this.goalSelector.addGoal(1, new KoboldCaptainGoal(this));
		this.goalSelector.addGoal(2, new KoboldMeleeGoal<>(this, 1.2D, false));
		this.targetSelector.addGoal(2, new KoboldTargetGoal<>(this, LivingEntity.class, new KoboldAttackSelector(this)));
	}

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack gem = player.getItemInHand(hand).copy();
        if (gem.is(KoboldsTags.CAPTAIN)) {
            if (this.isEffectiveAi()) {
                gem.setCount(1);
                this.setItemInHand(InteractionHand.OFF_HAND, gem);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
        if (drop.is(Items.EMERALD)) {
            return false;
        }
        return super.canReplaceCurrentItem(drop, hand, slot);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(KoboldsItems.KOBOLD_IRON_SWORD.get()));
        super.populateDefaultEquipmentSlots(randy, difficulty);
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor lvl, RandomSource randy, DifficultyInstance difficulty) {
        ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!stack.isEmpty()) {
            EnchantmentHelper.enchantItemFromProvider(stack, lvl.registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, randy);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }
}