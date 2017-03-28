package com.wingoku.calculator.utils;

import android.content.Context;

import com.wingoku.calculator.R;

/**
 * Created by Umer on 3/25/2017.
 */

/**
 * Class to create Mathematical Expressions based on the user input that will be parsed & calculated by MXParser
 */
public class ExpressionFactory {

    private StringBuilder mDisplayedStringExpressionBuilder;
    private StringBuilder mEvaluationStringExpressionBuilder;

    private String mPiString;
    private String mEString;
    private String mMultiplyString;
    private String mPiMXParserCompliantString;
    private String mEMXParserCompliantString;
    private String mMultiplyMXParserCompliantString;

    /**
     * Create Expression factory
     */
    public ExpressionFactory(Context context) {
        mDisplayedStringExpressionBuilder = new StringBuilder();
        mEvaluationStringExpressionBuilder = new StringBuilder();

        mPiString = context.getString(R.string.string_pie);
        mEString = context.getString(R.string.string_e);
        mMultiplyString = context.getString(R.string.string_multiply);

        mPiMXParserCompliantString = context.getString(R.string.string_mxparser_compliant_pi);
        mEMXParserCompliantString = context.getString(R.string.string_mxparser_compliant_e);
        mMultiplyMXParserCompliantString = context.getString(R.string.string_mxparser_compliant_multiply);
    }

    /**
     * Create Mathematical expression for performing calculations
     * @param newCharacter number/character entered by the user in the calculator
     * @return StringBuilder containing mathematical expression
     */
    public StringBuilder createExpression(String newCharacter) {
        mDisplayedStringExpressionBuilder.append(newCharacter);
        createMXParserCompliantEvaluationString(newCharacter);
        return mDisplayedStringExpressionBuilder;
    }

    /**
     * Set Mathematical expression string to be displayed in the calculator display.
     * This method must be called after activity/fragment's recreation
     * @param mathematicalExpression mathematical expression string.
     */
    public void setDisplayExpressionString(String mathematicalExpression) {
        // avoiding appending data in case some DI library persists this class's data automatically Like Dagger 2
        if(mDisplayedStringExpressionBuilder.length() == 0)
            mDisplayedStringExpressionBuilder.append(mathematicalExpression);
    }

    /**
     * Set Mathematical expression string to be evaluated by MXParser.
     * This method must be called after activity/fragment's recreation
     * @param mathematicalExpression mathematical expression string compliant with MXparser standards
     */
    public void setEvaluationExpressionString(String mathematicalExpression) {
        // avoiding appending data in case some DI library persists this class's data automatically Like Dagger 2
        if(mEvaluationStringExpressionBuilder.length() == 0)
            mEvaluationStringExpressionBuilder.append(mathematicalExpression);
    }

    /**
     *  Remove last character in the mathematical expression
     * @return String without last character entered by the user
     */
    public StringBuilder removeLastCharacter() {
        removeLastCharacterFromDisplayedString();
        removeLastCharacterFromEvaluationString();
        return mDisplayedStringExpressionBuilder;
    }

    /**
     * Remove last character from Math expression string that'll be evaluated by MXparser
     */
    private void removeLastCharacterFromEvaluationString() {
        int evaluationStringLength = mEvaluationStringExpressionBuilder.length();
        int lengthAfterDeletion = 0;

        if(evaluationStringLength > 0) {
            // true for strings like ____*pi or ____*e
            if (evaluationStringLength >= 3) {
                String subString = mEvaluationStringExpressionBuilder.substring(evaluationStringLength - 3, evaluationStringLength);

                if (subString.contains(mEMXParserCompliantString)) {
                    lengthAfterDeletion = evaluationStringLength - 2;
                }
                else if (subString.contains(mPiMXParserCompliantString)) {
                    lengthAfterDeletion = evaluationStringLength - 3;
                }
                else {
                    lengthAfterDeletion = evaluationStringLength - 1;
                }
            } else {
                lengthAfterDeletion = evaluationStringLength - 1;
            }
            mEvaluationStringExpressionBuilder.setLength(lengthAfterDeletion);
        }
    }

    /**
     * Remove last character from Math expression string that's being displayed on calculator screen
     */
    private void removeLastCharacterFromDisplayedString() {
        if(mDisplayedStringExpressionBuilder.length() > 0) {
            mDisplayedStringExpressionBuilder.setLength((mDisplayedStringExpressionBuilder.length() - 1));
        }
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
    private void createMXParserCompliantEvaluationString(String newCharacter) {
        if(newCharacter.equals(mPiString))
            newCharacter = newCharacter.replace(newCharacter, mPiMXParserCompliantString);
        else if(newCharacter.equals(mEString))
            newCharacter = newCharacter.replace(newCharacter, mEMXParserCompliantString);
        else if(newCharacter.equals(mMultiplyString))
            newCharacter = newCharacter.replace(newCharacter, mMultiplyMXParserCompliantString);
        mEvaluationStringExpressionBuilder.append(newCharacter);
    }
}