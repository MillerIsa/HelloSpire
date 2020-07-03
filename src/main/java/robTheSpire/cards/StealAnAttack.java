package robTheSpire.cards;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import robTheSpire.DefaultMod;

public class StealAnAttack extends AbstractStealACardByType {
    public static final String ID = DefaultMod.makeID(StealAnAttack.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealAnAttack() {
        super(CardType.ATTACK, ID, cardStrings);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public AbstractCard makeCopy(){
        return new StealAnAttack();
    }

}
