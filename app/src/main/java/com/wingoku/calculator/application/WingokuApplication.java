package com.wingoku.calculator.application;

import android.app.Application;

import com.wingoku.calculator.interfaces.DaggerExpressionFactoryComponent;
import com.wingoku.calculator.interfaces.ExpressionFactoryComponent;
import com.wingoku.calculator.modules.ExpressionFactoryModule;
import com.wingoku.calculator.utils.ExpressionFactory;

/**
 * Created by Umer on 3/26/2017.
 */

public class WingokuApplication extends Application {

    private ExpressionFactoryComponent mExpressionFactoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Building dagger DI component
        mExpressionFactoryComponent = DaggerExpressionFactoryComponent.builder().
                expressionFactoryModule(new ExpressionFactoryModule(new ExpressionFactory(this))).build();
    }

    /**
     * Dependency provider component method for ExpressionFactory.java
     * @return component that provides ExpressionFactory.java object using dependency injection
     */
    public ExpressionFactoryComponent getExpressionFactoryComponent() {
        return mExpressionFactoryComponent;
    }
}
