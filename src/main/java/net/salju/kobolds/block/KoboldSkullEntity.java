package net.salju.kobolds.block;

import net.salju.kobolds.init.KoboldsBlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;

public class KoboldSkullEntity extends SkullBlockEntity {
	public KoboldSkullEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return KoboldsBlockEntities.KOBOLD_SKULL.get();
	}
}