package net.salju.kobolds.compat;

import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.mehvahdjukaar.supplementaries.common.entities.goals.*;

public class Supplementaries  {
    public static void addCannonGoals(AbstractKoboldEntity kobold) {
        kobold.goalSelector.addGoal(1, new UseCannonBoatGoal(kobold));
        kobold.goalSelector.addGoal(3, new UseCannonBlockGoal(kobold, 1.0, 20));
    }

    public static void addBoatGoals(AbstractKoboldEntity kobold) {
        kobold.goalSelector.addGoal(2, new BoardBoatGoal(kobold, 1, 200));
        kobold.goalSelector.addGoal(3, new AbandonShipGoal(kobold));
        kobold.goalSelector.addGoal(6, new IAmTheCaptainGoal(kobold));
    }
}