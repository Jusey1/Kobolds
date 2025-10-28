package net.salju.kobolds.client.model;

import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.PoseStack;

public class RascalModel<T extends AbstractKoboldState> extends KoboldModel<T> {
	public final ModelPart bag;

	public RascalModel(ModelPart root) {
		super(root);
		this.bag = root.getChild("bag");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = KoboldModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("bag", CubeListBuilder.create().texOffs(26, 30).addBox(-1.0F, 0.0F, 2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(33, 7).addBox(-5.0F, -0.85F, -1.5F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T kobold) {
		this.rightArm.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * kobold.walkAnimationSpeed * 0.5F;
		this.leftArm.xRot = -0.7854F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F) * 1.4F * kobold.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(kobold.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * kobold.walkAnimationSpeed;
		this.head.yRot = kobold.yRot * ((float) Math.PI / 180F);
		this.head.xRot = kobold.xRot * ((float) Math.PI / 180F);
		this.rightArm.zRot += Mth.cos(kobold.ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (kobold.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (!kobold.getMainHandItem().isEmpty()) {
			if (kobold.isAggressive) {
				this.rightArm.xRot = -2.0944F;
				this.rightArm.yRot = 0.1745F;
			}
		}
		if (kobold.attackTime > 0.0F) {
			if (kobold.isAggressive) {
				float progress = kobold.attackTime;
				progress = 1.0F - kobold.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
			} else {
				float progress = kobold.attackTime;
				this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
				this.rightArm.yRot += this.body.yRot;
				this.leftArm.yRot += this.body.yRot;
				this.leftArm.xRot += this.body.yRot;
				progress = 1.0F - kobold.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				float f3 = Mth.sin(kobold.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
				rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 * 1.2D + (double) f3));
				rightArm.yRot += this.body.yRot * 2.0F;
				rightArm.zRot += Mth.sin(kobold.attackTime * (float) Math.PI) * -0.4F;
			}
		}
	}

	@Override
	public void translateToHand(AbstractKoboldState state, HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT, RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
        }
	}
}