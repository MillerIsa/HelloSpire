package robTheSpire.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.characters.TheDefault
import robTheSpire.powers.LuckPower

class LuckPowerCard : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    override fun use(p: AbstractPlayer, m: AbstractMonster?){
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(p, p, LuckPower(p, LUCK_AMOUNT)))
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private val cardStrings: CardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        const val LUCK_AMOUNT = 2
        val IMG = DefaultMod.makeCardPath("Power.png")
        private val RARITY = CardRarity.COMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.POWER
        val COLOR = TheDefault.Enums.COLOR_GRAY
        private const val COST = 1
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            initializeDescription()
        }
    }

}