package robTheSpire.relics.Default

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.DefaultMod.Companion.makeRelicOutlinePath
import robTheSpire.DefaultMod.Companion.makeRelicPath
import robTheSpire.util.TextureLoader.getTexture

class PlaceholderRelic : CustomRelic(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL) {
    // Flash at the start of Battle.
    override fun atBattleStartPreDraw() {
        flash()
    }

    // Gain 1 energy on equip.
    override fun onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1
    }

    // Lose 1 energy on unequip.
    override fun onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1
    }

    // Description
    override fun getUpdatedDescription(): String {
        return DESCRIPTIONS[0]
    }

    companion object {
        /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */
        // ID, images, text.
        val ID = makeID("PlaceholderRelic")
        private val IMG = getTexture(makeRelicPath("placeholder_relic.png"))
        private val OUTLINE = getTexture(makeRelicOutlinePath("placeholder_relic.png"))
    }
}