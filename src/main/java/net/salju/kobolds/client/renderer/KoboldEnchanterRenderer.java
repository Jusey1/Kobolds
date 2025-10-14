package net.salju.kobolds.client.renderer;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.init.KoboldsClient;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.salju.kobolds.client.renderer.layers.KoboldEyesLayer;
import net.salju.kobolds.client.model.KoboldModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class KoboldEnchanterRenderer extends MobRenderer<AbstractKoboldEntity, AbstractKoboldState, KoboldModel<AbstractKoboldState>> {
	public KoboldEnchanterRenderer(EntityRendererProvider.Context context) {
		super(context, new KoboldModel(context.bakeLayer(KoboldsClient.KOBOLD)), 0.36f);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new KoboldEyesLayer<>(this));
        this.addLayer(new HumanoidArmorLayer(this, ArmorModelSet.bake(KoboldsClient.KOBOLD_ARMOR, context.getModelSet(), KoboldModel::new), ArmorModelSet.bake(KoboldsClient.KOBOLD_ARMOR, context.getModelSet(), KoboldModel::new), context.getEquipmentRenderer()));
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
        if (kobold.getName().getString().equals("James")) {
            return "special/james";
        } else if (kobold.getName().getString().equals("Popper")) {
            return "special/popper";
        } else if (kobold.getDragonColor() >= 1) {
            return "special/dragon/enchanter/" + kobold.getDragonColor();
        }
        return "kobolds/enchanter";
    }
}