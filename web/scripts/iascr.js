if (typeof intuit === 'undefined' || !intuit) {
	intuit = {}; // since intuit is in global scope and because of a bug in IE we don't do a 'var intuit'.
}

if (!intuit.ipp) {
	intuit.ipp = {};
}

if (!intuit.ipp.anywhere) {
	intuit.ipp.anywhere = {};
}

intuit.ipp.anywhere = {
	version : '0.9.0',
	tags	: ['connectToIntuit', 'reconnectToIntuit', 'blueDot'],
	tagPrefix : 'ipp',
	developerGaTrackerInitiated: false,
	windowLoad	: function() {
		intuit.ipp.anywhere.tracking.setup();
		intuit.ipp.jQuery('script').each(function (){
			// check if this script file is from our domain
			
			var jsSrc = "https://js.appcenter.intuit.com/Content/IA/intuit.ipp.anywhere.js";
			var jsSrcParts = jsSrc.replace(/http[s]?:\/\//, '').split('/');
			var qs = intuit.ipp.ourDomain.exec(jsSrcParts[0]);

			intuit.ipp.anywhere.serviceHost = "workplace.intuit.com";
			intuit.ipp.jQuery('head').append("<link rel='stylesheet' href='https://" + intuit.ipp.anywhere.serviceHost + "/Content/IA/intuit.ipp.anywhere.css' type='text/css' media='all' />");
			intuit.ipp.jQuery('head').append("<!--[if IE 7]><style type='text/css'>.intuitPlatformConnectButton, .intuitPlatformReconnectButton { font-size:0; text-indent: 0; }</style><![endif]-->");
			if(intuit.ipp.anywhere.serviceHost.match(/appcenter.intuit.com/) || intuit.ipp.anywhere.serviceHost.match(/workplace.intuit.com/)) {
				intuit.ipp.anywhere.serviceHost = "workplace.intuit.com";
			}
			alert("setting host value");
			alert(intuit.ipp.anywhere.serviceHost);
			intuit.ipp.anywhere.init(jsSrc);
			return false;
		});
	},
	init	: function(srcFile) {
		// load the tiny scroll plugin
		(function($){$.tiny=$.tiny||{};$.tiny.scrollbar={options:{axis:'y',wheel:40,scroll:true,size:'auto',sizethumb:'auto'}};$.fn.tinyscrollbar=function(options){var options=$.extend({},$.tiny.scrollbar.options,options);this.each(function(){$(this).data('tsb',new Scrollbar($(this),options));});return this;};$.fn.tinyscrollbar_update=function(sScroll){return $(this).data('tsb').update(sScroll);};function Scrollbar(root,options){var oSelf=this;var oWrapper=root;var oViewport={obj:$('.intuitPlatformAppMenuDropdownAppsListScrollViewport',root)};var oContent={obj:$('.intuitPlatformAppMenuDropdownAppsListScrollOverview',root)};var oScrollbar={obj:$('.intuitPlatformAppMenuDropdownAppsListScrollbar',root)};var oTrack={obj:$('.intuitPlatformAppMenuDropdownAppsListScrollTrack',oScrollbar.obj)};var oThumb={obj:$('.intuitPlatformAppMenuDropdownAppsListScrollThumb',oScrollbar.obj)};var sAxis=options.axis=='x',sDirection=sAxis?'left':'top',sSize=sAxis?'Width':'Height';var iScroll,iPosition={start:0,now:0},iMouse={};function initialize(){oSelf.update();setEvents();return oSelf;}
this.update=function(sScroll){oViewport[options.axis]=oViewport.obj[0]['offset'+sSize];oContent[options.axis]=oContent.obj[0]['scroll'+sSize];oContent.ratio=oViewport[options.axis]/oContent[options.axis];oScrollbar.obj.toggleClass('intuitPlatformAppMenuDropdownAppsListScrollDisable',oContent.ratio>=1);oTrack[options.axis]=options.size=='auto'?oViewport[options.axis]:options.size;oThumb[options.axis]=Math.min(oTrack[options.axis],Math.max(0,(options.sizethumb=='auto'?(oTrack[options.axis]*oContent.ratio):options.sizethumb)));oScrollbar.ratio=options.sizethumb=='auto'?(oContent[options.axis]/oTrack[options.axis]):(oContent[options.axis]-oViewport[options.axis])/(oTrack[options.axis]-oThumb[options.axis]);iScroll=(sScroll=='relative'&&oContent.ratio<=1)?Math.min((oContent[options.axis]-oViewport[options.axis]),Math.max(0,iScroll)):0;iScroll=(sScroll=='bottom'&&oContent.ratio<=1)?(oContent[options.axis]-oViewport[options.axis]):isNaN(parseInt(sScroll))?iScroll:parseInt(sScroll);setSize();};function setSize(){oThumb.obj.css(sDirection,iScroll/oScrollbar.ratio);oContent.obj.css(sDirection,-iScroll);iMouse['start']=oThumb.obj.offset()[sDirection];var sCssSize=sSize.toLowerCase();oScrollbar.obj.css(sCssSize,oTrack[options.axis]);oTrack.obj.css(sCssSize,oTrack[options.axis]);oThumb.obj.css(sCssSize,oThumb[options.axis]);};function setEvents(){oThumb.obj.bind('mousedown',start);oThumb.obj[0].ontouchstart=function(oEvent){oEvent.preventDefault();oThumb.obj.unbind('mousedown');start(oEvent.touches[0]);return false;};oTrack.obj.bind('mouseup',drag);if(options.scroll&&this.addEventListener){oWrapper[0].addEventListener('DOMMouseScroll',wheel,false);oWrapper[0].addEventListener('mousewheel',wheel,false);}
else if(options.scroll){oWrapper[0].onmousewheel=wheel;}};function start(oEvent){iMouse.start=sAxis?oEvent.pageX:oEvent.pageY;var oThumbDir=parseInt(oThumb.obj.css(sDirection));iPosition.start=oThumbDir=='auto'?0:oThumbDir;$(document).bind('mousemove',drag);document.ontouchmove=function(oEvent){$(document).unbind('mousemove');drag(oEvent.touches[0]);};$(document).bind('mouseup',end);oThumb.obj.bind('mouseup',end);oThumb.obj[0].ontouchend=document.ontouchend=function(oEvent){$(document).unbind('mouseup');oThumb.obj.unbind('mouseup');end(oEvent.touches[0]);};return false;};function wheel(oEvent){if(!(oContent.ratio>=1)){oEvent=$.event.fix(oEvent||window.event);var iDelta=oEvent.wheelDelta?oEvent.wheelDelta/120:-oEvent.detail/3;iScroll-=iDelta*options.wheel;iScroll=Math.min((oContent[options.axis]-oViewport[options.axis]),Math.max(0,iScroll));oThumb.obj.css(sDirection,iScroll/oScrollbar.ratio);oContent.obj.css(sDirection,-iScroll);oEvent.preventDefault();};};function end(oEvent){$(document).unbind('mousemove',drag);$(document).unbind('mouseup',end);oThumb.obj.unbind('mouseup',end);document.ontouchmove=oThumb.obj[0].ontouchend=document.ontouchend=null;return false;};function drag(oEvent){if(!(oContent.ratio>=1)){iPosition.now=Math.min((oTrack[options.axis]-oThumb[options.axis]),Math.max(0,(iPosition.start+((sAxis?oEvent.pageX:oEvent.pageY)-iMouse.start))));iScroll=iPosition.now*oScrollbar.ratio;oContent.obj.css(sDirection,-iScroll);oThumb.obj.css(sDirection,iPosition.now);}
return false;};return initialize();};})(intuit.ipp.jQuery);
		// find all elements with the tag 'tagPrefix:tag'
		//alert(document.createElement("ipp:connectToIntuit"));
		intuit.ipp.jQuery.each(this.tags, function(index, value) {
			// for each tag of this type
			if (navigator.appVersion.indexOf("MSIE") != -1 && parseFloat(navigator.appVersion.split("MSIE")[1]) <= 7) {
				var tags = intuit.ipp.jQuery(value);
			} else {
				var tags = intuit.ipp.jQuery(intuit.ipp.anywhere.tagPrefix + '\\:' + value);
			}
			tags.each( function() {
				if (value == 'connectToIntuit') {
					intuit.ipp.anywhere.controller.connectToIntuit.execute(this);
				} else if (value == 'blueDot') {
					alert("bluedot");
					intuit.ipp.anywhere.controller.blueDot.execute(this);
				} else if (value == 'reconnectToIntuit') {
					intuit.ipp.anywhere.controller.reconnectToIntuit.execute(this);
				}
			});
		});
		//intuit.ipp.jQuery(".intuitPlatformConnectLink").live("click", function () {
		//	intuit.ipp.anywhere.controller.onCustomConnectToIntuitClicked(this);
		//});
	},
	setup	: function(opts) {
		if(opts == null) {
			opts = {}
		}
		intuit.ipp.anywhere.menuProxy = opts.menuProxy;
		intuit.ipp.anywhere.developerGaTrackerId = opts.gaTrackerId;
		intuit.ipp.anywhere.grantUrl = opts.grantUrl;
	}

};

// CONTROLLER
intuit.ipp.anywhere.controller = {
	connectToIntuit : {
		name : 'connectToIntuit',
		execute : function(elem){
			var view = intuit.ipp.anywhere.view.connectToIntuit;
			var model = intuit.ipp.anywhere.model;
			view.render(elem);
		}
	},

	onConnectToIntuitClicked : function(elem){
		// if(intuit.ipp.jQuery(elem).parent().attr('GrantURL')) {
		if(intuit.ipp.anywhere.grantUrl) {
			intuit.ipp.anywhere.tracking.trackEvent("ConnectButton", "Connect", "ConnectWithQuickbooks", "click");
			intuit.ipp.anywhere.service.openExternalPopupWindow({
				url: "https://"+intuit.ipp.anywhere.serviceHost+"/Connect/SessionStart?grantUrl="+encodeURIComponent(intuit.ipp.anywhere.grantUrl),
				centered: true
			});
		} else {
			if(console && console.log) {
				// console.log("Missing GrantURL parameter");
				console.log("Missing grantUrl in setup function");
			}
		}
	},
	
	onCustomConnectToIntuitClicked : function(elem) {
		if(intuit.ipp.jQuery(elem).attr('GrantURL')) {
			intuit.ipp.anywhere.tracking.trackEvent("ConnectButton", "CustomConnect", "ConnectWithQuickbooks", "click");
			intuit.ipp.anywhere.service.openExternalPopupWindow({
				url: "https://"+intuit.ipp.anywhere.serviceHost+"/Connect/SessionStart?grantUrl="+encodeURIComponent(intuit.ipp.jQuery(elem).attr('GrantURL')),
				centered: true
			});
		} else {
			if(console && console.log) {
				console.log("Missing GrantURL parameter");
			}
		}
	},
	
	reconnectToIntuit: {
		name : 'reconnectToIntuit',
		execute : function(elem){
			var view = intuit.ipp.anywhere.view.reconnectToIntuit;
			view.render(elem);
		}
	},
	
	onReconnectToIntuitClicked : function(elem) {
		if(intuit.ipp.anywhere.grantUrl) {
			intuit.ipp.anywhere.tracking.trackEvent("ReconnectButton", "Reconnect", "ReconnectWithQuickbooks", "click");
			intuit.ipp.anywhere.service.openExternalPopupWindow({
				url: "https://"+intuit.ipp.anywhere.serviceHost+"/Connect/SessionStart?reconnect=true&grantUrl="+encodeURIComponent(intuit.ipp.anywhere.grantUrl),
				centered: true
			});
		} else {
			if(console && console.log) {
				// console.log("Missing GrantURL parameter");
				console.log("Missing grantUrl in setup function");
			}
		}
	},

	blueDot : {
		name : 'blueDot',
		execute : function(elem) {
			/*qbo.ippmenu.servicePath = "/app/AppDisplay/SidebarData";
			qbo.ippmenu.companyID = "180908800";
			qbo.ippmenu.serviceTimeout = "5000";
			qbo.ippmenu.serviceDown = false;
			qbo.ippmenu.serviceDownMessage = "Not here";*/

			var view = intuit.ipp.anywhere.view.blueDot;
			var model = intuit.ipp.anywhere.model;
			//View.plugTracker(qbo.ippmenu.IPPMenuTracker);
			view.render(elem);
		}
	}
};

intuit.ipp.anywhere.view = {
	connectToIntuit : {
		render : function(elem) {
			// build the html inside the elem
			intuit.ipp.jQuery(elem).html("<a href='javascript:void(0)' class='intuitPlatformConnectButton'>Connect with QuickBooks</a>");
			intuit.ipp.anywhere.tracking.trackEvent("ConnectButton", "Render", "ConnectWithQuickBooks", "load");
			// init the listeners
			intuit.ipp.jQuery(".intuitPlatformConnectButton").click(function() {
				intuit.ipp.anywhere.controller.onConnectToIntuitClicked(this);
			});
		}
	},
	reconnectToIntuit : {
		render : function(elem) {
			// build the html inside the elem
			intuit.ipp.jQuery(elem).html("<a href='javascript:void(0)' class='intuitPlatformReconnectButton'>Reconnect with QuickBooks</a>");
			intuit.ipp.anywhere.tracking.trackEvent("ReconnectButton", "Render", "ReconnectWithQuickbooks", "click");
			// init the listeners
			intuit.ipp.jQuery(".intuitPlatformReconnectButton").click(function() {
				intuit.ipp.anywhere.controller.onReconnectToIntuitClicked(this);
			});
		}
	},
	blueDot : {
		utility : function() {
			var isMenuHidden = function(el) {
				return el.id === "intuitACLogo" && !intuit.ipp.jQuery(el).hasClass("opened");
			};
			var navigateToURL = function(url, openInNewWindow) {
				if (url) {
					if (openInNewWindow) {
						window.open(url);
					} else {
						// give a chance for the tracker to finish
						// before replacing the page
						setTimeout(function() {
							window.location = url;
						}, 100);
					}
				}
			};

			// don't know why intuit.ipp.jQuery.isArray does not work, maybe only for new jQuery version?
			var isArray = function(value) {
				return value &&
					   typeof value === 'object' &&
					   typeof value.length === 'number';
			};

			// for newest version of jQuery, use intuit.ipp.jQuery.closest
			var closestLink = function(el) {
				while (el !== document.body) {
					if (el.nodeName === "A") {
						return el;
					}
					el = el.parentNode;
				}
			};

		},

		render : function(elem) {
			var buildTools = function(data) {
				// var el = intuit.ipp.jQuery("<div id='intuitACNav' class='intuitACNav ipp'><a id='intuitACLogo' class='intuitACLogo ipp' href='javascript:void(0);' title='Intuit App Center'><span class='blueDot ipp'>&nbsp;</span></a><div id='intuitACTools' class='intuitACTools ipp'></div>");
				var el = intuit.ipp.jQuery('<div id="intuitPlatformAppMenu"><a id="intuitPlatformAppMenuLogo" href="javascript:void(0);" title="Intuit App Center"><span id="intuitPlatformAppMenuDot">&nbsp;</span></a><div id="intuitPlatformAppMenuDropdown" style="display: none;"><div id="intuitPlatformAppMenuDropdownTop"></div><div id="intuitPlatformAppMenuDropdownInner"></div></div></div>');
				return el;
			};

			var initListeners = function() {
				intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").click(toggle);
				//intuit.ipp.jQuery("#fb:like").click(handleTracking);
			};

			var toggle = function(e) {
				intuit.ipp.jQuery("#intuitPlatformAppMenuDropdown").toggle();

				var isVisible = intuit.ipp.jQuery("#intuitPlatformAppMenuDropdown").is(":visible");
				intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").toggleClass("opened", isVisible);

				// hide menu when user clicks outside of it
				// but only listen to body clicks if menu is visible
				// unbind when menu is not visible
				if (isVisible) {
					intuit.ipp.jQuery("body").bind("click", onBodyClicked);
				} else {
					intuit.ipp.jQuery("body").unbind("click", onBodyClicked);
				}

				intuit.ipp.anywhere.tracking.trackEvent("AppMenu", isVisible ? "Show" : "Hide", "", "click");
				intuit.ipp.anywhere.view.blueDot.setupScroll();
				e.preventDefault();
				return false;
			};

			var onBodyClicked = function(e) {
				if(!intuit.ipp.jQuery(e.target).parents().is("#intuitPlatformAppMenuDropdown")) {
					toggle(e);
				}
			};

			intuit.ipp.jQuery(elem).append(buildTools());

			initListeners();

			intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").bind("click", intuit.ipp.anywhere.model.blueDot.loadWidget);
			intuit.ipp.anywhere.tracking.trackEvent("AppMenu", "Render", "", "load");
			// intuit.ipp.anywhere.controller.onBlueDotClicked(elem);

			/* handler called only once upon first click of logo */
		},
		
		setupScroll: function() {
			ht = intuit.ipp.jQuery(".intuitPlatformAppMenuDropdownAppsList").height();
			intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownAppsListScroll").hide();
			max = (intuit.ipp.jQuery(window).height() - (intuit.ipp.jQuery("#intuitPlatformAppMenuDropdown").height() + intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").height()) - 50);
			max = (max < 40) ? 40 : max;
			if(max < ht) {
				intuit.ipp.jQuery(".intuitPlatformAppMenuDropdownAppsListScrollViewport").height(max);
			} else {
				intuit.ipp.jQuery(".intuitPlatformAppMenuDropdownAppsListScrollViewport").height(ht);
			}
			intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownAppsListScroll").show();
			intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownAppsListScroll").tinyscrollbar();
		}
	}
};

intuit.ipp.anywhere.model = {
	blueDot : {
		loadWidget: function(event) {
			if(intuit.ipp.anywhere.menuProxy) {
				intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownInner").html('<span class="intuitPlatformAppMenuDropdownHeader">Please wait, loading menu...</span>');
				intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").unbind("click", intuit.ipp.anywhere.model.blueDot.loadWidget);
				intuit.ipp.anywhere.tracking.trackEvent("AppMenu", "Load", "", "click");
				intuit.ipp.jQuery.ajax({
					url: intuit.ipp.anywhere.menuProxy,
					success: function(data) {
						alert(data.length);
						alert("success calling proxy");
						if(data.match(/ipp_unscheduled_maintenance/) || data.match(/ipp_scheduled_maintenance/) ) {
							intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownInner").html('<span class="intuitPlatformAppMenuDropdownHeader">We are sorry, but we cannot load the menu right now.</span>');
						} else {
							alert("populating app menu start");
							alert(data.length);
							intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownInner").html(data);
							alert("populating app menu complete");
						}
						intuit.ipp.jQuery("#intuitPlatformAppMenuDropdown").show();
						intuit.ipp.jQuery("#intuitPlatformAppMenuLogo").addClass("opened");
						intuit.ipp.anywhere.view.blueDot.setupScroll();
						intuit.ipp.jQuery(window).resize(intuit.ipp.anywhere.view.blueDot.setupScroll);
					},
					error: function(jqXHR, textStatus, errorThrown) {
						if(typeof console !== "undefined" && typeof console.log !== "undefined") {
							if(jqXHR.status == 404) {
								console.log("IPP: App Menu Proxy URL is incorrect");
							} else {
								console.log("IPP: App Menu Proxy returns " + jqXHR.status + " status code");
							}
						}
						intuit.ipp.jQuery("#intuitPlatformAppMenuDropdownInner").html('<span class="intuitPlatformAppMenuDropdownHeader">We are sorry, but we cannot load the menu right now.</span>');
					}
				});
			} else {
				if(typeof console !== "undefined" && typeof console.log !== "undefined") {
					console.log("IPP: Missing menu proxy");
				}
			}
			event.preventDefault();
			return false;
		}
	}
};

intuit.ipp.anywhere.service = {
	timeout : 5000,

	openPopupWindow : function(wind) {
		var url = "https://" + intuit.ipp.anywhere.serviceHost + wind.path;
		window.open(url, "ippPopupWindow", "location=1,width=400,height=300");
	},

	openExternalPopupWindow : function (wind) {
		parameters = "location=1";
		if(wind.height == null && wind.width == null) {
			wind.height = 630;
			wind.width = 703;
		}
		parameters += ",width=" + wind.width + ",height=" + wind.height;
		if(wind.centered) {
			parameters += ",left=" + (screen.width - wind.width)/2 + ",top=" + (screen.height - wind.height)/2;
		}
		window.open(wind.url, "ippPopupWindow", parameters);
	}
};

intuit.ipp.anywhere.tracking = {
	server: function () {
		return "https://" + intuit.ipp.anywhere.serviceHost + "/trackapi/TrackingActions"
	},

	getPageName: function () {
		return document.title;
	},

	setup: function () {
		// include google analytics here
		if(typeof window._gaq === "undefined" || typeof window._gaq._getAsyncTracker !== "function") {
			window._gaq = [];
			(function() {
				var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			})();
		}
		window._gaq.push(['intuitIppAnywhere._setAccount', 'UA-9225335-1']);
		if(intuit.ipp.anywhere.developerGaTrackerId) {
			setupDeveloperGA();
		}
	},

	setupDeveloperGA: function() {
		if(!intuit.ipp.anywhere.developerGaTrackerInitiated) {
			window._gaq.push(['intuitIppAnywhereDeveloper._setAccount', intuit.ipp.anywhere.developerGaTrackerId]);
			intuit.ipp.anywhere.developerGaTrackerInitiated = true;
		}
	},

	trackEvent : function(elementName, action, elementText, eventType) {
		//TODO determine exact strings we want to be sending to ga & splunk
		// splunk
		var eventData = { pn: intuit.ipp.anywhere.tracking.getPageName(), nm: "ia" + elementName, typ: action, txt: elementText, et: eventType, ref: document.referrer, url: document.URL };
		intuit.ipp.jQuery.getJSON(intuit.ipp.anywhere.tracking.server() + "?remote=true&" + intuit.ipp.anywhere.tracking.util.arrayToURL(eventData) + "&callback=?");
		// ga
		// we want our strings to look like /Process=IA&Action=&UserAction=
		var gaText = "/Process=IA&ControlName=" + encodeURIComponent(elementName) + "&Action=" + encodeURIComponent(action) + "&UserAction=" + encodeURIComponent(eventType.replace(/^\w/, function($0) { return $0.toUpperCase(); }));
		window._gaq.push(['intuitIppAnywhere._trackPageview', gaText]);
		if(intuit.ipp.anywhere.developerGaTrackerId) {
			setupDeveloperGA();
			window._gaq.push(['intuitIppAnywhereDeveloper._trackPageview', gaText]);
		}
	},

	util: {
		arrayToURL: function(array) {
			var pairs = [];
			for (var key in array) {
				if (array.hasOwnProperty(key)) {
					pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(array[key]));
				}
			}
			return pairs.join('&');
		}
	}
};

// function that starts it all. timeout is 0
window.setTimeout(function () {
	// these are the domains whose js files we're going to look at
	// intuit.ipp.ourDomain = /(.intuit.com).*?#(.*)/;
	intuit.ipp.ourDomain = /intuit.com$/;
	if(window.jQuery === undefined || !window.jQuery().delegate) {
		// minimum version 1.4.2
		var script_tag = document.createElement('script');
		script_tag.setAttribute("type","text/javascript");
		alert("inside setTimeout calling jquery");
		script_tag.setAttribute("src", "https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js");
		script_tag.onload = function () { 
			if(window.jQuery) {
				intuit.ipp.jQuery = window.jQuery.noConflict(true);
				intuit.ipp.anywhere.windowLoad();
			}
		};
		script_tag.onreadystatechange = function () { // Same thing but for IE
			if (this.readyState == 'complete' || this.readyState == 'loaded') {
				script_tag.onload();
			}
		};

		// Try to find the head, otherwise default to the documentElement
		(document.getElementsByTagName("head")[0] || document.documentElement).appendChild(script_tag);

	} else {
		// we do have jquery
		intuit.ipp.jQuery = window.jQuery;
		intuit.ipp.anywhere.windowLoad();
	}
});