/*
 *    Copyright 2023 ketikai
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pres.ketikai.hyper.api.agent;

import java.lang.instrument.Instrumentation;

/**
 * <p>提供 agent 程序接口</p>
 *
 * <p>Created on 2023/2/12 15:26</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface IAgent {

    /**
     * 程序入口
     *
     * @param args 参数
     * @param inst 提供对 java 程序的监测、协助接口
     */
    void agentmain(String args, Instrumentation inst);
}
