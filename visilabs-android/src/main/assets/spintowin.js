var PIXEL_RATIO = (function () {
  var ctx = document.createElement("canvas").getContext("2d"),
    dpr = window.devicePixelRatio || 1,
    bsr = ctx.webkitBackingStorePixelRatio ||
      ctx.mozBackingStorePixelRatio ||
      ctx.msBackingStorePixelRatio ||
      ctx.oBackingStorePixelRatio ||
      ctx.backingStorePixelRatio || 1;

  return dpr / bsr;
})();

var setHiDPICanvas = function (canvas, w, h, ratio) {
  if (!ratio) { ratio = PIXEL_RATIO; }
  var can = canvas;
  can.width = w * ratio;
  can.height = h * ratio;
  can.style.width = w + "px";
  can.style.height = h + "px";
  can.getContext("2d").setTransform(ratio, 0, 0, ratio, 0, 0);
};

function SpinToWin(config) {
  this.config = config;
  if (window.Android || window.BrowserTest) {
    this.convertConfigJson();
  }

  this.container = document.getElementById("container");
  this.canvasContainer = document.getElementById("canvas-container");
  this.wheelCanvas = document.getElementById("wheel-canvas");
  this.arrowCanvas = document.getElementById("arrow-canvas");

  this.wheelCanvasContext = this.wheelCanvas.getContext("2d");
  this.arrowCanvasContext = this.arrowCanvas.getContext("2d");
  this.closeButton = document.getElementById("spin-to-win-box-close");
  this.titleElement = document.getElementById("form-title");
  this.messageElement = document.getElementById("form-message");
  this.submitButton = document.getElementById("form-submit-btn");
  this.emailInput = document.getElementById("vl-form-input");
  this.consentContainer = document.getElementById("vl-form-consent");
  this.emailPermitContainer = document.getElementById("vl-permitform-email");
  this.consentCheckbox = document.getElementById("vl-form-checkbox");
  this.emailPermitCheckbox = document.getElementById("vl-form-checkbox-emailpermit");
  this.consentText = document.getElementById("vl-form-consent-text");
  this.emailPermitText = document.getElementById("vl-permitform-email-text");
  this.couponCode = document.getElementById("coupon-code");
  this.copyButton = document.getElementById("form-copy-btn");
  this.warning = document.getElementById("vl-warning");
  this.invalidEmailMessageLi = document.getElementById("invalid-email-message");
  this.checkConsentMessageLi = document.getElementById("check-consent-message");

  this.successMessageElement = document.getElementById("success-message");
  this.promocodeTitleElement = document.getElementById("promocode-title");

  this.formValidation = {
    email: true,
    consent: true
  };
  this.spinCompleted = false;
  this.config.circle_R = window.innerWidth / 2;
  var r = parseFloat(config.circle_R);
  this.config.windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
  this.config.r = (r * 2) > this.config.windowWidth ? 150 : r;
  this.config.centerX = this.config.r;
  this.config.centerY = this.config.r;
  this.config.selectedPromotionCode = "";
  this.config.selectedSlice = {};

  this.convertStringsToNumber();
  this.setCloseButton();


  this.config.campaigns = this.config.slices;
  this.config.mailFormEnabled = config.mailSubscription;
  this.config.pickedColors = [];
  this.config.middleCircleR = this.config.r / 5;
  this.config.fontFamily = "sans-serif"; // TODO: default gelirse ne yapılacak?
  this.config.isMobile = true;
  this.config.textDirection = "horizontal";
  this.config.angle = 2 * Math.PI / config.sliceCount;



  for (var i = 0; i < this.config.campaigns.length; i++) {
    this.config.pickedColors.push(this.config.campaigns[i].color);
  }

  this.config.slices = [], this.config.colors = [], this.config.promotionSlices = [], this.config.staticCodeSlices = [];

  for (var i = 0; i < this.config.campaigns.length; i++) {
    if (this.config.campaigns[i].type === "promotion") {
      this.config.promotionSlices.push(this.config.campaigns[i]);
    }
  }

  for (var i = 0; i < this.config.campaigns.length; i++) {
    if (this.config.campaigns[i].type === "staticcode") {
      this.config.staticCodeSlices.push(this.config.campaigns[i]);
    }
  }

  this.setContent();

  this.colorsHandler();
  this.textsHandler();
  this.styleHandler();
  //TODO: buna gerek olmayabilir, webview'de resize ihtimali var mı? kalmasında da zarar yok.
  window.onresize = function () {
    window.spinToWin.styleHandler();
  }






  for (var i = 0; i < config.sliceCount; i++) {
    this.sliceDrawer(i, config.colors[i]);
    this.mobileTextTyper(i, config.slices[i].displayName);
  }

  this.midCircleDrawer("#000000", this.config.middleCircleR);

  this.drawArrow();

  this.handleVisibility();

  window.spinToWin = this;
}

