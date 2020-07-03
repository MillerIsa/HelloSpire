package robTheSpire.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;

public class StealCardOfTypeAction extends AbstractCardStealingAction {
    public StealCardOfTypeAction(CardType type) {
        super((c, prohibited) -> c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.type == type);
    }
}
