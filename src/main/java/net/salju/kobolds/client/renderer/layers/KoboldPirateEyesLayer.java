package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.salju.kobolds.client.model.KoboldModel;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldPirateEyesLayer<S extends AbstractKoboldState, M extends KoboldModel<S>> extends EyesLayer<S, M> {
	public KoboldPirateEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S kobold, float f1, float f2) {
        buffer.order(1).submitModel(this.getParentModel(), kobold, pose, this.renderSpecialType(this.getKoboldEyes(kobold)), i, OverlayTexture.NO_OVERLAY, -1, null, kobold.outlineColor, null);
	}

	@Override
	public RenderType renderType() {
		return RenderTypes.eyes(Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/base_pirate.png"));
	}

    public RenderType renderSpecialType(String type) {
        if (!type.isEmpty()) {
            return RenderTypes.eyes(Identifier.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/eyes/" + type + ".png"));
        }
        return this.renderType();
    }

    public String getKoboldEyes(AbstractKoboldState kobold) {
        if (kobold.isDiamond) {
            return "diamond_pirate";
        } else if (kobold.dragonColor >= 1) {
            return "dragon/pirate/" + kobold.dragonColor;
        }
        return "base_pirate";
    }
}