SpinToWin.prototype.convertConfigJson = function () {
  //actiondata
  this.config.mailSubscription = this.config.actiondata.mail_subscription;
  this.config.sliceCount = this.config.actiondata.slice_count;
  this.config.slices = this.config.actiondata.slices;

  //spin_to_win_content
  this.config.title = this.config.actiondata.spin_to_win_content.title;
  this.config.message = this.config.actiondata.spin_to_win_content.message;
  this.config.placeholder = this.config.actiondata.spin_to_win_content.placeholder;
  this.config.buttonLabel = this.config.actiondata.spin_to_win_content.button_label;
  this.config.consentText = this.config.actiondata.spin_to_win_content.consent_text;
  this.config.emailPermitText = this.config.actiondata.spin_to_win_content.emailpermit_text;
  this.config.successMessage = this.config.actiondata.spin_to_win_content.success_message;
  this.config.invalidEmailMessage = this.config.actiondata.spin_to_win_content.invalid_email_message;
  this.config.checkConsentMessage = this.config.actiondata.spin_to_win_content.check_consent_message;
  this.config.promocodeTitle = this.config.actiondata.spin_to_win_content.promocode_title;
  this.config.copyButtonLabel = this.config.actiondata.spin_to_win_content.copybutton_label;

  var extendedProps = JSON.parse(decodeURIComponent(this.config.actiondata.ExtendedProps));
  this.config.displaynameTextColor = extendedProps.displayname_text_color;
  this.config.displaynameFontFamily = extendedProps.displayname_font_family;
  this.config.displaynameTextSize = extendedProps.displayname_text_size;
  this.config.titleTextColor = extendedProps.title_text_color;
  this.config.titleFontFamily = extendedProps.title_font_family;
  this.config.titleTextSize = extendedProps.title_text_size;
  this.config.textColor = extendedProps.text_color;
  this.config.textFontFamily = extendedProps.text_font_family;
  this.config.textSize = extendedProps.text_size;
  this.config.buttonColor = extendedProps.button_color;
  this.config.buttonTextColor = extendedProps.button_text_color;
  this.config.buttonFontFamily = extendedProps.button_font_family;
  this.config.buttonTextSize = extendedProps.button_text_size;
  this.config.promocodeTitleTextColor = extendedProps.promocode_title_text_color;
  this.config.promocodeTitleFontFamily = extendedProps.promocode_title_font_family;
  this.config.promocodeTitleTextSize = extendedProps.promocode_title_text_size;
  this.config.promocodeBackgroundColor = extendedProps.promocode_background_color;
  this.config.promocodeTextColor = extendedProps.promocode_text_color;
  this.config.copybuttonColor = extendedProps.copybutton_color;
  this.config.copybuttonTextColor = extendedProps.copybutton_text_color;
  this.config.copybuttonFontFamily = extendedProps.copybutton_font_family;
  this.config.copybuttonTextSize = extendedProps.copybutton_text_size;
  this.config.emailpermitTextSize = extendedProps.emailpermit_text_size;
  this.config.emailpermitTextUrl = extendedProps.emailpermit_text_url;
  this.config.consentTextSize = extendedProps.consent_text_size;
  this.config.consentTextUrl = extendedProps.consent_text_url;
  this.config.closeButtonColor = extendedProps.close_button_color;
  this.config.backgroundColor = extendedProps.background_color;
}

SpinToWin.prototype.getPromotionCode = function () {
  if (window.Android) {
    Android.getPromotionCode();
  } else if (window.webkit && window.webkit.messageHandlers) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "getPromotionCode"
    });
  } else {
    window.BrowserTest.getPromotionCode();
  }
};

