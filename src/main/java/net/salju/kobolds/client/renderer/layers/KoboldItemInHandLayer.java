package net.salju.kobolds.client.renderer.layers;

import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldItemInHandLayer<S extends AbstractKoboldState, M extends KoboldModel<S>> extends ItemInHandLayer<S, M> {
	public KoboldItemInHandLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int l, S state, float y, float x) {
        if (!(state.texture.getPath().contains("rascal")) || state.isAggressive) {
            super.submit(pose, buffer, l, state, y, x);
        }
    }
}