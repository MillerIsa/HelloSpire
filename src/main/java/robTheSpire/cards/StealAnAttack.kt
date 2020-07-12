package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import robTheSpire.DefaultMod.Companion.makeID

class StealAnAttack : AbstractStealACardByType(CardType.ATTACK, ID, cardStrings!!) {
    companion object {
        val ID = makeID(StealAnAttack::class.java.simpleName)
        private var cardStrings: CardStrings? = null

        init {
            cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        }
    }

    override fun makeCopy(): AbstractCard {
        return StealAnAttack()
    }
}