SpinToWin.prototype.subscribeEmail = function () {
  if (window.Android) {
    Android.subscribeEmail(this.emailInput.value.trim());
  } else if (window.webkit && window.webkit.messageHandlers) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "subscribeEmail",
      email: this.emailInput.value.trim()
    });
  }
};

SpinToWin.prototype.close = function () {
  if (window.Android) {
    Android.close();
  } else if (window.webkit && window.webkit.messageHandlers) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "close"
    });
  }
};

SpinToWin.prototype.copyToClipboard = function () {
  if (window.Android) {
    Android.copyToClipboard(this.couponCode.innerText);
  } else if (window.webkit.messageHandlers.eventHandler) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "copyToClipboard",
      couponCode: this.couponCode.innerText
    });
  }
};

SpinToWin.prototype.sendReport = function () {
  if (window.Android) {
    Android.sendReport();
  } else if (window.webkit && window.webkit.messageHandlers) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "sendReport"
    });
  }
};

SpinToWin.prototype.openUrl = function (url) {
  if (window.webkit && window.webkit.messageHandlers) {
    window.webkit.messageHandlers.eventHandler.postMessage({
      method: "openUrl",
      url : url
    });
  }
};

SpinToWin.prototype.convertStringsToNumber = function () {
  this.config.displaynameTextSize = isNaN(parseInt(this.config.displaynameTextSize)) ? 10 : parseInt(this.config.displaynameTextSize);
  this.config.titleTextSize = isNaN(parseInt(this.config.titleTextSize)) ? 10 : parseInt(this.config.titleTextSize);
  this.config.textSize = isNaN(parseInt(this.config.textSize)) ? 5 : parseInt(this.config.textSize);
  this.config.buttonTextSize = isNaN(parseInt(this.config.buttonTextSize)) ? 20 : parseInt(this.config.buttonTextSize);
  this.config.consentTextSize = isNaN(parseInt(this.config.consentTextSize)) ? 5 : parseInt(this.config.consentTextSize);
  this.config.copybuttonTextSize = isNaN(parseInt(this.config.copybuttonTextSize)) ? 20 : parseInt(this.config.copybuttonTextSize);
  this.config.promocodeTitleTextSize = isNaN(parseInt(this.config.promocodeTitleTextSize)) ? 20 : parseInt(this.config.promocodeTitleTextSize);
};

SpinToWin.prototype.setContent = function () {
  this.container.style.backgroundColor = this.config.backgroundColor;
  this.titleElement.innerHTML = this.config.title.replace(/\\n/g, '<br/>');
  this.titleElement.style.color = this.config.titleTextColor;
  this.titleElement.style.fontFamily = this.config.titleFontFamily;
  this.titleElement.style.fontSize = (this.config.titleTextSize + 20) + "px";
  this.messageElement.innerHTML = this.config.message.replace(/\\n/g, '<br/>');
  this.messageElement.style.color = this.config.textColor;
  this.messageElement.style.fontFamily = this.config.textFontFamily;
  this.messageElement.style.fontSize = (this.config.textSize + 10) + "px";
  this.submitButton.innerHTML = this.config.buttonLabel;
  this.submitButton.style.color = this.config.buttonTextColor;
  this.submitButton.style.backgroundColor = this.config.buttonColor;
  this.submitButton.style.fontFamily = this.config.buttonFontFamily;
  this.submitButton.style.fontSize = (this.config.buttonTextSize + 20) + "px";
  this.emailInput.placeholder = this.config.placeholder;
  this.consentText.innerHTML = this.prepareCheckboxHtmls(this.config.consentText, this.config.consentTextUrl);
  this.consentText.style.fontSize = (this.config.consentTextSize + 10) + "px";
  this.consentText.style.fontFamily = this.config.textFontFamily;
  this.emailPermitText.innerHTML = this.prepareCheckboxHtmls(this.config.emailPermitText, this.config.emailpermitTextUrl);
  this.emailPermitText.style.fontSize = (this.config.consentTextSize + 10) + "px";
  this.emailPermitText.style.fontFamily = this.config.textFontFamily;
  this.copyButton.innerHTML = this.config.copyButtonLabel;
  this.copyButton.style.color = this.config.copybuttonTextColor;
  this.copyButton.style.backgroundColor = this.config.copybuttonColor;
  this.copyButton.style.fontFamily = this.config.copybuttonFontFamily;
  this.copyButton.style.fontSize = (this.config.copybuttonTextSize + 20) + "px";
  this.invalidEmailMessageLi.innerHTML = this.config.invalidEmailMessage;
  this.invalidEmailMessageLi.style.fontSize = (this.config.consentTextSize + 10) + "px";
  this.invalidEmailMessageLi.style.fontFamily = this.config.textFontFamily;
  this.checkConsentMessageLi.innerHTML = this.config.checkConsentMessage;
  this.checkConsentMessageLi.style.fontSize = (this.config.consentTextSize + 10) + "px";
  this.checkConsentMessageLi.style.fontFamily = this.config.textFontFamily;

  this.couponCode.style.color = this.config.promocodeTextColor;
  this.couponCode.style.backgroundColor = this.config.promocodeBackgroundColor;
  this.couponCode.style.fontFamily = this.config.copybuttonFontFamily;
  this.couponCode.style.fontSize = (this.config.copybuttonTextSize + 20) + "px";

  this.successMessageElement.innerHTML = this.config.successMessage;
  this.successMessageElement.style.color = "green";

  this.promocodeTitleElement.innerHTML = this.config.promocodeTitle.replace(/\\n/g, '<br/>');
  this.promocodeTitleElement.style.color = this.config.promocodeTitleTextColor;
  this.promocodeTitleElement.style.fontFamily = this.config.promocodeTitleFontFamily;
  this.promocodeTitleElement.style.fontSize = (this.config.promocodeTitleTextSize + 20) + "px";

  this.container.addEventListener("click", function(event) {
    if(event.target.tagName != "INPUT") {
      document.activeElement.blur();
    }
  } );

  this.submitButton.addEventListener("click", this.submit);
  this.closeButton.addEventListener("click", evt => this.close());
  this.copyButton.addEventListener("click", evt => this.copyToClipboard());
};

