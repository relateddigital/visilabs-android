//TODO: Arayüzde tanımlanan "Consent Text Not Checked Message" json'da gelmiyor.

function SpinToWin(config) {

    this.wheelCanvas = {};
    this.arrowCanvas = {};
    this.wheelCanvasContext = {};
    this.arrowCanvasContext = {};
    this.submitButton = {};
    this.closeButton = {};
    this.emailInput = {};

    var r = parseFloat(config.actiondata.circle_R);
    this.config = config;
    this.config = config;
    this.config.language = config.actiondata.language;
    this.config.windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    this.config.r = (r * 2) > this.config.windowWidth ? 150 : r;
    this.config.centerX = this.config.r;
    this.config.centerY = this.config.r;
    this.config.auth = config.actiondata.auth;
    this.config.promoAuth = config.actiondata.promoAuth;
    this.config.sliceCount = config.actiondata.slice_count;
    this.config.campaigns = config.actiondata.slices;
    this.config.mailFormEnabled = config.actiondata.mail_subscription;
    this.config.htmlContent = config.actiondata.spin_to_win_content;
    this.config.pickedColors = [];
    this.config.middleCircleR = this.config.r / 10;
    this.config.middleCircleColor = "#000";
    this.config.fontFamily = "sans-serif";
    this.config.font_size = config.actiondata.font_size;
    this.config.isMobile = true;
    this.config.textDirection = "horizontal";
    this.config.angle = 2 * Math.PI / this.config.sliceCount;


    /*Mail*/
    this.config.invalidEmailMessage = "Geçerli bir email adresi yazınız.";
    this.config.checkConsentMessage = "İzin Metnini onaylayınız.";

    if (this.config.htmlContent.invalid_email_message && this.config.htmlContent.invalid_email_message !== "")
        this.config.invalidEmailMessage = this.config.invalid_email_message;

    if (this.config.htmlContent.check_consent_message && this.config.htmlContent.check_consent_message !== "")
        this.config.checkConsentMessage = this.config.htmlContent.check_consent_message;

    /*Mail*/

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

    this.colorsHandler();
    this.textsHandler();
    this.createHtmlElements();
    this.styleHandler();
    //TODO: buna gerek olmayabilir, webview'de resize ihtimali var mı? kalmasında da zarar yok.
    window.onresize = function () {
        window.spinToWin.styleHandler();
    }

    this.wheelCanvasContext = this.wheelCanvas.getContext("2d");
    this.arrowCanvasContext = this.arrowCanvas.getContext("2d");
    this.drawArrow();


    for (var i = 0; i < config.sliceCount; i++) {
        this.sliceDrawer(i, config.colors[i]);
        this.mobileTextTyper(i, config.slices[i].displayName, config.colors[i]);
    }
    this.midCircleDrawer(config.middleCircleColor, config.middleCircleR);

    window.spinToWin = this;
}


