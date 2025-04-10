package net.salju.kobolds.client.model;

import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;

public class ZomboldModel<T extends AbstractKoboldState> extends HumanoidModel<T> {
	public ZomboldModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = KoboldModel.createMesh(CubeDeformation.NONE, 0.0F);
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T kobold) {
		this.rightArm.xRot = -1.5708F;
		this.leftArm.xRot = -1.5708F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = 0.0F;
		this.leftLeg.xRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F) * 1.4F * kobold.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * kobold.walkAnimationSpeed;
		this.head.yRot = kobold.yRot * ((float) Math.PI / 180F);
		this.head.xRot = kobold.xRot * ((float) Math.PI / 180F);
		this.rightArm.xRot += Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F;
		this.leftArm.xRot -= Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F;
		this.rightArm.zRot += Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F + 0.05F;
		this.leftArm.zRot -= Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F + 0.05F;
		if (kobold.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (kobold.isAggressive) {
			if (kobold.getMainhandItem().getItem() instanceof TridentItem) {
				if (kobold.isLeftHanded) {
					this.rightArm.xRot = -1.5708F;
					this.leftArm.xRot = 2.8798F;
					this.leftArm.zRot = 0.1309F;
				} else {
					this.leftArm.xRot = -1.5708F;
					this.rightArm.xRot = 2.8798F;
					this.rightArm.zRot = -0.1309F;
				}
			} else {
				this.rightArm.xRot = -2.0944F;
				this.leftArm.xRot = -2.0944F;
			}
		}
		if (kobold.attackTime > 0.0F) {
			float progress = kobold.attackTime;
			progress = 1.0F - kobold.attackTime;
			progress = progress * progress;
			progress = progress * progress;
			progress = 1.0F - progress;
			float f2 = Mth.sin(progress * (float) Math.PI);
			leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
			rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
		}
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT -> {
				this.leftArm.translateAndRotate(pose);
				pose.translate(0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
			case RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
		}
	}
}