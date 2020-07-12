package robTheSpire.actions

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardType

class StealCardOfTypeAction(type: CardType?) : AbstractCardStealingAction(type, { c: AbstractCard, _: Set<String?>? -> c.color != AbstractCard.CardColor.COLORLESS && c.type != CardType.CURSE && c.type != CardType.STATUS })