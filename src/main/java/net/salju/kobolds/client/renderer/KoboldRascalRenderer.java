package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.RascalModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldRascalRenderer extends MobRenderer<AbstractKoboldEntity, AbstractKoboldState, RascalModel<AbstractKoboldState>> {
	public KoboldRascalRenderer(EntityRendererProvider.Context context) {
		super(context, new RascalModel(context.bakeLayer(KoboldsClient.KOBOLD_RASCAL)), 0.36f);
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractKoboldState kobold, float f1, float f2) {
				if (kobold.isAggressive) {
					super.render(pose, buffer, i, kobold, f1, f2);
				}
			}
		});
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_OUTER)), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractKoboldState kobold) {
		return kobold.texture;
	}

	@Override
	public AbstractKoboldState createRenderState() {
		return new AbstractKoboldState();
	}

	@Override
	public void extractRenderState(AbstractKoboldEntity kobold, AbstractKoboldState state, float f1) {
		super.extractRenderState(kobold, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(kobold, state, f1);
		state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/kobolds/rascal.png");
		state.isAggressive = kobold.isAggressive();
		state.isDiamond = kobold.isDiamond();
		state.isLeftHanded = kobold.isLeftHanded();
	}

	@Override
	public void render(AbstractKoboldState kobold, PoseStack stack, MultiBufferSource buffer, int i) {
		stack.pushPose();
		stack.translate(-0.025, 0, 0);
		float scale = 0.875F;
		stack.scale(scale, scale, scale);
		super.render(kobold, stack, buffer, i);
		stack.popPose();
	}
}