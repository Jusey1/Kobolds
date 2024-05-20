package net.salju.kobolds.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import java.util.List;
import com.google.common.collect.Lists;

public class KoboldPotionUtils {
	public static ItemStack sellPotion(ItemStack stack, int chance) {
		MobEffect primary = null;
		MobEffect secondary = null;
		int fi = 0;
		int si = 0;

		if (chance >= 90) {
			fi = 1200;
			int i = Mth.nextInt(RandomSource.create(), 0, 3);
			List<MobEffect> list = getSpecial();
			primary = list.get(i);
		} else if (chance >= 60) {
			primary = MobEffects.HEAL;
			secondary = MobEffects.REGENERATION;
			fi = 1;
			si = 900;
		} else {
			fi = 3600;
			int i = Mth.nextInt(RandomSource.create(), 0, 6);
			List<MobEffect> list = getCommon();
			primary = list.get(i);
		}
		if (chance >= 95 || chance <= 10) {
			si = 3600;
			int i = Mth.nextInt(RandomSource.create(), 0, 6);
			List<MobEffect> list = getCommon();
			if (list.get(i) != primary) {
				secondary = list.get(i);
			}
		}
		return makePotion(stack, primary, secondary, fi, si);
	}

	public static ItemStack makePotion(ItemStack stack, MobEffect first, @Nullable MobEffect second, int fi, int si) {
		CompoundTag tag = stack.getOrCreateTag();
		ListTag list = tag.getList("KoboldPotionEffects", 9);
		list.add(new MobEffectInstance(first, fi, 0).save(new CompoundTag()));
		if (second != null) {
			list.add(new MobEffectInstance(second, si, 0).save(new CompoundTag()));
		}
		tag.put("KoboldPotionEffects", list);
		return stack;
	}

	public static ItemStack copyPotion(ItemStack stack, ItemStack copy) {
		MobEffect primary = null;
		MobEffect secondary = null;
		int fi = 0;
		int si = 0;
		if (copy.getItem() instanceof KoboldPotionItem && !getEffects(copy).isEmpty()) {
			List<MobEffectInstance> list = getEffects(copy);
			for (int i = 0; i < list.size(); ++i) {
				MobEffectInstance instance = list.get(i);
				if (i == 0) {
					primary = instance.getEffect();
					fi = instance.getDuration();
				} else if (i == 1) {
					secondary = instance.getEffect();
					si = instance.getDuration();
				}
			}
		}
		return makePotion(stack, primary, secondary, fi, si);
	}

	public static List<MobEffectInstance> getEffects(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		List<MobEffectInstance> effects = Lists.newArrayList();
		if (tag != null && tag.contains("KoboldPotionEffects", 9)) {
			ListTag list = tag.getList("KoboldPotionEffects", 10);
			for (int i = 0; i < list.size(); ++i) {
				CompoundTag target = list.getCompound(i);
				MobEffectInstance instance = MobEffectInstance.load(target);
				if (instance != null) {
					MobEffect effect = instance.getEffect();
					int dur = instance.getDuration();
					if (effect.isInstantenous()) {
						effects.add(new MobEffectInstance(effect, 1, 0, false, false));
					} else {
						effects.add(new MobEffectInstance(effect, dur, 0));
					}
				}
			}
		}
		return effects;
	}

	public static List<MobEffect> getSpecial() {
		List<MobEffect> effects = Lists.newArrayList();
		effects.add(MobEffects.DAMAGE_RESISTANCE);
		effects.add(MobEffects.DAMAGE_BOOST);
		effects.add(MobEffects.DIG_SPEED);
		effects.add(MobEffects.LUCK);
		return effects;
	}

	public static List<MobEffect> getCommon() {
		List<MobEffect> effects = Lists.newArrayList();
		effects.add(MobEffects.FIRE_RESISTANCE);
		effects.add(MobEffects.WATER_BREATHING);
		effects.add(MobEffects.MOVEMENT_SPEED);
		effects.add(MobEffects.NIGHT_VISION);
		effects.add(MobEffects.INVISIBILITY);
		effects.add(MobEffects.SLOW_FALLING);
		effects.add(MobEffects.JUMP);
		return effects;
	}

	public static int getColor(ItemStack stack) {
		return getEffects(stack).isEmpty() ? 16253176 : getColor(getEffects(stack));
	}

	public static int getColor(List<MobEffectInstance> list) {
		int i = 3694022;
		float f = 0.0F;
		float f1 = 0.0F;
		float f2 = 0.0F;
		int j = 0;
		for (MobEffectInstance instance : list) {
			if (instance.getEffect().isInstantenous() || instance.getDuration() >= 1200) {
				int k = instance.getEffect().getColor();
				int l = instance.getAmplifier() + 1;
				f += (float) (l * (k >> 16 & 255)) / 255.0F;
				f1 += (float) (l * (k >> 8 & 255)) / 255.0F;
				f2 += (float) (l * (k >> 0 & 255)) / 255.0F;
				j += l;
			}
		}
		if (j == 0) {
			return 0;
		} else {
			f = f / (float) j * 255.0F;
			f1 = f1 / (float) j * 255.0F;
			f2 = f2 / (float) j * 255.0F;
			return (int) f << 16 | (int) f1 << 8 | (int) f2;
		}
	}
}