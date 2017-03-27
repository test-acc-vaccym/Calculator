package com.wingoku.calculator.utils;

import android.content.Context;

import com.wingoku.calculator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Umer on 3/25/2017.
 */

/**
 * Class to create Mathematical Expressions based on the user input that will be parsed & calculated by MXParser
 */
public class ExpressionFactory {

    private StringBuilder mDisplayedStringExpressionBuilder;
    private StringBuilder mEvaluationStringExpressionBuilder;

    private Context mContext;

    /**
     * Create Expression factory
     * @param con context from the Activity/fragment
     */
    public ExpressionFactory(Context con) {
        mDisplayedStringExpressionBuilder = new StringBuilder();
        mEvaluationStringExpressionBuilder = new StringBuilder();
        mContext = con;
    }

    /**
     * Create Mathematical expression for performing calculations
     * @param newToken number/character entered by the user in the calculator
     * @return StringBuilder containing mathematical expression
     */
    public StringBuilder createExpression(String newToken) {
        mDisplayedStringExpressionBuilder.append(newToken);
        mEvaluationStringExpressionBuilder.append(newToken);
        makeEvaluationStringExpressionMXParserCompliant();
        return mDisplayedStringExpressionBuilder;
    }

    /**
     *  Remove last character in the mathematical expression
     * @return String without last character entered by the user
     */
    public StringBuilder removeLastCharacter() {
        if(mDisplayedStringExpressionBuilder.length() == 0 && mEvaluationStringExpressionBuilder.length() == 0)
            return mDisplayedStringExpressionBuilder;

        mDisplayedStringExpressionBuilder.setLength((mDisplayedStringExpressionBuilder.length()-1));
        mEvaluationStringExpressionBuilder.setLength((mEvaluationStringExpressionBuilder.length()-1));
        return mDisplayedStringExpressionBuilder;
    }

    /**
     * Returns mathematical expression that's compliant with the parsing style of MXParser. For example: replacing x with *, π with *pi & e with *e
     * for successful parsing and calculation of the mathematical expression by MXParser
     * @return MXParser complaint mathematical string ready for peforming calculations
     */
    public String getEvaluationExpression() {
        return mEvaluationStringExpressionBuilder.toString();
    }

    /**
     * Clear the mathematical expressions saved in StringBuilders.
     */
    public void clear() {
        mDisplayedStringExpressionBuilder.setLength(0);
        mEvaluationStringExpressionBuilder.setLength(0);
    }

    /** Making Mathematical expression compliant with the parsing style of MXParser. For example: replacing x with *, π with *pi & e with *e
     * for successful parsing and calculation of the mathematical expression by MXParser
     */
    private void makeEvaluationStringExpressionMXParserCompliant() {
        Pattern multiply = Pattern.compile(mContext.getString(R.string.string_multiply));
        Pattern pi = Pattern.compile(mContext.getString(R.string.string_pie));
        Pattern e = Pattern.compile(mContext.getString(R.string.string_e));

        replaceAll(mEvaluationStringExpressionBuilder, multiply, "*");
        replaceAll(mEvaluationStringExpressionBuilder, pi, "*pi");
        replaceAll(mEvaluationStringExpressionBuilder, e, "*e");
    }

    /**
     * Replace all the occurances of a string in StringBuilder.
     * @param sb StringBuilder to perform replacement operation on
     * @param pattern Pattern/String to replace in sb
     * @param replacement string to replace with in StringBuilder
     */
    private void replaceAll(StringBuilder sb, Pattern pattern, String replacement) {
        Matcher m = pattern.matcher(sb);
        int start = 0;

        while (start < sb.length() && m.find(start)) {
            sb.replace(m.start(), m.end(), replacement);
            start = m.start() + replacement.length();
        }
    }
}