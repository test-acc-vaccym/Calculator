package com.wingoku.calculator.fragments;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wingoku.calculator.R;
import com.wingoku.calculator.application.WingokuApplication;
import com.wingoku.calculator.utils.Constants;
import com.wingoku.calculator.utils.ExpressionFactory;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Umer on 3/25/2017.
 */

public class CalculatorFragment extends Fragment{

    @BindView(R.id.tv_expression)
    TextView mExpressionTV;

    @BindView(R.id.tv_result)
    TextView mResultTV;

    @BindView(R.id.view_success)
    View mSuccessView;

    @BindView(R.id.view_error)
    View mErrorView;

    @Inject
    ExpressionFactory mExpressionFactory;

    private DecimalFormat mDecimalFormator;
    private Expression mExpressionEvaluator;
    private String mExpressionString = "";
    private String mResultString = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, view);

        // removes .0 from ABC.0
        mDecimalFormator = new DecimalFormat("0.#");
        mExpressionEvaluator = new Expression();
        // Dagger 2 dependency injection
        ((WingokuApplication)getActivity().getApplication()).getExpressionFactoryComponent().inject(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saving data to repopulate calculator display or view upon recreation of activity caused by orientation change
        outState.putString(Constants.KEY_PRECALCULATED_RESULT_STRING, mResultString);
        outState.putString(Constants.KEY_EXPRESSION_STRING, mExpressionString);
        outState.putString(Constants.KEY_EVALUATION_EXPRESSION, mExpressionFactory.getEvaluationExpression());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            mResultString = savedInstanceState.getString(Constants.KEY_PRECALCULATED_RESULT_STRING, getString(R.string.string_empty));
            mExpressionString = savedInstanceState.getString(Constants.KEY_EXPRESSION_STRING, getString(R.string.string_empty));
            mExpressionFactory.setDisplayExpressionString(mExpressionString);
            mExpressionFactory.setEvaluationExpressionString(savedInstanceState.getString(Constants.KEY_EVALUATION_EXPRESSION, getString(R.string.string_empty)));
            mExpressionTV.setText(mExpressionString);
            mResultTV.setText(mResultString);
        }
    }

    @OnClick({R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8,
            R.id.button_9, R.id.button_plus, R.id.button_minus, R.id.button_divide, R.id.button_multiply, R.id.button_e, R.id.button_log, R.id.button_delete,
            R.id.button_pie, R.id.button_decimal, R.id.button_equal})
    public void onNumpadClick(View v) {
        int id = v.getId();
        boolean finalizeResult = false;

        String buttonText = ((Button)v).getText().toString();
        if(!buttonText.equals(getString(R.string.string_del)) && !buttonText.equals(getString(R.string.string_equal)))
            mExpressionString = mExpressionFactory.createExpression(buttonText).toString();

        switch(id) {
            case R.id.button_delete:
                mExpressionString = mExpressionFactory.removeLastCharacter().toString();
                resetResultString();
                break;

            case R.id.button_equal:
                finalizeResult = true;
                break;
        }

        evaluateExpression(finalizeResult);

        mExpressionTV.setText(mExpressionString);
        mResultTV.setText(mResultString);
    }

    @OnLongClick(R.id.button_delete)
    public boolean onDeleteLongPress(View v) {
        resetAll();
        mExpressionTV.setText(mExpressionString);
        mResultTV.setText(mResultString);
        circleRevealAnimation(mSuccessView);

        return true;
    }

    /**
     * Set the visibility of Mathematical Result & Expression textViews in the Calculator display
     * @param setVisible true if it's required to show the Result & Expression TextViews in Calculator display. False otherwise.
     */
    private void showTextViews(boolean setVisible) {
        if(setVisible) {
            mExpressionTV.setVisibility(View.VISIBLE);
            mResultTV.setVisibility(View.VISIBLE);
        }
        else {
            mExpressionTV.setVisibility(View.INVISIBLE);
            mResultTV.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Calculate the Mathematical operations user have input in the app.
     * @param finalize Show the final result. Set it TRUE if the user has pressed = button
     */
    private void evaluateExpression(boolean finalize) {
        String evaluationMathExpression = mExpressionFactory.getEvaluationExpression();
        if(evaluationMathExpression.length() == 0)
            return;

        mExpressionEvaluator.setExpressionString(evaluationMathExpression);
        double evaluatedResult = MathFunctions.round(mExpressionEvaluator.calculate(), Constants.DECIMAL_PLACES);

        if(!Double.isNaN(evaluatedResult)) {
            if(finalize) {
                circleRevealAnimation(mSuccessView);
                resetAll();
                mExpressionString = mExpressionFactory.createExpression(mDecimalFormator.format(evaluatedResult)+"").toString();
            }
            else {
                mResultString = mDecimalFormator.format(evaluatedResult)+"";
            }
        }
        else if(finalize) {
            // show red circle animation and show error message
            circleRevealAnimation(mErrorView);
            mResultString = getString(R.string.string_bad_expression);
        }
    }

    @TargetApi(21)
    private void circleRevealAnimation(final View view) {
        if(Build.VERSION.SDK_INT < 21) {
            return;
        }

        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, view.getRight(), view.getBottom(), startRadius, endRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                showTextViews(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.INVISIBLE);
                showTextViews(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        anim.start();
    }

    /**
     * Reset the Calculator display and Expression Evaluator code to start fresh.
     */
    private void resetAll() {
        mExpressionFactory.clear();
        resetExpressionString();
        resetResultString();
    }

    /**
     * Reset Expression String
     */
    private void resetExpressionString() {
        mExpressionString = "";
    }

    /**
     * Reset Result String
     */
    private void resetResultString() {
        mResultString = "";
    }
}
