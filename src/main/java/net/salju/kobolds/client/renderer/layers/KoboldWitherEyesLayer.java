package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldWitherEyesLayer<S extends AbstractKoboldState, M extends SkeleboldModel<S>> extends EyesLayer<S, M> {
	public KoboldWitherEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S kobold, float f1, float f2) {
        buffer.order(1).submitModel(this.getParentModel(), kobold, pose, this.renderType(), i, OverlayTexture.NO_OVERLAY, -1, null, kobold.outlineColor, null);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/skeleton_wither.png"));
	}
}