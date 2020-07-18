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

class Strike : AbstractGoldStealingCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    init{
        baseDamage = DAMAGE
        this.baseGoldNumber = BASE_GOLD_NUMBER
        this.goldNumber = this.baseGoldNumber
        this.tags.add(CardTags.STRIKE)
        this.tags.add(CardTags.STARTER_STRIKE)
        println("Gold Number is: $goldNumber")
        println("Base Gold Number is $baseGoldNumber")
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        val IMG: String = DefaultMod.makeCardPath("strike.png")
        private val RARITY = CardRarity.BASIC
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR: CardColor? = TheDefault.Enums.COLOR_GRAY
        private const val COST = 1
        private const val UPGRADED_COST = COST
        private const val DAMAGE = 3
        private const val UPGRADE_PLUS_DMG = 0
        private const val BASE_GOLD_NUMBER = 3
        private const val UPGRADE_PLUS_GOLD_NUMBER = 2
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        AbstractDungeon.actionManager.addToBottom(
                DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
        AbstractDungeon.actionManager.addToBottom(
                FlatGoldAction(m,goldNumber))
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeGoldenNumber(UPGRADE_PLUS_GOLD_NUMBER)
            initializeDescription()
        }
    }

}