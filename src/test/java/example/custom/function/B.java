package example.custom.function;

import lang.sel.annotations.Function;
import lang.sel.commons.results.TypedResult;
import lang.sel.interfaces.AbstractFunction;

/**
 * Created by diegorubin on 10/7/16.
 */
@Function("B")
public class B extends AbstractFunction {
    @Override
    public TypedResult execute(TypedResult... operatorArguments) {
        return null;
    }
}