SpinToWin.prototype.validateForm = function () {
  var result = {
    email: true,
    consent: true
  };
  if (!this.validateEmail(this.emailInput.value)) {
    result.email = false;
  }
  if (!this.isNullOrWhitespace(this.consentText.innerText)) {
    result.consent = this.consentCheckbox.checked;
  }
  if (result.consent) {
    if (!this.isNullOrWhitespace(this.emailPermitText.innerText)) {
      result.consent = this.emailPermitCheckbox.checked;
    }
  }
  this.formValidation = result;
  return result;
};

SpinToWin.prototype.handleVisibility = function () {

  if (this.spinCompleted) {
    this.couponCode.style.display = "";
    this.copyButton.style.display = "";
    this.emailInput.style.display = "none";
    this.submitButton.style.display = "none";
    this.consentContainer.style.display = "none";
    this.emailPermitContainer.style.display = "none";
    this.warning.style.display = "none";
    this.successMessageElement.style.display = "";
    this.promocodeTitleElement.style.display = "";
    return;
  } else {
    this.couponCode.style.display = "none";
    this.copyButton.style.display = "none";
    this.successMessageElement.style.display = "none";
    this.promocodeTitleElement.style.display = "none";
  }

  this.warning.style.display = "none";

  if (this.config.mailFormEnabled) {
    if (!this.formValidation.email || !this.formValidation.consent) {
      this.warning.style.display = "";
      if (this.formValidation.email) {
        this.invalidEmailMessageLi.style.display = "none";
      } else {
        this.invalidEmailMessageLi.style.display = "";
      }
      if (this.formValidation.consent) {
        this.checkConsentMessageLi.style.display = "none";
      } else {
        this.checkConsentMessageLi.style.display = "";
      }
    } else {
      this.warning.style.display = "none";
    }

    if (this.isNullOrWhitespace(this.consentText.innerHTML)) {
      this.consentCheckbox.style.display = "none";
      this.consentContainer.style.display = "none";
    }

    if (this.isNullOrWhitespace(this.emailPermitText.innerHTML)) {
      this.emailPermitCheckbox.style.display = "none";
      this.emailPermitContainer.style.display = "none";
    }

  } else {
    this.emailInput.style.display = "none";
    this.consentContainer.style.display = "none";
    this.emailPermitContainer.style.display = "none";
  }
};

