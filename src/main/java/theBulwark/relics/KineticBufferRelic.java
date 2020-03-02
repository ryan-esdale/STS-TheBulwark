package theBulwark.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theBulwark.DefaultMod;
import theBulwark.powers.KineticBufferPower;
import theBulwark.util.TextureLoader;

import static theBulwark.DefaultMod.makeRelicOutlinePath;
import static theBulwark.DefaultMod.makeRelicPath;

public class KineticBufferRelic extends CustomRelic {

    public static final String ID = DefaultMod.makeID("KineticBufferRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic2.png"));

    public KineticBufferRelic() { super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT); }

    @Override
    public void atBattleStart(){
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KineticBufferPower(AbstractDungeon.player, 0 ), 0 ));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
