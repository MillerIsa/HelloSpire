package robTheSpire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import robTheSpire.DefaultMod;

import static com.megacrit.cardcrawl.powers.AbstractPower.PowerType.BUFF;

public class BloodIsGoldPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(BloodIsGoldPower.class.getSimpleName());
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final int CONVERSION_RATE = 2; // Means that instead of losing 1 * HP will lose CONVERSION_RATE * gold


    public BloodIsGoldPower(AbstractCreature owner, int amount) {
        type = BUFF;
        updateDescription();

        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("fading");
    }

    @Override
    public void updateDescription() {
        String[] desc = powerStrings.DESCRIPTIONS;
        this.description = "TODO";
    }


    private void loseGold(int goldGain){
        loseGold( AbstractDungeon.player,this.owner, goldGain);
    }

    private void loseGold(AbstractCreature goldStealer, AbstractCreature victim, int goldLoss){
        AbstractDungeon.player.loseGold(goldLoss);
        for (int i = 0; i < goldLoss; ++i) {
            AbstractDungeon.effectList.add(new GainPennyEffect(goldStealer, victim.hb.cX, victim.hb.cY, goldStealer.hb.cX, goldStealer.hb.cY, true));
        }
    }

    //Lose gold instead of HP according to conversion rate.
    @Override
    public int onLoseHp(final int damageAmount) {
        final int rawGoldToLose = CONVERSION_RATE * damageAmount;
        int goldToLose = Math.min(rawGoldToLose, AbstractDungeon.player.gold);
        int remainder = goldToLose % CONVERSION_RATE;
        goldToLose -= remainder;
        loseGold(goldToLose);
        return damageAmount - goldToLose / CONVERSION_RATE;
    }


    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

}