package com.yuan.devlibrary._12_______Utils;

import java.util.List;
import java.util.Locale;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/***此类用于汉子转化成拼音***/
public class PhoneticizeUtils
{
	/*******************************************获取某一个汉字的首字母******************************************/
	public static char getFirstLetterForChineseChar(char chineseChar)
	{
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		if (chineseChar > 128)/**ASCLL码值大于128就一定是汉字*/
		{
			try
			{
				return PinyinHelper.toHanyuPinyinStringArray(chineseChar, format)[0].toLowerCase(Locale.getDefault()).charAt(0);
			}
			catch (BadHanyuPinyinOutputFormatCombination e)
			{
				StringBuilder errorstr = new StringBuilder();
				StackTraceElement[] stackTraceElement = e.getStackTrace();
				for (int i = 0; i < stackTraceElement.length; i++)
				{
					errorstr.append(stackTraceElement[i].getFileName());
					errorstr.append(stackTraceElement[i].getClassName());
					errorstr.append(stackTraceElement[i].getMethodName());
					errorstr.append(stackTraceElement[i].getLineNumber());
				}
				Log.i("CCTPY_Exception", errorstr.toString());
			}
		}
		return Character.toLowerCase(chineseChar);
	}

	/**********************************获取汉字字符串中每个汉字首字母组成的字符串*******************************/
	public static String getFirstLetterStrForChineseStr(String chineseStr)
	{
		char[] strArray = chineseStr.toCharArray();
		StringBuilder strBuilder = new StringBuilder();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int index = 0; index < strArray.length; index++)
		{
			if (strArray[index] > 128)/**ASCLL码值大于128就一定是汉字*/
			{
				try
				{
					String[] hz_to_py = PinyinHelper.toHanyuPinyinStringArray(strArray[index], format);
					if (hz_to_py != null)
						strBuilder.append(hz_to_py[0].charAt(0));
				}
				catch (BadHanyuPinyinOutputFormatCombination e)
				{
					StringBuilder errorStr = new StringBuilder();
					StackTraceElement[] stackTraceElement = e.getStackTrace();
					for (int i = 0; i < stackTraceElement.length; i++)
					{
						errorStr.append(stackTraceElement[i].getFileName());
						errorStr.append(stackTraceElement[i].getClassName());
						errorStr.append(stackTraceElement[i].getMethodName());
						errorStr.append(stackTraceElement[i].getLineNumber());
					}
					Log.i("CCTPY_Exception",errorStr.toString());
				}
			}
			else
				strBuilder.append(strArray[index]);
		}
		String result_str = strBuilder.toString().trim();
		return result_str.toLowerCase(Locale.getDefault());
	}

	/********************************获取汉字字符串中每个汉字的中文拼音组成的字符串*****************************/
	public static String getAllLetterStrForChineseStr(String chineseStr)
	{
		char[] strArray = chineseStr.toCharArray();
		StringBuilder strbuilder = new StringBuilder();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int index = 0; index < strArray.length; index++)
		{
			if (strArray[index] > 128)/**ASCLL码值大于128就一定是汉字*/
			{
				try
				{
					String[] hz_to_py = PinyinHelper.toHanyuPinyinStringArray(strArray[index], format);
					if (hz_to_py != null)
					{
						if(index == strArray.length - 1)
							strbuilder.append(hz_to_py[0]);
						else
							strbuilder.append(hz_to_py[0]+"-");
					}
				}
				catch (BadHanyuPinyinOutputFormatCombination e)
				{
					StringBuilder errorstr = new StringBuilder();
					StackTraceElement[] stackTraceElement = e.getStackTrace();
					for (int i = 0; i < stackTraceElement.length; i++)
					{
						errorstr.append(stackTraceElement[i].getFileName());
						errorstr.append(stackTraceElement[i].getClassName());
						errorstr.append(stackTraceElement[i].getMethodName());
						errorstr.append(stackTraceElement[i].getLineNumber());
					}
					Log.i("CCTPY_Exception", errorstr.toString());
				}
			}
			else
				strbuilder.append(strArray[index]);
		}
		String result_str = strbuilder.toString().trim();
		return result_str.toLowerCase(Locale.getDefault());
	}

	/******************************让集合中的汉子字符串根据字典顺序呈现A-Z的次序排列****************************/
	public static List<String> sortChineseStrListByNaturalOrder(List<String> chineseStrList)
	{
		List<String> orgList=new ArrayList<String>();
		List<String> sortOrgList=new ArrayList<String>();
		for(String str : chineseStrList)
		{
			orgList.add(getAllLetterStrForChineseStr(str));
			sortOrgList.add(getAllLetterStrForChineseStr(str));
		}
		Collections.sort(sortOrgList);
		List<Integer> sortOrgIndexList=new ArrayList<Integer>();
		for(String str : sortOrgList)
		{
			for(int index=0;index<orgList.size(); index++)
			{
				if(str.equals(orgList.get(index)))
				{
					sortOrgIndexList.add(index);
				}
			}
		}
		orgList.clear();
		sortOrgList.clear();
		List<String> resultSortList = new ArrayList<>();
		for(int index:sortOrgIndexList)
			resultSortList.add(chineseStrList.get(index));
		return resultSortList;
	}

	/****************************让集合中的汉子字符串根据字典反向顺序呈现Z-A的次序排列**************************/
	public static List<String> sortChineseStrListByReverseNaturalOrder(List<String> chineseStrList)
	{
		List<String> orgList=new ArrayList<String>();
		List<String> sortOrgList=new ArrayList<String>();
		for(String str : chineseStrList)
		{
			orgList.add(getAllLetterStrForChineseStr(str));
			sortOrgList.add(getAllLetterStrForChineseStr(str));
		}
		Collections.sort(sortOrgList);
		List<Integer> sortOrgIndexList=new ArrayList<Integer>();
		for(String str : sortOrgList)
		{
			for(int index=0;index<orgList.size(); index++)
			{
				if(str.equals(orgList.get(index)))
				{
					sortOrgIndexList.add(index);
				}
			}
		}
		orgList.clear();
		sortOrgList.clear();
		List<String> resultSortList = new ArrayList<>();
		for(int index = sortOrgIndexList.size() - 1;index >= 0;index--)
			resultSortList.add(chineseStrList.get(sortOrgIndexList.get(index)));
		return resultSortList;
	}

	/******************************查询已排序的中文集合中第一次出现指定字符的下标值*****************************/
	public static int findInitIndexForCharInSortedChineseStrList(List<String> sortedChineseStrList,char charactor)
	{
		List<String> orgList=new ArrayList<String>();
		for(String str : sortedChineseStrList)
			orgList.add(getAllLetterStrForChineseStr(str));
		for(int index=0;index<orgList.size();index++)
			if(orgList.get(index).startsWith(String.valueOf(charactor)))
				return index;
		return -1;
	}
}