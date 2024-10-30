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

import org.json.JSONArray;
import org.json.JSONObject;
import se.curity.identityserver.sdk.attribute.scim.v2.ResourceAttributes;
import se.curity.identityserver.sdk.authorization.GraphQLObligation;
import se.curity.identityserver.sdk.authorization.ObligationAlterationResult;

import java.util.HashSet;
import java.util.Set;

class AuthZenFilterObligation implements GraphQLObligation.CanReadAttributes {

    Set _attributesToFilter = new HashSet<>();
    public AuthZenFilterObligation(JSONArray returnedObligations)
    {
        for (Object obj: returnedObligations.getJSONObject(0).getJSONArray("AttributeAssignment"))
        {
            JSONObject obligation = (JSONObject) obj;
            String fieldToFilter = obligation.getString("AttributeId");
            Boolean filter = obligation.getBoolean("Value");

            if(!filter) //if the obligation is false access is not allowed to the field and should be filtered from the response
            {
                _attributesToFilter.add(fieldToFilter);
            }
        }
    }

    @Override
    public ObligationAlterationResult<ResourceAttributes<?>> filterReadAttributes(Input input) {
        ResourceAttributes returnAttributes = input.getResourceAttributes();

        for (Object s : _attributesToFilter)
        {
            returnAttributes = returnAttributes.removeAttribute(s.toString());
        }

        return ObligationAlterationResult.of(returnAttributes);
    }
}
