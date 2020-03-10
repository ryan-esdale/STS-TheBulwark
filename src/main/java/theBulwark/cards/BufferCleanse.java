package theBulwark.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theBulwark.DefaultMod;
import theBulwark.actions.ReduceKineticBufferPowerAction;
import theBulwark.characters.TheDefault;

import static theBulwark.DefaultMod.makeCardPath;

public class BufferCleanse extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * A Better Defend Gain 1 Plated Armor. Affected by Dexterity.
     */

    // TEXT DECLARATION 

    public static final String ID = DefaultMod.makeID(BufferCleanse.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION 	

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    //private static final int UPGRADE_REDUCED_COST = 1;

    //private static final int BLOCK = 1;
    //private static final int UPGRADE_PLUS_BLOCK = 2;

    private static final int CLEANSE = 7;
    private static final int UPGRADE_PLUS_CLEANSE = 2;

    // /STAT DECLARATION/


    public BufferCleanse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CLEANSE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(p, p, new PlatedArmorPower(p, block), block));
        if( p.hasPower("theBulwark:KineticBufferPower")) {
            DefaultMod.logger.info("Calling custom power reduce");
            AbstractDungeon.actionManager.addToBottom(new ReduceKineticBufferPowerAction(p, p, p.getPower("theBulwark:KineticBufferPower"), this.magicNumber));
        }
        //p.getPower("KineticBufferPower").amount -= this.magicNumber;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber( UPGRADE_PLUS_CLEANSE );
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            //upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}