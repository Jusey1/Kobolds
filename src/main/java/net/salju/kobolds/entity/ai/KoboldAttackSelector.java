package net.salju.kobolds.entity.ai;

import net.salju.kobolds.entity.KoboldWarrior;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class KoboldAttackSelector implements Predicate<LivingEntity> {
	private final AbstractKoboldEntity kobold;

	public KoboldAttackSelector(AbstractKoboldEntity source) {
		this.kobold = source;
	}

	public boolean test(@Nullable LivingEntity target) {
		if (!(target instanceof ZombifiedPiglin)) {
			if (kobold instanceof KoboldWarrior) {
				return (target instanceof Zombie || target instanceof AbstractSkeleton || target instanceof Spider || target instanceof Raider);
			} else {
				return (target instanceof Zombie || target instanceof Silverfish || target instanceof Raider);
			}
		}
		return false;
	}
}