package net.salju.kobolds.events;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;

public class KoboldsManager {
	public static AttributeSupplier.Builder createAttributes(double h, double d, double a, double s) {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, h).add(Attributes.ATTACK_DAMAGE, d).add(Attributes.ARMOR, a).add(Attributes.MOVEMENT_SPEED, s);
	}

	public static Holder<Enchantment> getEnchantment(RegistryAccess target, String id, String name) {
		return target.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(id, name)));
	}
}