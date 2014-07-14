/*
Copyright (c) Microsoft
All Rights Reserved
Apache 2.0 License
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */

package com.microsoft.aad.test.todoapi;

public class Constants {

    public static final String SDK_VERSION = "1.0";

    /**
     * UTF-8 encoding
     */
    public static final String UTF8_ENCODING = "UTF-8";

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String HEADER_AUTHORIZATION_VALUE_PREFIX = "Bearer ";

    // -------------------------------AAD
    // PARAMETERS----------------------------------
    static final String AUTHORITY_URL = "https://login.windows.net/omercantest.onmicrosoft.com";

    static final String CLIENT_ID = "650a6609-5463-4bc4-b7c6-19df7990a8bc";

    static final String RESOURCE_ID = "https://omercantest.onmicrosoft.com/AllHandsTry";

    static final String REDIRECT_URL = "http://taskapp";

    static String USER_HINT = "faruk@omercantest.onmicrosoft.com";

    // Endpoint we are targeting for the deployed WebAPI service
    static final String SERVICE_URL = "https://android.azurewebsites.net";

    // ------------------------------------------------------------------------------------------

    static final String TABLE_WORKITEM = "WorkItem";

    public static final String SHARED_PREFERENCE_NAME = "com.example.com.test.settings";

    public static final String KEY_NAME_ASK_BROKER_INSTALL = "test.settings.ask.broker";

    public static final String KEY_NAME_CHECK_BROKER = "test.settings.check.broker";

}
