package com.cqrs.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.cqrs.application.category.city.query.findone.FindOneQuery;
import com.cqrs.application.category.city.query.findone.FindOneQueryValidator;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationContext;
import br.com.fluentvalidator.context.ValidationResult;

import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MediatorImpl implements Mediator {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private HttpServletResponse httpResponse;

    @Override
    public <T> Response<T> send(Request<T> request) {
        //
        Response<T> response = new Response<>();
        try {

            if (request != null) {
                String nameSpace = request.getClass().getName();

                Class<?> clazz = Class.forName(nameSpace+"Validator");
                if(clazz != null){
                    Constructor<?> ctor = clazz.getConstructor();
                    Validator<FindOneQuery>  vld = (Validator<FindOneQuery>) ctor.newInstance();
    
                    ValidationResult result = vld.validate((FindOneQuery) request);
    
                    if (!result.isValid()) {
                        response.errors = new HashMap<>();
                        result.getErrors().forEach(x -> {
                            List<String> emasge = new ArrayList<>();
                            emasge.add(x.getMessage());
                            response.errors.put(x.getField(), emasge);
                        });
                    }
                } 
            }

            MediatorPlanRequest<T> plan = new MediatorPlanRequest<>(RequestHandler.class, "handle", request.getClass(),
                    ctx);
            response.data = plan.invoke(request);
        } catch (Exception e) {
            response.errors = new HashMap<String, List<String>>();
        }
        //
        httpResponse.setStatus(500);
        return response;
    }

    private List<Class<?>> getAllExtendedTypesRecursively(Class<?> clazz) {
        List<Class<?>> res = new ArrayList<>();

        do {
            res.add(clazz);

            // First, add all the interfaces implemented by this class
            Class<?>[] extend = clazz.getInterfaces();
            if (extend.length > 0) {
                res.addAll(Arrays.asList(extend));

                for (Class<?> interfaze : extend) {
                    res.addAll(getAllExtendedTypesRecursively(interfaze));
                }
            }

            // Add the super class
            Class<?> superClass = clazz.getSuperclass();

            // Interfaces does not have java,lang.Object as superclass, they have null, so
            // break the cycle and return
            if (superClass == null) {
                break;
            }

            // Now inspect the superclass
            clazz = superClass;
        } while (!"java.lang.Object".equals(clazz.getCanonicalName()));

        return res;
    }

    @Override
    public Response<Void> notify(Notification notification) {
        Response<Void> response = new Response<>();
        List<NotificationHandler<Notification>> handlers = MediatorPlanNotify.getInstances(ctx,
                notification.getClass());
        List<Exception> exceptions = null;

        for (NotificationHandler<Notification> handler : handlers) {
            try {
                handler.handle(notification);
            } catch (Exception ex) {
                if (exceptions == null)
                    exceptions = new ArrayList<>();

                exceptions.add(ex);
            }
        }

        if (exceptions != null) {
            // response.exception = new AggregateException(exceptions);
        }

        return response;
    }

    class MediatorPlanRequest<T> {
        Method handleMethod;
        Object handlerInstanceBuilder;

        public MediatorPlanRequest(Class<?> handlerType, String handlerMethodName, Class<?> messageType,
                ApplicationContext context) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
            handlerInstanceBuilder = getBean(handlerType, messageType, context);
            handleMethod = handlerInstanceBuilder.getClass().getDeclaredMethod(handlerMethodName, messageType);
        }

        private Object getBean(Class<?> handlerType, Class<?> messageType, ApplicationContext context)
                throws ClassNotFoundException {
            Map<String, ?> beans = context.getBeansOfType(handlerType);
            for (Map.Entry<String, ?> entry : beans.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                Type[] interfaces = clazz.getGenericInterfaces();
                for (Type interace : interfaces) {
                    Type parameterType = ((ParameterizedType) interace).getActualTypeArguments()[0];
                    if (parameterType.equals(messageType)) {
                        return entry.getValue();
                    }
                }
            }

            throw new ClassNotFoundException("Handler not found. Did you forget to register this?");
        }

        public T invoke(Request<T> request)
                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return (T) handleMethod.invoke(handlerInstanceBuilder, request);
        }
    }

    static class MediatorPlanNotify {
        public static List<NotificationHandler<Notification>> getInstances(ApplicationContext ctx,
                Class<?> messageType) {
            List<NotificationHandler<Notification>> instances = new ArrayList<>();

            Map<String, ?> beans = ctx.getBeansOfType(NotificationHandler.class);
            for (Map.Entry<String, ?> entry : beans.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                Type[] interfaces = clazz.getGenericInterfaces();
                for (Type interace : interfaces) {
                    Type parameterType = ((ParameterizedType) interace).getActualTypeArguments()[0];
                    if (parameterType.equals(messageType)) {
                        instances.add((NotificationHandler<Notification>) entry.getValue());
                    }
                }
            }

            return instances;
        }
    }
}
