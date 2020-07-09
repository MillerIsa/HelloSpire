package robTheSpire.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.actions.FlatGoldAction
import robTheSpire.characters.TheDefault

class Strike : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    init{
        baseDamage = DAMAGE
        this.exhaust = true
        this.baseMagicNumber = BASE_MAGIC_NUMBER
        this.magicNumber = this.baseMagicNumber
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        val IMG: String = DefaultMod.makeCardPath("Attack.png")
        private val RARITY = CardRarity.COMMON
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR: CardColor = TheDefault.Enums.COLOR_GRAY
        private const val COST = 1
        private const val UPGRADED_COST = COST
        private const val DAMAGE = 3
        private const val UPGRADE_PLUS_DMG = 0
        private const val BASE_MAGIC_NUMBER = 5
        private const val UPGRADE_PLUS_MAGIC_NUMBER = 3
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        AbstractDungeon.actionManager.addToBottom(
                DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
        AbstractDungeon.actionManager.addToBottom(
                FlatGoldAction(m,magicNumber ))
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
            upgradeBaseCost(UPGRADED_COST)
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER)
            initializeDescription()
        }
    }

}