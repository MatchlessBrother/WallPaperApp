package company.petrifaction.boss.util;

import java.text.DecimalFormat;

public class DecimalFormatTools
{
    public static Float keepOneDecimalDigits(Float value)
    {
        return new Float(new DecimalFormat("0.0").format(value));
    }

    public static Float keepTwoDecimalDigits(Float value)
    {
        return new Float(new DecimalFormat("0.00").format(value));
    }

    public static Double keepOneDecimalDigits(Double value)
    {
        return new Double(new DecimalFormat("0.0").format(value));
    }

    public static Double keepTwoDecimalDigits(Double value)
    {
        return new Double(new DecimalFormat("0.00").format(value));
    }
}