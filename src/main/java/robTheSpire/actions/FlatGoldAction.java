package robTheSpire.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import robTheSpire.powers.LuckPower;

public class FlatGoldAction extends AbstractGameAction {
    private final int increaseGold;
    private final DamageInfo info;
    private static final float DURATION = 0.1F;

    public FlatGoldAction(AbstractCreature target, DamageInfo info, int goldAmount) {
        int increaseGold1;
        this.info = info;
        this.setValues(target, info);
        increaseGold1 = goldAmount;
        if(AbstractDungeon.player.hasPower(LuckPower.POWER_ID)) {
            increaseGold1 += AbstractDungeon.player.getPower(LuckPower.POWER_ID).amount;
        }
        this.increaseGold = increaseGold1;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
            // gain gold --------------------------------------------------
            AbstractDungeon.player.gainGold(this.increaseGold);
            for (int i = 0; i < this.increaseGold; ++i) {
                AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, true));
            }
            // gain gold --------------------------------------------------

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
