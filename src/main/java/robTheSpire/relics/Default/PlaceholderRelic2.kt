package robTheSpire.relics.Default

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.StrengthPower
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.DefaultMod.Companion.makeRelicOutlinePath
import robTheSpire.DefaultMod.Companion.makeRelicPath
import robTheSpire.util.TextureLoader.getTexture

class PlaceholderRelic2 : CustomRelic(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT) {
    // Gain 1 Strength on on equip.
    override fun atBattleStart() {
        flash()
        AbstractDungeon.actionManager.addToTop(ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, StrengthPower(AbstractDungeon.player, 1), 1))
        AbstractDungeon.actionManager.addToTop(RelicAboveCreatureAction(AbstractDungeon.player, this))
    }

    // Description
    override fun getUpdatedDescription(): String {
        return DESCRIPTIONS[0]
    }

    companion object {
        /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */
        // ID, images, text.
        val ID = makeID("PlaceholderRelic2")
        private val IMG = getTexture(makeRelicPath("placeholder_relic2.png"))
        private val OUTLINE = getTexture(makeRelicOutlinePath("placeholder_relic2.png"))
    }
}