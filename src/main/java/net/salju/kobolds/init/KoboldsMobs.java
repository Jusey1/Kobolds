package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.entity.*;
import net.salju.kobolds.events.KoboldsManager;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber
public class KoboldsMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Kobolds.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Kobold>> KOBOLD = register("kobold", EntityType.Builder.of(Kobold::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldWarrior>> KOBOLD_WARRIOR = register("kobold_warrior", EntityType.Builder.of(KoboldWarrior::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldEnchanter>> KOBOLD_ENCHANTER = register("kobold_enchanter", EntityType.Builder.of(KoboldEnchanter::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldRascal>> KOBOLD_RASCAL = register("kobold_rascal", EntityType.Builder.of(KoboldRascal::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldEngineer>> KOBOLD_ENGINEER = register("kobold_engineer", EntityType.Builder.of(KoboldEngineer::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<Kobold>> KOBOLD_PIRATE = register("kobold_pirate", EntityType.Builder.of(Kobold::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldCaptain>> KOBOLD_CAPTAIN = register("kobold_captain", EntityType.Builder.of(KoboldCaptain::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldChild>> KOBOLD_CHILD = register("kobold_child", EntityType.Builder.of(KoboldChild::new, MobCategory.MISC).sized(0.4f, 0.76f).eyeHeight(0.66F).ridingOffset(-0.225F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldZombie>> KOBOLD_ZOMBIE = register("kobold_zombie", EntityType.Builder.of(KoboldZombie::new, MobCategory.MONSTER).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldSkeleton>> KOBOLD_SKELETON = register("kobold_skeleton", EntityType.Builder.of(KoboldSkeleton::new, MobCategory.MONSTER).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldWither>> WITHERBOLD = register("witherbold", EntityType.Builder.of(KoboldWither::new, MobCategory.MONSTER).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F).fireImmune());

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, registryname))));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KOBOLD.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_WARRIOR.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_ENCHANTER.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_RASCAL.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_ENGINEER.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_PIRATE.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_CAPTAIN.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_CHILD.get(), KoboldsManager.createAttributes(18.0, 1.0, 2.0, 0.25).build());
		event.put(KOBOLD_ZOMBIE.get(), KoboldsManager.createAttributes(18.0, 3.0, 2.0, 0.2).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
		event.put(KOBOLD_SKELETON.get(), KoboldsManager.createAttributes(18.0, 3.0, 0.0, 0.25).build());
		event.put(WITHERBOLD.get(), KoboldsManager.createAttributes(32.0, 3.0, 2.0, 0.25).build());
	}
}