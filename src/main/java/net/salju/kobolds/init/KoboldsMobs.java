package net.salju.kobolds.init;

import net.salju.kobolds.entity.*;
import net.salju.kobolds.KoboldsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KoboldsMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KoboldsMod.MODID);
	public static final RegistryObject<EntityType<Kobold>> KOBOLD = register("kobold", EntityType.Builder.<Kobold>of(Kobold::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(Kobold::new));
	public static final RegistryObject<EntityType<KoboldWarrior>> KOBOLD_WARRIOR = register("kobold_warrior", EntityType.Builder.<KoboldWarrior>of(KoboldWarrior::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldWarrior::new));
	public static final RegistryObject<EntityType<KoboldEnchanter>> KOBOLD_ENCHANTER = register("kobold_enchanter", EntityType.Builder.<KoboldEnchanter>of(KoboldEnchanter::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldEnchanter::new));
	public static final RegistryObject<EntityType<KoboldRascal>> KOBOLD_RASCAL = register("kobold_rascal", EntityType.Builder.<KoboldRascal>of(KoboldRascal::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldRascal::new));
	public static final RegistryObject<EntityType<KoboldEngineer>> KOBOLD_ENGINEER = register("kobold_engineer", EntityType.Builder.<KoboldEngineer>of(KoboldEngineer::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldEngineer::new));
	public static final RegistryObject<EntityType<KoboldPirate>> KOBOLD_PIRATE = register("kobold_pirate", EntityType.Builder.<KoboldPirate>of(KoboldPirate::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldPirate::new));
	public static final RegistryObject<EntityType<KoboldCaptain>> KOBOLD_CAPTAIN = register("kobold_captain", EntityType.Builder.<KoboldCaptain>of(KoboldCaptain::new, MobCategory.MISC).sized(0.5f, 1.48f).setCustomClientFactory(KoboldCaptain::new));
	public static final RegistryObject<EntityType<KoboldChild>> KOBOLD_CHILD = register("kobold_child", EntityType.Builder.<KoboldChild>of(KoboldChild::new, MobCategory.MISC).sized(0.4f, 0.76f).setCustomClientFactory(KoboldChild::new));
	public static final RegistryObject<EntityType<KoboldZombie>> KOBOLD_ZOMBIE = register("kobold_zombie", EntityType.Builder.<KoboldZombie>of(KoboldZombie::new, MobCategory.MONSTER).sized(0.5f, 1.48f).setCustomClientFactory(KoboldZombie::new));
	public static final RegistryObject<EntityType<KoboldSkeleton>> KOBOLD_SKELETON = register("kobold_skeleton", EntityType.Builder.<KoboldSkeleton>of(KoboldSkeleton::new, MobCategory.MONSTER).sized(0.5f, 1.48f).setCustomClientFactory(KoboldSkeleton::new));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			KoboldZombie.init();
			KoboldSkeleton.init();
		});
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