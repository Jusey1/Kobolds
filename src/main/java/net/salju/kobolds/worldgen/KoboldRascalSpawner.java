package net.salju.kobolds.worldgen;

import net.salju.kobolds.init.KoboldsMobs;
import net.salju.kobolds.entity.KoboldRascal;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class KoboldRascalSpawner {
	private final RandomSource randy = RandomSource.create();
	private final KoboldData data;
	private int tickDelay;
	private int spawnDelay;
	private int spawnChance;

	public KoboldRascalSpawner(KoboldData info) {
		this.data = info;
		this.tickDelay = 1200;
		this.spawnDelay = info.getRascalDelay();
		this.spawnChance = info.getRascalChance();
		if (this.spawnDelay == 0 && this.spawnChance == 0) {
			this.spawnDelay = 18000;
			info.setRascalDelay(this.spawnDelay);
			this.spawnChance = 25;
			info.setRascalChance(this.spawnChance);
		}
	}

	public int tick(ServerLevel world) {
		if (--this.tickDelay > 0) {
			return 0;
		} else {
			this.tickDelay = 1200;
			this.spawnDelay -= 1200;
			this.data.setRascalDelay(this.spawnDelay);
			if (this.spawnDelay > 0) {
				return 0;
			} else {
				this.spawnDelay = 18000;
				if (!world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
					return 0;
				} else {
					int i = this.spawnChance;
					this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 100);
					this.data.setRascalChance(this.spawnChance);
					if (this.randy.nextInt(100) > i) {
						return 0;
					} else if (this.spawn(world)) {
						this.spawnChance = 25;
						return 1;
					} else {
						return 0;
					}
				}
			}
		}
	}

	private boolean spawn(ServerLevel world) {
		Player player = world.getRandomPlayer();
		if (player == null) {
			return true;
		} else {
			BlockPos pos = player.blockPosition();
			BlockPos spawn = this.findSpawnPositionNear(world, pos, 32);
			if (spawn != null && this.hasEnoughSpace(world, spawn) && !(spawn.getY() > 42) && player.level().dimension() == Level.OVERWORLD) {
				KoboldRascal rascal = KoboldsMobs.KOBOLD_RASCAL.get().spawn(world, spawn, MobSpawnType.EVENT);
				if (rascal != null) {
					return true;
				}
			}
			return false;
		}
	}

	@Nullable
	private BlockPos findSpawnPositionNear(LevelReader world, BlockPos pos, int i) {
		BlockPos spawn = null;
		for (int c = 0; c < 25; ++c) {
			int x = pos.getX() + this.randy.nextInt(i * 2) - i;
			int y = pos.getY() + this.randy.nextInt(i * 2) - i;
			int z = pos.getZ() + this.randy.nextInt(i * 2) - i;
			BlockPos poz = new BlockPos(x, y, z);
			if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, world, poz, KoboldsMobs.KOBOLD_RASCAL.get())) {
				spawn = poz;
				break;
			}
		}
		return spawn;
	}

	private boolean hasEnoughSpace(BlockGetter getter, BlockPos poz) {
		for (BlockPos pos : BlockPos.betweenClosed(poz, poz.offset(1, 2, 1))) {
			if (!getter.getBlockState(pos).getCollisionShape(getter, pos).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}