package robTheSpire.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.characters.TheDefault
import robTheSpire.powers.LuckPower

class LuckPowerCard : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    init {
        this.baseMagicNumber = LUCK_AMOUNT
        this.magicNumber = this.baseMagicNumber
    }
    override fun use(p: AbstractPlayer, m: AbstractMonster?){
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(p, p, LuckPower(p, magicNumber), magicNumber))
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        val IMG = DefaultMod.makeCardPath("lucky_charm.png")
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.POWER
        val COLOR = TheDefault.Enums.COLOR_GRAY
        private const val COST = 1
        private const val LUCK_AMOUNT = 2
        private const val LUCK_UPGRADE_PLUS = 1
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            initializeDescription()
            upgradeMagicNumber(LUCK_UPGRADE_PLUS)
        }
    }

}