package com.quickjs;

import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JSContext extends JSObject implements Closeable {
    final QuickJS quickJS;
    private final long contextPtr;
    Map<Integer, QuickJS.MethodDescriptor> functionRegistry = new HashMap<>();
    final LinkedList<WeakReference<JSValue>> refs = new LinkedList<>();

    JSContext(QuickJS quickJS, long contextPtr) {
        super(null, QuickJS._getGlobalObject(contextPtr));
        this.quickJS = quickJS;
        this.contextPtr = contextPtr;
        this.context = this;
    }

    long getContextPtr() {
        return this.contextPtr;
    }

    void addObjRef(JSValue reference) {
        if (reference.getClass() != JSValue.class) {
            refs.add(new WeakReference<>(reference));
        }
    }

    void releaseObjRef(JSValue reference) {
        for (WeakReference<JSValue> it : refs) {
            JSValue tmp = it.get();
            if (tmp == reference) {
                refs.remove(it);
                return;
            }
        }
    }

    @Override
    public void close() {
        if (released) {
            return;
        }
        functionRegistry.clear();
        WeakReference[] arr = new WeakReference[refs.size()];
        refs.toArray(arr);
        for (WeakReference it : arr) {
            JSValue tmp = (JSValue) it.get();
            if (tmp != null) {
                tmp.close();
            }
        }
        super.close();
        QuickJS._releaseContext(contextPtr);
        QuickJS.sContextMap.remove(getContextPtr());
    }

    private Object executeScript(TYPE expectedType, String source, String fileName) throws QuickJSScriptException {
        Object object = QuickJS._executeScript(this.getContextPtr(), expectedType.value, source, fileName);
        QuickJS.checkException(context);
        return object;
    }

    /**
     * @return Integer/Double/Boolean/String/JSObject/JSArray/JSFunction
     */
    public Object executeScript(String source, String fileName) throws QuickJSScriptException {
        return executeScript(JSValue.TYPE.UNKNOWN, source, fileName);
    }

    public int executeIntegerScript(String source, String fileName) throws QuickJSScriptException {
        return (int) executeScript(JSValue.TYPE.INTEGER, source, fileName);
    }

    public double executeDoubleScript(String source, String fileName) throws QuickJSScriptException {
        return (double) executeScript(JSValue.TYPE.DOUBLE, source, fileName);
    }

    public boolean executeBooleanScript(String source, String fileName) throws QuickJSScriptException {
        return (boolean) executeScript(JSValue.TYPE.BOOLEAN, source, fileName);
    }

    public String executeStringScript(String source, String fileName) throws QuickJSScriptException {
        return (String) executeScript(JSValue.TYPE.STRING, source, fileName);
    }

    public void executeVoidScript(String source, String fileName) throws QuickJSScriptException {
        executeScript(JSValue.TYPE.NULL, source, fileName);
    }

    public JSArray executeArrayScript(String source, String fileName) throws QuickJSScriptException {
        return (JSArray) executeScript(JSValue.TYPE.JS_ARRAY, source, fileName);
    }

    public JSObject executeObjectScript(String source, String fileName) throws QuickJSScriptException {
        return (JSObject) executeScript(JSValue.TYPE.JS_OBJECT, source, fileName);
    }

    void registerCallback(JavaCallback callback, JSFunction functionHandle) {
        QuickJS.MethodDescriptor methodDescriptor = new QuickJS.MethodDescriptor();
        methodDescriptor.callback = callback;
        functionRegistry.put(callback.hashCode(), methodDescriptor);
    }

    void registerCallback(JavaVoidCallback callback, JSFunction functionHandle) {
        QuickJS.MethodDescriptor methodDescriptor = new QuickJS.MethodDescriptor();
        methodDescriptor.voidCallback = callback;
        functionRegistry.put(callback.hashCode(), methodDescriptor);
    }

}
