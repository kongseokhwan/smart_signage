package com.kulcloud.signage.cms.ui;

import org.apache.commons.lang3.StringUtils;

import com.kulcloud.signage.cms.ui.page.LivePage;
import com.kulcloud.signage.cms.ui.page.TenantPage;
import com.kulcloud.signage.cms.ui.page.VodPage;
import com.kulcloud.signage.cms.ui.page.HtmlPage;
import com.kulcloud.signage.commons.ui.PageView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.HighlightCondition;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

@Push
@PWA(name = "Signage Contents Management System", shortName = "Signage CMS")
public class MainView extends AppLayout {

    private static final long serialVersionUID = 1L;
     
    private Tabs menuTabs;
    
    public MainView() {
    	Image img = new Image("/images/cms_35px.png", "Signage CMS");
        img.setHeight("35px");
        addToNavbar(new DrawerToggle(), img);
        
        menuTabs = new Tabs();
        menuTabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(menuTabs);
		
		addPageMenu(TenantPage.class);
		addPageMenu(HtmlPage.class);
		addPageMenu(LivePage.class);
		addPageMenu(VodPage.class);
    }
    
    private void addPageMenu(Class<? extends Component> page) {
    	if(page != null) {
			Tab pageTab;
			PageView pageView = page.getDeclaredAnnotation(PageView.class);
			final RouterLink link = new RouterLink(pageView.title(), page);
			if(StringUtils.isEmpty(pageView.icon())) {
				if(StringUtils.isEmpty(pageView.image())) {
					pageTab = new Tab(link);
				} else {
					HorizontalLayout layout = new HorizontalLayout(new Image(pageView.image(), null), link);
					layout.setAlignItems(Alignment.CENTER);
					pageTab = new Tab(layout);
				}
			} else {
				HorizontalLayout layout = new HorizontalLayout(new Icon(VaadinIcon.valueOf(pageView.icon())), link);
				layout.setAlignItems(Alignment.CENTER);
				pageTab = new Tab(layout);
			}

			final String pageUrl = RouteConfiguration.forRegistry(UI.getCurrent().getRouter().getRegistry())
	                .getUrl(page);
			final Tab tab = pageTab;
			link.setHighlightCondition(new HighlightCondition<RouterLink>() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public boolean shouldHighlight(RouterLink t, AfterNavigationEvent event) {
					String url = event.getLocation().getPath();
					if (pageUrl.equals(url)) {
						menuTabs.setSelectedTab(tab);
						link.getStyle().set("color", "blue");
						return true;
					} else {
						link.getStyle().set("color", "gray");
						return false;
					}
				}
			});
			menuTabs.add(pageTab);
		}
    }
}

