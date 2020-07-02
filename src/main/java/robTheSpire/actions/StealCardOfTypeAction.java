package robTheSpire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class StealCardOfTypeAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final CardType cardType;

    public StealCardOfTypeAction(CardType type) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.cardType = type;
    }


    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices(cardType);

        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1],true);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade();
                    }

                    disCard.current_x = -1000.0F * Settings.scale;
                    if (this.amount == 1) {

                        AbstractDungeon.player.masterDeck.addToTop(disCard);
                    } else{
                        throw new IllegalStateException("Tried to steal more than one card. This is invalid because it is only possible to steal one card.");
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }

        }
        this.tickDuration();
    }



    private ArrayList<AbstractCard> generateCardChoices(CardType type) {
        ArrayList<AbstractCard> skillChoices = new ArrayList<>();

        while(skillChoices.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp;
            System.out.println("About to get A Card");
            tmp = CardLibrary.getAnyColorCard(type, AbstractDungeon.rollRarity());
            System.out.println("Got card " + tmp.cardID);

            for (AbstractCard c : skillChoices) {
                System.out.println("c's cardID is " + c.cardID);
                System.out.println("tmp's cardID is " + tmp.cardID);
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            System.out.println("Dupe is: " + dupe);
            if (!dupe) {
                skillChoices.add(tmp.makeCopy());
            }
            System.out.println("Number of skill choices is" + skillChoices.size());
        }

        return skillChoices;
    }

/*
    public static AbstractCard getAnyColorCard(final AbstractCard.CardType type, final AbstractCard.CardRarity rarity) {
        final CardGroup anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (final Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if (c.getValue().rarity == rarity && c.getValue().type != AbstractCard.CardType.CURSE && c.getValue().type != AbstractCard.CardType.STATUS && c.getValue().type == type && (!UnlockTracker.isCardLocked(c.getKey()) || Settings.treatEverythingAsUnlocked())) {
                anyCard.addToBottom(c.getValue());
            }
        }
        anyCard.shuffle(AbstractDungeon.cardRng);
        return anyCard.getRandomCard(true, rarity);
    }

 */
}
