/*
 * Copyright 2009 Volvo Information Technology AB 
 * 
 * Licensed under the Volvo IT Corporate Source License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://java.volvo.com/licenses/CORPORATE-SOURCE-LICENSE 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.fredrik.bookitinfra.model;

import java.util.HashMap;
import java.util.Map;

public class NetworkPolicy {    

	String apiVersion = "networking.k8s.io/v1";
	String kind = "NetworkPolicy";
	Map<String, String> metadata = null; 

	Spec spec = null;

	public NetworkPolicy(String namespace, String name, String portType, int portNr) {
		metadata = new HashMap<>();
		metadata.put("name", name + "-policy");
		metadata.put("namespace", namespace);
		
		spec = new Spec(name, portType, portNr);
	}

	class Spec {
		PodSelector podSelector = null;
		Ports[] ingress = null;
		
		Spec(String name, String portType, int portNr) {
			this.podSelector = new PodSelector(name);
			this.ingress = new Ports[1];
			this.ingress[0] = new Ports(portType, portNr);
		}		
	}

	class PodSelector {
		Map<String, String> matchLabels = null;
		
		public PodSelector(String name) {
			matchLabels = new HashMap<>();
			matchLabels.put("app", name);
		}
	}

	class Ports {
		Port[] ports = null;
		
		public Ports(String portType, int portNr) {
			Port port = new Port(portType, portNr);
			ports = new Port[1];
			ports[0] = port;
		}
	}

	class Port {
		String protocol = "TCP";
		int port = 8888;
		
		public Port(String type, int portNr) {
			this.protocol = type;
			this.port = portNr;
		}
	}
}
