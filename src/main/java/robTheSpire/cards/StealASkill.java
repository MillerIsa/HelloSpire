package robTheSpire.cards;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import robTheSpire.DefaultMod;

public class StealASkill extends AbstractStealACardByType {
    public static final String ID = DefaultMod.makeID(StealASkill.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealASkill() {
        super(CardType.SKILL, ID, cardStrings,CardRarity.UNCOMMON);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public AbstractCard makeCopy(){
        return new StealASkill();
    }

}
