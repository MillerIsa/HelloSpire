package robTheSpire.powers

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.DefaultMod


class GoldenWallPower(theOwner: AbstractCreature, armorAmt: Int) : AbstractPower() {
    override fun playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_METALLICIZE", 0.05f)
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + CONVERSION_RATE + DESCRIPTIONS[2]
    }

    override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean) {
        flash()
        addToBot(GainBlockAction(owner, owner, amount))
    }

    companion object {
        const val CONVERSION_RATE = 2
        val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS
    }

    init {
        name = NAME
        ID = POWER_ID
        this.owner = theOwner
        amount = armorAmt
        updateDescription()
        loadRegion("armor")
    }
}
//TODO: Update Maven to copy the FAT jar instead of the normal jar.
