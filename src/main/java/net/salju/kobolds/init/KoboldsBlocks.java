package net.salju.kobolds.init;

import net.salju.kobolds.block.KoboldWallSkull;
import net.salju.kobolds.block.KoboldSkull;
import net.salju.kobolds.KoboldsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class KoboldsBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, KoboldsMod.MODID);
	public static final RegistryObject<Block> KOBOLD_SKULL = REGISTRY.register("kobold_skull", () -> new KoboldSkull(KoboldSkull.Types.SKELEBOLD, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SKELETON).mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).pushReaction(PushReaction.DESTROY).randomTicks()));
	public static final RegistryObject<Block> KOBOLD_SKULL_WALL = REGISTRY.register("kobold_skull_wall", () -> new KoboldWallSkull(KoboldSkull.Types.SKELEBOLD, BlockBehaviour.Properties.of().mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).dropsLike(KOBOLD_SKULL.get()).pushReaction(PushReaction.DESTROY).randomTicks()));
}