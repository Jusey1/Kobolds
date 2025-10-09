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
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldEyesLayer<S extends AbstractKoboldState, M extends KoboldModel<S>> extends EyesLayer<S, M> {
	public KoboldEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldState kobold, float f1, float f2) {
        this.getParentModel().renderToBuffer(pose, buffer.getBuffer(this.renderSpecialType(this.getKoboldEyes(kobold))), i, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public RenderType renderType() {
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/base.png"));
	}

    public RenderType renderSpecialType(String type) {
        if (!type.isEmpty()) {
            return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/" + type + ".png"));
        }
        return this.renderType();
    }

    public String getKoboldEyes(AbstractKoboldState kobold) {
        if (kobold.isDiamond) {
            return "diamond";
        } else if (kobold.texture.getPath().contains("popper")) {
            return "popper";
        } else if (kobold.dragonColor >= 1) {
            return "dragon/" + kobold.dragonColor;
        }
        return "base";
    }
}