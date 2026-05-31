package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.entity.*;
import net.salju.kobolds.events.KoboldsManager;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

@EventBusSubscriber
public class KoboldsMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Kobolds.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Kobold>> KOBOLD = register("kobold", EntityType.Builder.of(Kobold::new, MobCategory.MISC).sized(0.5F, 1.48F).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldZombie>> KOBOLD_ZOMBIE = register("kobold_zombie", EntityType.Builder.of(KoboldZombie::new, MobCategory.MONSTER).sized(0.5F, 1.48F).eyeHeight(1.26F).ridingOffset(-0.45F));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Kobolds.MODID, registryname))));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KOBOLD.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_ZOMBIE.get(), KoboldsManager.createAttributes(18.0, 3.0, 2.0, 0.2).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
	}

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(KOBOLD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(KOBOLD_ZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}