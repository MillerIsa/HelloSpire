package robTheSpire.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.CardIgnore;
import robTheSpire.DefaultMod;
import robTheSpire.actions.DamageToGoldAction;
import robTheSpire.characters.TheDefault;

import static robTheSpire.DefaultMod.makeCardPath;
@CardIgnore
public class StrikeGold extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(StrikeGold.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Thief.png");

    // STAT DECLARATION (These values are used to initialize the fields in the parent class AbstractCard)
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 5;

    public StrikeGold() {
        super(StrikeGold.class, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.exhaust = true;
        this.tags.add(CardTags.STRIKE);
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
        return new StrikeGold();
    }

}
