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

public class KoboldEyesLayer<S extends AbstractKoboldState, M extends KoboldModel<S>> extends EyesLayer<S, M> {
	public KoboldEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldState kobold, float f1, float f2) {
		VertexConsumer eyes = buffer.getBuffer(this.renderType());
		if (kobold.isDiamond) {
			eyes = buffer.getBuffer(this.renderDiamondType());
		} else if (kobold.isPopper) {
			eyes = buffer.getBuffer(this.renderPopperType());
		}
		this.getParentModel().renderToBuffer(pose, eyes, 15728640, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/base.png"));
	}

	public RenderType renderDiamondType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/diamond.png"));
	}

	public RenderType renderPopperType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/popper.png"));
	}
}