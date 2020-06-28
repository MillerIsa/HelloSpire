package helloSpire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helloSpire.DefaultMod;
import helloSpire.characters.TheDefault;
import helloSpire.powers.CappedGoldStealPower;

import static helloSpire.DefaultMod.makeCardPath;


public class Chris extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(Chris.class.getSimpleName());
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Chris() {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.ENEMY);
        this.magicNumber = 4;
        System.out.println("Chris card initialized!");
    }


    @Override
    public void upgrade() {
        //throw new IllegalAccessException("Upgrading this card isn't allowed because I haven't implemented its upgrade yet."); //TODO: Implement this method
    }

    @Override
    public AbstractCard makeCopy() {
        return new Chris();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).StealGold(magicNumber);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new CappedGoldStealPower(m, 4), 1));

    }
}