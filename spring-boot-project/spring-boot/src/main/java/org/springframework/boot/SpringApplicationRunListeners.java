/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;

/**
 * A collection of {@link SpringApplicationRunListener}.
 *
 * @author Phillip Webb
 */
class SpringApplicationRunListeners {

	private final Log log;

	private final List<SpringApplicationRunListener> listeners;

	SpringApplicationRunListeners(Log log, Collection<? extends SpringApplicationRunListener> listeners) {
		this.log = log;
		this.listeners = new ArrayList<>(listeners);
	}

	void starting() {
		/**
		 *广播器initialMulticaster这个类的作用
		 *	1. 首先会广播一个事件，并且对询问所有的监听器是否感兴趣：
		 * 		for (ApplicationListener<?> listener : getApplicationListeners(event, type))
		 * 		在getApplicationListeners()传入了两个参数event，type，作为兴趣源或者事件类型。
		 * 		这个方法的作用就是告诉所有的监听器，现在有了一个type类型的event，谁感兴趣？
		 * 	2. 通知所有监听器
		 * 		getApplicationListeners()里面会遍历所有的监听器，每一个监听器都会对这个方法进行判断，
		 * 		是否对这个事件感兴趣。
		 * 		如何判断呢？
		 * 			a).用两个方法确定
		 * 				supportsEventType(eventType)和supportsSourceType(sourceType)，这两个方法
		 * 				可以理解为通过传入一个事件类型到监听器里面，进而判断是否对这个事件感兴趣。如果感兴趣
		 * 				返回true，如果不感兴趣返回false。如果是true会被添加到一个list中，再由后续的代码进行处理。
		 * 			b).在监听器进行回调的时候
		 * 				除了a)以外也可以通过onApplicationEvent(E event)进行判断，如果事件类型不感兴趣，那么不作
		 * 				任何的处理即可。注：Spring是通过a)+b)进行双重判断的。
		 *	3. 获得所有对这个事件感兴趣的监听器以后，遍历执行onApplicationEvent(E event)方法。
		 *		这里的代码传递一个ApplicationStartingEvent的事件到里面去，最终决定如何执行相关的方法。
		 *	4. initialMulticaster金额图就阿布带事故SimpleApplicationEventMulticaster类型的对象
		 *		主要有两个方法，一个是广播事件，一个是执行listener的onApplicationEvent()方法。
 		 */
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.starting();
		}
	}

	void environmentPrepared(ConfigurableEnvironment environment) {
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.environmentPrepared(environment);
		}
	}

	void contextPrepared(ConfigurableApplicationContext context) {
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.contextPrepared(context);
		}
	}

	void contextLoaded(ConfigurableApplicationContext context) {
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.contextLoaded(context);
		}
	}

	void started(ConfigurableApplicationContext context) {
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.started(context);
		}
	}

	void running(ConfigurableApplicationContext context) {
		for (SpringApplicationRunListener listener : this.listeners) {
			listener.running(context);
		}
	}

	void failed(ConfigurableApplicationContext context, Throwable exception) {
		for (SpringApplicationRunListener listener : this.listeners) {
			callFailedListener(listener, context, exception);
		}
	}

	private void callFailedListener(SpringApplicationRunListener listener, ConfigurableApplicationContext context,
			Throwable exception) {
		try {
			listener.failed(context, exception);
		}
		catch (Throwable ex) {
			if (exception == null) {
				ReflectionUtils.rethrowRuntimeException(ex);
			}
			if (this.log.isDebugEnabled()) {
				this.log.error("Error handling failed", ex);
			}
			else {
				String message = ex.getMessage();
				message = (message != null) ? message : "no error message";
				this.log.warn("Error handling failed (" + message + ")");
			}
		}
	}

}
