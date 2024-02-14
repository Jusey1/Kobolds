package net.salju.kobolds.init;

import net.salju.kobolds.worldgen.*;
import net.salju.kobolds.KoboldsMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.core.registries.Registries;
import com.mojang.serialization.Codec;

public class KoboldsStructures {
	public static final DeferredRegister<StructureType<?>> REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, KoboldsMod.MODID);
	public static final RegistryObject<StructureType<KoboldDenStructure>> KOBOLD_DEN = REGISTRY.register("kobold_den", () -> stuff(KoboldDenStructure.CODEC));
	public static final RegistryObject<StructureType<KoboldSurfaceStructure>> KOBOLD_SURFACE = REGISTRY.register("kobold_surface", () -> stuff(KoboldSurfaceStructure.CODEC));

	private static <T extends Structure> StructureType<T> stuff(Codec<T> codec) {
		return () -> codec;
	}
}