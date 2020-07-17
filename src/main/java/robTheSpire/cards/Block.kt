package robTheSpire.cards


import basemod.BaseMod
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY
import robTheSpire.powers.AbstractGoldConversionPower.Companion.convertGoldToResource
import robTheSpire.powers.GoldenWallPower

class Block: AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        this.addToBot(GainBlockAction(AbstractDungeon.player,AbstractDungeon.player, convertGoldToResource(block.toDouble(), CONVERSION_RATE)))
    }

    // Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            baseMagicNumber = (CONVERSION_RATE * (baseBlock + UPGRADED_BLOCK_PLUS)).toInt()
            magicNumber = baseMagicNumber
            upgradedMagicNumber = true
            upgradeBlock(UPGRADED_BLOCK_PLUS)
        }
    }

    companion object {
        val ID = makeID(Block::class.java.simpleName)
        val IMG = makeCardPath("goldicize.png")
        private val RARITY = CardRarity.BASIC
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.SKILL
        val COLOR = COLOR_GRAY
        private const val COST = 1
        private const val DAMAGE = 0
        private const val UPGRADE_PLUS_DMG = 0
        private const val CONVERSION_RATE: Double = 1.0 / 3.0
        private const val BLOCK = 6
        private const val UPGRADED_BLOCK_PLUS = 3
    }

    init {
        baseMagicNumber = (CONVERSION_RATE * BLOCK).toInt()
        magicNumber = baseMagicNumber
        baseBlock = BLOCK
        tags.add(CardTags.STARTER_DEFEND)
    }
}