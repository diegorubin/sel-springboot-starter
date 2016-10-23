package lang.sel.springboot;

import lang.sel.annotations.Constant;
import lang.sel.annotations.Function;
import lang.sel.annotations.Operator;
import lang.sel.core.SelContext;
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
 * Sel loader
 * <p>
 * All logic for search operators, functions and constants and add them in the engine context is implemented in this
 * class.
 * <p>
 * This is a spring component.
 *
 * @author diegorubin
 */
@Component
public class SelLoader {

  private static final String VALUE = "value";

  /**
   * This method search for specific class of engine.
   * <p>
   * the standard features will also be charged
   *
   * @param selContext  the sel context instance
   * @param packageRoot the package root where the functions, constants and operators are declared
   */
  public void load(final SelContext selContext, final String packageRoot) {
    findAndLoad(selContext, "lang.sel");
    findAndLoad(selContext, packageRoot);
  }

  private void findAndLoad(final SelContext selContext, final String packageRoot) {

    final ClassLoader classLoader = SelLoader.class.getClassLoader();

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
          addOperator(selContext, attributes, loadedClass);
        }

        attributes = beanDefinition.getMetadata().getAnnotationAttributes(Function.class.getName());
        if (attributes != null) {
          FunctionOptions options = new FunctionOptions();
          options.setNumberOfArguments("".equals(attributes.get("numberOfArguments")) ?
              null : Integer.valueOf((String) attributes.get("numberOfArguments")));
          selContext.addFunction((String) attributes.get(VALUE), loadedClass, options);
        }

        attributes = beanDefinition.getMetadata().getAnnotationAttributes(Constant.class.getName());
        if (attributes != null) {
          selContext.addConstant((String) attributes.get(VALUE), loadedClass);
        }

      } catch (final Exception e) {
      }

    }

  }

  private void addOperator(final SelContext selContext, final Map<String, Object> attributes, final Class operator) {
    if (BinaryOperator.class.isAssignableFrom(operator)) {
      selContext.addBinaryOperator((String) attributes.get(VALUE), operator);
    }
    if (UnaryOperator.class.isAssignableFrom(operator)) {
      selContext.addUnaryOperator((String) attributes.get(VALUE), operator);
    }
  }
}

