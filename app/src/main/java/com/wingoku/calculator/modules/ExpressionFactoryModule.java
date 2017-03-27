package com.wingoku.calculator.modules;

import com.wingoku.calculator.utils.ExpressionFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Umer on 3/26/2017.
 */

@Module
public class ExpressionFactoryModule {
    private ExpressionFactory mExpressionFactory;

    public ExpressionFactoryModule(ExpressionFactory expressionFactory) {
        this.mExpressionFactory = expressionFactory;
    }

    @Provides
    ExpressionFactory provideStringExpressionFactory() {
        return mExpressionFactory;
    }
}
