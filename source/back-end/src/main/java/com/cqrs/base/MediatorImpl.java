package com.cqrs.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
                try {
                    Class<?> clazz = Class.forName(nameSpace + "Validator");
                    Constructor<?> ctor = clazz.getConstructor();
                    Validator<T> vld = (Validator<T>) ctor.newInstance();

                    ValidationResult result = vld.validate((T) request);

                    if (!result.isValid()) {
                        response.errors = new HashMap<>();
                        result.getErrors().forEach(x -> {
                            List<String> emasge = new ArrayList<>();
                            emasge.add(x.getMessage());
                            response.errors.put(x.getField(), emasge);
                        });
                    }
                } catch (Exception e) {
                    if(!(e instanceof ClassNotFoundException)){
                        response.errors = new HashMap<>();
                        List<String> emasge = new ArrayList<>();
                        emasge.add("ValidationResult:" + e.getMessage());
                        response.errors.put("system", emasge);
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
