package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID

class StealAPower : AbstractStealACardByType(CardType.POWER, ID, cardStrings!!, CardRarity.RARE, makeCardPath("steal_power.png")) {
    companion object {
        val ID = makeID(StealAPower::class.java.simpleName)
        private var cardStrings: CardStrings? = null

        init {
            cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        }
    }

    override fun makeCopy(): AbstractCard {
        return StealAPower()
    }
}