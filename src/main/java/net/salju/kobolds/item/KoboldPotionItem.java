package net.salju.kobolds.item;

import net.salju.kobolds.init.KoboldsItems;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import java.util.List;

public class KoboldPotionItem extends Item {
	public KoboldPotionItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(KoboldPotionUtils.getEffects(stack), list, 1.0F);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);
		if (KoboldPotionUtils.getEffects(stack).isEmpty()) {
			if (stack == player.getMainHandItem() && player.getOffhandItem().getItem() instanceof KoboldPotionItem) {
				player.setItemSlot(EquipmentSlot.MAINHAND, KoboldPotionUtils.copyPotion(stack, player.getOffhandItem()));
				player.setItemSlot(EquipmentSlot.OFFHAND, empty);
			}
			return super.use(world, player, hand);
		}
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity target) {
		ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);
		super.finishUsingItem(stack, world, target);
		for (MobEffectInstance effect : KoboldPotionUtils.getEffects(stack)) {
			target.addEffect(effect);
		}
		if (stack.isEmpty()) {
			return empty;
		} else {
			if (target instanceof Player player && !player.getAbilities().instabuild) {
				if (stack.getItem() == KoboldsItems.KOBOLD_POTION_INFINITY.get()) {
					player.getCooldowns().addCooldown(stack.getItem(), 240);
				} else {
					stack.shrink(1);
					if (!player.getInventory().add(empty))
						player.drop(empty, false);
				}
			}
			return stack;
		}
	}
}