//TODO: randomNumber çalışıyor mu bak?
SpinToWin.prototype.colorsHandler = function () {
  if (this.config.pickedColors.length == 0) {
    for (var i = 0; i < this.config.sliceCount; i++) {
      this.config.colors.push("rgb(" + randomInt(0, 256) + "," + randomInt(0, 256) + "," + randomInt(0, 256) + ")");
    }
  } else if (this.config.pickedColors.length <= this.config.sliceCount) {
    for (var i = 0; i < this.config.sliceCount; i++) {
      if (i >= this.config.pickedColors.length) {
        this.config.colors.push(this.config.pickedColors[i % this.config.pickedColors.length]);
      } else {
        this.config.colors.push(this.config.pickedColors[i]);
      }
    }
  }
};

SpinToWin.prototype.textsHandler = function () {
  if (this.config.campaigns.length <= this.config.sliceCount) {
    for (var i = 0; i < this.config.sliceCount; i++) {
      if (i >= this.config.campaigns.length) {
        this.config.slices.push(this.config.campaigns[i % this.config.campaigns.length]);
      } else {
        this.config.slices.push(this.config.campaigns[i]);
      }
    }
  }
};

SpinToWin.prototype.styleHandler = function () {

  var wheelCanvasWidth = (config.r * 2),
    wheelCanvasHeight = (config.r * 2);
  this.wheelCanvas.width = wheelCanvasWidth;
  this.wheelCanvas.height = wheelCanvasHeight;
  var wheelCanvasStyle = {};

  setHiDPICanvas(this.wheelCanvas, wheelCanvasWidth, wheelCanvasHeight, PIXEL_RATIO);
  setHiDPICanvas(this.arrowCanvas, wheelCanvasWidth, wheelCanvasHeight, PIXEL_RATIO);


  wheelCanvasStyle.transform = "translateX(0px) rotate(" + this.randomInt(0, 360) + "deg)";
  wheelCanvasStyle.transitionProperty = "transform";
  wheelCanvasStyle.transitionDuration = "0s";
  wheelCanvasStyle.transitionTimingFunction = "ease-out";
  wheelCanvasStyle.transitionDelay = "0s";
  wheelCanvasStyle.borderRadius = "50%";
  Object.assign(this.wheelCanvas.style, wheelCanvasStyle);


  this.canvasContainer.style.position = "absolute";
  this.canvasContainer.style.bottom = (-wheelCanvasHeight / 2) + "px";

  var styleEl = document.createElement("style"),
    styleString = "#canvas-container{float:left;width:" + config.r + "px;height:" + (2 * config.r) + "px}" +
      "#arrow-container{float:left;height:" + (2 * config.r) + "px;width:" + config.r + "px;margin:20px 0;overflow:hidden}" +
      "#wheel-container{float:left;height:" + (2 * config.r) + "px;width:" + config.r + "px;margin:20px 0;overflow:hidden}" +
      "#form-container{width:300px;box-sizing:border-box;float:right}" +
      "#form-container>div{position:absolute;top:50%;transform:translateY(-50%);margin:0 30px;width: 240px;}" +
      "#form-title, #form-message, #success-message, #promocode-title{text-align:center;}" +
      "#warning{display:none; position: absolute; z-index: 3; background: #fcf6c1; font-size: 12px; border: 1px solid #ccc; top: 105%;width: 100%; box-sizing: border-box;}" +
      "#warning>ul{margin: 2px;padding-inline-start: 20px;}" +
      "#form-consent{font-size:12px;color:#555;width:100%;padding:5px 0;position:relative;}" +
      "#form-consent input[type='checkbox']{opacity:0;position:absolute}" +
      "#form-consent label{position:relative;display:inline-block;padding-left:18px}" +
      "#form-consent label::before," +
      "#form-consent label::after{position:absolute;content:'';display:inline-block;cursor:pointer}" +
      "#form-consent label::before{height:12px;width:12px;border:1px solid;left:0;top:0}" +
      "#form-consent label::after{height:4px;width:8.5px;border-left:2px solid;border-bottom:2px solid;transform:rotate(-45deg);left:2px;top:2px}" +
      "#form-consent input[type='checkbox']+label::after{content:none}" +
      "#form-consent input[type='checkbox']:checked+label::after{content:''}" +
      "#form-consent input[type='checkbox']:focus+label::before{outline:#3b99fc auto 5px}" +
      ".form-submit-btn{transition:.2s filter ease-in-out;}" +
      ".form-submit-btn:hover{filter: brightness(110%);transition:.2s filter ease-in-out;}" +
      ".form-submit-btn.disabled{filter: grayscale(100%);transition:.2s filter ease-in-out;}" +
      "@media only screen and (max-width:2500px){" +
      "#canvas-container{float:unset;width:100%;text-align:center;position:relative}" +
      "#wheel-container{width:" + (config.r * 2) + "px;margin:0 auto;float:unset;transform:rotate(-90deg)}" +
      "#arrow-container{width:" + (config.r * 2) + "px;margin:0 auto;float:unset;}" +
      "#form-container{float:unset;width:100%;}" +
      "#form-container>div{transform:unset;top:unset;margin:20px;width:calc(100% - 40px)}" +
      "}";

  styleEl.id = "vl-styles";
  if (!document.getElementById("vl-styles")) {
    styleEl.innerHTML = styleString;
    document.head.appendChild(styleEl);
  } else {
    document.getElementById("vl-styles").innerHTML = styleString;
  }
};

