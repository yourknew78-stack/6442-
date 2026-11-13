import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.*;
import java.util.*;

import static org.junit.Assert.*;


/**
 * NOTE: The test here provide only basic, existence-checking of the expected elements.
 * Note that you should create and declare classes, etc. following the instructions.
 */
@RunWith(Parameterized.class)
public class Q5Test {
    enum Kind { CLASS, FIELD, METHOD }

    @Parameterized.Parameters(name = "{index}: {0} {1}")
    public static Collection<Object[]> data() throws Exception {
        return Arrays.asList(new Object[][]{
            {Kind.CLASS, "User",               null, null, null, null},
            {Kind.CLASS, "FavouriteList",      null, null, null, null},
            {Kind.CLASS, "Item",               null, null, null, null},
            {Kind.CLASS, "Observer",           null, null, null, null},
            {Kind.CLASS, "NotificationService",null, null, null, null},

            {Kind.FIELD, "User",              "id",         null, null, TypeSpec.of(String.class)},
            {Kind.FIELD, "User",              "name",       null, null, TypeSpec.of(String.class)},
            {Kind.FIELD, "User",              "favourites", null, null, TypeSpec.of(Class.forName("FavouriteList"))},
            {Kind.FIELD, "FavouriteList",     "items",      null, null, TypeSpec.of(List.class, Arg.cls(Class.forName("Item")))},
            {Kind.FIELD, "FavouriteList",     "observers",  null, null, TypeSpec.of(List.class, Arg.cls(Class.forName("Observer")))},
            {Kind.FIELD, "Item",              "id",         null, null, TypeSpec.of(String.class)},
            {Kind.FIELD, "Item",              "name",       null, null, TypeSpec.of(String.class)},
            {Kind.FIELD, "NotificationService","channel",   null, null, TypeSpec.of(String.class)},

            {Kind.METHOD, "User",            null, "getFavourites", new Class[]{}, TypeSpec.of(Class.forName("FavouriteList"))},
            {Kind.METHOD, "User",            null, "setFavourites", new Class[]{Class.forName("FavouriteList")}, TypeSpec.of(void.class)},
            {Kind.METHOD, "FavouriteList",   null, "addItem", new Class[]{Class.forName("Item")},          TypeSpec.of(void.class)},
            {Kind.METHOD, "FavouriteList",   null, "removeItem", new Class[]{Class.forName("Item")},          TypeSpec.of(void.class)},
            {Kind.METHOD, "FavouriteList",   null, "attach", new Class[]{Class.forName("Observer")},      TypeSpec.of(void.class)},
            {Kind.METHOD, "FavouriteList",   null, "detach", new Class[]{Class.forName("Observer")},      TypeSpec.of(void.class)},
            {Kind.METHOD, "FavouriteList",   null, "notify", new Class[]{String.class, Class.forName("Item")}, TypeSpec.of(void.class)},
            {Kind.METHOD, "Observer",        null, "update", new Class[]{String.class, Class.forName("Item")}, TypeSpec.of(void.class)},
            {Kind.METHOD, "NotificationService", null, "update", new Class[]{String.class, Class.forName("Item")}, TypeSpec.of(void.class)},        });
    }

    private final Kind kind;
    private final String className;
    private final String fieldName;
    private final String methodName;
    private final Class<?>[] paramTypes;
    private final TypeSpec expectedType;

    public Q5Test(Kind kind, String className, String fieldName, String methodName, Class<?>[] paramTypes, TypeSpec expectedType) {
        this.kind = kind;
        this.className = className;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.paramTypes = (paramTypes == null) ? new Class<?>[0] : paramTypes;
        this.expectedType = expectedType;
    }

    @Test
    public void testDeclarationExists() throws Exception {
        Class<?> clazz = Class.forName(className);
        switch (kind) {
            case CLASS:
                assertNotNull("Class should exist: " + className, clazz);
                break;
            case FIELD: {
                Field f = clazz.getDeclaredField(fieldName);
                if (expectedType != null) assertTypeMatches(f.getGenericType(), expectedType,className + "." + fieldName);
                break;
            }
            case METHOD: {
                Method m = clazz.getDeclaredMethod(methodName, paramTypes);
                if (expectedType != null) assertTypeMatches(m.getGenericReturnType(), expectedType, className + "." + methodName + " return type");
                break;
            }
        }
    }

    static final class TypeSpec {
        final Class<?> raw;
        final Arg[] args;

        TypeSpec(Class<?> raw, Arg... args) {
            this.raw = raw;
            this.args = args;
        }

        static TypeSpec of(Class<?> raw, Arg... args) { return new TypeSpec(raw, args); }
    }

    sealed interface Arg permits Arg.ClassArg {
        static Arg cls(Class<?> c)          { return new ClassArg(c); }
        record ClassArg(Class<?> type) implements Arg {}
    }

    static void assertTypeMatches(Type actual, TypeSpec spec, String where) {
        Class<?> actualRaw;
        if (actual instanceof Class<?> c) actualRaw = c;
        else if (actual instanceof ParameterizedType p) actualRaw = (Class<?>) p.getRawType();
        else {
            fail(where + ": unsupported Type " + actual.getTypeName());
            return;
        }
        assertEquals(where + ": raw type mismatch", spec.raw, actualRaw);

        if (spec.args == null || spec.args.length == 0) return;

        assertTrue(where + ": expected parameterized type", actual instanceof ParameterizedType);
        Type[] actualArgs = ((ParameterizedType) actual).getActualTypeArguments();
        assertEquals(where + ": generic arg count mismatch", spec.args.length, actualArgs.length);

        for (int i = 0; i < spec.args.length; i++) {
            Arg expected = spec.args[i];
            Type a = actualArgs[i];
            String pos = where + " arg[" + i + "]";

            switch (expected) {
                case Arg.ClassArg(Class<?> type) -> {
                    assertTrue(pos + ": expected class type", a instanceof Class<?>);
                    assertEquals(pos + ": class mismatch", type, (Class<?>) a);
                }
                case null, default -> fail(pos + ": unknown expected arg type");
            }
        }
    }
}
