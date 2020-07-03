package robTheSpire.cards;


import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.DefaultMod;
import robTheSpire.actions.StealCardAction;

import robTheSpire.characters.TheDefault;
import sun.security.jca.GetInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static robTheSpire.DefaultMod.makeCardPath;

public class StealACard extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(StealACard.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealACard() {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF);
        this.exhaust = true;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StealCardAction());
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.exhaust = false;
            ExhaustiveVariable.setBaseValue(this, 2);
            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new robTheSpire.cards.StealACard();
    }


    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
