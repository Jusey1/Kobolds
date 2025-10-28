package net.salju.kobolds.client.model;

import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class SkeleboldModel<T extends AbstractKoboldState> extends KoboldModel<T> {
	public SkeleboldModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = KoboldModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(46, 16).addBox(-2.01F, -0.85F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(33, 16).addBox(0.01F, -0.85F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(13, 31).addBox(-1.0F, -0.01F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 14.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 31).addBox(-1.0F, -0.01F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 14.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 15).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 15).addBox(-0.5F, 6.0F, -4.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void translateToHand(AbstractKoboldState state, HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT -> {
				this.leftArm.translateAndRotate(pose);
				pose.translate(0.018, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
			case RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.018, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
		}
	}
}