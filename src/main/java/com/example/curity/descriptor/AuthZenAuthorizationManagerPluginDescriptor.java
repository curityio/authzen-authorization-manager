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
package com.example.curity.descriptor;

import com.example.curity.authorization.AuthZenAuthorizationManagerPlugin;
import com.example.curity.config.AuthZenAuthorizationManagerPluginConfig;
import se.curity.identityserver.sdk.authorization.graphql.GraphQLAuthorizationManager;
import se.curity.identityserver.sdk.plugin.descriptor.AuthorizationManagerPluginDescriptor;

public final class AuthZenAuthorizationManagerPluginDescriptor implements AuthorizationManagerPluginDescriptor<AuthZenAuthorizationManagerPluginConfig>
{
    @Override
    public String getPluginImplementationType() { return "authzen-authorization-manager"; }

    @Override
    public Class<? extends AuthZenAuthorizationManagerPluginConfig> getConfigurationType()
    {
        return AuthZenAuthorizationManagerPluginConfig.class;
    }

    public Class<? extends GraphQLAuthorizationManager> getGraphQLAuthorizationManager()
    {
        return AuthZenAuthorizationManagerPlugin.class;
    }
}
