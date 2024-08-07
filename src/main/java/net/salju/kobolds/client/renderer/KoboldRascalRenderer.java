package net.salju.kobolds.client.renderer;

import net.salju.kobolds.init.KoboldsModels;
import net.salju.kobolds.entity.KoboldRascal;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.RascalModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldRascalRenderer extends MobRenderer<KoboldRascal, RascalModel<KoboldRascal>> {
	public KoboldRascalRenderer(EntityRendererProvider.Context context) {
		super(context, new RascalModel(context.bakeLayer(KoboldsModels.KOBOLD_RASCAL)), 0.36f);
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new ItemInHandLayer<KoboldRascal, RascalModel<KoboldRascal>>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int inty, KoboldRascal kobold, float f1, float f2, float f3, float f4, float f5, float f6) {
				if (kobold.isAggressive()) {
					super.render(pose, buffer, inty, kobold, f1, f2, f3, f4, f5, f6);
				}
			}
		});
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsModels.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsModels.KOBOLD_ARMOR_OUTER)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(KoboldRascal entity) {
		return new ResourceLocation("kobolds:textures/entity/kobolds/rascal.png");
	}

	@Override
	public void render(KoboldRascal kobold, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}