package robTheSpire.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.actions.DamageToGoldAction
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY

class StrikeGold : AbstractDefaultCard(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET) {
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        addToBot(DamageToGoldAction(m, DamageInfo(p, damage, damageTypeForTurn), AttackEffect.NONE))
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
        }
    }

    override fun makeCopy(): AbstractCard {
        return StrikeGold()
    }

    companion object {
        val ID = makeID(StrikeGold::class.java.simpleName)
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val IMG = makeCardPath("StrikeGold.png")

        // STAT DECLARATION (These values are used to initialize the fields in the parent class AbstractCard)
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR = COLOR_GRAY
        private const val COST = 1
        private const val DAMAGE = 10
        private const val UPGRADE_PLUS_DMG = 5
    }

    init {
        baseDamage = DAMAGE
        exhaust = true
        tags.add(CardTags.STRIKE)
    }
}