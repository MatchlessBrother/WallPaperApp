package company.petrifaction.boss.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.yuan.devlibrary._12_______Utils.StringUtils;

public class EncryptionTools
{
    public static String md5(String content)
    {
        if(StringUtils.isEmpty(content)) return "";
        try
        {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            StringBuffer stringBuffer = new StringBuffer();
            MD5.update(content.getBytes());
            byte[] bytes = MD5.digest();
            for(byte bt : bytes)
            {
                String tempStr = Integer.toHexString(bt & 0xff);
                if(tempStr.length()==1)tempStr = "0" + tempStr;
                stringBuffer.append(tempStr);
            }
            return stringBuffer.toString().trim();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}