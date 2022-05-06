package theBulwark.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theBulwark.DefaultMod;
import theBulwark.characters.TheDefault;
import theBulwark.powers.SlipstreamPower;

import static theBulwark.DefaultMod.makeCardPath;

public class Slipstream extends AbstractDynamicCard {

        // TEXT DECLARATION

        public static final String ID = DefaultMod.makeID(Slipstream.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
        public static final String IMG = makeCardPath("Attack.png");// "public static final String IMG = makeCardPath("Slipstream.png");
        // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


        // /TEXT DECLARATION/


        // STAT DECLARATION

        private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
        private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
        private static final CardType TYPE = CardType.POWER;       //
        public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

        private static final int COST = 2;
        private static final int UPGRADED_COST = 1;

        private static final int MAGIC_NUMBER = 1;
        private static final int UPGRADE_MAGIC_NUMBER = 1;

        // /STAT DECLARATION/


        public Slipstream() {
                super(ID,IMG,COST,TYPE,COLOR,RARITY,TARGET);
                magicNumber = baseMagicNumber = MAGIC_NUMBER;
        }


        // Actions the card should do.
        @Override
        public void use(AbstractPlayer p,AbstractMonster m){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,
                new SlipstreamPower(p,p,magicNumber),magicNumber));
                }


        // Upgraded stats.
        @Override
        public void upgrade(){
                if(!upgraded){
                        upgradeName();
                        upgradeMagicNumber(UPGRADE_MAGIC_NUMBER);
                        upgradeBaseCost(UPGRADED_COST);
                        initializeDescription();
                }
        }
}
