package theBulwark.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theBulwark.DefaultMod;
import theBulwark.characters.TheDefault;
import theBulwark.powers.KineticBufferPower;

import static theBulwark.DefaultMod.makeCardPath;

public class Brace extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Brace.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Skill.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(Brace.ID);

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    // /STAT DECLARATION/


    public Brace() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;

        updateDesc(false );
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction( calculateCardDraw() )
        );
    }

    @Override
    public void triggerWhenDrawn(){
        updateDesc(true);
    }

    @Override
    public void onMoveToDiscard(){
        updateDesc(false);
    }

    @Override
    public void applyPowers(){
        super.applyPowers();
        updateDesc(true );
    }

    private int calculateCardDraw(){
        int cardsToDraw = 0;
        if( AbstractDungeon.player.hasPower(KineticBufferPower.POWER_ID) ){
            cardsToDraw = magicNumber * (AbstractDungeon.player.getPower(KineticBufferPower.POWER_ID).amount/5);
        }
        return cardsToDraw;
    }

    private void updateDesc(boolean inHand) {
        if(this.upgraded)
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        else{
            this.rawDescription = cardStrings.DESCRIPTION;
        }
        if( inHand )
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] +
                    this.calculateCardDraw() + cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }


}
