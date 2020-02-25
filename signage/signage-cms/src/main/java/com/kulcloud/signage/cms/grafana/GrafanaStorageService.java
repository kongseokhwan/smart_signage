package com.kulcloud.signage.cms.grafana;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.cms.StorageService;
import com.kulcloud.signage.cms.data.ContentPK;
import com.kulcloud.signage.cms.html.data.HtmlContent;
import com.kulcloud.signage.cms.html.data.HtmlContentRepository;

//@Service
public class GrafanaStorageService implements StorageService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<GrafanaTemplate> dashboards;
	private HtmlContentRepository htmlRepo;
	
	@Autowired
	public GrafanaStorageService(Optional<List<GrafanaTemplate>> dashboards, HtmlContentRepository htmlRepo) {
		this.dashboards = dashboards.orElse(new ArrayList<>());
		this.htmlRepo = htmlRepo;
	}
	
	public boolean createStorage(String tenantId) {
		// TODO tenant influxdb datasource 추가
		for (GrafanaTemplate dashboard : dashboards) {
			String dashboardId = dashboard.getDashboardId(tenantId);
			if(!htmlRepo.findById(new ContentPK(tenantId, dashboardId)).isPresent()) {
				String url = dashboard.createDashboard(tenantId);
				if(url != null) {
					try {
						HtmlContent content = new HtmlContent();
						content.setId(new ContentPK(tenantId, dashboardId));
						content.setLink(true);
						content.setName(dashboardId);
						content.setTitle(dashboard.getDashboardTitle(tenantId));
						content.setDescription(dashboard.getDashboardTitle(tenantId));
						content.setUrl(url);
						htmlRepo.save(content);
					}catch(Throwable ex) {
						logger.error("Cannot create a dashboard of grafana: " + dashboardId + ", " + tenantId, ex);
						dashboard.deleteDashboard(tenantId);
						
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		return true;
	}

	public boolean deleteStorage(String tenantId) {
		for (GrafanaTemplate dashboard : dashboards) {
			String dashboardId = dashboard.getDashboardId(tenantId);
			Optional<HtmlContent> optional = htmlRepo.findById(new ContentPK(tenantId, dashboardId));
			if(optional.isPresent()) {
				dashboard.deleteDashboard(tenantId);
				htmlRepo.delete(optional.get());
			}
		}
		
		// TODO tenant influxdb datasource 제거
		return true;
	}

}
