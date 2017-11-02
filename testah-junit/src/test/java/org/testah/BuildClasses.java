package org.testah;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class BuildClasses {

    @Test
    public void buildVerboseAssert() {

        StringBuilder logMsg;
        StringBuilder param;
        for (final Method method : Assert.class.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                && (method.getName().startsWith("assert") || method.getName().startsWith("fail")))
            {
                logMsg = new StringBuilder("public static " + method.getGenericReturnType() + " " + method.getName() + "(");
                param = new StringBuilder("Assert." + method.getName() + "(");
                int count = 0;
                for (final Parameter methodParams : method.getParameters()) {
                    logMsg
                        .append(count > 0 ? ", final " : " final ")
                        .append(methodParams.getType().getSimpleName())
                        .append(" ")
                        .append(methodParams.getName());
                    param.append((count > 0 ? "," : "") + methodParams.getName());
                    count++;
                }
                logMsg.append(") {\n");
                logMsg.append("try{\n");
                logMsg.append(param.toString() + ");\n");
                if (logMsg.toString().contains("arg2")) {
                    logMsg.append("addAssertHistory(message,false,\"" + method.getName() + "\",expected,actual);\n");
                } else {
                    logMsg.append("addAssertHistory(\"\",false,\"" + method.getName() + "\",expected,actual);\n");
                }
                logMsg.append("}catch(Exception e){\n");
                if (logMsg.toString().contains("arg2")) {
                    logMsg.append("addAssertHistory(message,true,\"" + method.getName() + "\",expected,actual, e);\n");
                } else {
                    logMsg.append("addAssertHistory(\"\",true,\"" + method.getName() + "\",expected,actual, e);\n");
                }
                logMsg.append("if (getThrowExceptionOnFail()) {\n");
                logMsg.append("throw e;\n");
                logMsg.append("}\n");

                logMsg.append("}\n");

                logMsg.append("}\n");
                if (logMsg.toString().contains("arg2")) {
                    System.out.println(logMsg.toString().replace("arg0", "message").replace("arg1", "expected")
                        .replace("arg2", "actual"));
                } else {
                    System.out.println(logMsg.toString().replace("arg0", "expected").replace("arg1", "actual"));
                }

            }
        }

    }

}
