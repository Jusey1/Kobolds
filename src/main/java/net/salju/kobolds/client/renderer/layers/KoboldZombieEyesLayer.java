package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.entity.KoboldZombie;
import net.salju.kobolds.client.model.ZomboldModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;

public class KoboldZombieEyesLayer<T extends KoboldZombie, M extends ZomboldModel<T>> extends EyesLayer<T, M> {
	public KoboldZombieEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entities/kobold_eyes_zombie.png"));
	}
}