package com.quickjs;

public class JSArray extends JSObject {


    public JSArray(JSContext context) {
        super(context, QuickJS._initNewJSArray(context.getContextPtr()));
    }

    JSArray(JSContext context, long tag, int u_int32, double u_float64, long u_ptr) {
        super(context, tag, u_int32, u_float64, u_ptr);
    }

    public Object get(int expectedType, int index) {
        return QuickJS._arrayGet(this.getContextPtr(), expectedType, this, index);
    }

    public JSArray pushObject(Object value) {
        QuickJS._arrayAdd(getContextPtr(), this, value);
        return this;
    }


    public int getInteger(int index) {
        Object result = get(JSValue.TYPE_INTEGER, index);
        if (result instanceof Integer) {
            return (int) result;
        }
        return 0;
    }

    public boolean getBoolean(int index) {
        Object result = get(JSValue.TYPE_BOOLEAN, index);
        if (result instanceof Boolean) {
            return (boolean) result;
        }
        return false;
    }

    public double getDouble(int index) {
        Object result = get(JSValue.TYPE_DOUBLE, index);
        if (result instanceof Double) {
            return (double) result;
        }
        return 0;
    }

    public String getString(int index) {
        Object result = get(JSValue.TYPE_STRING, index);
        if (result instanceof String) {
            return (String) result;
        }
        return null;
    }

    public JSObject getObject(int index) {
        Object result = get(JSValue.TYPE_JS_OBJECT, index);
        if (result instanceof JSObject) {
            return (JSObject) result;
        }
        return null;
    }

    public JSArray getArray(int index) {
        Object result = get(JSValue.TYPE_JS_ARRAY, index);
        if (result instanceof JSArray) {
            return (JSArray) result;
        }
        return null;
    }

    public TYPE getType(int index) {
        JSValue value = QuickJS._arrayGetValue(this.getContextPtr(), this, index);
        if (value == null) {
            return JSValue.TYPE.NULL;
        }
        return value.getType();
    }

    public JSArray push(int value) {
        return pushObject(value);
    }

    public JSArray push(double value) {
        return pushObject(value);
    }

    public JSArray push(String value) {
        return pushObject(value);
    }

    public JSArray push(boolean value) {
        return pushObject(value);
    }

    public JSArray push(JSValue value) {
        // TODO
//        value.released = true;
        return pushObject(value);
    }

    public int length() {
        return getInteger("length");
    }
}
