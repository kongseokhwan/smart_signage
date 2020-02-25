var $iframe = document.querySelector(".iframe"),
	namespace = "urn:x-cast:com.kulcloud.signage",
	sock = null,
	timerId = 0,
	proxyUrl = "/proxy",
	wsUrl = null,
	PAGE_MEDIA = 0,
	PAGE_IFRAME = 1;

const device_status = {
		device_id: -1,
		play: 'IDLE',
		mute: false,
		seek: 0,
		volume: 0.5,
		content: '',
		player_type: ''
};
const context = cast.framework.CastReceiverContext.getInstance();
const playerManager = context.getPlayerManager();
const castDebugLogger = cast.debug.CastDebugLogger.getInstance();
// Enable debug logger and show a warning on receiver
// NOTE: make sure it is disabled on production
context.setLoggerLevel(cast.framework.LoggerLevel.DEBUG);
castDebugLogger.setEnabled(true);
//Set verbosity level for custom tags
castDebugLogger.loggerLevelByTags = {
  'ANALYTICS': cast.framework.LoggerLevel.INFO,
};

function isEmpty(value) { 
	if( value == "" || value == null || value == undefined || 
			( value != null && typeof value == "object" && !Object.keys(value).length ) )
	{ 
		return true;
	} else { 
		return false;
	}
}
	
function makeRequest (method, url) {
  return new Promise(function (resolve, reject) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url);
    xhr.onload = function () {
      if (this.status >= 200 && this.status < 300) {
        resolve(JSON.parse(xhr.response));
      } else {
        reject({
          status: this.status,
          statusText: xhr.statusText
        });
      }
    };
    xhr.onerror = function () {
      reject({
        status: this.status,
        statusText: xhr.statusText
      });
    };
    xhr.send();
  });
}

// show video listener
playerManager.addEventListener(
		cast.framework.events.EventType.MEDIA_INFORMATION_CHANGED,
  event => {
	  castDebugLogger.info('ANALYTICS', 'MEDIA_INFORMATION_CHANGED:', event);
	  if(!isEmpty(event.media)) {
		  displayMedia(event.media.contentUrl);
		  sendDeviceStatus();
	  }
});

playerManager.addEventListener(
	[
		cast.framework.events.EventType.PLAY,
		cast.framework.events.EventType.PLAYING,
		cast.framework.events.EventType.PAUSE,
		cast.framework.events.EventType.BUFFERING,
		cast.framework.events.EventType.ERROR,
		cast.framework.events.EventType.MEDIA_FINISHED,
		cast.framework.events.EventType.ENDED
	],
  event => {
	device_status.play = playerManager.getPlayerState();
	sendDeviceStatus();
});

playerManager.addEventListener(
	cast.framework.events.EventType.SEEKED,
  event => {
	device_status.seek = event.currentMediaTime;
	sendDeviceStatus();
});
context.addEventListener(
	cast.framework.system.EventType.SYSTEM_VOLUME_CHANGED,
  event => {
	device_status.volume = Number.parseFloat(event.data.level).toFixed(1);
	device_status.mute = event.data.muted;
	sendDeviceStatus();
});

//handler for the 'ready' event
context.addEventListener(cast.framework.system.EventType.READY, function(event) {
	castDebugLogger.info('ANALYTICS', "Received Ready event: ", event);
	context.setApplicationState("Application status is ready...");
});

// handler for 'senderconnected' event
context.addEventListener(cast.framework.system.EventType.SENDER_CONNECTED, function(event) {
	castDebugLogger.info('ANALYTICS', 'Received Sender Connected event: ' + event.data);
	castDebugLogger.info('ANALYTICS', context.getSender(event.data).userAgent);
});

// handler for 'senderdisconnected' event
context.addEventListener(cast.framework.system.EventType.SENDER_DISCONNECTED, function(event) {
	castDebugLogger.info('ANALYTICS', 'Received Sender Disconnected event: ' + event.data);
});

// show webpage
context.addCustomMessageListener(namespace, event => {
	castDebugLogger.info('ANALYTICS', "Received Message: " + event.data);

	if (event.data.request_type == 'websocket') {
		device_status.device_id = event.data.device_id;
		wsUrl = event.data.url;
		connectWs();
	} else if (event.data.request_type == 'content' ||
			event.data.request_type == 'desktop') {
	    let currentUrl = addHttp(event.data.url);
	    let useProxy = event.data.proxy;
	
	    // display the message from the sender
	    displayWebpage(currentUrl, useProxy);
	} else if(event.data.request_type == 'stopPlay') {
		playerManager.stop();
	} else {
		castDebugLogger.info('ANALYTICS', "Unknown Message: " + event.data);
	}
});

function displayWebpage(currentUrl, useProxy) {
	let url = iframeUrl(currentUrl, useProxy);
	$iframe.src = url;
	$iframe.onload = function() {
		device_status.content = $iframe.src;
		device_status.player_type = 'iframe';
		pages.showPage(PAGE_IFRAME);
		playerManager.stop();
		castDebugLogger.info('ANALYTICS', 'Load in iframe: ' + url);
		context.setApplicationState("Displaying: " + currentUrl);
		// inform all senders on the CastMessageBus of the incoming message event
		// sender message listener will be invoked
		context.sendCustomMessage(namespace, undefined, messagePacket());
	};
}

function displayMedia(currentUrl) {
	pages.showPage(PAGE_MEDIA);
	device_status.content = currentUrl;
	device_status.player_type = 'media';
	$iframe.onload = null;
	$iframe.src = "about:blank";
	context.setApplicationState("Displaying a media");
	castDebugLogger.info('ANALYTICS', 'Displaying a media');
}

function iframeUrl(currentUrl, useProxy) {
  if (useProxy) {
    return proxyUrl + "?url=" + currentUrl;
  }
  else {
    return currentUrl;
  }
}

function addHttp(url) {
  if (!/^(?:f|ht)tps?\:\/\//.test(url)) {
    url = "http://" + url;
  }
  return url;
}

function connectWs() {
	if(isEmpty(sock) && !isEmpty(wsUrl)) {
		sock = new SockJS(wsUrl);
		sock.onopen = function() {
			castDebugLogger.info('ANALYTICS', 'Connect a websocket: ' + wsUrl);
			sendDeviceStatus();
			timerId = setInterval(function () {
				sendDeviceStatus();
			}, 10000); // 10seconds hearbeat
		};
		
		sock.onmessage = function(e) {
			castDebugLogger.info('ANALYTICS', 'Receive a message through websocket: ' + e.data);
		};
		
		sock.onclose = function() {
			clearInterval(timerId);
			timerId = 0;
			castDebugLogger.info('ANALYTICS', 'Closed a websocket');
			sock = null;
		}
	}
}

function sendDeviceStatus() {
	if(isEmpty(sock)) {
		connectWs();
	} else {
		setTimeout(function() {
			let jsonString = JSON.stringify({type: 'STATUS', device_id: device_status.device_id, message: device_status});
			console.log('sendDeviceStatus: ' + jsonString);
			sock.send(jsonString);
		});
	}
}
context.start();