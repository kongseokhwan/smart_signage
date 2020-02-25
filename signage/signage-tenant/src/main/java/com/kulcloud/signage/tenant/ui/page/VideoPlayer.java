package com.kulcloud.signage.tenant.ui.page;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("video-player")
@JavaScript("src/components/dash.all.min.js")
@JsModule("src/components/video-player.js")
public class VideoPlayer extends PolymerTemplate<VideoPlayer.VideoPlayerModel>{

    private static final long serialVersionUID = 1L;
    
    private String url;

    public VideoPlayer() {
    	super();
    }
    
    public VideoPlayer(String url) {
    	super();
    	this.url = url;
    }
    
    @Override
	protected void onAttach(AttachEvent attachEvent) {
		if(!StringUtils.isEmpty(url)) {
			setUrl(url);
		}
	}
	
    public void setUrl(String url) {
    	getModel().setUrl(url);
    }

    /**
     * This model binds properties between WowzaTest and wowza-test
     */
    public interface VideoPlayerModel extends TemplateModel {
        void setUrl(String url);
    }
}
