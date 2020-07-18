package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.actions.FlatGoldAction
import robTheSpire.characters.TheDefault


class FlatSteal : AbstractGoldStealingCard(ID, DefaultMod.makeCardPath("normal_steal_skill.png"), 1, CardType.ATTACK, TheDefault.Enums.COLOR_GRAY, CardRarity.COMMON, CardTarget.ENEMY) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
       addToBot(FlatGoldAction(m, goldNumber))
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeGoldenNumber(UPGRADED_PLUS_GOLD_NUMBER)
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
        const val UPGRADED_PLUS_GOLD_NUMBER: Int = 3
        const val BASE_GOLD_NUMBER : Int = 8
    }

    init {
        baseDamage = 0
        exhaust = true
        baseGoldNumber =  BASE_GOLD_NUMBER
        goldNumber = baseGoldNumber
    }
}