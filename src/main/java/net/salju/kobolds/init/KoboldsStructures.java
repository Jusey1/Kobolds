package net.salju.kobolds.init;

import net.salju.kobolds.worldgen.*;
import net.salju.kobolds.Kobolds;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.core.registries.Registries;
import com.mojang.serialization.MapCodec;

public class KoboldsStructures {
	public static final DeferredRegister<StructureType<?>> REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, Kobolds.MODID);
	public static final DeferredHolder<StructureType<?>, StructureType<AbstractKoboldStructure>> KOBOLD_DEN = REGISTRY.register("kobold_den", () -> stuff(AbstractKoboldStructure.CODEC));

	private static <T extends Structure> StructureType<T> stuff(MapCodec<T> codec) {
		return () -> codec;
	}
}