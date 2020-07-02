package robTheSpire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;
import java.util.Map;

public class StealCardOfTypeAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    final CardGroup anyCard;

    public StealCardOfTypeAction(CardType type) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        initCardGroup(type);
    }


    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices();

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



    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> skillChoices = new ArrayList<>();

        while(skillChoices.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp;
            System.out.println("About to get A Card");
            tmp = this.getAnyColorCard(AbstractDungeon.rollRarity());//TODO prevent stealing colorless cards.
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


    private AbstractCard getAnyColorCard(final AbstractCard.CardRarity rarity) {
        anyCard.shuffle(AbstractDungeon.cardRng);
        return anyCard.getRandomCard(true, rarity);
    }

    private void initCardGroup (CardType type){
        for (final Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if (c.getValue().color != AbstractCard.CardColor.COLORLESS && c.getValue().type != AbstractCard.CardType.CURSE && c.getValue().type != AbstractCard.CardType.STATUS && c.getValue().type == type/* && (!UnlockTracker.isCardLocked(c.getKey()) || Settings.treatEverythingAsUnlocked()) // This means everything is unlocked*/) {
                anyCard.addToBottom(c.getValue());
            }
        }
    }


}
