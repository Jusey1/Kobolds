package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.salju.kobolds.client.model.KoboldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldPirateEyesLayer<S extends AbstractKoboldState, M extends KoboldModel<S>> extends EyesLayer<S, M> {
	public KoboldPirateEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldState state, float f1, float f2) {
		VertexConsumer eyes = buffer.getBuffer(this.renderType());
		if (state.isDiamond) {
			eyes = buffer.getBuffer(this.renderDiamondType());
		}
		this.getParentModel().renderToBuffer(pose, eyes, i, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/base_pirate.png"));
	}

	public RenderType renderDiamondType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/diamond_pirate.png"));
	}
}