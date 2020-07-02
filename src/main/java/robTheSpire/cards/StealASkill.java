package robTheSpire.cards;


import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import robTheSpire.DefaultMod;

public class StealASkill extends AbstractStealACardByType {
    public static final String ID = DefaultMod.makeID(StealASkill.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealASkill() {
        //super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF);
        super(CardType.SKILL, ID, cardStrings);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

}
