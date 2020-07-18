package robTheSpire.variables

import basemod.abstracts.DynamicVariable
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import robTheSpire.DefaultMod
import robTheSpire.cards.AbstractDefaultCard
import robTheSpire.cards.AbstractGoldStealingCard

class GoldVariable : DynamicVariable() {
    // Custom Dynamic Variables are what you do if you need your card text to display a cool, changing number that the base game doesn't provide.
    // If the !D! and !B! (for Damage and Block) etc. are not enough for you, this is how you make your own one. It Changes In Real Time!
    // This is what you type in your card string to make the variable show up. Remember to encase it in "!"'s in the json!
    override fun key(): String {
        println("returning id: "+ DefaultMod.makeID("Gold"))
        return DefaultMod.makeID("Gold")
    }

    override fun isModified(card: AbstractCard): Boolean {
        return (card as AbstractGoldStealingCard).isGoldenNumberModified
    }

    override fun value(card: AbstractCard): Int {
        return (card as AbstractGoldStealingCard).goldNumber
    }

    override fun baseValue(card: AbstractCard): Int {
        return (card as AbstractGoldStealingCard).baseGoldNumber
    }

    override fun upgraded(card: AbstractCard): Boolean {
        return (card as AbstractGoldStealingCard).isUpgradedGoldenNumber
    }

    override fun getNormalColor(): Color {
        return Color(1.00f, 1.00f, .37f, 1.00f)//Pale Yellow
    }

}