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

package pres.ketikai.hyper.impl.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pres.ketikai.hyper.api.agent.IAgent;

import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>委托 agent </p>
 *
 * <p>Created on 2023/2/12 16:42</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Primary
@Component
public final class DelegateAgent implements IAgent {

    private final Map<IAgent, Boolean> agents = new LinkedHashMap<>(16);

    /**
     * 程序入口
     *
     * @param args 参数
     * @param inst 提供对 java 程序的监测、协助接口
     */
    @Override
    public void agentmain(String args, Instrumentation inst) {
        synchronized (agents) {
            final Set<Map.Entry<IAgent, Boolean>> entries = agents.entrySet();
            for (final Map.Entry<IAgent, Boolean> entry : entries) {
                if (entry.getValue()) {
                    entry.getKey().agentmain(args, inst);
                    entry.setValue(false);
                }
            }
        }
    }

    @Autowired
    private void addAgents(Collection<IAgent> agents) {
        if (agents == null || agents.isEmpty()) {
            return;
        }

        for (final IAgent agent : agents) {
            if (!equals(agent)) {
                this.agents.putIfAbsent(agent, true);
            }
        }
    }
}
