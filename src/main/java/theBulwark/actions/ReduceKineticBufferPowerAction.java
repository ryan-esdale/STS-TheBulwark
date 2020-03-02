package theBulwark.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReduceKineticBufferPowerAction extends AbstractGameAction {
    private String powerID;
    private AbstractPower powerInstance;

    // Action to allow removing of stacks of Kinetic Buffer without removing the power when it hits 0
    public ReduceKineticBufferPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerInstance, int amount) {
        this.setValues(target, source, amount);
        if (Settings.FAST_MODE) {
            this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.powerInstance = powerInstance;
        this.actionType = ActionType.REDUCE_POWER;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            AbstractPower reduceMe = null;
            if (this.powerID != null) {
                reduceMe = this.target.getPower(this.powerID);
            } else if (this.powerInstance != null) {
                reduceMe = this.powerInstance;
            }

            if (reduceMe != null) {
                //Make sure the kinetic buffer power never hits the remove power action
                if (this.amount > reduceMe.amount ) {
                    this.amount = reduceMe.amount;
                }
                BaseMod.logger.info("Reducing power: "+reduceMe.ID+" by amount "+this.amount);

                reduceMe.reducePower(this.amount);
                reduceMe.updateDescription();
                AbstractDungeon.onModifyPower();

            }
        }

        this.tickDuration();
    }
}
