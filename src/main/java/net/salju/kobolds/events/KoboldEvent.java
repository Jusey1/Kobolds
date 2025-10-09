package net.salju.kobolds.events;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.neoforged.bus.api.Event;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class KoboldEvent extends Event {
    private final AbstractKoboldEntity kobold;

	public KoboldEvent(AbstractKoboldEntity target) {
        this.kobold = target;
    }

    public AbstractKoboldEntity getKobold() {
        return this.kobold;
    }

    public static class DragonEvent extends KoboldEvent {
        private @Nullable LivingEntity dragon;

        public DragonEvent(AbstractKoboldEntity target) {
            super(target);
        }

        public @Nullable LivingEntity getDragon() {
            return this.dragon;
        }

        public void setDragon(@Nullable LivingEntity target) {
            this.dragon = target;
        }
    }

    public static class DragonColorEvent extends KoboldEvent {
        private final @Nullable LivingEntity dragon;
        private int color;

        public DragonColorEvent(AbstractKoboldEntity target, @Nullable LivingEntity owner) {
            super(target);
            this.dragon = owner;
        }

        public @Nullable LivingEntity getDragon() {
            return this.dragon;
        }

        public int getColorInt() {
            return this.color;
        }

        public void setColorInt(int i) {
            this.color = i;
        }
    }
}