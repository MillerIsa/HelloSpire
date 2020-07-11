package robTheSpire.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import robTheSpire.characters.TheDefault;

public class StealCardAction extends AbstractCardStealingAction {
    private static final int RARITY_FLAT_PERCENTAGE_BUFF = -25;
    public StealCardAction() {
        super(null,(c, prohibited) -> c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.color == TheDefault.Enums.getCOLOR_GRAY() && !prohibited.contains(c.cardID));
    }


    @Override
    protected AbstractCard.CardRarity rollRarity(Random rng) {
        int roll = AbstractDungeon.cardRng.random(99);
        roll += AbstractDungeon.cardBlizzRandomizer;
        roll += RARITY_FLAT_PERCENTAGE_BUFF;
        System.out.println("Rolled a " + roll);
        return AbstractDungeon.currMapNode == null ? getCardRarityFallback(roll) : AbstractDungeon.getCurrRoom().getCardRarity(roll);
    }

    @Override
    protected AbstractCard.CardRarity getCardRarityFallback(int roll) {
        System.out.println("WARNING: Current map node was NULL while rolling rarities. Using fallback card rarity method.");
        int rareRate = 3 + RARITY_FLAT_PERCENTAGE_BUFF;
        if (roll < rareRate) {
            return AbstractCard.CardRarity.RARE;
        } else {
            return roll < 40 ? AbstractCard.CardRarity.UNCOMMON : AbstractCard.CardRarity.COMMON;
        }
    }
}
