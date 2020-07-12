package robTheSpire.powers

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import robTheSpire.DefaultMod
import robTheSpire.util.TextureLoader


class GoldenWallPower(theOwner: AbstractCreature, armorAmt: Int) : AbstractGoldConversionPower(theOwner, armorAmt,2, "Goldicize") {
    init {
        name = NAME
        ID = POWER_ID
        type = PowerType.BUFF
        amount = armorAmt

        region128 = TextureAtlas.AtlasRegion(tex128, 0, 0, 128, 128)
        region48 = TextureAtlas.AtlasRegion(tex48, 0, 0, 48, 48)
    }


    override fun playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_METALLICIZE", 0.05f)
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + conversionRate + DESCRIPTIONS[2]
    }

    override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean) {
        if(!EscapeCountdownPower.isEscapingThisTurn) {
            val blockToGain: Int = convertGoldToResource(amount)
            if (blockToGain > 0) {
                flash()
                println("Should gain $blockToGain block.")
                addToBot(GainBlockAction(owner, owner, blockToGain))
            }
        }
    }



    companion object {
        private val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS

        private val tex128 = TextureLoader.getTexture(DefaultMod.makePowerPath("Glowing_Goldicize_128.png"))
        private val tex48 = TextureLoader.getTexture(DefaultMod.makePowerPath("Glowing_Goldicize_48.png"))
    }

}
