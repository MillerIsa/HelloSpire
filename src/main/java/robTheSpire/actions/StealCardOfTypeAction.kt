package robTheSpire.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

public class StealCardOfTypeAction extends AbstractCardStealingAction {
    public StealCardOfTypeAction(AbstractCard.CardType type) {
        super(type, (c, prohibited) -> c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS);
    }
}
