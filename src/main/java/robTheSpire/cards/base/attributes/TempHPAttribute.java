package robTheSpire.cards.base.attributes;

import robTheSpire.cards.base.EYBCard;

public class TempHPAttribute extends AbstractAttribute
{
    public static TempHPAttribute Instance = new TempHPAttribute();

    public TempHPAttribute()
    {
        this.icon = ICONS.TempHP.Texture();
    }

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        mainText = null;
        iconTag = null;
        suffix = null;

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, boolean useMagicNumber)
    {
        if (useMagicNumber)
        {
            mainText = card.GetMagicNumberString();
        }
        else
        {
            mainText = null;
        }

        iconTag = null;
        suffix = null;

        return this;
    }
}