SpinToWin.prototype.drawArrow = function () {
  this.arrowCanvasContext.fillStyle = '#000000';
  this.arrowCanvasContext.beginPath();
  console.log(config.centerX);
  console.log(config.centerY);
  var triangleHeight = config.centerY / 3;
  var triangleHalfBase = config.centerY / 7;
  this.arrowCanvasContext.moveTo(config.centerX - triangleHalfBase, config.centerY);
  this.arrowCanvasContext.lineTo(config.centerX, config.centerY - triangleHeight);
  this.arrowCanvasContext.lineTo(config.centerX + triangleHalfBase, config.centerY);
  this.arrowCanvasContext.lineTo(config.centerX - triangleHalfBase, config.centerY);
  this.arrowCanvasContext.closePath();
  this.arrowCanvasContext.stroke();
  this.arrowCanvasContext.fill();
};

SpinToWin.prototype.sliceDrawer = function (sliceNumber, sliceColor) {
  this.wheelCanvasContext.beginPath();
  this.wheelCanvasContext.fillStyle = sliceColor;
  this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
  this.wheelCanvasContext.arc(config.centerX, config.centerY, config.r, config.angle * sliceNumber, config.angle * (sliceNumber + 1));
  this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
  this.wheelCanvasContext.fill();
  this.wheelCanvasContext.closePath();
};


SpinToWin.prototype.mobileTextTyper = function (sliceNumber, sliceText) {
  var fontSize = this.config.displaynameTextSize + 10;
  var fontColor = this.config.displaynameTextColor;
  var fontFamily = this.config.displaynameFontFamily;
  this.wheelCanvasContext.save();
  this.wheelCanvasContext.translate(config.centerX + (Math.cos(sliceNumber * config.angle) * config.r), config.centerY + (Math.sin(sliceNumber * config.angle) * config.r));
  this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
  this.wheelCanvasContext.rotate((config.angle * sliceNumber + config.angle / 2) + (Math.PI / 2));
  this.wheelCanvasContext.fillStyle = fontColor;
  this.wheelCanvasContext.font = 'bolder ' + fontSize + 'px ' + fontFamily;
  var textWidth = this.wheelCanvasContext.measureText(sliceText).width + 10;
  var arcValue = Math.PI * 2 * (config.r - fontSize) / config.sliceCount;
  if (textWidth < arcValue) {
    this.wheelCanvasContext.textAlign = "center";
    sliceText = sliceText.split("|").join(" ");
    this.wheelCanvasContext.fillText(sliceText, Math.PI * (config.r - fontSize) / config.sliceCount, 20)
  } else {
    this.wheelCanvasContext.textAlign = "center";
    var lines = sliceText.split("|");
    for (var j = 0; j < lines.length; j++) {
      this.wheelCanvasContext.fillText(lines[j], Math.PI * (config.r) / config.sliceCount, (j * fontSize) + 20);
    }
  }
  this.wheelCanvasContext.restore();
};

