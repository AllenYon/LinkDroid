/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package cn.link.httpclient;

import org.json.JSONArray;

public abstract class JSONArrayHandler extends StringHandler {


    public abstract void onSuccess(JSONArray response);


    @Override
    public void onSuccess(String response) {
        try {
            JSONArray jarr = new JSONArray(response);
            onSuccess(filter(jarr));
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e);
        }
    }

    public abstract JSONArray filter(JSONArray jarr) throws Exception;
}
