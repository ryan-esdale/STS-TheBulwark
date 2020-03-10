package theBulwark.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import theBulwark.DefaultMod;
import theBulwark.util.TextureLoader;

import static theBulwark.DefaultMod.makePowerPath;

public class KineticBufferPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("KineticBufferPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int bufferedDamage;
    //public int amount;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public KineticBufferPower(final AbstractCreature owner, int buffer ) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = buffer;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
/*
    @Override
    //public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
    public int onAttacked(DamageInfo info, int damageAmount) {
        int reducedDamageAmount = damageAmount;

        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
            this.flash();
            if( damageAmount%2 == 0 ){
                reducedDamageAmount = damageAmount/2;
            }else{
                reducedDamageAmount = (damageAmount/2)+1;
            }
            this.stackPower( damageAmount/2 );
        }

        return reducedDamageAmount;
    }
*/

    private void applyThreshold( int healAmount){
        DefaultMod.logger.info("Health Moving from "+this.owner.currentHealth+" to "+ (this.owner.currentHealth+healAmount) );
        DefaultMod.logger.info("Threshold showing "+this.amount+" over "+ (this.owner.currentHealth+healAmount)/2 );
        if( this.amount >= (this.owner.currentHealth+healAmount)/2F && !this.owner.hasPower(ThresholdPower.POWER_ID)){
            DefaultMod.logger.info("Adding threshold" );
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.owner, this.owner, new ThresholdPower(this.owner, this.owner, 1))
            );
        }else if( this.amount < (this.owner.currentHealth+healAmount)/2F && this.owner.hasPower(ThresholdPower.POWER_ID) ){
            DefaultMod.logger.info( "Removing Threshold");
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(this.owner, this.owner, ThresholdPower.POWER_ID, 1)
            );
        }
    }

    //Apply Threshold if buffered damage is over half of current life
    @Override
    public int onHeal( int healAmount ){
        applyThreshold(healAmount);
        return healAmount;
    }

    @Override
    public int onLoseHp( int damageAmount ){
        applyThreshold(-damageAmount);
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);

        if( this.owner.hasPower(NanobotCoatingPower.POWER_ID) ){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.owner, this.owner, new RegenPower(this.owner, this.owner.getPower(NanobotCoatingPower.POWER_ID).amount))
            );
        }
        applyThreshold(0);
    }

    @Override
    public void reducePower(int reduceAmount){
        super.reducePower(reduceAmount);
        applyThreshold(0);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {

        //Only need to trigger if there's damage buffered
        if( this.amount > 0 ) {
            this.flashWithoutSound();
            this.playApplyPowerSfx();
            int damageAmount = 0;

            if (this.amount % 2 == 0) {
                damageAmount = this.amount / 2;
            } else {
                damageAmount = (this.amount / 2) + 1;
            }

            this.reducePower(damageAmount);
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, damageAmount, DamageInfo.DamageType.THORNS)));
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + FontHelper.colorString(String.valueOf(this.amount), "b") + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new KineticBufferPower(owner, amount);
    }
}

