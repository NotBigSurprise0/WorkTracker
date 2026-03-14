package app;

public class Utility 
{
    public static String formatPay(double pay)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        int wholePart = (int)pay;
        sb.append(wholePart);
        sb.append(".");
        int decimalPart = (int)Math.round(pay * 100) % 100;
        sb.append(decimalPart);
        return sb.toString();
    }
}
