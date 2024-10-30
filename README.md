# OpenID AuthZEN Authorization Manager Plugin

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)


PDPs: https://github.com/openid/authzen/blob/main/interop/authzen-todo-backend/src/pdps.json



A prototype Authorization Manager written in Java using an external [OpenID AuthZEN)](https://openid.github.io/authzen/) authorization engine also known as a [Policy Decision Point (PDP)](https://curity.io/resources/learn/entitlement-management-system/#the-policy-decision-point) for authorization.

**Note**: The plugin requires at least version 7.3 of the Curity Identity Server.

## Introduction

The Curity Identity Server can leverage Authorization Managers to control access to exposed GraphQL APIs for DCR and User Management. Authorization Managers can be custom built using the [Curity Java Plugin SDK](https://curity.io/docs/idsvr-java-plugin-sdk/latest/). This is an example of a custom Authorization manager that acts as a [Policy Enforcement Point (PEP)](https://curity.io/resources/learn/entitlement-management-system/#the-policy-enforcement-point) in an OpenID AuthZEN architecture. The OpenID AuthZEN Authorization Manager sends a JSON formatted request to a configured PDP that holds a policy. The PDP responds with a decision. The OpenID AuthZEN Authorization Manager handles the response and allows/denies access to the requested resource.

## Building the Plugin

Build the plugin by issuing the command `mvn package`. This will produce a JAR file in the `target/authzen-authorization-manager` directory, which can be installed.

## Installing the Plugin

To install the plugin, copy the compiled JAR (and all of its dependencies) from `target/authzen-authorization-manager` into `${IDSVR_HOME}/usr/share/plugins/${pluginGroup}` on each node, including the admin node.

For more information about installing plugins, refer to the [Plugin Installation section of the Documentation](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#plugin-installation).

## Required Dependencies

For a list of the dependencies and their versions, run `mvn dependency:list`. Ensure that all of these are installed in the plugin group; otherwise, they will not be accessible to this plug-in and run-time errors will result.

## Configuring the Plugin

The plugin needs an HttpClient, host, port and path configured in order to communicate with the OpenID AuthZEN PDP.

### The configuration parameters
| Name | Type | Description | Example | Default |
|------|------|-------------|---------|---------|
| `HttpClient`| String | The ID of the HttpClient that the Authorization Manager will use to call the OpenID AuthZEN PDP. | `authzen-http-client` |  |
| `PDP Host`  | String | The hostname of the OpenID AuthZEN PDP. | `authzen-pdp.example.com` | `localhost` |
| `PDP Port`  | String | The port that the OpenID AuthZEN PDP is exposing its service on.  | `8443` | `443` |
| `PDP Path`  | String | The path of the OpenID AuthZEN PDP that accepts authorization requests. | `/pdp` |  `/access/v1/evaluation` |

When committed, the Authorization Manager is available to be used throughout the Curity Identity Server.

### DCR GraphQL API

In order to protect the DCR GraphQL API the Authorization Manager needs to be added to the Token Service Profile. Navigate to `Token Service` -> `General`, in the drop-down for Authorization Manager, choose the newly created Authorization Manager.

### User Management GraphQL API

In order to protect the User Management GraphQL API the Authorization Manager needs to be added to the User Management Profile. Navigate to `User Management` -> `General`, in the drop-down for Authorization Manager, choose the newly created Authorization Manager.


### OpenID AuthZEN Sample Request/Response

#### Request

The example request below is what the OpenID AuthZEN Authorization manager sends to the PDP.

```json
POST /services/pdp HTTP/1.1
Host: localhost:443
Content-Type: application/+json
{
    "subject": {
        "type": "user",
        "id": "alice"
    },
    "resource": {
        "type": "api",
        "id": "user-management"
    },
    "action": {
        "name": "can_read",
        "properties": {
            "method": "POST"
        }
    }
}
```

#### Response

The response from the PDP is very simple and just denotes `true` or `false` if access is granted or not.

```json
{"decision":false}
```

## More Information

- Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server
- [OpenID AuthZEN Working Group)](https://openid.github.io/authzen/)
- [Curity Identity Server GraphQL APIs](https://curity.io/docs/idsvr/latest/developer-guide/graphql/index.html)
- [User Management with GraphQL](https://curity.io/resources/learn/graphql-user-management/)
- [Authorizing Access to User Data](https://curity.io/resources/learn/authorizing-user-access/)

Copyright (C) 2024 Curity AB.