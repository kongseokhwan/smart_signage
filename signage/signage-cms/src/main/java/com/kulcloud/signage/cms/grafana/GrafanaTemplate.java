package com.kulcloud.signage.cms.grafana;

public interface GrafanaTemplate {

	public String getDashboardId(String tenantId);
	public String getDashboardTitle(String tenantId);
	public String createDashboard(String tenantId);
	public boolean deleteDashboard(String tenantId);
	
}
