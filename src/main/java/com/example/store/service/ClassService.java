package com.example.store.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

import static com.example.store.util.ErrorMessages.CANNOT_CREATE_INSTANCE;

/**
 * ClassService uses reflection to create an instance of an object.
 * This is so that we keep D from SOLID in Service's methods. "New in service method breaks solid".
 * By using new (see below example) will generate a code hard to cover.
 * </br>
 *  public void serviceMethod() {
 *      CustomObject c = new CustomObject();
 *      service.call(c);
 *  }
 * </br>
 * In order to cover the above code we are force to use ugly "hacks" such as "any(CustomObject.class)" or
 * argument capture. This type of code should only be used when we are forced to cover legacy code for coverage's sake.
 * </br>
 * Unit tests true purposes are not for coverage. Their purpose are for making sure that the code works as expected, and that
 * someone that changes existing code, is aware that he is making a change and should be responsible to cover the new
 * code accordingly. Code coverage should be a consequence of a code well written and not target. Otherwise, we risk falling
 * into making code coverage on a broken code - and that code coverage is indeed useless.
 */
@Service
public class ClassService {

    /**
     * Creates an instance using default constructor.
     * @param clazz Class for example ProductDTO.class
     * @return instance of provided class
     */
    public <T> T create(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                 | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(CANNOT_CREATE_INSTANCE + e.getMessage(), e);
        }
    }

    /**
     * Creates an instance using parameter constructor.
     * For most cases types can be obtained from values however there is a situation where that doesn't work.
     * For example if we have two constructors CustomClass(byte, Byte) CustomClass(Byte, byte) we are unable to
     * find which of the constructor is being referred first or second. By providing the types we can call one or
     * the other without problem by just setting constructorTypes = {byte.class, Byte.class}.
     * </br>
     * @param clazz Class
     * @param constructorTypes the constructor's parameter types
     * @param constructorValues the constructor's parameter types
     * @return instance of provided class using relevant constructor
     */
    public <T> T create(Class<T> clazz, Class<?>[] constructorTypes, Object[] constructorValues) {
        try {
            return clazz.getDeclaredConstructor(constructorTypes).newInstance(constructorValues);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                 | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(CANNOT_CREATE_INSTANCE + e.getMessage(), e);
        }
    }
}
