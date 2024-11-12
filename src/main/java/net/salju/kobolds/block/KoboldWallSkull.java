package net.salju.kobolds.block;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class KoboldWallSkull extends WallSkullBlock {
	public KoboldWallSkull(SkullBlock.Type type, BlockBehaviour.Properties props) {
		super(type, props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullBlockEntity(pos, state);
	}

	@Override
	public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block neighborBlock, Orientation ori, boolean moving) {
		super.neighborChanged(blockstate, world, pos, neighborBlock, ori, moving);
		if (world.getBestNeighborSignal(pos) > 0) {
			if (world instanceof ServerLevel lvl && !world.isDay() && world.canSeeSkyFromBelowWater(pos)) {
				summonSkelebold(lvl, BlockPos.containing((pos.getX() + 0.5), pos.getY(), (pos.getZ() + 0.5)));
			}
		}
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel lvl, BlockPos pos, RandomSource random) {
		super.tick(blockstate, lvl, pos, random);
		if (!lvl.isDay() && lvl.canSeeSkyFromBelowWater(pos)) {
			summonSkelebold(lvl, BlockPos.containing((pos.getX() + 0.5), pos.getY(), (pos.getZ() + 0.5)));
		}
	}

	private void summonSkelebold(ServerLevel lvl, BlockPos pos) {
		lvl.destroyBlock(pos, false);
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
		bolt.moveTo(Vec3.atBottomCenterOf(pos));
		bolt.setVisualOnly(true);
		KoboldsMobs.KOBOLD_SKELETON.get().spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
		lvl.addFreshEntity(bolt);
	}
}