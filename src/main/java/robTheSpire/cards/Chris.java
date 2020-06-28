package robTheSpire.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class Chris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chris.class).SetAttack(0, CardRarity.UNCOMMON);

    public Chris()
    {
        super(DATA);

        Initialize(4, 0, 4);
        SetUpgrade(2, 0, 2);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Top.Draw(1)
                .SetFilter(card -> card.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(card), false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).StealGold(magicNumber);
    }
}

/*
   Copyright 2019 EatYourBeatS

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   This code is from the repository https://github.com/EatYourBeetS/STS-AnimatorMod/
   The repository hosts the code for "The Animator" Slay the Spire mod.
 */