package net.salju.kobolds.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;

public abstract class AbstractKoboldSkeleton extends AbstractSkeleton {
	public AbstractKoboldSkeleton(EntityType<? extends AbstractKoboldSkeleton> type, Level world) {
		super(type, world);
		this.getEyePosition(0.5F);
	}
}