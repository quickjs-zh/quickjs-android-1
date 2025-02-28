package com.quickjs;

import com.quickjs.JSArray;
import com.quickjs.JSContext;
import com.quickjs.JSObject;
import com.quickjs.JSValue;
import com.quickjs.QuickJS;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSArrayTest {
    JSArray array;
    private JSContext context;
    private QuickJS quickJS;

    @Before
    public void setUp() throws Exception {
        quickJS = QuickJS.createRuntime();
        context = quickJS.createContext();
        array = new JSArray(context);
    }

    @After
    public void tearDown() throws Exception {
        context.close();
        quickJS.close();
    }

    @Test
    public void getInteger() {
        array.push(1);
        array.push(Integer.MAX_VALUE);
        array.push(Integer.MIN_VALUE);
        assertEquals(1, array.getInteger(0));
        assertEquals(Integer.MAX_VALUE, array.getInteger(1));
        assertEquals(Integer.MIN_VALUE, array.getInteger(2));
    }

    @Test
    public void getBoolean() {
        array.push(true);
        array.push(false);
        assertTrue(array.getBoolean(0));
        assertFalse(array.getBoolean(1));
    }

    @Test
    public void getDouble() {
        array.push(1.0);
        array.push(Double.MAX_VALUE);
        array.push(Double.MIN_VALUE);
        assertEquals(1.0, array.getDouble(0), 1);
        assertEquals(Double.MAX_VALUE, array.getDouble(1), 1);
        assertEquals(Double.MIN_VALUE, array.getDouble(2), 1);
    }

    @Test
    public void getString() {
        array.push("Hello");
        array.push("World");
        array.push("Wiki");
        assertEquals("Hello", array.getString(0));
        assertEquals("World", array.getString(1));
        assertEquals("Wiki", array.getString(2));
    }


    @Test
    public void length() {
        array.push(1);
        array.push(true);
        array.push(1.0);
        array.push("Hello");
        assertEquals(4, array.length());
    }

    @Test
    public void getObject() {
        JSObject user = new JSObject(context);
        user.set("name", "Wiki");
        array.push(user);
        Object result = array.getObject(0);
//        String result = array.getObject(0).getString("name");
//        assertEquals("Wiki", result);
    }

    @Test
    public void getArray() {
        JSArray user = new JSArray(context);
        user.push("Wiki");
        array.push(user);
        assertEquals("Wiki", array.getArray(0).getString(0));
    }

    @Test
    public void getType() {
        JSArray array = new JSArray(context);
        array.push("1");
        array.push(1);
        array.push(true);
        array.push(1.1);
        assertEquals(JSValue.TYPE.STRING, array.getType(0));
        assertEquals(JSValue.TYPE.INTEGER, array.getType(1));
        assertEquals(JSValue.TYPE.BOOLEAN, array.getType(2));
        assertEquals(JSValue.TYPE.DOUBLE, array.getType(3));
//        array.close();
    }

    @Test
    public void test() {
//        JSArray array = new JSArray(context);
//        JSObject value = new JSObject(context);
//        value.set("name", "Wiki");
//        array.push(value);

        JSArray array = new JSArray(context);
        JSObject value = new JSObject(context);
        value.set("name", "Wiki");
        array.push(value);
        // TODO
    }

    @Test
    public void test2() {
        JSArray array = new JSArray(context);
        JSObject value = new JSObject(context);
        value.set("name", "Wiki");
        array.push(value);
        value.set("age", 18);
        assertEquals(18, array.getObject(0).getInteger("age"));
    }

    @Test
    public void test3() {
        JSArray array = new JSArray(context);
        JSObject value = new JSObject(context);
        value.set("name", "Wiki");
        array.push(value);
        value.set("age", 18);
        assertEquals(18, array.getObject(0).getInteger("age"));
    }


}