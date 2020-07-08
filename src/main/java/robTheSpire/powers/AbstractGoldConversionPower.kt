package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.DefaultMod
import robTheSpire.vfx.LosePennyEffect


abstract class AbstractGoldConversionPower(theOwner: AbstractCreature, armorAmt: Int, protected val conversionRate: Int, protected val regionName: String) : AbstractPower() {

    protected fun loseGold(goldLoss: Int) {
        loseGold(AbstractDungeon.player, owner, goldLoss)
    }

    private fun loseGold(goldStealer: AbstractCreature, victim: AbstractCreature, goldLoss: Int) {
        AbstractDungeon.player.loseGold(goldLoss)
        for (i in 0 until goldLoss) {
            AbstractDungeon.effectList.add(LosePennyEffect(goldStealer, victim.hb.cX, victim.hb.cY, goldStealer.hb.cX, goldStealer.hb.cY - 600, true))
        }
    }


    //returns how many resource units the gold converts into
    protected fun convertGoldToResource(resourceAmount: Int): Int {
        val rawGoldToLose = conversionRate * resourceAmount
        var goldToLose = rawGoldToLose.coerceAtMost(AbstractDungeon.player.gold)
        val remainder = goldToLose % conversionRate
        goldToLose -= remainder
        loseGold(goldToLose)
        return goldToLose / conversionRate
    }


    companion object {
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
        loadRegion(regionName)
    }
}