SpinToWin.prototype.midCircleDrawer = function (circleColor, circleRadius) {
  this.wheelCanvasContext.beginPath();
  this.wheelCanvasContext.fillStyle = circleColor;
  this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
  this.wheelCanvasContext.arc(config.centerX, config.centerY, circleRadius, 0, 2 * Math.PI);
  this.wheelCanvasContext.fill();
  this.wheelCanvasContext.closePath();
};

SpinToWin.prototype.submit = function () {
  if (config.mailFormEnabled) {
    this.formValidation = window.spinToWin.validateForm();
    if (!window.spinToWin.formValidation.email || !window.spinToWin.formValidation.consent) {
      window.spinToWin.handleVisibility();
      return;
    }
    window.spinToWin.subscribeEmail();
  }
  window.spinToWin.sendReport();
  window.spinToWin.handleVisibility();
  window.spinToWin.submitButton.removeEventListener("click", window.spinToWin.submit);
  window.spinToWin.getPromotionCode();
};

SpinToWin.prototype.spin = function (sliceIndex, promotionCode) {
  if (sliceIndex > -1) {
    window.spinToWin.config.selectedSlice = window.spinToWin.config.slices[sliceIndex];
    window.spinToWin.config.selectedSlice.code = promotionCode;
  } else {
    sliceIndex = window.spinToWin.randomInt(0, window.spinToWin.config.staticCodeSlices.length);
    window.spinToWin.config.selectedSlice = window.spinToWin.config.staticCodeSlices[window.spinToWin.randomInt(0, window.spinToWin.config.staticCodeSlices.length)];
  }
  window.spinToWin.spinHandler(sliceIndex);
};

SpinToWin.prototype.spinHandler = function (result) {
  var vl_form_input = document.getElementById("vl-form-input");
  if (vl_form_input !== null)
    vl_form_input.setAttribute("disabled", "");
  var vl_form_checkbox = document.getElementById("vl-form-checkbox");
  if (vl_form_checkbox !== null)
    vl_form_checkbox.setAttribute("disabled", "");
  var vl_form_checkbox_emailpermit = document.getElementById("vl-form-checkbox-emailpermit");
  if (vl_form_checkbox_emailpermit !== null)
    vl_form_checkbox_emailpermit.setAttribute("disabled", "");
  var vl_form_submit_btn = document.getElementsByClassName("form-submit-btn");
  if (vl_form_submit_btn !== null)
    vl_form_submit_btn[0].classList.add("disabled");
  var currentAngle = Math.round((parseFloat(this.wheelCanvas.style.transform.split("(")[2]) % 360));
  var sliceDeg = 360 / this.config.sliceCount;
  var startSlice = Math.floor((360 - currentAngle) / sliceDeg);
  var spinCount = this.randomInt(3, 8);
  var spinDeg = (spinCount * 360) + (startSlice - result) * sliceDeg;
  var spinDuration = parseFloat((spinDeg / 360).toFixed(2));
  spinDuration = spinDuration > 7.5 ? 7.5 : spinDuration;
  this.wheelCanvas.style.transform = "translateX(0px) rotate(" + (spinDeg + currentAngle) + "deg)";
  this.wheelCanvas.style.transitionDuration = spinDuration + "s";
  setTimeout(function () {
    window.spinToWin.wheelCanvas.style.transform = "translateX(0px) rotate(" + (spinDeg + currentAngle) % 360 + "deg)";
    window.spinToWin.wheelCanvas.style.transitionDuration = "0s";
    window.spinToWin.resultHandler(window.spinToWin.config.slices[result]);
  }, spinDuration * 1000);
};

SpinToWin.prototype.resultHandler = function (res) {
  this.spinCompleted = true;
  this.couponCode.innerText = res.code;
  this.couponCode.value = res.code;
  this.handleVisibility();
};


//Helper functions

