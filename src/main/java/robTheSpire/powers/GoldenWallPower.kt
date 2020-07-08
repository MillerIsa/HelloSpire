package robTheSpire.powers

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.DefaultMod
import robTheSpire.vfx.LosePennyEffect


class GoldenWallPower(theOwner: AbstractCreature, armorAmt: Int) : AbstractPower() {
    override fun playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_METALLICIZE", 0.05f)
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + CONVERSION_RATE + DESCRIPTIONS[2]
    }

    override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean) {

        flash()
        val blockToGain: Int = convertGoldToBlock(amount)
        println("Should gain $blockToGain block.")
        addToBot(GainBlockAction(owner, owner, blockToGain))
    }

    private fun loseGold(goldLoss: Int) {
        loseGold(AbstractDungeon.player, owner, goldLoss)
    }

    private fun loseGold(goldStealer: AbstractCreature, victim: AbstractCreature, goldLoss: Int) {
        AbstractDungeon.player.loseGold(goldLoss)
        for (i in 0 until goldLoss) {
            AbstractDungeon.effectList.add(LosePennyEffect(goldStealer, victim.hb.cX, victim.hb.cY, goldStealer.hb.cX, goldStealer.hb.cY - 600, true))
        }
    }


    //returns how many resource units the gold converts into
     private fun convertGoldToBlock(damageAmount: Int): Int {
        val rawGoldToLose = CONVERSION_RATE * damageAmount
        var goldToLose = rawGoldToLose.coerceAtMost(AbstractDungeon.player.gold)
        val remainder = goldToLose % CONVERSION_RATE
        goldToLose -= remainder
        loseGold(goldToLose)
        return goldToLose / CONVERSION_RATE
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
