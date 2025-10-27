package net.salju.kobolds.block;

import net.salju.kobolds.init.KoboldsMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;

public class KoboldSkull extends SkullBlock {
	public KoboldSkull(SkullBlock.Type type, BlockBehaviour.Properties props) {
		super(type, props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullBlockEntity(pos, state);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blok, Orientation ori, boolean moving) {
		super.neighborChanged(state, world, pos, blok, ori, moving);
		if (world.getBestNeighborSignal(pos) > 0) {
			if (world instanceof ServerLevel lvl && world.isMoonVisible() && world.canSeeSkyFromBelowWater(pos)) {
				summonSkelebold(lvl, BlockPos.containing((pos.getX() + 0.5), pos.getY(), (pos.getZ() + 0.5)));
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource random) {
		super.tick(state, lvl, pos, random);
		if (lvl.isMoonVisible() && lvl.canSeeSkyFromBelowWater(pos)) {
			summonSkelebold(lvl, BlockPos.containing((pos.getX() + 0.5), pos.getY(), (pos.getZ() + 0.5)));
		}
	}

	protected EntityType<?> getKoboldSkeleton() {
		return KoboldsMobs.KOBOLD_SKELETON.get();
	}

	private void summonSkelebold(ServerLevel lvl, BlockPos pos) {
		lvl.destroyBlock(pos, false);
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
        if (bolt != null) {
            bolt.snapTo(pos.getX(), pos.getY(), pos.getZ());
            bolt.setVisualOnly(true);
            this.getKoboldSkeleton().spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
            lvl.addFreshEntity(bolt);
        }
	}

	public enum Types implements SkullBlock.Type {
		SKELEBOLD;

		@Override
		public String getSerializedName() {
			return "skelebold";
		}
	}
}