//TODO: randomNumber çalışıyor mu bak?
SpinToWin.prototype.colorsHandler = function() {
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

SpinToWin.prototype.textsHandler = function() {
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

SpinToWin.prototype.styleHandler = function() {
    var arrowContainerTop = (config.r - (config.r /5.2)) + "px"; // R: 250 top: 205, R: 200 top: 162, R: 150 top: 121
    var styleEl = document.createElement("style"),
        titleStyle = config.htmlContent.title_style,
        textStyle = config.htmlContent.message_style,
        buttonStyle = config.htmlContent.button_style,
        containerStyle = config.htmlContent.container_style,
        inputStyle = config.htmlContent.input_style,
        consentTextContainerStyle = config.htmlContent.consent_text_container_style,
        validationMessageStyle = config.htmlContent.validation_message_style,
        successMessageStyle = config.htmlContent.success_message_style,
        styleString = "#lightbox-outer{}" +
            "#container{" + containerStyle + ";z-index:1000;border-radius: 10px;position:fixed;width:" + (config.r + 300) + "px;height:" + ((2 * config.r) + 40) + "px;top:calc(50% - " + ((2 * config.r) + 40) / 2 + "px);left:calc(50% - " + (config.r + 150) / 2 + "px);box-sizing:border-box}" +
            "#canvas-container{float:left;width:" + config.r + "px;height:" + (2 * config.r) + "px}" +
            "#wheel-container{float:left;height:" + (2 * config.r) + "px;width:" + config.r + "px;margin:20px 0;overflow:hidden}" +
            "#arrow-container{width:20px;height:30px;display:inline-block;position:absolute;box-sizing:border-box;top:calc(50% - 14px);left:" + (config.r - 10) + "px;z-index:1;transform:rotate(90deg)}" +
            "#form-container{width:300px;box-sizing:border-box;float:right}" +
            "#form-container>div{position:absolute;top:50%;transform:translateY(-50%);margin:0 30px;width: 240px;}" +
            "#form-title{text-align:center;}" +
            "#form-message{text-align:center;}" +
            ".vl-successMessage{text-align:center;}" +
            "#form-title{" + titleStyle + "}" +
            "#form-desc{" + textStyle + "}" +
            "#vl-form-input{" + inputStyle + ";border: 1px solid #ccc;display:block;width:60%;box-sizing:border-box;height:35px;font-size:16px;margin-top:10px;outline-color:#3b99fc;padding-left:5px}" +
            ".validationMessage{" + validationMessageStyle + "}" +
            "#warning{display:none; position: absolute; z-index: 3; background: #fcf6c1; font-size: 12px; border: 1px solid #ccc; top: 105%;width: 100%; box-sizing: border-box;}" +
            "#warning>ul{margin: 2px;padding-inline-start: 20px;}" +
            ".successMessage{" + successMessageStyle + "}" +
            ".consentText-container{" + consentTextContainerStyle + "}" +
            "#form-consent{font-size:12px;color:#555;width:100%;padding:5px 0;position:relative;}" +
            "#form-aggreement-link{color:#555;opacity:.75;text-decoration:none}" +
            "#form-consent input[type='checkbox']{opacity:0;position:absolute}" +
            "#form-consent label{position:relative;display:inline-block;padding-left:18px}" +
            "#form-consent label::before," +
            "#form-consent label::after{position:absolute;content:'';display:inline-block;cursor:pointer}" +
            "#form-consent label::before{height:12px;width:12px;border:1px solid;left:0;top:0}" +
            "#form-consent label::after{height:4px;width:8.5px;border-left:2px solid;border-bottom:2px solid;transform:rotate(-45deg);left:2px;top:2px}" +
            "#form-consent input[type='checkbox']+label::after{content:none}" +
            "#form-consent input[type='checkbox']:checked+label::after{content:''}" +
            "#form-consent input[type='checkbox']:focus+label::before{outline:#3b99fc auto 5px}" +

            "#form-emailpermit{font-size:12px;color:#555;width:100%;padding:5px 0;position:relative;}" +
            "#form-emailpermit input[type='checkbox']{opacity:0;position:absolute}" +
            "#form-emailpermit label{position:relative;display:inline-block;padding-left:18px}" +
            "#form-emailpermit label::before," +
            "#form-emailpermit label::after{position:absolute;content:'';display:inline-block;cursor:pointer}" +
            "#form-emailpermit label::before{height:12px;width:12px;border:1px solid;left:0;top:0}" +
            "#form-emailpermit label::after{height:4px;width:8.5px;border-left:2px solid;border-bottom:2px solid;transform:rotate(-45deg);left:2px;top:2px}" +
            "#form-emailpermit input[type='checkbox']+label::after{content:none}" +
            "#form-emailpermit input[type='checkbox']:checked+label::after{content:''}" +
            "#form-emailpermit input[type='checkbox']:focus+label::before{outline:#3b99fc auto 5px}" +

            ".form-submit-btn{" + buttonStyle + ";transition:.2s filter ease-in-out;}" +
            ".form-submit-btn:hover{filter: brightness(110%);transition:.2s filter ease-in-out;}" +
            ".form-submit-btn.disabled{filter: grayscale(100%);transition:.2s filter ease-in-out;}" +
            "@media only screen and (max-width:2500px){" +
            //"#container{width:100%;top:unset;bottom:-" + config.r + "px;left:unset;height:auto}" +
            "#container{width:100%;top:unset;bottom:-" + config.r + "px;left:0px;height:auto}" +
            "#canvas-container{float:unset;width:100%;text-align:center;position:relative}" +
            "#wheel-container{width:" + (config.r * 2) + "px;margin:0 auto;float:unset;transform:rotate(-90deg)}" +
            "#arrow-container{top:" +  arrowContainerTop + ";transform:rotate(45deg);left:calc(50% - 10px)}" +
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
    var formEl = document.querySelector("#form-container>div"), formHeight = parseFloat(getComputedStyle(formEl).height);
    formEl.parentNode.style.height = formHeight + 450 + "px";

};

SpinToWin.prototype.createHtmlElements = function() {
    var containerDiv = document.getElementById("container");
    var formContainerDiv = this.createForm();
    containerDiv.appendChild(formContainerDiv);
    var wheelContainerDiv = this.createWheel();
    containerDiv.appendChild(wheelContainerDiv);
    document.body.appendChild(containerDiv);
};

SpinToWin.prototype.createForm = function() {
    var formContainerDiv = document.getElementById("form-container");
    var formContainerInnerDiv = document.getElementById("form-container-inner");
    var titleDiv = this.createHtmlElement("div", "form-title", undefined, config.actiondata.spin_to_win_content.title, {fontSize : config.actiondata.font_size + "px"});
    var messageDiv = this.createHtmlElement("div", "form-message", undefined, config.actiondata.spin_to_win_content.message, {fontSize : config.actiondata.font_size + "px"});
    var submitButtonContainerDiv = this.createHtmlElement("div", undefined, "center-container");
    this.submitButton = this.createHtmlElement("input", "form-submit-btn", "form-submit-btn", undefined, undefined, "button", config.actiondata.spin_to_win_content.button_label, evt => this.submit());
    this.closeButton = document.getElementById("spin-to-win-box-close");
    this.closeButton.addEventListener("click", this.close);

    formContainerInnerDiv.appendChild(titleDiv);
    formContainerInnerDiv.appendChild(messageDiv);

    if(config.mailFormEnabled) {
        var emailInputContainerDiv = this.createHtmlElement("div", undefined, "center-container");
        this.emailInput = this.createHtmlElement("input", "vl-form-input", undefined, undefined, undefined, "email", undefined, undefined, this.config.htmlContent.placeholder);
        emailInputContainerDiv.appendChild(this.emailInput);
        formContainerInnerDiv.appendChild(emailInputContainerDiv);
    }




    submitButtonContainerDiv.appendChild(this.submitButton);
    formContainerInnerDiv.appendChild(submitButtonContainerDiv);
    formContainerDiv.appendChild(formContainerInnerDiv);
    return formContainerDiv;
};



//TODO: bunu css'e ekle
SpinToWin.prototype.getWheelCanvasStyle = function() {
    var wheelCanvasStyle = {};
    wheelCanvasStyle.transform = "translateX(0px) rotate(" + this.randomInt(0, 360) + "deg)";
    wheelCanvasStyle.transitionProperty = "transform";
    wheelCanvasStyle.transitionDuration = "0s";
    wheelCanvasStyle.transitionTimingFunction = "ease-out";
    wheelCanvasStyle.transitionDelay = "0s";
    wheelCanvasStyle.borderRadius = "50%";
    return wheelCanvasStyle;
};


SpinToWin.prototype.drawArrow = function() {
    this.arrowCanvasContext.beginPath();
    this.arrowCanvasContext.moveTo(0, 0);
    this.arrowCanvasContext.lineTo(config.centerX + 100, config.centerY);
    this.arrowCanvasContext.lineTo(config.centerX, config.centerY + 100);
    this.arrowCanvasContext.closePath();
    this.arrowCanvasContext.lineWidth = 30;
    this.arrowCanvasContext.strokeStyle = '#000000';
    this.arrowCanvasContext.stroke();
    this.arrowCanvasContext.fillStyle = "#000000";
    this.arrowCanvasContext.fill();
};

SpinToWin.prototype.sliceDrawer = function(sliceNumber, sliceColor) {
    this.wheelCanvasContext.beginPath();
    this.wheelCanvasContext.fillStyle = sliceColor;
    this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
    this.wheelCanvasContext.arc(config.centerX, config.centerY, config.r, config.angle * sliceNumber,  config.angle * (sliceNumber + 1));
    this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
    this.wheelCanvasContext.fill();
    this.wheelCanvasContext.closePath();
};


SpinToWin.prototype.mobileTextTyper = function(sliceNumber, sliceText, sliceColor) {
    var fontSize = config.font_size;
    this.wheelCanvasContext.save();
    this.wheelCanvasContext.translate(config.centerX + (Math.cos(sliceNumber * config.angle) * config.r), config.centerY + (Math.sin(sliceNumber * config.angle) * config.r));
    this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
    this.wheelCanvasContext.rotate((config.angle * sliceNumber + config.angle / 2) + (Math.PI / 2));
    this.wheelCanvasContext.fillStyle = this.lightOrDark(sliceColor) == "light" ? "#000" : "#fff";
    this.wheelCanvasContext.font = 'bolder ' + fontSize + 'px ' + config.fontFamily;
    var textWidth = this.wheelCanvasContext.measureText(sliceText).width + 10;
    var arcValue = Math.PI * 2 * (config.r - fontSize) / config.sliceCount;
    if (textWidth < arcValue) {
        this.wheelCanvasContext.textAlign = "center";
        sliceText = sliceText.split("|").join(" ");
        this.wheelCanvasContext.fillText(sliceText, Math.PI * (config.r - fontSize) / config.sliceCount, 10)
    }
    else {
        this.wheelCanvasContext.textAlign = "center";
        var lines = sliceText.split("|");
        for (var j = 0; j < lines.length; j++) {
            this.wheelCanvasContext.fillText(lines[j], Math.PI * (config.r) / config.sliceCount, (j * fontSize) + 10);
        }
    }
    this.wheelCanvasContext.restore();
};

SpinToWin.prototype.midCircleDrawer = function(circleColor, circleRadius) {
    this.wheelCanvasContext.beginPath();
    this.wheelCanvasContext.fillStyle = circleColor;
    this.wheelCanvasContext.moveTo(config.centerX, config.centerY);
    this.wheelCanvasContext.arc(config.centerX, config.centerY, circleRadius, 0, 2 * Math.PI);
    this.wheelCanvasContext.fill();
    this.wheelCanvasContext.closePath();
};

SpinToWin.prototype.createWheel = function() {
    var canvasContainerDiv = this.createHtmlElement("div", "canvas-container");
    var arrowContainerDiv = this.createHtmlElement("div", "arrow-container");
    var wheelContainerDiv = this.createHtmlElement("div", "wheel-container");
    this.arrowCanvas = this.createHtmlElement("canvas", "arrow-canvas", undefined, undefined);
    this.wheelCanvas = this.createHtmlElement("canvas", "wheel-canvas", undefined, undefined, this.getWheelCanvasStyle());
    var wheelCanvasWidth  = (config.r * 2), wheelCanvasHeight = (config.r * 2);
    this.wheelCanvas.width = wheelCanvasWidth;
    this.wheelCanvas.height = wheelCanvasHeight;
    arrowContainerDiv.appendChild(this.arrowCanvas);
    wheelContainerDiv.appendChild(this.wheelCanvas);
    canvasContainerDiv.appendChild(arrowContainerDiv);
    canvasContainerDiv.appendChild(wheelContainerDiv);
    return canvasContainerDiv;

};

SpinToWin.prototype.submit = function() {
    if (config.mailFormEnabled) {
        //TODO: burası değişecek
        this.submitButton.removeEventListener("click", this.submit);
        //VisilabsObject.SendClickRequest(27, window.visilabs_spin_to_win.id);
        //VisilabsObject.SendClickRequest(window.visilabs_spin_to_win.acttype, window.visilabs_spin_to_win.id);
        var ga_value = JSON.parse(localStorage.getItem("act-" + this.config.id));
        //VisilabsObject.SendClickToGA(window.visilabs_spin_to_win.id, ga_value);
        this.async_resultGenerator(this).then(function (data) {
            window.spinToWin.spinHandler(data);
        });
    }
    else {
        this.submitButton.removeEventListener("click", this.submit);
        //VisilabsObject.SendClickRequest(27, window.visilabs_spin_to_win.id);
        //VisilabsObject.SendClickRequest(window.visilabs_spin_to_win.acttype, window.visilabs_spin_to_win.id);
        var ga_value = JSON.parse(localStorage.getItem("act-" + this.config.id));
        //VisilabsObject.SendClickToGA(window.visilabs_spin_to_win.id, ga_value);
        this.async_resultGenerator(this).then(function (data) {
            window.spinToWin.spinHandler(data);
        });
    }
};

SpinToWin.prototype.close = function() {
    console.log("close button clicked");
};

SpinToWin.prototype.async_resultGenerator = function(spinToWinObject) {
    var lastResult;
    return new Promise(function (resolve, reject) {
        if (config.promotionSlices.length > 0) {
            var firstResult = config.promotionSlices[spinToWinObject.randomInt(0, config.promotionSlices.length)];
            var promotionCode;

            var prom = undefined; // TODO:

            if (prom !== undefined && prom.success) {
                promotionCode = prom.promocode;
                lastResult = firstResult;
            }
            else {
                lastResult = config.staticCodeSlices[spinToWinObject.randomInt(0, config.staticCodeSlices.length)];
            }


            lastResult = config.staticCodeSlices[spinToWinObject.randomInt(0, config.staticCodeSlices.length)];

            var finalResult = undefined; // TODO:

            var index;
            for (var i = 0; i < config.slices.length; i++) {
                if (config.slices[i] === lastResult) {
                    finalResult = lastResult;
                    if (promotionCode === undefined)
                        config.slices[i].code = spinToWinObject.StringDecrypt(lastResult.code);
                    else
                        config.slices[i].code = promotionCode;
                    index = i;
                    break;
                }
            }
            resolve(index);


            //TODO: visilabs_object.AddParameter ne işe yarıyor bak
            visilabs_object.AddParameter("promoAuth", promoAuth);
            visilabs_object.AddParameter("promotionId", firstResult.code);
            visilabs_object.AddParameter("actionid", actid);
            visilabs_object.GetPromo(function (prom) {
                var promotionCode;
                if (prom !== undefined && prom.success) {
                    promotionCode = prom.promocode;
                    lastResult = firstResult;
                }
                else {
                    lastResult = window.visilabs_spin_to_win.staticCodeSlices[randomInt(0, window.visilabs_spin_to_win.staticCodeSlices.length)];
                }
                var index;
                for (var i = 0; i < window.visilabs_spin_to_win.slices.length; i++) {
                    if (window.visilabs_spin_to_win.slices[i] === lastResult) {
                        finalResult = lastResult;
                        if (promotionCode === undefined)
                            window.visilabs_spin_to_win.slices[i].code = visilabs_object.StringDecrypt(lastResult.code);
                        else
                            window.visilabs_spin_to_win.slices[i].code = promotionCode;
                        index = i;
                        break;
                    }
                }
                resolve(index);
            });

        }
        else {
            lastResult = window.visilabs_spin_to_win.staticCodeSlices[randomInt(0, window.visilabs_spin_to_win.staticCodeSlices.length)];
            var index;
            for (var i = 0; i < window.visilabs_spin_to_win.slices.length; i++) {
                if (window.visilabs_spin_to_win.slices[i] === lastResult) {
                    window.visilabs_spin_to_win.slices[i].code = visilabs_object.StringDecrypt(lastResult.code);
                    index = i;
                    break;
                }
            }
            resolve(index);
        }
    });
};

SpinToWin.prototype.spinHandler = function(result) {
    var spinHandler_R = window.spinToWin.r;
    var spinHandler_isMobile =  true; //window.visilabs_spin_to_win.isMobile;
    var vl_form_input = document.getElementById("vl-form-input");
    if (vl_form_input !== null)
        vl_form_input.setAttribute("disabled", "");
    var vl_form_checkbox = document.getElementById("vl-form-checkbox");
    if (vl_form_checkbox !== null)
        vl_form_checkbox.setAttribute("disabled", "");
    var vl_form_checkbox_emailpermit = document.getElementById("vl-form-checkbox-emailpermit");
    if (vl_form_checkbox_emailpermit !== null)
        vl_form_checkbox_emailpermit.setAttribute("disabled", "");
    var vl_form_submit_btn = document.getElementsByClassName("form-submit-btn"); // document.getElementsByClassName("vl-form-submit-btn");
    if (vl_form_submit_btn !== null)
        vl_form_submit_btn[0].classList.add("disabled");
    var currentAngle = Math.round((parseFloat(this.wheelCanvas.style.transform.split("(")[2]) % 360));
    var sliceDeg = 360 / this.config.sliceCount;
    var startSlice = Math.floor((360 - currentAngle) / sliceDeg);
    var spinCount = this.randomInt(3, 8);
    var spinDeg = (spinCount * 360) + (startSlice - result) * sliceDeg;
    var spinDuration = parseFloat((spinDeg / 360).toFixed(2));
    spinDuration = spinDuration > 7.5 ? 7.5 : spinDuration;
    this.wheelCanvas.style.transform = "translateX(" + (spinHandler_isMobile ? 0 : -spinHandler_R) + "px) rotate(" + (spinDeg + currentAngle) + "deg)";
    this.wheelCanvas.style.transitionDuration = spinDuration + "s";
    setTimeout(function () {
        window.spinToWin.wheelCanvas.style.transform = "translateX(" + (spinHandler_isMobile ? 0 : -spinHandler_R) + "px) rotate(" + (spinDeg + currentAngle) % 360 + "deg)";
        window.spinToWin.wheelCanvas.style.transitionDuration = "0s";
        window.spinToWin.resultHandler(window.spinToWin.config.slices[result]);
    }, spinDuration * 1000);

};

SpinToWin.prototype.resultHandler = function(res) {
    console.log(res);
    var successMessage = "<strong>" + res.displayName.split("|").join(" ") + "</strong> kazandınız. Kupon kodunuz: ";
    var copyToClipboardButtonLabel = "KOPYALA";

    if (window.spinToWin.config.language && window.spinToWin.config.language !== "") {
        switch (window.spinToWin.language) {
            case "En":
                successMessage = "You've won <strong>" + res.displayName.split("|").join(" ") + "</strong>. Your coupon code: ";
                copyToClipboardButtonLabel = "COPY";
                break;
            case "Tr":
                successMessage = "<strong>" + res.displayName.split("|").join(" ") + "</strong> kazandınız. Kupon kodunuz: ";
                copyToClipboardButtonLabel = "KOPYALA";
                break;
            default:
                break;
        }
    }

    if (window.spinToWin.config.htmlContent.success_message && window.spinToWin.config.htmlContent.success_message !== "")
        successMessage = window.spinToWin.config.htmlContent.success_message.replace("<%PromotionDisplayName%>", res.displayName.split("|").join(" "));

    var resultText = "<div class='vl-successMessage' style='width: 100%'>"
        + successMessage
        + "</div>"
        + "<input id='vl_copy_input' type='text' value='" + res.code + "' disabled style='position: absolute; left: -9999px' />"
        + "<div style='text-align:center; padding: 5px; margin: 5px 0; border: 1px solid #ddd; background: #eee; font-weight: 700'>" + res.code + "</div>"
        + "<div class='center-container'>"
        + "<input class='vl-form-submit-btn' type='button' id='vl-copy' value='" + copyToClipboardButtonLabel + "' />"
        + "</div>" ;

    var formEl = document.querySelector("#form-container>div");
    formEl.innerHTML = resultText;
    var clickCopy = document.getElementById("vl-copy");
    clickCopy.addEventListener("click", function () { window.spinToWin.visiCopyTextToClipboard(res.code, window.spinToWin.config.language) }, false);

};


//Helper functions

SpinToWin.prototype.randomInt = function(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
};

SpinToWin.prototype.lightOrDark = function(color) {
    var r, g, b, hsp;
    if (color.indexOf("rgb") != -1) {
        color = color.replace("rgb(" || "rgba(", "");
        color = color.replace(")", "");
        var colorArr = color.split(",");
        r = parseFloat(colorArr[0]);
        g = parseFloat(colorArr[1]);
        b = parseFloat(colorArr[2]);
    }
    else {
        color = +("0x" + color.slice(1).replace(
            color.length < 5 && /./g, '$&$&'));
        r = color >> 16;
        g = color >> 8 & 255;
        b = color & 255;
    }
    hsp = Math.sqrt(
        0.299 * (r * r) +
        0.587 * (g * g) +
        0.114 * (b * b)
    );
    if (hsp > 127.5) {
        return 'light';
    }
    else {
        return 'dark';
    }
};

SpinToWin.prototype.createHtmlElement = function(tag, id, className, innerText, style, type, value, clickListener, placeholder) {
    var element = document.createElement(tag);
    if(id){
        element.id = id;
    }
    if(innerText) {
        element.innerText = innerText;
    }
    if(style) {
        Object.assign(element.style, style);
    }
    if(className) {
        element.className = className;
    }
    if(type) {
        element.type = type;
    }
    if(value) {
        element.value = value;
    }
    if(clickListener) {
        element.addEventListener("click", clickListener);
    }
    if(placeholder) {
        element.placeholder = placeholder;
    }
    return element;
};

SpinToWin.prototype.StringDecrypt = function (a) {
    var b = a.split(","); for (var i = 0; i < b.length; i++)b[i] = String.fromCharCode((0x100 - parseInt(b[i], 0x10))); return b.join("");
};

SpinToWin.prototype.visiCopyTextToClipboard = function(text, language) {

    if (!navigator.clipboard) {
        visiFallbackCopyTextToClipboard(text, language);
        return;
    }

    navigator.clipboard.writeText(text).then(function () {

        var copiedToClipboardMessage = "Panoya kopyalandı.";

        if (language && language !== "") {
            switch (language) {
                case "En":
                    copiedToClipboardMessage = "Copied to clipboard.";
                    break;
                case "Tr":
                    copiedToClipboardMessage = "Panoya kopyalandı.";
                    break;
                //case "De":
                //    break;
                default:
                    break;
            }
        }

        alert(copiedToClipboardMessage);

    }, function (err) {
    });
};




