package cs203t10.ryver.cms.util;

import java.beans.FeatureDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class CustomBeanUtils extends BeanUtils {

    public static void copyNonNullProperties(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName ->
                        wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    public static boolean nonNullIsSubsetOf(Object source, Object target) throws BeansException {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        final BeanWrapper wrappedTarget = new BeanWrapperImpl(target);
        Set<String> sourceNonNullProperties = Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName ->
                        wrappedSource.getPropertyValue(propertyName) != null)
                .collect(Collectors.toSet());
        Set<String> targetProperties = Stream.of(wrappedTarget.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .collect(Collectors.toSet());
        return targetProperties.containsAll(sourceNonNullProperties);
    }

}

