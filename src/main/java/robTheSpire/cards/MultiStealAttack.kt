package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.actions.MultiStealAction
import robTheSpire.characters.TheDefault


class MultiStealAttack : AbstractDynamicCard(ID, DefaultMod.makeCardPath("Pummel.png"), 1, CardType.ATTACK, TheDefault.Enums.COLOR_GRAY, CardRarity.RARE, CardTarget.ENEMY) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        for (i in 0 until magicNumber) {
            addToBot(MultiStealAction(m, DamageInfo(p, damage, damageTypeForTurn)))
        }
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeMagicNumber(1)
            rawDescription = cardStrings.UPGRADE_DESCRIPTION
            initializeDescription()
        }
    }

    override fun makeCopy(): AbstractCard {
        return MultiStealAttack()
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(MultiStealAttack::class.java.simpleName)
        private val cardStrings: CardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
    }

    init {
        baseDamage = 2
        exhaust = true
        baseMagicNumber = 4
        magicNumber = baseMagicNumber
    }
}