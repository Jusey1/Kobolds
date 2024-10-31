package net.salju.kobolds.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.salju.kobolds.entity.*;
import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class KoboldsMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Kobolds.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Kobold>> KOBOLD = register("kobold", EntityType.Builder.<Kobold>of(Kobold::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldWarrior>> KOBOLD_WARRIOR = register("kobold_warrior", EntityType.Builder.<KoboldWarrior>of(KoboldWarrior::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldEnchanter>> KOBOLD_ENCHANTER = register("kobold_enchanter", EntityType.Builder.<KoboldEnchanter>of(KoboldEnchanter::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldRascal>> KOBOLD_RASCAL = register("kobold_rascal", EntityType.Builder.<KoboldRascal>of(KoboldRascal::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldEngineer>> KOBOLD_ENGINEER = register("kobold_engineer", EntityType.Builder.<KoboldEngineer>of(KoboldEngineer::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<Kobold>> KOBOLD_PIRATE = register("kobold_pirate", EntityType.Builder.<Kobold>of(Kobold::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldCaptain>> KOBOLD_CAPTAIN = register("kobold_captain", EntityType.Builder.<KoboldCaptain>of(KoboldCaptain::new, MobCategory.MISC).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldChild>> KOBOLD_CHILD = register("kobold_child", EntityType.Builder.<KoboldChild>of(KoboldChild::new, MobCategory.MISC).sized(0.4f, 0.76f).eyeHeight(0.66F).ridingOffset(-0.225F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldZombie>> KOBOLD_ZOMBIE = register("kobold_zombie", EntityType.Builder.<KoboldZombie>of(KoboldZombie::new, MobCategory.MONSTER).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));
	public static final DeferredHolder<EntityType<?>, EntityType<KoboldSkeleton>> KOBOLD_SKELETON = register("kobold_skeleton", EntityType.Builder.<KoboldSkeleton>of(KoboldSkeleton::new, MobCategory.MONSTER).sized(0.5f, 1.48f).eyeHeight(1.26F).ridingOffset(-0.45F));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, registryname))));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KOBOLD.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_WARRIOR.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_ENCHANTER.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_RASCAL.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_ENGINEER.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_PIRATE.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_CAPTAIN.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_CHILD.get(), AbstractKoboldEntity.createAttributes().build());
		event.put(KOBOLD_ZOMBIE.get(), KoboldZombie.createAttributes().build());
		event.put(KOBOLD_SKELETON.get(), KoboldSkeleton.createAttributes().build());
	}
}