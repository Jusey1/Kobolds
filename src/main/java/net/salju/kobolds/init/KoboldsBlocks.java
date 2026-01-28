package net.salju.kobolds.init;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.block.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

@EventBusSubscriber
public class KoboldsBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.createBlocks(Kobolds.MODID);
	public static final DeferredHolder<Block, Block> KOBOLD_SKULL = REGISTRY.register("kobold_skull", () -> new KoboldSkull(KoboldSkull.Types.SKELEBOLD, createBaseProps("kobold_skull").instrument(NoteBlockInstrument.SKELETON).mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).pushReaction(PushReaction.DESTROY).randomTicks()));
	public static final DeferredHolder<Block, Block> KOBOLD_SKULL_WALL = REGISTRY.register("kobold_skull_wall", () -> new KoboldWallSkull(KoboldSkull.Types.SKELEBOLD, createBaseProps("kobold_skull_wall").mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).pushReaction(PushReaction.DESTROY).randomTicks()));
	public static final DeferredHolder<Block, Block> KOBOLD_WITHER_SKULL = REGISTRY.register("kobold_wither_skull", () -> new KoboldWitherSkull(KoboldWitherSkull.Types.WITHERBOLD, createBaseProps("kobold_wither_skull").instrument(NoteBlockInstrument.WITHER_SKELETON).mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).pushReaction(PushReaction.DESTROY).randomTicks()));
	public static final DeferredHolder<Block, Block> KOBOLD_WITHER_SKULL_WALL = REGISTRY.register("kobold_wither_skull_wall", () -> new KoboldWitherWallSkull(KoboldWitherSkull.Types.WITHERBOLD, createBaseProps("kobold_wither_skull_wall").mapColor(MapColor.NONE).sound(SoundType.BONE_BLOCK).strength(1.0F).pushReaction(PushReaction.DESTROY).randomTicks()));

	public static BlockBehaviour.Properties createBaseProps(String name) {
		return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Kobolds.MODID, name)));
	}

	@SubscribeEvent
	public static void onBlockEntityValidBlocks(BlockEntityTypeAddBlocksEvent event) {
		event.modify(BlockEntityType.SKULL, KOBOLD_SKULL.get());
		event.modify(BlockEntityType.SKULL, KOBOLD_SKULL_WALL.get());
		event.modify(BlockEntityType.SKULL, KOBOLD_WITHER_SKULL.get());
		event.modify(BlockEntityType.SKULL, KOBOLD_WITHER_SKULL_WALL.get());
	}
}