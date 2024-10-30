/*
 *  Copyright 2024 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.curity.authorization;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class AuthZenHelper {

    private static String SUBJECT = "subject";
    private static String ACTION = "action";
    private static String RESOURCE = "resource";
    private static String CONTEXT = "context";

    public static String getAuthZenRequest(String subject, String action, String resource){

        JsonObject subjectAttributes = Json.createObjectBuilder()
                .add(SUBJECT, createAttribute("user", subject))
                .build();

        JsonObject resourceAttributes = Json.createObjectBuilder()
                .add(RESOURCE, createAttribute("api", resource))
                .build();

        JsonObject actionAttributes = Json.createObjectBuilder()
                .add(ACTION, Json.createObjectBuilder()
                        .add("name", "can_read")
                        .add("properties", Json.createObjectBuilder().add("method", action))
                )
                .build();

        return Json.createObjectBuilder()
                .add(SUBJECT, subjectAttributes.get(SUBJECT))
                .add(RESOURCE, resourceAttributes.get(RESOURCE))
                .add(ACTION, actionAttributes.get(ACTION))
                .build()
                .toString();
    }

    private static JsonObject createAttribute(String type, String id)
    {
        return Json.createObjectBuilder()
                .add("type", type)
                .add("id", id)
                .build();
    }
}
