package net.salju.kobolds.events;

import net.salju.kobolds.worldgen.KoboldRascalSpawner;
import net.salju.kobolds.worldgen.KoboldData;
import net.salju.kobolds.entity.KoboldRascal;
import net.salju.kobolds.entity.AbstractKoboldEntity;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class KoboldsEvents {
	private static final Map<ServerLevel, KoboldRascalSpawner> RASCAL_MAP = new HashMap<>();

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		Entity target = event.getEntity();
		if (target instanceof Raider johnny) {
			johnny.targetSelector.addGoal(3, new NearestAttackableTargetGoal(johnny, AbstractKoboldEntity.class, false));
		} else if (target instanceof Zombie billy && !(billy instanceof ZombifiedPiglin)) {
			billy.targetSelector.addGoal(3, new NearestAttackableTargetGoal(billy, AbstractKoboldEntity.class, false));
		}
	}

	@SubscribeEvent
	public static void onAttackTarget(LivingChangeTargetEvent event) {
		LivingEntity target = event.getNewTarget();
		LivingEntity billy = event.getEntity();
		if (billy instanceof Zombie && target instanceof KoboldRascal && !(billy.getLastHurtByMob() == target)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onTick(TickEvent.LevelTickEvent event) {
		if (!event.level.isClientSide && event.level instanceof ServerLevel lvl) {
			KoboldData info = KoboldData.get(lvl);
			RASCAL_MAP.computeIfAbsent(lvl, k -> new KoboldRascalSpawner(info));
			KoboldRascalSpawner spawner = RASCAL_MAP.get(lvl);
			spawner.tick(lvl);
		}
	}
}