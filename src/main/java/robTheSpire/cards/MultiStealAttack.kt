package robTheSpire.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.PummelDamageAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.characters.TheDefault


class MultiStealAttack : AbstractCard(ID, cardStrings.NAME, "red/attack/pummel", 1, cardStrings.DESCRIPTION, CardType.ATTACK, TheDefault.Enums.COLOR_GRAY, CardRarity.UNCOMMON, CardTarget.ENEMY) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        for (i in 1 until magicNumber) {
            addToBot(PummelDamageAction(m, DamageInfo(p, damage, damageTypeForTurn)))
        }
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY))
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
        @JvmField val ID: String = "robTheSpire:MultiStealAttack"
        private val cardStrings: CardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
    }

    init {
        baseDamage = 2
        exhaust = true
        baseMagicNumber = 4
        magicNumber = baseMagicNumber
    }
}