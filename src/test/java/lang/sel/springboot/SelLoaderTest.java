package lang.sel.springboot;

import lang.sel.core.SelContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 * @author diegorubin
 */
@RunWith(MockitoJUnitRunner.class)
public class SelLoaderTest {

  private SelContext selContext;
  private SelLoader selLoader;

  @Before
  public void setup() {
    selLoader = new SelLoader();
    selContext = new SelContext();
  }

  @Test
  public void shouldLoadTestFunctions() {
    selLoader.load(selContext, "example.custom.function");
    Assert.assertTrue(selContext.isFunction("B"));
  }

  @Test
  public void shouldLoadBuiltinOperators() {
    selLoader.load(selContext, "example.custom.operator");
    Assert.assertTrue(selContext.isBinaryOperator("AND"));
    Assert.assertTrue(selContext.isUnaryOperator("NOT"));
  }

  @Test
  public void shouldThrowExceptionIfInvalidClassIsAnnotated() {
    selContext = mock(SelContext.class);
    Mockito.doThrow(new RuntimeException()).when(selContext).addFunction(anyString(), any(), any());
    selLoader.load(selContext, "lang.sel.parsers.campaign");
  }

}

