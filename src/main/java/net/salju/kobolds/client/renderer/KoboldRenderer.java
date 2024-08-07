package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsModels;
import net.salju.kobolds.entity.Kobold;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldRenderer extends MobRenderer<Kobold, KoboldModel<Kobold>> {
	public KoboldRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel(context.bakeLayer(KoboldsModels.KOBOLD)), 0.36f);
		this.addLayer(new ItemInHandLayer<Kobold, KoboldModel<Kobold>>(this, context.getItemInHandRenderer()));
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsModels.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsModels.KOBOLD_ARMOR_OUTER)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(Kobold kobold) {
		if (kobold.getDisplayName().getString().equals("Popper")) {
			return new ResourceLocation("kobolds:textures/entity/special/popper.png");
		} else {
			return new ResourceLocation("kobolds:textures/entity/kobolds/base.png");
		}
	}

	@Override
	public void render(Kobold kobold, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}