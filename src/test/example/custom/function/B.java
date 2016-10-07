package example.custom.function;

import lang.sel.annotations.Function;
import lang.sel.interfaces.AbstractFunction;
import lang.sel.interfaces.OperationResult;
import lang.sel.interfaces.OperatorArgument;

/**
 * Created by diegorubin on 10/7/16.
 */
@Function("B")
public class B extends AbstractFunction {
  @Override
  public OperationResult execute(OperatorArgument... operatorArguments) {
    return null;
  }
}
