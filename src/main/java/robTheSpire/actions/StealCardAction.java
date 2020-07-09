package robTheSpire.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import robTheSpire.characters.TheDefault;

public class StealCardAction extends AbstractCardStealingAction {
    public StealCardAction() {
        super(null,(c, prohibited) -> c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.color == TheDefault.Enums.COLOR_GRAY && !prohibited.contains(c.cardID));
    }
}
