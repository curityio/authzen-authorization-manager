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

import com.example.curity.config.AuthZenAuthorizationManagerPluginConfig;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.attribute.ContextAttributes;
import se.curity.identityserver.sdk.attribute.SubjectAttributes;
import se.curity.identityserver.sdk.authorization.AuthorizationResult;
import se.curity.identityserver.sdk.authorization.GraphQLObligation;
import se.curity.identityserver.sdk.authorization.graphql.GraphQLAuthorizationActionAttributes;
import se.curity.identityserver.sdk.authorization.graphql.GraphQLAuthorizationManager;
import se.curity.identityserver.sdk.authorization.graphql.GraphQLAuthorizationResourceAttributes;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static se.curity.identityserver.sdk.http.HttpRequest.fromString;

public final class AuthZenAuthorizationManagerPlugin implements GraphQLAuthorizationManager
{
    private static final Logger _logger = LoggerFactory.getLogger(AuthZenAuthorizationManagerPlugin.class);
    private final AuthZenAuthorizationManagerPluginConfig _config;
    private final ExceptionFactory _exceptionFactory;
    private final HttpClient _pdpClient;

    public AuthZenAuthorizationManagerPlugin(AuthZenAuthorizationManagerPluginConfig config,
                                             ExceptionFactory exceptionFactory)
    {
        _config = config;
        _exceptionFactory = exceptionFactory;
        _pdpClient = config.getHttpClient();
    }

    @Override
    public AuthorizationResult<GraphQLObligation> getGraphQLAuthorizationResult(
            SubjectAttributes subjectAttributes,
            GraphQLAuthorizationActionAttributes graphQLAuthorizationActionAttributes,
            GraphQLAuthorizationResourceAttributes graphQLAuthorizationResourceAttributes,
            ContextAttributes contextAttributes)
    {
        String subject = subjectAttributes.getSubject();
        String method = "POST"; //Hard-coded, graphQLAuthorizationActionAttributes does not yet contain the method
        String resource = graphQLAuthorizationResourceAttributes.get("resourceType").getValue().toString();

        String authZenRequestBody = AuthZenHelper.getAuthZenRequest(subject, method, resource);

        try
        {
            URI pdpURI = new URI(String.format("%s://%s:%s%s", _pdpClient.getScheme(), _config.getPDPHost(), _config.getPDPPort(), _config.getPDPPath()));
            HttpResponse pdpResponse = _pdpClient.request(pdpURI)
                    .contentType("application/json")
                    .body(fromString(authZenRequestBody, StandardCharsets.UTF_8))
                    .post()
                    .response();

            if (pdpResponse.statusCode() != 200)
            {
                return AuthorizationResult.deny("PDP Http Status code " + pdpResponse.statusCode());
            }

            JSONObject pdpResponseBody = new JSONObject(pdpResponse.body(HttpResponse.asString()));

            boolean decision = pdpResponseBody.optBoolean("decision");

            if (decision == Boolean.TRUE)
            {
                return AuthorizationResult.allow(new AuthZenBeginObligation(true));
            }
            else if(decision == Boolean.FALSE)
            {
                return AuthorizationResult.deny("Authorization decision response from PDP is false, returning Deny");
            }

            _logger.debug("Unable to determine decision, returning Deny");
            return AuthorizationResult.deny("Unable to determine decision, returning Deny");
        }
        catch (URISyntaxException e)
        {
            throw _exceptionFactory.externalServiceException("Unable to connect to PDP using configured URI.");
        } catch (HttpClient.HttpClientException e)
        {
            throw _exceptionFactory.externalServiceException("Unable to connect to PDP. Check the connection.");
        }
    }
}