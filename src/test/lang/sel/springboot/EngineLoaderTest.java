package lang.sel.springboot;

import lang.sel.core.EngineContext;
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
public class EngineLoaderTest {

  private EngineContext engineContext;
  private EngineLoader engineLoader;

  @Before
  public void setup() {
    engineLoader = new EngineLoader();
    engineContext = new EngineContext();
  }

  @Test
  public void shouldLoadTestFunctions() {
    engineLoader.load(engineContext, "example.custom.function");
    Assert.assertTrue(engineContext.isFunction("B"));
  }

  @Test
  public void shouldLoadBuiltinOperators() {
    engineLoader.load(engineContext, "example.custom.operator");
    Assert.assertTrue(engineContext.isBinaryOperator("AND"));
    Assert.assertTrue(engineContext.isUnaryOperator("NOT"));
  }

  @Test
  public void shouldThrowExceptionIfInvalidClassIsAnnotated() {
    engineContext = mock(EngineContext.class);
    Mockito.doThrow(new RuntimeException()).when(engineContext).addFunction(anyString(), any(), any());
    engineLoader.load(engineContext, "lang.sel.parsers.campaign");
  }

}

