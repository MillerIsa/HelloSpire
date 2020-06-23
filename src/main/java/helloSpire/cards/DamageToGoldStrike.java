package helloSpire.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helloSpire.DefaultMod;
import helloSpire.actions.DamageToGoldAction;
import helloSpire.actions.FlatGoldAction;
import helloSpire.characters.TheDefault;

import static helloSpire.DefaultMod.makeCardPath;
public class DamageToGoldStrike extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(DamageToGoldStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Thief.png");

    // STAT DECLARATION (These values are used to initialize the fields in the parent class AbstractCard)
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 5;

    public DamageToGoldStrike() {
        super(ID, cardStrings.NAME, IMG, COST, cardStrings.DESCRIPTION, TYPE, COLOR, RARITY, TARGET); //TODO: Change image URL, make default values constructor level
        this.baseDamage = DAMAGE;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageToGoldAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.NONE));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(5);
        }

    }

    public AbstractCard makeCopy() {
        return new helloSpire.cards.DamageToGoldStrike();
    }

}
