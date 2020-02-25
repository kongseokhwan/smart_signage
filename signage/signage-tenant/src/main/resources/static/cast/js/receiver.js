var $iframe = document.querySelector(".iframe"),
namespace = "urn:x-cast:com.kulcloud.signage";

var $iframe = document.querySelector(".iframe"),
	namespace = "urn:x-cast:com.kulcloud.signage",
	currentUrl = null,
	useProxy = false,
	proxyUrl = "/proxy",
	PAGE_MEDIA = 0,
	PAGE_IFRAME = 1;

const context = cast.framework.CastReceiverContext.getInstance();
const playerManager = context.getPlayerManager();
const castDebugLogger = cast.debug.CastDebugLogger.getInstance();
// Enable debug logger and show a warning on receiver
// NOTE: make sure it is disabled on production
castDebugLogger.setEnabled(true);
//Set verbosity level for custom tags
castDebugLogger.loggerLevelByTags = {
  'ANALYTICS': cast.framework.LoggerLevel.INFO,
};

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
		cast.framework.events.EventType.LOADED_DATA,
  event => {
	  castDebugLogger.info('ANALYTICS', 'REQUEST_LOAD:', event);
	  displayMedia();
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
	context.sendCustomMessage(namespace, event.senderId, messagePacket());
});

// handler for 'senderdisconnected' event
context.addEventListener(cast.framework.system.EventType.SENDER_DISCONNECTED, function(event) {
	castDebugLogger.info('ANALYTICS', 'Received Sender Disconnected event: ' + event.data);
});

// show webpage
context.addCustomMessageListener(namespace, event => {
	castDebugLogger.info('ANALYTICS', "Received Message: " + event.data);

    currentUrl = addHttp(event.data.url);
    useProxy = event.data.proxy;

    // display the message from the sender
    displayWebpage();
});

function displayWebpage() {
	let url = iframeUrl();
	$iframe.src = url;
	$iframe.onload = function() {
		pages.showPage(PAGE_IFRAME);
		playerManager.stop();
		castDebugLogger.info('ANALYTICS', 'Load in iframe: ' + url);
		context.setApplicationState("Displaying: " + currentUrl);
		// inform all senders on the CastMessageBus of the incoming message event
		// sender message listener will be invoked
		context.sendCustomMessage(namespace, undefined, messagePacket());
	};
}
function displayIframe(url) {
	$iframe.src = url;
	castDebugLogger.info('ANALYTICS', 'Load in iframe: ' + url);
	$iframe.onload = function() {
		pages.showPage(PAGE_IFRAME);
		playerManager.stop();
		context.setApplicationState("Displaying: " + url);
		castDebugLogger.info('ANALYTICS', 'Displaying: ' + url);
	};
}

function displayMedia() {
	pages.showPage(PAGE_MEDIA);
	$iframe.onload = null;
	$iframe.src = "about:blank";
	context.setApplicationState("Displaying a media");
	castDebugLogger.info('ANALYTICS', 'Displaying a media');
}

function messagePacket() {
  return JSON.stringify({
    url: currentUrl || "Nothing",
    proxy: useProxy
  });
}

function iframeUrl() {
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

context.start();