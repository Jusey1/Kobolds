package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.salju.kobolds.client.model.ZomboldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldZombieEyesLayer<S extends AbstractKoboldState, M extends ZomboldModel<S>> extends EyesLayer<S, M> {
	public KoboldZombieEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldState zombo, float f1, float f2) {
		VertexConsumer eyes = buffer.getBuffer(this.renderType());
		if (zombo.getZomboType.equals("pirate") || zombo.getZomboType.equals("pirate_captain")) {
			eyes = buffer.getBuffer(this.renderPirateType());
		}
		this.getParentModel().renderToBuffer(pose, eyes, i, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/zombie.png"));
	}

	public RenderType renderPirateType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/zombie_pirate.png"));
	}
}