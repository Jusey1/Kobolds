package net.salju.kobolds.events;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.entity.KoboldRascal;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber
public class KoboldsEvents {
	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Raider johnny) {
			johnny.targetSelector.addGoal(3, new NearestAttackableTargetGoal(johnny, AbstractKoboldEntity.class, false));
		} else if (event.getEntity() instanceof Zombie billy && !(billy instanceof ZombifiedPiglin)) {
			billy.targetSelector.addGoal(3, new NearestAttackableTargetGoal(billy, AbstractKoboldEntity.class, false));
		}
	}

	@SubscribeEvent
	public static void onAttackTarget(LivingChangeTargetEvent event) {
		if (event.getEntity() instanceof Zombie && event.getOriginalAboutToBeSetTarget() instanceof KoboldRascal && event.getEntity().getLastHurtByMob() != event.getOriginalAboutToBeSetTarget()) {
			event.setCanceled(true);
		} else if (event.getEntity() instanceof AbstractKoboldEntity && event.getOriginalAboutToBeSetTarget() instanceof AbstractKoboldEntity) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getSource().getEntity() != null && event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof LivingEntity src) {
			int e = src.getMainHandItem().getEnchantmentLevel(src.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "prospector"))));
			if (e > 0) {
				double check = src.hasEffect(MobEffects.LUCK) ? 0.25 : 0.05;
				if (!event.getEntity().isAlive() && !event.getEntity().level().isClientSide()) {
					if (Math.random() <= check) {
						for (int i = 0; i < e; i++) {
							event.getEntity().level().addFreshEntity(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(Items.EMERALD)));
						}
					}
				}
			}
		}
	}
}