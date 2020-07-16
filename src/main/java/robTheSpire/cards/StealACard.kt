package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.actions.StealCardAction
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY

class StealACard : AbstractDefaultCard(ID, cardStrings!!.NAME, makeCardPath("steal_a_card.png"), 2, cardStrings!!.DESCRIPTION, CardType.SKILL, COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF) {
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        addToBot(StealCardAction())
        println("Using StealACard!")
    }

    override fun upgrade() {
        if (!upgraded) {
//            this.exhaust = false;
//            ExhaustiveVariable.setBaseValue(this, 2);
//            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            upgradeBaseCost(1)
            upgradeName()
            rawDescription = cardStrings!!.UPGRADE_DESCRIPTION
            initializeDescription()
        }
    }

    override fun makeCopy(): AbstractCard {
        return StealACard()
    }

    companion object {
        val ID = makeID(StealACard::class.java.simpleName)
        private var cardStrings: CardStrings? = null

        init {
            cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
            println("ID for StealACard is: $ID")
        }
    }

    init {
        exhaust = true
    }
}