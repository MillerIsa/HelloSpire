package robTheSpire.cards

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod
import robTheSpire.characters.TheDefault
import robTheSpire.powers.GoldenWallPower
import robTheSpire.powers.LuckPower
import robTheSpire.util.TextureLoader

class LuckPowerCard : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    init {
        this.baseMagicNumber = BASE_MAGIC
        this.magicNumber = this.baseMagicNumber
    }
    override fun use(p: AbstractPlayer, m: AbstractMonster?){
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(p, p, LuckPower(p, LUCK_AMOUNT), LUCK_AMOUNT))
    }

    companion object {
        @JvmField val ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        const val LUCK_AMOUNT = 1
        val IMG = DefaultMod.makeCardPath("Power.png")
        private val RARITY = CardRarity.UNCOMMON
        private val TARGET = CardTarget.SELF
        private val TYPE = CardType.POWER
        val COLOR = TheDefault.Enums.COLOR_GRAY
        private const val COST = 1
        private const val BASE_MAGIC = 1
        private const val MAGIC_UPGRADE_PLUS = 1
    }

    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            initializeDescription()
            upgradeMagicNumber(MAGIC_UPGRADE_PLUS)
        }
    }

}