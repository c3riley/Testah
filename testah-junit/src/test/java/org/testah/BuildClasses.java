/*
#
# Copyright (c) 2014-2016 Cazena, Inc., as an unpublished work.
# This notice does not imply unrestricted or public access to these
# materials which are a trade secret of Cazena, Inc. or its
# subsidiaries or affiliates (together referred to as "Cazena"), and
# which may not be copied, reproduced, used, sold or transferred to any
# third party without Cazena's prior written consent.
#
# All rights reserved.
*/
package org.testah;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import org.junit.Assert;
import org.junit.Test;

public class BuildClasses {

    @Test
    public void buildVerboseAssert() {

        StringBuilder s;
        String param;
        for (final Method m : Assert.class.getMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && (m.getName().startsWith("assert") || m.getName().startsWith("fail"))) {
                s = new StringBuilder("public static " + m.getGenericReturnType() + " " + m.getName() + "(");
                param = "Assert." + m.getName() + "(";
                int i = 0;
                for (final Parameter p : m.getParameters()) {
                    s.append((i > 0 ? ", final " : " final ") + p.getType().getSimpleName() + " " + p.getName());
                    param += (i > 0 ? "," : "") + p.getName();
                    i++;
                }
                s.append(") {\n");
                s.append("try{\n");
                s.append(param + ");\n");
                if (s.toString().contains("arg2")) {
                    s.append("addAssertHistory(message,false,\"" + m.getName() + "\",expected,actual);\n");
                } else {
                    s.append("addAssertHistory(\"\",false,\"" + m.getName() + "\",expected,actual);\n");
                }
                s.append("}catch(Exception e){\n");
                if (s.toString().contains("arg2")) {
                    s.append("addAssertHistory(message,true,\"" + m.getName() + "\",expected,actual, e);\n");
                } else {
                    s.append("addAssertHistory(\"\",true,\"" + m.getName() + "\",expected,actual, e);\n");
                }
                s.append("if (getThrowExceptionOnFail()) {\n");
                s.append("throw e;\n");
                s.append("}\n");

                s.append("}\n");

                s.append("}\n");
                if (s.toString().contains("arg2")) {
                    System.out.println(s.toString().replace("arg0", "message").replace("arg1", "expected").replace("arg2", "actual"));
                } else {
                    System.out.println(s.toString().replace("arg0", "expected").replace("arg1", "actual"));
                }

            }
        }

    }

}
