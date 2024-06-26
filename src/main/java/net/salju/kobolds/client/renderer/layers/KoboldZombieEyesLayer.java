package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.entity.KoboldZombie;
import net.salju.kobolds.client.model.ZomboldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldZombieEyesLayer<T extends KoboldZombie, M extends ZomboldModel<T>> extends EyesLayer<T, M> {
	public KoboldZombieEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, KoboldZombie zombo, float f1, float f2, float f3, float f4, float f5, float f6) {
		VertexConsumer eyes = buffer.getBuffer(this.renderType());
		if (zombo.getZomboType() == "pirate" || zombo.getZomboType() == "pirate_captain") {
			eyes = buffer.getBuffer(this.renderPirateType());
		}
		this.getParentModel().renderToBuffer(pose, eyes, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entity/eyes/zombie.png"));
	}

	public RenderType renderPirateType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entity/eyes/zombie_pirate.png"));
	}
}