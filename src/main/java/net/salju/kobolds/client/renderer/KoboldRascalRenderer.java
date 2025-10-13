package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.RascalModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldRascalRenderer extends MobRenderer<AbstractKoboldEntity, AbstractKoboldState, RascalModel<AbstractKoboldState>> {
	public KoboldRascalRenderer(EntityRendererProvider.Context context) {
		super(context, new RascalModel(context.bakeLayer(KoboldsClient.KOBOLD_RASCAL)), 0.36f);
		this.addLayer(new KoboldEyesLayer<>(this));
		this.addLayer(new ItemInHandLayer<>(this) {
			public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, AbstractKoboldState kobold, float f1, float f2) {
				if (kobold.isAggressive) {
					super.submit(pose, buffer, i, kobold, f1, f2);
				}
			}
		});
        this.addLayer(new HumanoidArmorLayer(this, KoboldsClient.KOBOLD_ARMOR, KoboldsClient.KOBOLD_ARMOR, context.getEquipmentRenderer()));
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
		HumanoidMobRenderer.extractHumanoidRenderState(kobold, state, f1, this.itemModelResolver);
        state.texture = ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, "textures/entity/" + this.getKoboldType(kobold) + ".png");
		state.isAggressive = kobold.isAggressive();
		state.isDiamond = kobold.isDiamond();
		state.isLeftHanded = kobold.isLeftHanded();
		state.rightStack = kobold.getItemHeldByArm(HumanoidArm.RIGHT);
		state.leftStack = kobold.getItemHeldByArm(HumanoidArm.LEFT);
        state.dragonColor = kobold.getDragonColor();
	}

	@Override
	public void submit(AbstractKoboldState kobold, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
		pose.pushPose();
		pose.translate(-0.025, 0, 0);
		float scale = 0.875F;
		pose.scale(scale, scale, scale);
		super.submit(kobold, pose, buffer, c);
		pose.popPose();
	}

    public String getKoboldType(AbstractKoboldEntity kobold) {
        if (kobold.getDragonColor() >= 1) {
            return "special/dragon/rascal/" + kobold.getDragonColor();
        }
        return "kobolds/rascal";
    }
}