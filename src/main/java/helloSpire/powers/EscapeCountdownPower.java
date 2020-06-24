package helloSpire.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import helloSpire.DefaultMod;

import java.util.Arrays;

public class EscapeCountdownPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID("EscapeCountdownPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public EscapeCountdownPower(AbstractCreature owner, int turns) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.updateDescription();
        this.loadRegion("fading");
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount;
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        System.out.println("End of Turn method called.");
        if (this.amount == 1 && !this.owner.isDying) {
            //this.addToBot(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1F));
            //this.addToBot(new SuicideAction((AbstractMonster)this.owner));
            System.out.println("Time to escape!");
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            this.updateDescription();
            System.out.println("Reducing Escaping Countdown by 1.");
        }

    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
