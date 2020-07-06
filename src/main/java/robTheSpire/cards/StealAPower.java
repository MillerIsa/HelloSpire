package robTheSpire.cards;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import robTheSpire.DefaultMod;

public class StealAPower extends AbstractStealACardByType {
    public static final String ID = DefaultMod.makeID(StealAPower.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealAPower() {
        super(CardType.POWER, ID, cardStrings, CardRarity.RARE);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public AbstractCard makeCopy(){
        return new StealAPower();
    }

}
