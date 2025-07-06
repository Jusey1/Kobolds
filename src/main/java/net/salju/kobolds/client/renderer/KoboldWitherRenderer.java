package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.client.renderer.layers.KoboldWitherEyesLayer;
import net.salju.kobolds.entity.KoboldWither;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.client.model.SkeleboldModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldWitherRenderer extends MobRenderer<KoboldWither, AbstractKoboldState, SkeleboldModel<AbstractKoboldState>> {
	public KoboldWitherRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeleboldModel(context.bakeLayer(KoboldsClient.WITHERBOLD)), 0.36f);
		this.addLayer(new KoboldWitherEyesLayer<>(this));
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new HumanoidArmorLayer(this, new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_INNER)), new KoboldArmorModel(context.bakeLayer(KoboldsClient.KOBOLD_ARMOR_OUTER)), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractKoboldState witherbold) {
		return witherbold.texture;
	}

	@Override
	public AbstractKoboldState createRenderState() {
		return new AbstractKoboldState();
	}

	@Override
	public void extractRenderState(KoboldWither witherbold, AbstractKoboldState state, float f1) {
		super.extractRenderState(witherbold, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(witherbold, state, f1, this.itemModelResolver);
		state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/undead/skeleton_wither.png");
		state.isAggressive = witherbold.isAggressive();
		state.isCharging = witherbold.isCharging();
		state.isLeftHanded = witherbold.isLeftHanded();
		state.rightStack = witherbold.getItemHeldByArm(HumanoidArm.RIGHT);
		state.leftStack = witherbold.getItemHeldByArm(HumanoidArm.LEFT);
	}

	@Override
	public void render(AbstractKoboldState witherbold, PoseStack pose, MultiBufferSource buffer, int i) {
		pose.pushPose();
		pose.translate(-0.025, 0, 0);
		float scale = 0.875F;
		pose.scale(scale, scale, scale);
		super.render(witherbold, pose, buffer, i);
		pose.popPose();
	}
}