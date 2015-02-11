package com.redhat.gss.bpms.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;

import com.redhat.gss.bpms.model.ParamInfo;

@Startup
@Singleton
public class DataModelsLoader {

	KieServices kieServices;

	// TODO: improve using a cache API
	Map<String, KieContainer> containers;

	@PostConstruct
	public void loadDataModel() {
		kieServices = KieServices.Factory.get();
		containers = new HashMap<String, KieContainer>();
	}

	public Object loadObject(ParamInfo p) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException {
		Object obj = getContainer(p.getArtifactId(), p.getGroupId(),
				p.getVersion()).getClassLoader().loadClass(p.getParamType())
				.newInstance();
		Map<String, Object> attrs = p.getAttributes();
		for (Map.Entry<String, Object> attr : attrs.entrySet()) {
			String attrName = attr.getKey();
			Object val = attr.getValue();
			String methodName = "set" + attrName.substring(0, 1).toUpperCase()
					+ attrName.substring(1);
			Method m = obj.getClass().getMethod(methodName, val.getClass());
			m.invoke(obj, val);
		}
		return obj;
	}

	public KieContainer getContainer(String artifactId, String groupId,
			String version) {
		String key = artifactId + groupId + version;
		KieContainer kContainer = containers.get(key);
		if (kContainer == null) {
			ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId,
					version);
			kContainer = kieServices.newKieContainer(releaseId);
			containers.put(key, kContainer);
		}
		return kContainer;
	}

}
