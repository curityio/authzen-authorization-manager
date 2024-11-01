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
package com.example.curity.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultString;
import se.curity.identityserver.sdk.config.annotation.Description;
import se.curity.identityserver.sdk.service.HttpClient;

public interface AuthZenAuthorizationManagerPluginConfig extends Configuration
{
    @Description("The Http Client used to communicate with the PDP")
    HttpClient getHttpClient();

    @Description("The PDP hostname")
    @DefaultString("localhost")
    String getPDPHost();

    @Description("The PDP port")
    @DefaultString("443")
    String getPDPPort();

    @Description("The PDP path")
    @DefaultString("/access/v1/evaluation")
    String getPDPPath();
}
