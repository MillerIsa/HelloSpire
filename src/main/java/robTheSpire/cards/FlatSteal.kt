package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.actions.FlatGoldAction
import robTheSpire.characters.TheDefault


class FlatSteal : AbstractDynamicCard(ID,DefaultMod.makeCardPath("Pummel.png"), 1, CardType.ATTACK, TheDefault.Enums.COLOR_GRAY, CardRarity.COMMON, CardTarget.ENEMY) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
       addToBot(FlatGoldAction(m, magicNumber))
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeMagicNumber(UPGRADED_MAGIC_NUMBER)
            rawDescription = cardStrings.UPGRADE_DESCRIPTION
            initializeDescription()
        }
    }

    override fun makeCopy(): AbstractCard {
        return FlatSteal()
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private val cardStrings: CardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        const val UPGRADED_MAGIC_NUMBER : Int = 3
        const val BASE_MAGIC_NUMBER : Int = 8
    }

    init {
        baseDamage = 0
        exhaust = true
        baseMagicNumber =  BASE_MAGIC_NUMBER
        magicNumber = baseMagicNumber
    }
}