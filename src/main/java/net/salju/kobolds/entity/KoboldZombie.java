package net.salju.kobolds.entity;

import net.salju.kobolds.init.KoboldsMobs;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class KoboldZombie extends AbstractKoboldZombie {
	public KoboldZombie(EntityType<KoboldZombie> type, Level world) {
		super(type, world);
	}

    @Override
    public void convertZombo() {
        Kobold kobold = this.convertTo(KoboldsMobs.KOBOLD.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
        if (kobold != null) {
            kobold.setType(this.zomboType);
        }
    }

    @Override
    public void setZombo(AbstractKoboldEntity target) {
        if (target instanceof Kobold kobold) {
            if (kobold.isWarrior()) {
                this.setZomboType("warrior");
                this.zomboType = 45;
            } else if (kobold.isEngineer()) {
                this.setZomboType("engineer");
                this.zomboType = 95;
            } else if (kobold.isEnchanter()) {
                this.setZomboType("enchanter");
                this.zomboType = 10;
            } else {
                this.setZomboType("base");
                this.zomboType = 45;
            }
        }
    }

    @Override
    public void setZombo(int i) {
        this.zomboType = i;
        if (i >= 85) {
            this.setZomboType("engineer");
        } else if (i <= 15) {
            this.setZomboType("enchanter");
        } else {
            this.setZomboType("base");
        }
    }
}