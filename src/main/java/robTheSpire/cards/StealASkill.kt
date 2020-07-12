package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import robTheSpire.DefaultMod.Companion.makeID

class StealASkill : AbstractStealACardByType(CardType.SKILL, ID, cardStrings!!, CardRarity.UNCOMMON) {
    companion object {
        val ID = makeID(StealASkill::class.java.simpleName)
        private var cardStrings: CardStrings? = null

        init {
            cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        }
    }

    override fun makeCopy(): AbstractCard {
        return StealASkill()
    }
}