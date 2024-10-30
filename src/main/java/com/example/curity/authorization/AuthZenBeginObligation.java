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

import se.curity.identityserver.sdk.authorization.GraphQLObligation;
import se.curity.identityserver.sdk.authorization.ObligationDecisionResult;

class AuthZenBeginObligation implements GraphQLObligation.BeginOperation {
    private ObligationDecisionResult _decisionResult;

    public AuthZenBeginObligation(Boolean decision)
    {
        if (decision) {
            _decisionResult = ObligationDecisionResult.allow();
        }
    }

    @Override
    public ObligationDecisionResult canPerformOperation(Input input) {
        return _decisionResult;
    }
}
