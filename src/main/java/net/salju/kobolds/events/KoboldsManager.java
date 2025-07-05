package net.salju.kobolds.events;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class KoboldsManager {
	public static AttributeSupplier.Builder createAttributes(double h, double d, double a, double s) {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, h).add(Attributes.ATTACK_DAMAGE, d).add(Attributes.ARMOR, a).add(Attributes.MOVEMENT_SPEED, s);
	}
}