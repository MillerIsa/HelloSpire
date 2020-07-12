package robTheSpire.patches.relics

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.megacrit.cardcrawl.cards.AbstractCard

/*
 * Patches have a pretty detailed documentation. Go check em out here:
 *
 *  https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch
 */
object BottledPlaceholderField {
    var inBottledPlaceholderField = SpireField(SpireField.DefaultValue { false })

    // SpireField is a wonderful thing that lets us add our own fields to preexisting classes in the game.
    // In this scenario we're going to add a boolean named "inBottledPlaceholderField" to the "makeStatEquivalentCopy" method inside AbstractCard
    object MakeStatEquivalentCopy {
        fun Postfix(result: AbstractCard, self: AbstractCard?): AbstractCard {
            // This is a postfix patch, meaning it'll be inserted at the very end of makeStatEquivalentCopy()
            inBottledPlaceholderField[result] = inBottledPlaceholderField[self] // Read:
            // set inBottledPlaceholderField to have the card and true/false depending on whether it's bottled or not.
            return result // Return the bottled card.
        }
    }
}