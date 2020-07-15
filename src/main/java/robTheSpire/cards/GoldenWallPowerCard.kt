package robTheSpire.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY
import robTheSpire.powers.GoldenWallPower

class GoldenWallPowerCard : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(p, p, GoldenWallPower(p, ARMOR), ARMOR))
    }

    // Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeBaseCost(UPGRADED_COST)
            initializeDescription()
        }
    }

    companion object {
        val ID = makeID(GoldenWallPowerCard::class.java.simpleName)
        val IMG = makeCardPath("goldicize.png")
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.POWER
        val COLOR = COLOR_GRAY
        private const val COST = 1
        private const val UPGRADED_COST = 0
        private const val DAMAGE = 0
        private const val UPGRADE_PLUS_DMG = 0
        private const val ARMOR = 5
    }

    init {
        baseDamage = DAMAGE
    }
}