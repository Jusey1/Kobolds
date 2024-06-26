package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.model.KoboldModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldEyesLayer<T extends AbstractKoboldEntity, M extends KoboldModel<T>> extends EyesLayer<T, M> {
	public KoboldEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldEntity kobold, float f1, float f2, float f3, float f4, float f5, float f6) {
		VertexConsumer eyes = buffer.getBuffer(this.renderType());
		if (kobold.isDiamond()) {
			eyes = buffer.getBuffer(this.renderDiamondType());
		} else if (kobold.getDisplayName().getString().equals("Popper")) {
			eyes = buffer.getBuffer(this.renderPopperType());
		}
		this.getParentModel().renderToBuffer(pose, eyes, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entity/eyes/base.png"));
	}

	public RenderType renderDiamondType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entity/eyes/diamond.png"));
	}

	public RenderType renderPopperType() {
		return RenderType.eyes(new ResourceLocation("kobolds:textures/entity/eyes/popper.png"));
	}
}