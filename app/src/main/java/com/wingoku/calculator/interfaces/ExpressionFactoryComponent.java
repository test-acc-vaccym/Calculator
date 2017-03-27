package com.wingoku.calculator.interfaces;

import com.wingoku.calculator.fragments.CalculatorFragment;
import com.wingoku.calculator.modules.ExpressionFactoryModule;

import dagger.Component;

/**
 * Created by Umer on 3/26/2017.
 */

@Component (modules = {ExpressionFactoryModule.class})
public interface ExpressionFactoryComponent {
    /**
     * Inject dependency in the provided class's context
     * @param fragment fragment in which dependency is to be injected
     */
    void inject(CalculatorFragment fragment);
}
