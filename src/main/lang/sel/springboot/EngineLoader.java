package lang.sel.springboot;

import lang.sel.annotations.Constant;
import lang.sel.annotations.Function;
import lang.sel.annotations.Operator;
import lang.sel.core.EngineContext;
import lang.sel.core.wrappers.FunctionOptions;
import lang.sel.interfaces.BinaryOperator;
import lang.sel.interfaces.UnaryOperator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Engine loader
 * <p>
 * All logic for search operators, functions and constants and add them in the engine context is implemented in this
 * class.
 * <p>
 * This is a spring component.
 *
 * @author diegorubin
 */
@Component
public class EngineLoader {

  private static final String VALUE = "value";

  /**
   * This method search for specific class of engine.
   * <p>
   * the standard features will also be charged
   *
   * @param engineContext the engine context instance
   * @param packageRoot   the package root where the functions, constants and operators are declared
   */
  public void load(final EngineContext engineContext, final String packageRoot) {
    findAndLoad(engineContext, "lang.sel");
    findAndLoad(engineContext, packageRoot);
  }

  private void findAndLoad(final EngineContext engineContext, final String packageRoot) {

    final ClassLoader classLoader = EngineLoader.class.getClassLoader();

    final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Function.class));
    scanner.addIncludeFilter(new AnnotationTypeFilter(Operator.class));
    scanner.addIncludeFilter(new AnnotationTypeFilter(Constant.class));

    for (final BeanDefinition bd : scanner.findCandidateComponents(packageRoot)) {
      final ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) bd;
      try {
        final Class loadedClass = classLoader.loadClass(beanDefinition.getBeanClassName());

        Map<String, Object> attributes = beanDefinition.getMetadata().getAnnotationAttributes(Operator.class.getName());
        if (attributes != null) {
          addOperator(engineContext, attributes, loadedClass);
        }

        attributes = beanDefinition.getMetadata().getAnnotationAttributes(Function.class.getName());
        if (attributes != null) {
          FunctionOptions options = new FunctionOptions();
          options.setNumberOfArguments("".equals(attributes.get("numberOfArguments")) ?
              null : Integer.valueOf((String) attributes.get("numberOfArguments")));
          engineContext.addFunction((String) attributes.get(VALUE), loadedClass, options);
        }

        attributes = beanDefinition.getMetadata().getAnnotationAttributes(Constant.class.getName());
        if (attributes != null) {
          engineContext.addConstant((String) attributes.get(VALUE), loadedClass);
        }

      } catch (final Exception e) {
      }

    }

  }

  private void addOperator(final EngineContext engineContext, final Map<String, Object> attributes, final Class operator) {
    if (BinaryOperator.class.isAssignableFrom(operator)) {
      engineContext.addBinaryOperator((String) attributes.get(VALUE), operator);
    }
    if (UnaryOperator.class.isAssignableFrom(operator)) {
      engineContext.addUnaryOperator((String) attributes.get(VALUE), operator);
    }
  }
}