SpinToWin.prototype.prepareCheckboxHtmls = function (text, url) {
  if (this.isNullOrWhitespace(text)) {
    return "";
  }
  else if (this.isNullOrWhitespace(url)) {
    return text.replaceAll('<LINK>', '').replaceAll('</LINK>', '');
  }
  else if (!text.includes("<LINK>")) {
    if (window.webkit && window.webkit.messageHandlers.eventHandler) {
      return '<a href="javascript:window.spinToWin.openUrl(\'' + url + '\')">' + text +  '</a>';
    } else {
      return '<a href="' + url + '">' + text + '</a>';
    }
  } else {
    var linkRegex = /<LINK>(.*?)<\/LINK>/g;
    var regexResult;
    while((regexResult = linkRegex.exec(text)) !== null) {
      var outerHtml = regexResult[0];
      var innerHtml = regexResult[1];
      if (window.webkit && window.webkit.messageHandlers.eventHandler) {
        var link = '<a href="javascript:window.spinToWin.openUrl(\'' + url + '\')">' + innerHtml +  '</a>';
        text = text.replace(outerHtml, link);
      } else {
        var link = '<a href="' + url + '">' + innerHtml +  '</a>';
        text = text.replace(outerHtml, link);
      }
    }
    return text;
  }
};

SpinToWin.prototype.randomInt = function (min, max) {
  return Math.floor(Math.random() * (max - min) + min);
};

SpinToWin.prototype.isNullOrWhitespace = function (input) {
  if (typeof input === 'undefined' || input == null) return true;
  return input.replace(/\s/g, '').length < 1;
};

SpinToWin.prototype.validateEmail = function (email) {
  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(email);
};

SpinToWin.prototype.setCloseButton = function () {
  if (this.config.closeButtonColor == "black") {
    this.closeButton.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABvUlEQVRoQ+2Z4U0DMQyFfYsxRIEN2AomQEjMgBisKG0jRac0sf2ec1ZF//Qkkvh9fo4vDZtcP08i8nN7Ll9b85zx8dyIKtp/i+BXEfnsqM0K00JU2S9FbO8PdUA2mLtaZyCZymyU8MteGA64WXO0M1ONVeB04IENQKWtzbRqwuIWpta0Lxn1xAVAJi292jctEARk1nBvE5sXIgK5Yo+6kWtBEMgdc9ZW3Qs7gKBYM5CiBwqgBIJjaECiYWAI6/GDEnDnEG1NrSM1Pi0wu2StIKwyYybkkmQPCApDh0BAvDAhECiIFSYMggGihdG8TrxlDu2RvTBNtkcwEATLEUtr7sHAEGwQT5lRICJALDA0iH+Qwa61bnqaK7SFlGen9Jvd6sQeCE4ovADgBBUGBdE4seQSEAGxQFhemi5NrknKclp61eQB8TjhOZuZtJkGg06EwlhAGE6EwWhBIiCoDUADEglBg5mBrICgwDz8JfZKJygN4GH/0XOkE5AzrSOZIMwNYMnJVHOpBf7q3ApIRifMZaYBmb1rwISrpw8TPgPJAjHdM0Xou4i8dfKSDWIE81HFfonIcwOTFaIH8y0ipz/jH10bOlDCXQAAAABJRU5ErkJggg==";
  } else {
    this.closeButton.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABwklEQVRoQ+2aa24CMQyE7QuUM7YHK2csF0i1qJF2A0lszxiiVfkDEnnM52fCoiIipZRvEfncPm8vVdX6ecX3UkrZ6bqq6pe2EKvDNBBV7nUD2dMdHLCaZ4ZaR1+uFGZTnbMBK8CYNP4leze83p0zJoh9DlgnvLKKeTQdyqxnYjaQV8tDv/AukAEU0fC08UUWYgFF9+528OiCCBCy5/AogizsBUL3mp6p0A0sQIw9piDZfYYBcW/aFotlwbAgXCBsGCaEG4QFw4YIgaAwGRBhkChMFgQE4oXJhIBBrDCWyojeRs3ldyTGYu3RfBSC4pEqMArDgKCCRMKMBUEH8cAwIf5BeonrzROmVyhVyxNSrRFYMBQQrycyYGAQFKJCoZ6BQCwQVaBnrOUk8ODVyCRrTrRWzoQJeQQRhMwdHnO8HmEIYawBhRZTAHMtV2dnbxzNs14EmXIkA8JzaraU5ilIJgQT5vw/mb7CE228I3ue97ECYhVvT0KuA20BON+jtxU8geTM3SMrQnhL8/AvHKy7Apo7JkPPBlm6KirUMn+qczRgFQhLmG2h9SMiHxn3aIulvWM6hr/VZD/ArOaJSTW7qerlF9bSa7Pl7TDpAAAAAElFTkSuQmCC";
  }
};