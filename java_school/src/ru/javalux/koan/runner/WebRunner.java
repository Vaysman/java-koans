package ru.javalux.koan.runner;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.result.KoanMethodResult;
import com.sandwich.koan.result.KoanSuiteResult;
import com.sandwich.koan.runner.KoanMethodRunner;
import ru.javalux.koan.Koan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class WebRunner {
    public KoanSuiteResult runKoan(Koan...koans) {
        List<String> passingSuites = new ArrayList<String>();
        List<String> failingSuites = new ArrayList<String>();
        int successfull = 0;
        int total = 0;
        KoanMethodResult failure = null;

        for (Koan koan: koans) {
            Class clazz = koan.getClazz();
            Object suite = constructSuite(clazz);
            String suiteName = suite.getClass().getName();
            String name = suiteName.substring(suiteName.lastIndexOf('.') + 1);
            List<KoanMethod> koanMethods = getKoanMethods(suite);
            Collections.sort(koanMethods, new KoanMethodBySourceComparator(koan.getSource()));
            for (KoanMethod koanMethod : koanMethods) {
                total++;
                KoanMethodResult result = KoanMethodRunner.runDoNotCheckSource(suite, koanMethod);
                if (KoanMethodResult.PASSED != result) {
                    // Test failed!
                    failure = result;
                    failingSuites.add(name);
                    break;
                } else {
                    successfull++;
                }
                if (failure == null) {
                    passingSuites.add(name);
                }
            }
        }
        return new KoanSuiteResult.KoanResultBuilder()
                .numberPassing(successfull)
                .totalNumberOfKoanMethods(total)
                .passingCases(passingSuites)
                .methodResult(failure)
                .remainingCases(failingSuites).build();
    }

    private class KoanMethodBySourceComparator implements Comparator<KoanMethod> {
        private String src;

        public KoanMethodBySourceComparator(String source) {
            src = source;
        }

        @Override
        public int compare(KoanMethod arg0, KoanMethod arg1) {
            Class<?> declaringClass0 = arg0.getMethod().getDeclaringClass();
            Class<?> declaringClass1 = arg1.getMethod().getDeclaringClass();
            if(declaringClass0 != declaringClass1){
                Logger.getAnonymousLogger().severe("no idea how to handle comparing the classes: " + declaringClass0 + " and: "+declaringClass1);
                return 0;
            }
            int index0 = src.indexOf(arg0.getMethod().getName());
            int index1 = src.indexOf(arg1.getMethod().getName());
            return (index0 > index1 ? 1 : 0) - (index0 < index1 ? 1 : 0);
        }
    }

    private List<KoanMethod> getKoanMethods(Object suite) {
        final List<KoanMethod> methods = new ArrayList<KoanMethod>();
        for (Method method : suite.getClass().getMethods()) {
            if (method.getAnnotation(com.sandwich.koan.Koan.class) != null) {
                methods.add(KoanMethod.getInstance(method));
            }
        }
        return methods;
    }

    private Object constructSuite(Class<?> koan) {
        Object suite;
        try {
            suite = koan.newInstance();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        return suite;
    }

}
