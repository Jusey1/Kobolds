package net.salju.kobolds.init;

import net.salju.kobolds.block.KoboldSkullEntity;
import net.salju.kobolds.KoboldsMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class KoboldsBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KoboldsMod.MODID);
	public static final RegistryObject<BlockEntityType<KoboldSkullEntity>> KOBOLD_SKULL = REGISTRY.register("kobold_skull", () -> BlockEntityType.Builder.of(KoboldSkullEntity::new, KoboldsBlocks.KOBOLD_SKULL.get(), KoboldsBlocks.KOBOLD_SKULL_WALL.get()).build(null));
}