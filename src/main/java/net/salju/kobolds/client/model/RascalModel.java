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
import net.minecraft.world.item.Item;
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
	public void translateToHand(AbstractKoboldState state, HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT, RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.045, 0.096, 0.0);
				pose.scale(0.75F, 0.75F, 0.75F);
			}
        }
	}

    @Override
    protected void defaultPose(T target) {
        super.defaultPose(target);
        this.leftArm.xRot = -0.7854F;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
    }

    @Override
    protected void poseArms(T target, Item item, ModelPart mainArm, ModelPart offArm) {
        if (target.isAggressive) {
            this.rightArm.xRot = -2.0944F;
            this.rightArm.yRot = 0.1745F;
        }
    }
}