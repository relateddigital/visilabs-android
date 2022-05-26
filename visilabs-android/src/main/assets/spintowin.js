!function(e,t){"use strict";"object"==typeof module&&"object"==typeof module.exports?module.exports=e.document?t(e,!0):function(e){if(!e.document)throw new Error("jQuery requires a window with a document");return t(e)}:t(e)}("undefined"!=typeof window?window:this,(function(e,t){"use strict";var n=[],r=Object.getPrototypeOf,i=n.slice,o=n.flat?function(e){return n.flat.call(e)}:function(e){return n.concat.apply([],e)},s=n.push,a=n.indexOf,u={},l=u.toString,c=u.hasOwnProperty,f=c.toString,p=f.call(Object),d={},h=function(e){return"function"==typeof e&&"number"!=typeof e.nodeType&&"function"!=typeof e.item},g=function(e){return null!=e&&e===e.window},v=e.document,m={type:!0,src:!0,nonce:!0,noModule:!0};function y(e,t,n){var r,i,o=(n=n||v).createElement("script");if(o.text=e,t)for(r in m)(i=t[r]||t.getAttribute&&t.getAttribute(r))&&o.setAttribute(r,i);n.head.appendChild(o).parentNode.removeChild(o)}function x(e){return null==e?e+"":"object"==typeof e||"function"==typeof e?u[l.call(e)]||"object":typeof e}var b="3.6.0",w=function(e,t){return new w.fn.init(e,t)};function T(e){var t=!!e&&"length"in e&&e.length,n=x(e);return!h(e)&&!g(e)&&("array"===n||0===t||"number"==typeof t&&t>0&&t-1 in e)}w.fn=w.prototype={jquery:b,constructor:w,length:0,toArray:function(){return i.call(this)},get:function(e){return null==e?i.call(this):e<0?this[e+this.length]:this[e]},pushStack:function(e){var t=w.merge(this.constructor(),e);return t.prevObject=this,t},each:function(e){return w.each(this,e)},map:function(e){return this.pushStack(w.map(this,(function(t,n){return e.call(t,n,t)})))},slice:function(){return this.pushStack(i.apply(this,arguments))},first:function(){return this.eq(0)},last:function(){return this.eq(-1)},even:function(){return this.pushStack(w.grep(this,(function(e,t){return(t+1)%2})))},odd:function(){return this.pushStack(w.grep(this,(function(e,t){return t%2})))},eq:function(e){var t=this.length,n=+e+(e<0?t:0);return this.pushStack(n>=0&&n<t?[this[n]]:[])},end:function(){return this.prevObject||this.constructor()},push:s,sort:n.sort,splice:n.splice},w.extend=w.fn.extend=function(){var e,t,n,r,i,o,s=arguments[0]||{},a=1,u=arguments.length,l=!1;for("boolean"==typeof s&&(l=s,s=arguments[a]||{},a++),"object"==typeof s||h(s)||(s={}),a===u&&(s=this,a--);a<u;a++)if(null!=(e=arguments[a]))for(t in e)r=e[t],"__proto__"!==t&&s!==r&&(l&&r&&(w.isPlainObject(r)||(i=Array.isArray(r)))?(n=s[t],o=i&&!Array.isArray(n)?[]:i||w.isPlainObject(n)?n:{},i=!1,s[t]=w.extend(l,o,r)):void 0!==r&&(s[t]=r));return s},w.extend({expando:"jQuery"+(b+Math.random()).replace(/\D/g,""),isReady:!0,error:function(e){throw new Error(e)},noop:function(){},isPlainObject:function(e){var t,n;return!(!e||"[object Object]"!==l.call(e))&&(!(t=r(e))||"function"==typeof(n=c.call(t,"constructor")&&t.constructor)&&f.call(n)===p)},isEmptyObject:function(e){var t;for(t in e)return!1;return!0},globalEval:function(e,t,n){y(e,{nonce:t&&t.nonce},n)},each:function(e,t){var n,r=0;if(T(e))for(n=e.length;r<n&&!1!==t.call(e[r],r,e[r]);r++);else for(r in e)if(!1===t.call(e[r],r,e[r]))break;return e},makeArray:function(e,t){var n=t||[];return null!=e&&(T(Object(e))?w.merge(n,"string"==typeof e?[e]:e):s.call(n,e)),n},inArray:function(e,t,n){return null==t?-1:a.call(t,e,n)},merge:function(e,t){for(var n=+t.length,r=0,i=e.length;r<n;r++)e[i++]=t[r];return e.length=i,e},grep:function(e,t,n){for(var r=[],i=0,o=e.length,s=!n;i<o;i++)!t(e[i],i)!==s&&r.push(e[i]);return r},map:function(e,t,n){var r,i,s=0,a=[];if(T(e))for(r=e.length;s<r;s++)null!=(i=t(e[s],s,n))&&a.push(i);else for(s in e)null!=(i=t(e[s],s,n))&&a.push(i);return o(a)},guid:1,support:d}),"function"==typeof Symbol&&(w.fn[Symbol.iterator]=n[Symbol.iterator]),w.each("Boolean Number String Function Array Date RegExp Object Error Symbol".split(" "),(function(e,t){u["[object "+t+"]"]=t.toLowerCase()}));var C=function(e){var t,n,r,i,o,s,a,u,l,c,f,p,d,h,g,v,m,y,x="sizzle"+1*new Date,b=e.document,w=0,T=oe(),C=oe(),E=oe(),k=oe(),A=function(e,t){return e===t&&(c=!0),0},N={}.hasOwnProperty,S=[],D=(S.pop,S.push),q=S.push,L=S.slice,j=function(e,t){for(var n=0,r=e.length;n<r;n++)if(e[n]===t)return n;return-1},H="checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",O="[\\x20\\t\\r\\n\\f]",P="(?:\\\\[\\da-fA-F]{1,6}[\\x20\\t\\r\\n\\f]?|\\\\[^\\r\\n\\f]|[\\w-]|[^\0-\\x7f])+",I="\\[[\\x20\\t\\r\\n\\f]*("+P+")(?:"+O+"*([*^$|!~]?=)"+O+"*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|("+P+"))|)"+O+"*\\]",R=":("+P+")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|"+I+")*)|.*)\\)|)",W=new RegExp(O+"+","g"),B=new RegExp("^[\\x20\\t\\r\\n\\f]+|((?:^|[^\\\\])(?:\\\\.)*)[\\x20\\t\\r\\n\\f]+$","g"),M=new RegExp("^[\\x20\\t\\r\\n\\f]*,[\\x20\\t\\r\\n\\f]*"),F=new RegExp("^[\\x20\\t\\r\\n\\f]*([>+~]|[\\x20\\t\\r\\n\\f])[\\x20\\t\\r\\n\\f]*"),$=new RegExp(O+"|>"),_=new RegExp(R),z=new RegExp("^"+P+"$"),U={ID:new RegExp("^#("+P+")"),CLASS:new RegExp("^\\.("+P+")"),TAG:new RegExp("^("+P+"|[*])"),ATTR:new RegExp("^"+I),PSEUDO:new RegExp("^"+R),CHILD:new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\([\\x20\\t\\r\\n\\f]*(even|odd|(([+-]|)(\\d*)n|)[\\x20\\t\\r\\n\\f]*(?:([+-]|)[\\x20\\t\\r\\n\\f]*(\\d+)|))[\\x20\\t\\r\\n\\f]*\\)|)","i"),bool:new RegExp("^(?:"+H+")$","i"),needsContext:new RegExp("^[\\x20\\t\\r\\n\\f]*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\([\\x20\\t\\r\\n\\f]*((?:-\\d)?\\d*)[\\x20\\t\\r\\n\\f]*\\)|)(?=[^-]|$)","i")},X=/HTML$/i,V=/^(?:input|select|textarea|button)$/i,Y=/^h\d$/i,Q=/^[^{]+\{\s*\[native \w/,G=/^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,K=/[+~]/,J=new RegExp("\\\\[\\da-fA-F]{1,6}[\\x20\\t\\r\\n\\f]?|\\\\([^\\r\\n\\f])","g"),Z=function(e,t){var n="0x"+e.slice(1)-65536;return t||(n<0?String.fromCharCode(n+65536):String.fromCharCode(n>>10|55296,1023&n|56320))},ee=/([\0-\x1f\x7f]|^-?\d)|^-$|[^\0-\x1f\x7f-\uFFFF\w-]/g,te=function(e,t){return t?"\0"===e?"�":e.slice(0,-1)+"\\"+e.charCodeAt(e.length-1).toString(16)+" ":"\\"+e},ne=function(){f()},re=function(e,t,n){var r=t.dir,i=t.next,o=i||r,s=n&&"parentNode"===o,a=w++;return t.first?function(t,n,i){for(;t=t[r];)if(1===t.nodeType||s)return e(t,n,i);return!1}:function(t,n,u){var l,c,f,p=[0,a];if(u){for(;t=t[r];)if((1===t.nodeType||s)&&e(t,n,u))return!0}else for(;t=t[r];)if(1===t.nodeType||s)if(c=(f=t[x]||(t[x]={}))[t.uniqueID]||(f[t.uniqueID]={}),i&&i===t.nodeName.toLowerCase())t=t[r]||t;else{if((l=c[o])&&0===l[0]&&l[1]===a)return p[2]=l[2];if(c[o]=p,p[2]=e(t,n,u))return!0}return!1}}((function(e){return!0===e.disabled&&"fieldset"===e.nodeName.toLowerCase()}),{dir:"parentNode",next:"legend"});try{q.apply(S=L.call(b.childNodes),b.childNodes),S[b.childNodes.length].nodeType}catch(e){q={apply:S.length?function(e,t){D.apply(e,L.call(t))}:function(e,t){for(var n=e.length,r=0;e[n++]=t[r++];);e.length=n-1}}}function ie(e,t,r,i){var o,a,l,c,d,v,m,b=t&&t.ownerDocument,w=t?t.nodeType:9;if(r=r||[],"string"!=typeof e||!e||1!==w&&9!==w&&11!==w)return r;if(!i&&(f(t),t=t||p,h)){if(11!==w&&(d=G.exec(e)))if(o=d[1]){if(9===w){if(!(l=t.getElementById(o)))return r;if(l.id===o)return r.push(l),r}else if(b&&(l=b.getElementById(o))&&y(t,l)&&l.id===o)return r.push(l),r}else{if(d[2])return q.apply(r,t.getElementsByTagName(e)),r;if((o=d[3])&&n.getElementsByClassName&&t.getElementsByClassName)return q.apply(r,t.getElementsByClassName(o)),r}if(n.qsa&&!k[e+" "]&&(!g||!g.test(e))&&(1!==w||"object"!==t.nodeName.toLowerCase())){if(m=e,b=t,1===w&&($.test(e)||F.test(e))){for((b=K.test(e)&&he(t.parentNode)||t)===t&&n.scope||((c=t.getAttribute("id"))?c=c.replace(ee,te):t.setAttribute("id",c=x)),a=(v=s(e)).length;a--;)v[a]=(c?"#"+c:":scope")+" "+ve(v[a]);m=v.join(",")}try{return q.apply(r,b.querySelectorAll(m)),r}catch(t){k(e,!0)}finally{c===x&&t.removeAttribute("id")}}}return u(e.replace(B,"$1"),t,r,i)}function oe(){var e=[];return function t(n,i){return e.push(n+" ")>r.cacheLength&&delete t[e.shift()],t[n+" "]=i}}function se(e){return e[x]=!0,e}function ae(e){var t=p.createElement("fieldset");try{return!!e(t)}catch(e){return!1}finally{t.parentNode&&t.parentNode.removeChild(t),t=null}}function ue(e,t){for(var n=e.split("|"),i=n.length;i--;)r.attrHandle[n[i]]=t}function le(e,t){var n=t&&e,r=n&&1===e.nodeType&&1===t.nodeType&&e.sourceIndex-t.sourceIndex;if(r)return r;if(n)for(;n=n.nextSibling;)if(n===t)return-1;return e?1:-1}function ce(e){return function(t){return"input"===t.nodeName.toLowerCase()&&t.type===e}}function fe(e){return function(t){var n=t.nodeName.toLowerCase();return("input"===n||"button"===n)&&t.type===e}}function pe(e){return function(t){return"form"in t?t.parentNode&&!1===t.disabled?"label"in t?"label"in t.parentNode?t.parentNode.disabled===e:t.disabled===e:t.isDisabled===e||t.isDisabled!==!e&&re(t)===e:t.disabled===e:"label"in t&&t.disabled===e}}function de(e){return se((function(t){return t=+t,se((function(n,r){for(var i,o=e([],n.length,t),s=o.length;s--;)n[i=o[s]]&&(n[i]=!(r[i]=n[i]))}))}))}function he(e){return e&&void 0!==e.getElementsByTagName&&e}for(t in n=ie.support={},o=ie.isXML=function(e){var t=e&&e.namespaceURI,n=e&&(e.ownerDocument||e).documentElement;return!X.test(t||n&&n.nodeName||"HTML")},f=ie.setDocument=function(e){var t,i,s=e?e.ownerDocument||e:b;return s!=p&&9===s.nodeType&&s.documentElement?(d=(p=s).documentElement,h=!o(p),b!=p&&(i=p.defaultView)&&i.top!==i&&(i.addEventListener?i.addEventListener("unload",ne,!1):i.attachEvent&&i.attachEvent("onunload",ne)),n.scope=ae((function(e){return d.appendChild(e).appendChild(p.createElement("div")),void 0!==e.querySelectorAll&&!e.querySelectorAll(":scope fieldset div").length})),n.attributes=ae((function(e){return e.className="i",!e.getAttribute("className")})),n.getElementsByTagName=ae((function(e){return e.appendChild(p.createComment("")),!e.getElementsByTagName("*").length})),n.getElementsByClassName=Q.test(p.getElementsByClassName),n.getById=ae((function(e){return d.appendChild(e).id=x,!p.getElementsByName||!p.getElementsByName(x).length})),n.getById?(r.filter.ID=function(e){var t=e.replace(J,Z);return function(e){return e.getAttribute("id")===t}},r.find.ID=function(e,t){if(void 0!==t.getElementById&&h){var n=t.getElementById(e);return n?[n]:[]}}):(r.filter.ID=function(e){var t=e.replace(J,Z);return function(e){var n=void 0!==e.getAttributeNode&&e.getAttributeNode("id");return n&&n.value===t}},r.find.ID=function(e,t){if(void 0!==t.getElementById&&h){var n,r,i,o=t.getElementById(e);if(o){if((n=o.getAttributeNode("id"))&&n.value===e)return[o];for(i=t.getElementsByName(e),r=0;o=i[r++];)if((n=o.getAttributeNode("id"))&&n.value===e)return[o]}return[]}}),r.find.TAG=n.getElementsByTagName?function(e,t){return void 0!==t.getElementsByTagName?t.getElementsByTagName(e):n.qsa?t.querySelectorAll(e):void 0}:function(e,t){var n,r=[],i=0,o=t.getElementsByTagName(e);if("*"===e){for(;n=o[i++];)1===n.nodeType&&r.push(n);return r}return o},r.find.CLASS=n.getElementsByClassName&&function(e,t){if(void 0!==t.getElementsByClassName&&h)return t.getElementsByClassName(e)},v=[],g=[],(n.qsa=Q.test(p.querySelectorAll))&&(ae((function(e){var t;d.appendChild(e).innerHTML="<a id='"+x+"'></a><select id='"+x+"-\r\\' msallowcapture=''><option selected=''></option></select>",e.querySelectorAll("[msallowcapture^='']").length&&g.push("[*^$]=[\\x20\\t\\r\\n\\f]*(?:''|\"\")"),e.querySelectorAll("[selected]").length||g.push("\\[[\\x20\\t\\r\\n\\f]*(?:value|"+H+")"),e.querySelectorAll("[id~="+x+"-]").length||g.push("~="),(t=p.createElement("input")).setAttribute("name",""),e.appendChild(t),e.querySelectorAll("[name='']").length||g.push("\\[[\\x20\\t\\r\\n\\f]*name[\\x20\\t\\r\\n\\f]*=[\\x20\\t\\r\\n\\f]*(?:''|\"\")"),e.querySelectorAll(":checked").length||g.push(":checked"),e.querySelectorAll("a#"+x+"+*").length||g.push(".#.+[+~]"),e.querySelectorAll("\\\f"),g.push("[\\r\\n\\f]")})),ae((function(e){e.innerHTML="<a href='' disabled='disabled'></a><select disabled='disabled'><option/></select>";var t=p.createElement("input");t.setAttribute("type","hidden"),e.appendChild(t).setAttribute("name","D"),e.querySelectorAll("[name=d]").length&&g.push("name[\\x20\\t\\r\\n\\f]*[*^$|!~]?="),2!==e.querySelectorAll(":enabled").length&&g.push(":enabled",":disabled"),d.appendChild(e).disabled=!0,2!==e.querySelectorAll(":disabled").length&&g.push(":enabled",":disabled"),e.querySelectorAll("*,:x"),g.push(",.*:")}))),(n.matchesSelector=Q.test(m=d.matches||d.webkitMatchesSelector||d.mozMatchesSelector||d.oMatchesSelector||d.msMatchesSelector))&&ae((function(e){n.disconnectedMatch=m.call(e,"*"),m.call(e,"[s!='']:x"),v.push("!=",R)})),g=g.length&&new RegExp(g.join("|")),v=v.length&&new RegExp(v.join("|")),t=Q.test(d.compareDocumentPosition),y=t||Q.test(d.contains)?function(e,t){var n=9===e.nodeType?e.documentElement:e,r=t&&t.parentNode;return e===r||!(!r||1!==r.nodeType||!(n.contains?n.contains(r):e.compareDocumentPosition&&16&e.compareDocumentPosition(r)))}:function(e,t){if(t)for(;t=t.parentNode;)if(t===e)return!0;return!1},A=t?function(e,t){if(e===t)return c=!0,0;var r=!e.compareDocumentPosition-!t.compareDocumentPosition;return r||(1&(r=(e.ownerDocument||e)==(t.ownerDocument||t)?e.compareDocumentPosition(t):1)||!n.sortDetached&&t.compareDocumentPosition(e)===r?e==p||e.ownerDocument==b&&y(b,e)?-1:t==p||t.ownerDocument==b&&y(b,t)?1:l?j(l,e)-j(l,t):0:4&r?-1:1)}:function(e,t){if(e===t)return c=!0,0;var n,r=0,i=e.parentNode,o=t.parentNode,s=[e],a=[t];if(!i||!o)return e==p?-1:t==p?1:i?-1:o?1:l?j(l,e)-j(l,t):0;if(i===o)return le(e,t);for(n=e;n=n.parentNode;)s.unshift(n);for(n=t;n=n.parentNode;)a.unshift(n);for(;s[r]===a[r];)r++;return r?le(s[r],a[r]):s[r]==b?-1:a[r]==b?1:0},p):p},ie.matches=function(e,t){return ie(e,null,null,t)},ie.matchesSelector=function(e,t){if(f(e),n.matchesSelector&&h&&!k[t+" "]&&(!v||!v.test(t))&&(!g||!g.test(t)))try{var r=m.call(e,t);if(r||n.disconnectedMatch||e.document&&11!==e.document.nodeType)return r}catch(e){k(t,!0)}return ie(t,p,null,[e]).length>0},ie.contains=function(e,t){return(e.ownerDocument||e)!=p&&f(e),y(e,t)},ie.attr=function(e,t){(e.ownerDocument||e)!=p&&f(e);var i=r.attrHandle[t.toLowerCase()],o=i&&N.call(r.attrHandle,t.toLowerCase())?i(e,t,!h):void 0;return void 0!==o?o:n.attributes||!h?e.getAttribute(t):(o=e.getAttributeNode(t))&&o.specified?o.value:null},ie.escape=function(e){return(e+"").replace(ee,te)},ie.error=function(e){throw new Error("Syntax error, unrecognized expression: "+e)},ie.uniqueSort=function(e){var t,r=[],i=0,o=0;if(c=!n.detectDuplicates,l=!n.sortStable&&e.slice(0),e.sort(A),c){for(;t=e[o++];)t===e[o]&&(i=r.push(o));for(;i--;)e.splice(r[i],1)}return l=null,e},i=ie.getText=function(e){var t,n="",r=0,o=e.nodeType;if(o){if(1===o||9===o||11===o){if("string"==typeof e.textContent)return e.textContent;for(e=e.firstChild;e;e=e.nextSibling)n+=i(e)}else if(3===o||4===o)return e.nodeValue}else for(;t=e[r++];)n+=i(t);return n},r=ie.selectors={cacheLength:50,createPseudo:se,match:U,attrHandle:{},find:{},relative:{">":{dir:"parentNode",first:!0}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:!0},"~":{dir:"previousSibling"}},preFilter:{ATTR:function(e){return e[1]=e[1].replace(J,Z),e[3]=(e[3]||e[4]||e[5]||"").replace(J,Z),"~="===e[2]&&(e[3]=" "+e[3]+" "),e.slice(0,4)},CHILD:function(e){return e[1]=e[1].toLowerCase(),"nth"===e[1].slice(0,3)?(e[3]||ie.error(e[0]),e[4]=+(e[4]?e[5]+(e[6]||1):2*("even"===e[3]||"odd"===e[3])),e[5]=+(e[7]+e[8]||"odd"===e[3])):e[3]&&ie.error(e[0]),e},PSEUDO:function(e){var t,n=!e[6]&&e[2];return U.CHILD.test(e[0])?null:(e[3]?e[2]=e[4]||e[5]||"":n&&_.test(n)&&(t=s(n,!0))&&(t=n.indexOf(")",n.length-t)-n.length)&&(e[0]=e[0].slice(0,t),e[2]=n.slice(0,t)),e.slice(0,3))}},filter:{TAG:function(e){var t=e.replace(J,Z).toLowerCase();return"*"===e?function(){return!0}:function(e){return e.nodeName&&e.nodeName.toLowerCase()===t}},CLASS:function(e){var t=T[e+" "];return t||(t=new RegExp("(^|[\\x20\\t\\r\\n\\f])"+e+"("+O+"|$)"))&&T(e,(function(e){return t.test("string"==typeof e.className&&e.className||void 0!==e.getAttribute&&e.getAttribute("class")||"")}))},ATTR:function(e,t,n){return function(r){var i=ie.attr(r,e);return null==i?"!="===t:!t||(i+="","="===t?i===n:"!="===t?i!==n:"^="===t?n&&0===i.indexOf(n):"*="===t?n&&i.indexOf(n)>-1:"$="===t?n&&i.slice(-n.length)===n:"~="===t?(" "+i.replace(W," ")+" ").indexOf(n)>-1:"|="===t&&(i===n||i.slice(0,n.length+1)===n+"-"))}},CHILD:function(e,t,n,r,i){var o="nth"!==e.slice(0,3),s="last"!==e.slice(-4),a="of-type"===t;return 1===r&&0===i?function(e){return!!e.parentNode}:function(t,n,u){var l,c,f,p,d,h,g=o!==s?"nextSibling":"previousSibling",v=t.parentNode,m=a&&t.nodeName.toLowerCase(),y=!u&&!a,b=!1;if(v){if(o){for(;g;){for(p=t;p=p[g];)if(a?p.nodeName.toLowerCase()===m:1===p.nodeType)return!1;h=g="only"===e&&!h&&"nextSibling"}return!0}if(h=[s?v.firstChild:v.lastChild],s&&y){for(b=(d=0===(l=(c=(f=(p=v)[x]||(p[x]={}))[p.uniqueID]||(f[p.uniqueID]={}))[e]||[])[0]&&l[1])&&l[2],p=d&&v.childNodes[d];p=++d&&p&&p[g]||(b=d=0)||h.pop();)if(1===p.nodeType&&++b&&p===t){c[e]=[0,d,b];break}}else if(y&&(b=d=0===(l=(c=(f=(p=t)[x]||(p[x]={}))[p.uniqueID]||(f[p.uniqueID]={}))[e]||[])[0]&&l[1]),!1===b)for(;(p=++d&&p&&p[g]||(b=d=0)||h.pop())&&((a?p.nodeName.toLowerCase()!==m:1!==p.nodeType)||!++b||(y&&((c=(f=p[x]||(p[x]={}))[p.uniqueID]||(f[p.uniqueID]={}))[e]=[0,b]),p!==t)););return(b-=i)===r||b%r==0&&b/r>=0}}},PSEUDO:function(e,t){var n,i=r.pseudos[e]||r.setFilters[e.toLowerCase()]||ie.error("unsupported pseudo: "+e);return i[x]?i(t):i.length>1?(n=[e,e,"",t],r.setFilters.hasOwnProperty(e.toLowerCase())?se((function(e,n){for(var r,o=i(e,t),s=o.length;s--;)e[r=j(e,o[s])]=!(n[r]=o[s])})):function(e){return i(e,0,n)}):i}},pseudos:{not:se((function(e){var t=[],n=[],r=a(e.replace(B,"$1"));return r[x]?se((function(e,t,n,i){for(var o,s=r(e,null,i,[]),a=e.length;a--;)(o=s[a])&&(e[a]=!(t[a]=o))})):function(e,i,o){return t[0]=e,r(t,null,o,n),t[0]=null,!n.pop()}})),has:se((function(e){return function(t){return ie(e,t).length>0}})),contains:se((function(e){return e=e.replace(J,Z),function(t){return(t.textContent||i(t)).indexOf(e)>-1}})),lang:se((function(e){return z.test(e||"")||ie.error("unsupported lang: "+e),e=e.replace(J,Z).toLowerCase(),function(t){var n;do{if(n=h?t.lang:t.getAttribute("xml:lang")||t.getAttribute("lang"))return(n=n.toLowerCase())===e||0===n.indexOf(e+"-")}while((t=t.parentNode)&&1===t.nodeType);return!1}})),target:function(t){var n=e.location&&e.location.hash;return n&&n.slice(1)===t.id},root:function(e){return e===d},focus:function(e){return e===p.activeElement&&(!p.hasFocus||p.hasFocus())&&!!(e.type||e.href||~e.tabIndex)},enabled:pe(!1),disabled:pe(!0),checked:function(e){var t=e.nodeName.toLowerCase();return"input"===t&&!!e.checked||"option"===t&&!!e.selected},selected:function(e){return e.parentNode&&e.parentNode.selectedIndex,!0===e.selected},empty:function(e){for(e=e.firstChild;e;e=e.nextSibling)if(e.nodeType<6)return!1;return!0},parent:function(e){return!r.pseudos.empty(e)},header:function(e){return Y.test(e.nodeName)},input:function(e){return V.test(e.nodeName)},button:function(e){var t=e.nodeName.toLowerCase();return"input"===t&&"button"===e.type||"button"===t},text:function(e){var t;return"input"===e.nodeName.toLowerCase()&&"text"===e.type&&(null==(t=e.getAttribute("type"))||"text"===t.toLowerCase())},first:de((function(){return[0]})),last:de((function(e,t){return[t-1]})),eq:de((function(e,t,n){return[n<0?n+t:n]})),even:de((function(e,t){for(var n=0;n<t;n+=2)e.push(n);return e})),odd:de((function(e,t){for(var n=1;n<t;n+=2)e.push(n);return e})),lt:de((function(e,t,n){for(var r=n<0?n+t:n>t?t:n;--r>=0;)e.push(r);return e})),gt:de((function(e,t,n){for(var r=n<0?n+t:n;++r<t;)e.push(r);return e}))}},r.pseudos.nth=r.pseudos.eq,{radio:!0,checkbox:!0,file:!0,password:!0,image:!0})r.pseudos[t]=ce(t);for(t in{submit:!0,reset:!0})r.pseudos[t]=fe(t);function ge(){}function ve(e){for(var t=0,n=e.length,r="";t<n;t++)r+=e[t].value;return r}return ge.prototype=r.filters=r.pseudos,r.setFilters=new ge,s=ie.tokenize=function(e,t){var n,i,o,s,a,u,l,c=C[e+" "];if(c)return t?0:c.slice(0);for(a=e,u=[],l=r.preFilter;a;){for(s in n&&!(i=M.exec(a))||(i&&(a=a.slice(i[0].length)||a),u.push(o=[])),n=!1,(i=F.exec(a))&&(n=i.shift(),o.push({value:n,type:i[0].replace(B," ")}),a=a.slice(n.length)),r.filter)!(i=U[s].exec(a))||l[s]&&!(i=l[s](i))||(n=i.shift(),o.push({value:n,type:s,matches:i}),a=a.slice(n.length));if(!n)break}return t?a.length:a?ie.error(e):C(e,u).slice(0)},a=ie.compile=function(e,t){var n,r=[],i=[],o=E[e+" "];if(!o){for(t||(t=s(e)),n=t.length;n--;)(o=matcherFromTokens(t[n]))[x]?r.push(o):i.push(o);(o=E(e,matcherFromGroupMatchers(i,r))).selector=e}return o},u=ie.select=function(e,t,n,i){var o,u,l,c,f,p="function"==typeof e&&e,d=!i&&s(e=p.selector||e);if(n=n||[],1===d.length){if((u=d[0]=d[0].slice(0)).length>2&&"ID"===(l=u[0]).type&&9===t.nodeType&&h&&r.relative[u[1].type]){if(!(t=(r.find.ID(l.matches[0].replace(J,Z),t)||[])[0]))return n;p&&(t=t.parentNode),e=e.slice(u.shift().value.length)}for(o=U.needsContext.test(e)?0:u.length;o--&&(l=u[o],!r.relative[c=l.type]);)if((f=r.find[c])&&(i=f(l.matches[0].replace(J,Z),K.test(u[0].type)&&he(t.parentNode)||t))){if(u.splice(o,1),!(e=i.length&&ve(u)))return q.apply(n,i),n;break}}return(p||a(e,d))(i,t,!h,n,!t||K.test(e)&&he(t.parentNode)||t),n},n.sortStable=x.split("").sort(A).join("")===x,n.detectDuplicates=!!c,f(),n.sortDetached=ae((function(e){return 1&e.compareDocumentPosition(p.createElement("fieldset"))})),ae((function(e){return e.innerHTML="<a href='#'></a>","#"===e.firstChild.getAttribute("href")}))||ue("type|href|height|width",(function(e,t,n){if(!n)return e.getAttribute(t,"type"===t.toLowerCase()?1:2)})),n.attributes&&ae((function(e){return e.innerHTML="<input/>",e.firstChild.setAttribute("value",""),""===e.firstChild.getAttribute("value")}))||ue("value",(function(e,t,n){if(!n&&"input"===e.nodeName.toLowerCase())return e.defaultValue})),ae((function(e){return null==e.getAttribute("disabled")}))||ue(H,(function(e,t,n){var r;if(!n)return!0===e[t]?t.toLowerCase():(r=e.getAttributeNode(t))&&r.specified?r.value:null})),ie}(e);w.find=C,w.expr=C.selectors,w.expr[":"]=w.expr.pseudos,w.uniqueSort=w.unique=C.uniqueSort,w.text=C.getText,w.isXMLDoc=C.isXML,w.contains=C.contains,w.escapeSelector=C.escape;var E=function(e,t,n){for(var r=[],i=void 0!==n;(e=e[t])&&9!==e.nodeType;)if(1===e.nodeType){if(i&&w(e).is(n))break;r.push(e)}return r},k=function(e,t){for(var n=[];e;e=e.nextSibling)1===e.nodeType&&e!==t&&n.push(e);return n},A=w.expr.match.needsContext;function N(e,t){return e.nodeName&&e.nodeName.toLowerCase()===t.toLowerCase()}var S=/^<([a-z][^\/\0>:\x20\t\r\n\f]*)[\x20\t\r\n\f]*\/?>(?:<\/\1>|)$/i;function D(e,t,n){return h(t)?w.grep(e,(function(e,r){return!!t.call(e,r,e)!==n})):t.nodeType?w.grep(e,(function(e){return e===t!==n})):"string"!=typeof t?w.grep(e,(function(e){return a.call(t,e)>-1!==n})):w.filter(t,e,n)}w.filter=function(e,t,n){var r=t[0];return n&&(e=":not("+e+")"),1===t.length&&1===r.nodeType?w.find.matchesSelector(r,e)?[r]:[]:w.find.matches(e,w.grep(t,(function(e){return 1===e.nodeType})))},w.fn.extend({find:function(e){var t,n,r=this.length,i=this;if("string"!=typeof e)return this.pushStack(w(e).filter((function(){for(t=0;t<r;t++)if(w.contains(i[t],this))return!0})));for(n=this.pushStack([]),t=0;t<r;t++)w.find(e,i[t],n);return r>1?w.uniqueSort(n):n},filter:function(e){return this.pushStack(D(this,e||[],!1))},not:function(e){return this.pushStack(D(this,e||[],!0))},is:function(e){return!!D(this,"string"==typeof e&&A.test(e)?w(e):e||[],!1).length}});var q,L=/^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]+))$/;(w.fn.init=function(e,t,n){var r,i;if(!e)return this;if(n=n||q,"string"==typeof e){if(!(r="<"===e[0]&&">"===e[e.length-1]&&e.length>=3?[null,e,null]:L.exec(e))||!r[1]&&t)return!t||t.jquery?(t||n).find(e):this.constructor(t).find(e);if(r[1]){if(t=t instanceof w?t[0]:t,w.merge(this,w.parseHTML(r[1],t&&t.nodeType?t.ownerDocument||t:v,!0)),S.test(r[1])&&w.isPlainObject(t))for(r in t)h(this[r])?this[r](t[r]):this.attr(r,t[r]);return this}return(i=v.getElementById(r[2]))&&(this[0]=i,this.length=1),this}return e.nodeType?(this[0]=e,this.length=1,this):h(e)?void 0!==n.ready?n.ready(e):e(w):w.makeArray(e,this)}).prototype=w.fn,q=w(v);var j=/^(?:parents|prev(?:Until|All))/,H={children:!0,contents:!0,next:!0,prev:!0};function O(e,t){for(;(e=e[t])&&1!==e.nodeType;);return e}w.fn.extend({has:function(e){var t=w(e,this),n=t.length;return this.filter((function(){for(var e=0;e<n;e++)if(w.contains(this,t[e]))return!0}))},closest:function(e,t){var n,r=0,i=this.length,o=[],s="string"!=typeof e&&w(e);if(!A.test(e))for(;r<i;r++)for(n=this[r];n&&n!==t;n=n.parentNode)if(n.nodeType<11&&(s?s.index(n)>-1:1===n.nodeType&&w.find.matchesSelector(n,e))){o.push(n);break}return this.pushStack(o.length>1?w.uniqueSort(o):o)},index:function(e){return e?"string"==typeof e?a.call(w(e),this[0]):a.call(this,e.jquery?e[0]:e):this[0]&&this[0].parentNode?this.first().prevAll().length:-1},add:function(e,t){return this.pushStack(w.uniqueSort(w.merge(this.get(),w(e,t))))},addBack:function(e){return this.add(null==e?this.prevObject:this.prevObject.filter(e))}}),w.each({parent:function(e){var t=e.parentNode;return t&&11!==t.nodeType?t:null},parents:function(e){return E(e,"parentNode")},parentsUntil:function(e,t,n){return E(e,"parentNode",n)},next:function(e){return O(e,"nextSibling")},prev:function(e){return O(e,"previousSibling")},nextAll:function(e){return E(e,"nextSibling")},prevAll:function(e){return E(e,"previousSibling")},nextUntil:function(e,t,n){return E(e,"nextSibling",n)},prevUntil:function(e,t,n){return E(e,"previousSibling",n)},siblings:function(e){return k((e.parentNode||{}).firstChild,e)},children:function(e){return k(e.firstChild)},contents:function(e){return null!=e.contentDocument&&r(e.contentDocument)?e.contentDocument:(N(e,"template")&&(e=e.content||e),w.merge([],e.childNodes))}},(function(e,t){w.fn[e]=function(n,r){var i=w.map(this,t,n);return"Until"!==e.slice(-5)&&(r=n),r&&"string"==typeof r&&(i=w.filter(r,i)),this.length>1&&(H[e]||w.uniqueSort(i),j.test(e)&&i.reverse()),this.pushStack(i)}}));var P=/[^\x20\t\r\n\f]+/g;function I(e){return e}function R(e){throw e}function W(e,t,n,r){var i;try{e&&h(i=e.promise)?i.call(e).done(t).fail(n):e&&h(i=e.then)?i.call(e,t,n):t.apply(void 0,[e].slice(r))}catch(e){n.apply(void 0,[e])}}w.Callbacks=function(e){e="string"==typeof e?function(e){var t={};return w.each(e.match(P)||[],(function(e,n){t[n]=!0})),t}(e):w.extend({},e);var t,n,r,i,o=[],s=[],a=-1,u=function(){for(i=i||e.once,r=t=!0;s.length;a=-1)for(n=s.shift();++a<o.length;)!1===o[a].apply(n[0],n[1])&&e.stopOnFalse&&(a=o.length,n=!1);e.memory||(n=!1),t=!1,i&&(o=n?[]:"")},l={add:function(){return o&&(n&&!t&&(a=o.length-1,s.push(n)),function t(n){w.each(n,(function(n,r){h(r)?e.unique&&l.has(r)||o.push(r):r&&r.length&&"string"!==x(r)&&t(r)}))}(arguments),n&&!t&&u()),this},remove:function(){return w.each(arguments,(function(e,t){for(var n;(n=w.inArray(t,o,n))>-1;)o.splice(n,1),n<=a&&a--})),this},has:function(e){return e?w.inArray(e,o)>-1:o.length>0},empty:function(){return o&&(o=[]),this},disable:function(){return i=s=[],o=n="",this},disabled:function(){return!o},lock:function(){return i=s=[],n||t||(o=n=""),this},locked:function(){return!!i},fireWith:function(e,n){return i||(n=[e,(n=n||[]).slice?n.slice():n],s.push(n),t||u()),this},fire:function(){return l.fireWith(this,arguments),this},fired:function(){return!!r}};return l},w.extend({Deferred:function(t){var n=[["notify","progress",w.Callbacks("memory"),w.Callbacks("memory"),2],["resolve","done",w.Callbacks("once memory"),w.Callbacks("once memory"),0,"resolved"],["reject","fail",w.Callbacks("once memory"),w.Callbacks("once memory"),1,"rejected"]],r="pending",i={state:function(){return r},always:function(){return o.done(arguments).fail(arguments),this},catch:function(e){return i.then(null,e)},pipe:function(){var e=arguments;return w.Deferred((function(t){w.each(n,(function(n,r){var i=h(e[r[4]])&&e[r[4]];o[r[1]]((function(){var e=i&&i.apply(this,arguments);e&&h(e.promise)?e.promise().progress(t.notify).done(t.resolve).fail(t.reject):t[r[0]+"With"](this,i?[e]:arguments)}))})),e=null})).promise()},then:function(t,r,i){var o=0;function s(t,n,r,i){return function(){var a=this,u=arguments,l=function(){var e,l;if(!(t<o)){if((e=r.apply(a,u))===n.promise())throw new TypeError("Thenable self-resolution");l=e&&("object"==typeof e||"function"==typeof e)&&e.then,h(l)?i?l.call(e,s(o,n,I,i),s(o,n,R,i)):(o++,l.call(e,s(o,n,I,i),s(o,n,R,i),s(o,n,I,n.notifyWith))):(r!==I&&(a=void 0,u=[e]),(i||n.resolveWith)(a,u))}},c=i?l:function(){try{l()}catch(e){w.Deferred.exceptionHook&&w.Deferred.exceptionHook(e,c.stackTrace),t+1>=o&&(r!==R&&(a=void 0,u=[e]),n.rejectWith(a,u))}};t?c():(w.Deferred.getStackHook&&(c.stackTrace=w.Deferred.getStackHook()),e.setTimeout(c))}}return w.Deferred((function(e){n[0][3].add(s(0,e,h(i)?i:I,e.notifyWith)),n[1][3].add(s(0,e,h(t)?t:I)),n[2][3].add(s(0,e,h(r)?r:R))})).promise()},promise:function(e){return null!=e?w.extend(e,i):i}},o={};return w.each(n,(function(e,t){var s=t[2],a=t[5];i[t[1]]=s.add,a&&s.add((function(){r=a}),n[3-e][2].disable,n[3-e][3].disable,n[0][2].lock,n[0][3].lock),s.add(t[3].fire),o[t[0]]=function(){return o[t[0]+"With"](this===o?void 0:this,arguments),this},o[t[0]+"With"]=s.fireWith})),i.promise(o),t&&t.call(o,o),o},when:function(e){var t=arguments.length,n=t,r=Array(n),o=i.call(arguments),s=w.Deferred(),a=function(e){return function(n){r[e]=this,o[e]=arguments.length>1?i.call(arguments):n,--t||s.resolveWith(r,o)}};if(t<=1&&(W(e,s.done(a(n)).resolve,s.reject,!t),"pending"===s.state()||h(o[n]&&o[n].then)))return s.then();for(;n--;)W(o[n],a(n),s.reject);return s.promise()}});var B=/^(Eval|Internal|Range|Reference|Syntax|Type|URI)Error$/;w.Deferred.exceptionHook=function(t,n){e.console&&e.console.warn&&t&&B.test(t.name)&&e.console.warn("jQuery.Deferred exception: "+t.message,t.stack,n)},w.readyException=function(t){e.setTimeout((function(){throw t}))};var M=w.Deferred();function F(){v.removeEventListener("DOMContentLoaded",F),e.removeEventListener("load",F),w.ready()}w.fn.ready=function(e){return M.then(e).catch((function(e){w.readyException(e)})),this},w.extend({isReady:!1,readyWait:1,ready:function(e){(!0===e?--w.readyWait:w.isReady)||(w.isReady=!0,!0!==e&&--w.readyWait>0||M.resolveWith(v,[w]))}}),w.ready.then=M.then,"complete"===v.readyState||"loading"!==v.readyState&&!v.documentElement.doScroll?e.setTimeout(w.ready):(v.addEventListener("DOMContentLoaded",F),e.addEventListener("load",F));var $=function(e,t,n,r,i,o,s){var a=0,u=e.length,l=null==n;if("object"===x(n))for(a in i=!0,n)$(e,t,a,n[a],!0,o,s);else if(void 0!==r&&(i=!0,h(r)||(s=!0),l&&(s?(t.call(e,r),t=null):(l=t,t=function(e,t,n){return l.call(w(e),n)})),t))for(;a<u;a++)t(e[a],n,s?r:r.call(e[a],a,t(e[a],n)));return i?e:l?t.call(e):u?t(e[0],n):o},_=/^-ms-/,z=/-([a-z])/g;function U(e,t){return t.toUpperCase()}function X(e){return e.replace(_,"ms-").replace(z,U)}var V=function(e){return 1===e.nodeType||9===e.nodeType||!+e.nodeType};function Y(){this.expando=w.expando+Y.uid++}Y.uid=1,Y.prototype={cache:function(e){var t=e[this.expando];return t||(t={},V(e)&&(e.nodeType?e[this.expando]=t:Object.defineProperty(e,this.expando,{value:t,configurable:!0}))),t},set:function(e,t,n){var r,i=this.cache(e);if("string"==typeof t)i[X(t)]=n;else for(r in t)i[X(r)]=t[r];return i},get:function(e,t){return void 0===t?this.cache(e):e[this.expando]&&e[this.expando][X(t)]},access:function(e,t,n){return void 0===t||t&&"string"==typeof t&&void 0===n?this.get(e,t):(this.set(e,t,n),void 0!==n?n:t)},remove:function(e,t){var n,r=e[this.expando];if(void 0!==r){if(void 0!==t){n=(t=Array.isArray(t)?t.map(X):(t=X(t))in r?[t]:t.match(P)||[]).length;for(;n--;)delete r[t[n]]}(void 0===t||w.isEmptyObject(r))&&(e.nodeType?e[this.expando]=void 0:delete e[this.expando])}},hasData:function(e){var t=e[this.expando];return void 0!==t&&!w.isEmptyObject(t)}};var Q=new Y,G=new Y,K=/^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,J=/[A-Z]/g;function Z(e,t,n){var r;if(void 0===n&&1===e.nodeType)if(r="data-"+t.replace(J,"-$&").toLowerCase(),"string"==typeof(n=e.getAttribute(r))){try{n=function(e){return"true"===e||"false"!==e&&("null"===e?null:e===+e+""?+e:K.test(e)?JSON.parse(e):e)}(n)}catch(e){}G.set(e,t,n)}else n=void 0;return n}w.extend({hasData:function(e){return G.hasData(e)||Q.hasData(e)},data:function(e,t,n){return G.access(e,t,n)},removeData:function(e,t){G.remove(e,t)},_data:function(e,t,n){return Q.access(e,t,n)},_removeData:function(e,t){Q.remove(e,t)}}),w.fn.extend({data:function(e,t){var n,r,i,o=this[0],s=o&&o.attributes;if(void 0===e){if(this.length&&(i=G.get(o),1===o.nodeType&&!Q.get(o,"hasDataAttrs"))){for(n=s.length;n--;)s[n]&&0===(r=s[n].name).indexOf("data-")&&(r=X(r.slice(5)),Z(o,r,i[r]));Q.set(o,"hasDataAttrs",!0)}return i}return"object"==typeof e?this.each((function(){G.set(this,e)})):$(this,(function(t){var n;if(o&&void 0===t)return void 0!==(n=G.get(o,e))||void 0!==(n=Z(o,e))?n:void 0;this.each((function(){G.set(this,e,t)}))}),null,t,arguments.length>1,null,!0)},removeData:function(e){return this.each((function(){G.remove(this,e)}))}}),w.extend({queue:function(e,t,n){var r;if(e)return t=(t||"fx")+"queue",r=Q.get(e,t),n&&(!r||Array.isArray(n)?r=Q.access(e,t,w.makeArray(n)):r.push(n)),r||[]},dequeue:function(e,t){t=t||"fx";var n=w.queue(e,t),r=n.length,i=n.shift(),o=w._queueHooks(e,t);"inprogress"===i&&(i=n.shift(),r--),i&&("fx"===t&&n.unshift("inprogress"),delete o.stop,i.call(e,(function(){w.dequeue(e,t)}),o)),!r&&o&&o.empty.fire()},_queueHooks:function(e,t){var n=t+"queueHooks";return Q.get(e,n)||Q.access(e,n,{empty:w.Callbacks("once memory").add((function(){Q.remove(e,[t+"queue",n])}))})}}),w.fn.extend({queue:function(e,t){var n=2;return"string"!=typeof e&&(t=e,e="fx",n--),arguments.length<n?w.queue(this[0],e):void 0===t?this:this.each((function(){var n=w.queue(this,e,t);w._queueHooks(this,e),"fx"===e&&"inprogress"!==n[0]&&w.dequeue(this,e)}))},dequeue:function(e){return this.each((function(){w.dequeue(this,e)}))},clearQueue:function(e){return this.queue(e||"fx",[])},promise:function(e,t){var n,r=1,i=w.Deferred(),o=this,s=this.length,a=function(){--r||i.resolveWith(o,[o])};for("string"!=typeof e&&(t=e,e=void 0),e=e||"fx";s--;)(n=Q.get(o[s],e+"queueHooks"))&&n.empty&&(r++,n.empty.add(a));return a(),i.promise(t)}});var ee=/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,te=new RegExp("^(?:([+-])=|)("+ee+")([a-z%]*)$","i"),ne=["Top","Right","Bottom","Left"],re=v.documentElement,ie=function(e){return w.contains(e.ownerDocument,e)},oe={composed:!0};re.getRootNode&&(ie=function(e){return w.contains(e.ownerDocument,e)||e.getRootNode(oe)===e.ownerDocument});var se=function(e,t){return"none"===(e=t||e).style.display||""===e.style.display&&ie(e)&&"none"===w.css(e,"display")};function ae(e,t,n,r){var i,o,s=20,a=r?function(){return r.cur()}:function(){return w.css(e,t,"")},u=a(),l=n&&n[3]||(w.cssNumber[t]?"":"px"),c=e.nodeType&&(w.cssNumber[t]||"px"!==l&&+u)&&te.exec(w.css(e,t));if(c&&c[3]!==l){for(u/=2,l=l||c[3],c=+u||1;s--;)w.style(e,t,c+l),(1-o)*(1-(o=a()/u||.5))<=0&&(s=0),c/=o;c*=2,w.style(e,t,c+l),n=n||[]}return n&&(c=+c||+u||0,i=n[1]?c+(n[1]+1)*n[2]:+n[2],r&&(r.unit=l,r.start=c,r.end=i)),i}var ue={};function le(e){var t,n=e.ownerDocument,r=e.nodeName,i=ue[r];return i||(t=n.body.appendChild(n.createElement(r)),i=w.css(t,"display"),t.parentNode.removeChild(t),"none"===i&&(i="block"),ue[r]=i,i)}function ce(e,t){for(var n,r,i=[],o=0,s=e.length;o<s;o++)(r=e[o]).style&&(n=r.style.display,t?("none"===n&&(i[o]=Q.get(r,"display")||null,i[o]||(r.style.display="")),""===r.style.display&&se(r)&&(i[o]=le(r))):"none"!==n&&(i[o]="none",Q.set(r,"display",n)));for(o=0;o<s;o++)null!=i[o]&&(e[o].style.display=i[o]);return e}w.fn.extend({show:function(){return ce(this,!0)},hide:function(){return ce(this)},toggle:function(e){return"boolean"==typeof e?e?this.show():this.hide():this.each((function(){se(this)?w(this).show():w(this).hide()}))}});var fe,pe,de=/^(?:checkbox|radio)$/i,he=/<([a-z][^\/\0>\x20\t\r\n\f]*)/i,ge=/^$|^module$|\/(?:java|ecma)script/i;fe=v.createDocumentFragment().appendChild(v.createElement("div")),(pe=v.createElement("input")).setAttribute("type","radio"),pe.setAttribute("checked","checked"),pe.setAttribute("name","t"),fe.appendChild(pe),d.checkClone=fe.cloneNode(!0).cloneNode(!0).lastChild.checked,fe.innerHTML="<textarea>x</textarea>",d.noCloneChecked=!!fe.cloneNode(!0).lastChild.defaultValue,fe.innerHTML="<option></option>",d.option=!!fe.lastChild;var ve={thead:[1,"<table>","</table>"],col:[2,"<table><colgroup>","</colgroup></table>"],tr:[2,"<table><tbody>","</tbody></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],_default:[0,"",""]};function me(e,t){var n;return n=void 0!==e.getElementsByTagName?e.getElementsByTagName(t||"*"):void 0!==e.querySelectorAll?e.querySelectorAll(t||"*"):[],void 0===t||t&&N(e,t)?w.merge([e],n):n}function ye(e,t){for(var n=0,r=e.length;n<r;n++)Q.set(e[n],"globalEval",!t||Q.get(t[n],"globalEval"))}ve.tbody=ve.tfoot=ve.colgroup=ve.caption=ve.thead,ve.th=ve.td,d.option||(ve.optgroup=ve.option=[1,"<select multiple='multiple'>","</select>"]);var xe=/<|&#?\w+;/;function be(e,t,n,r,i){for(var o,s,a,u,l,c,f=t.createDocumentFragment(),p=[],d=0,h=e.length;d<h;d++)if((o=e[d])||0===o)if("object"===x(o))w.merge(p,o.nodeType?[o]:o);else if(xe.test(o)){for(s=s||f.appendChild(t.createElement("div")),a=(he.exec(o)||["",""])[1].toLowerCase(),u=ve[a]||ve._default,s.innerHTML=u[1]+w.htmlPrefilter(o)+u[2],c=u[0];c--;)s=s.lastChild;w.merge(p,s.childNodes),(s=f.firstChild).textContent=""}else p.push(t.createTextNode(o));for(f.textContent="",d=0;o=p[d++];)if(r&&w.inArray(o,r)>-1)i&&i.push(o);else if(l=ie(o),s=me(f.appendChild(o),"script"),l&&ye(s),n)for(c=0;o=s[c++];)ge.test(o.type||"")&&n.push(o);return f}var we=/^([^.]*)(?:\.(.+)|)/;function Te(){return!0}function Ce(){return!1}function Ee(e,t){return e===function(){try{return v.activeElement}catch(e){}}()==("focus"===t)}function ke(e,t,n,r,i,o){var s,a;if("object"==typeof t){for(a in"string"!=typeof n&&(r=r||n,n=void 0),t)ke(e,a,n,r,t[a],o);return e}if(null==r&&null==i?(i=n,r=n=void 0):null==i&&("string"==typeof n?(i=r,r=void 0):(i=r,r=n,n=void 0)),!1===i)i=Ce;else if(!i)return e;return 1===o&&(s=i,i=function(e){return w().off(e),s.apply(this,arguments)},i.guid=s.guid||(s.guid=w.guid++)),e.each((function(){w.event.add(this,t,i,r,n)}))}function Ae(e,t,n){n?(Q.set(e,t,!1),w.event.add(e,t,{namespace:!1,handler:function(e){var r,o,s=Q.get(this,t);if(1&e.isTrigger&&this[t]){if(s.length)(w.event.special[t]||{}).delegateType&&e.stopPropagation();else if(s=i.call(arguments),Q.set(this,t,s),r=n(this,t),this[t](),s!==(o=Q.get(this,t))||r?Q.set(this,t,!1):o={},s!==o)return e.stopImmediatePropagation(),e.preventDefault(),o&&o.value}else s.length&&(Q.set(this,t,{value:w.event.trigger(w.extend(s[0],w.Event.prototype),s.slice(1),this)}),e.stopImmediatePropagation())}})):void 0===Q.get(e,t)&&w.event.add(e,t,Te)}w.event={global:{},add:function(e,t,n,r,i){var o,s,a,u,l,c,f,p,d,h,g,v=Q.get(e);if(V(e))for(n.handler&&(n=(o=n).handler,i=o.selector),i&&w.find.matchesSelector(re,i),n.guid||(n.guid=w.guid++),(u=v.events)||(u=v.events=Object.create(null)),(s=v.handle)||(s=v.handle=function(t){return void 0!==w&&w.event.triggered!==t.type?w.event.dispatch.apply(e,arguments):void 0}),l=(t=(t||"").match(P)||[""]).length;l--;)d=g=(a=we.exec(t[l])||[])[1],h=(a[2]||"").split(".").sort(),d&&(f=w.event.special[d]||{},d=(i?f.delegateType:f.bindType)||d,f=w.event.special[d]||{},c=w.extend({type:d,origType:g,data:r,handler:n,guid:n.guid,selector:i,needsContext:i&&w.expr.match.needsContext.test(i),namespace:h.join(".")},o),(p=u[d])||((p=u[d]=[]).delegateCount=0,f.setup&&!1!==f.setup.call(e,r,h,s)||e.addEventListener&&e.addEventListener(d,s)),f.add&&(f.add.call(e,c),c.handler.guid||(c.handler.guid=n.guid)),i?p.splice(p.delegateCount++,0,c):p.push(c),w.event.global[d]=!0)},remove:function(e,t,n,r,i){var o,s,a,u,l,c,f,p,d,h,g,v=Q.hasData(e)&&Q.get(e);if(v&&(u=v.events)){for(l=(t=(t||"").match(P)||[""]).length;l--;)if(d=g=(a=we.exec(t[l])||[])[1],h=(a[2]||"").split(".").sort(),d){for(f=w.event.special[d]||{},p=u[d=(r?f.delegateType:f.bindType)||d]||[],a=a[2]&&new RegExp("(^|\\.)"+h.join("\\.(?:.*\\.|)")+"(\\.|$)"),s=o=p.length;o--;)c=p[o],!i&&g!==c.origType||n&&n.guid!==c.guid||a&&!a.test(c.namespace)||r&&r!==c.selector&&("**"!==r||!c.selector)||(p.splice(o,1),c.selector&&p.delegateCount--,f.remove&&f.remove.call(e,c));s&&!p.length&&(f.teardown&&!1!==f.teardown.call(e,h,v.handle)||w.removeEvent(e,d,v.handle),delete u[d])}else for(d in u)w.event.remove(e,d+t[l],n,r,!0);w.isEmptyObject(u)&&Q.remove(e,"handle events")}},dispatch:function(e){var t,n,r,i,o,s,a=new Array(arguments.length),u=w.event.fix(e),l=(Q.get(this,"events")||Object.create(null))[u.type]||[],c=w.event.special[u.type]||{};for(a[0]=u,t=1;t<arguments.length;t++)a[t]=arguments[t];if(u.delegateTarget=this,!c.preDispatch||!1!==c.preDispatch.call(this,u)){for(s=w.event.handlers.call(this,u,l),t=0;(i=s[t++])&&!u.isPropagationStopped();)for(u.currentTarget=i.elem,n=0;(o=i.handlers[n++])&&!u.isImmediatePropagationStopped();)u.rnamespace&&!1!==o.namespace&&!u.rnamespace.test(o.namespace)||(u.handleObj=o,u.data=o.data,void 0!==(r=((w.event.special[o.origType]||{}).handle||o.handler).apply(i.elem,a))&&!1===(u.result=r)&&(u.preventDefault(),u.stopPropagation()));return c.postDispatch&&c.postDispatch.call(this,u),u.result}},handlers:function(e,t){var n,r,i,o,s,a=[],u=t.delegateCount,l=e.target;if(u&&l.nodeType&&!("click"===e.type&&e.button>=1))for(;l!==this;l=l.parentNode||this)if(1===l.nodeType&&("click"!==e.type||!0!==l.disabled)){for(o=[],s={},n=0;n<u;n++)void 0===s[i=(r=t[n]).selector+" "]&&(s[i]=r.needsContext?w(i,this).index(l)>-1:w.find(i,this,null,[l]).length),s[i]&&o.push(r);o.length&&a.push({elem:l,handlers:o})}return l=this,u<t.length&&a.push({elem:l,handlers:t.slice(u)}),a},addProp:function(e,t){Object.defineProperty(w.Event.prototype,e,{enumerable:!0,configurable:!0,get:h(t)?function(){if(this.originalEvent)return t(this.originalEvent)}:function(){if(this.originalEvent)return this.originalEvent[e]},set:function(t){Object.defineProperty(this,e,{enumerable:!0,configurable:!0,writable:!0,value:t})}})},fix:function(e){return e[w.expando]?e:new w.Event(e)},special:{load:{noBubble:!0},click:{setup:function(e){var t=this||e;return de.test(t.type)&&t.click&&N(t,"input")&&Ae(t,"click",Te),!1},trigger:function(e){var t=this||e;return de.test(t.type)&&t.click&&N(t,"input")&&Ae(t,"click"),!0},_default:function(e){var t=e.target;return de.test(t.type)&&t.click&&N(t,"input")&&Q.get(t,"click")||N(t,"a")}}}},w.removeEvent=function(e,t,n){e.removeEventListener&&e.removeEventListener(t,n)},w.Event=function(e,t){if(!(this instanceof w.Event))return new w.Event(e,t);e&&e.type?(this.originalEvent=e,this.type=e.type,this.isDefaultPrevented=e.defaultPrevented||void 0===e.defaultPrevented&&!1===e.returnValue?Te:Ce,this.target=e.target&&3===e.target.nodeType?e.target.parentNode:e.target,this.currentTarget=e.currentTarget,this.relatedTarget=e.relatedTarget):this.type=e,t&&w.extend(this,t),this.timeStamp=e&&e.timeStamp||Date.now(),this[w.expando]=!0},w.Event.prototype={constructor:w.Event,isDefaultPrevented:Ce,isPropagationStopped:Ce,isImmediatePropagationStopped:Ce,isSimulated:!1,preventDefault:function(){var e=this.originalEvent;this.isDefaultPrevented=Te,e&&!this.isSimulated&&e.preventDefault()},stopPropagation:function(){var e=this.originalEvent;this.isPropagationStopped=Te,e&&!this.isSimulated&&e.stopPropagation()},stopImmediatePropagation:function(){var e=this.originalEvent;this.isImmediatePropagationStopped=Te,e&&!this.isSimulated&&e.stopImmediatePropagation(),this.stopPropagation()}},w.each({altKey:!0,bubbles:!0,cancelable:!0,changedTouches:!0,ctrlKey:!0,detail:!0,eventPhase:!0,metaKey:!0,pageX:!0,pageY:!0,shiftKey:!0,view:!0,char:!0,code:!0,charCode:!0,key:!0,keyCode:!0,button:!0,buttons:!0,clientX:!0,clientY:!0,offsetX:!0,offsetY:!0,pointerId:!0,pointerType:!0,screenX:!0,screenY:!0,targetTouches:!0,toElement:!0,touches:!0,which:!0},w.event.addProp),w.each({focus:"focusin",blur:"focusout"},(function(e,t){w.event.special[e]={setup:function(){return Ae(this,e,Ee),!1},trigger:function(){return Ae(this,e),!0},_default:function(){return!0},delegateType:t}})),w.each({mouseenter:"mouseover",mouseleave:"mouseout",pointerenter:"pointerover",pointerleave:"pointerout"},(function(e,t){w.event.special[e]={delegateType:t,bindType:t,handle:function(e){var n,r=this,i=e.relatedTarget,o=e.handleObj;return i&&(i===r||w.contains(r,i))||(e.type=o.origType,n=o.handler.apply(this,arguments),e.type=t),n}}})),w.fn.extend({on:function(e,t,n,r){return ke(this,e,t,n,r)},one:function(e,t,n,r){return ke(this,e,t,n,r,1)},off:function(e,t,n){var r,i;if(e&&e.preventDefault&&e.handleObj)return r=e.handleObj,w(e.delegateTarget).off(r.namespace?r.origType+"."+r.namespace:r.origType,r.selector,r.handler),this;if("object"==typeof e){for(i in e)this.off(i,t,e[i]);return this}return!1!==t&&"function"!=typeof t||(n=t,t=void 0),!1===n&&(n=Ce),this.each((function(){w.event.remove(this,e,n,t)}))}});var Ne=/<script|<style|<link/i,Se=/checked\s*(?:[^=]|=\s*.checked.)/i,De=/^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g;function qe(e,t){return N(e,"table")&&N(11!==t.nodeType?t:t.firstChild,"tr")&&w(e).children("tbody")[0]||e}function Le(e){return e.type=(null!==e.getAttribute("type"))+"/"+e.type,e}function je(e){return"true/"===(e.type||"").slice(0,5)?e.type=e.type.slice(5):e.removeAttribute("type"),e}function He(e,t){var n,r,i,o,s,a;if(1===t.nodeType){if(Q.hasData(e)&&(a=Q.get(e).events))for(i in Q.remove(t,"handle events"),a)for(n=0,r=a[i].length;n<r;n++)w.event.add(t,i,a[i][n]);G.hasData(e)&&(o=G.access(e),s=w.extend({},o),G.set(t,s))}}function Oe(e,t){var n=t.nodeName.toLowerCase();"input"===n&&de.test(e.type)?t.checked=e.checked:"input"!==n&&"textarea"!==n||(t.defaultValue=e.defaultValue)}function Pe(e,t,n,r){t=o(t);var i,s,a,u,l,c,f=0,p=e.length,g=p-1,v=t[0],m=h(v);if(m||p>1&&"string"==typeof v&&!d.checkClone&&Se.test(v))return e.each((function(i){var o=e.eq(i);m&&(t[0]=v.call(this,i,o.html())),Pe(o,t,n,r)}));if(p&&(s=(i=be(t,e[0].ownerDocument,!1,e,r)).firstChild,1===i.childNodes.length&&(i=s),s||r)){for(u=(a=w.map(me(i,"script"),Le)).length;f<p;f++)l=i,f!==g&&(l=w.clone(l,!0,!0),u&&w.merge(a,me(l,"script"))),n.call(e[f],l,f);if(u)for(c=a[a.length-1].ownerDocument,w.map(a,je),f=0;f<u;f++)l=a[f],ge.test(l.type||"")&&!Q.access(l,"globalEval")&&w.contains(c,l)&&(l.src&&"module"!==(l.type||"").toLowerCase()?w._evalUrl&&!l.noModule&&w._evalUrl(l.src,{nonce:l.nonce||l.getAttribute("nonce")},c):y(l.textContent.replace(De,""),l,c))}return e}function Ie(e,t,n){for(var r,i=t?w.filter(t,e):e,o=0;null!=(r=i[o]);o++)n||1!==r.nodeType||w.cleanData(me(r)),r.parentNode&&(n&&ie(r)&&ye(me(r,"script")),r.parentNode.removeChild(r));return e}w.extend({htmlPrefilter:function(e){return e},clone:function(e,t,n){var r,i,o,s,a=e.cloneNode(!0),u=ie(e);if(!(d.noCloneChecked||1!==e.nodeType&&11!==e.nodeType||w.isXMLDoc(e)))for(s=me(a),r=0,i=(o=me(e)).length;r<i;r++)Oe(o[r],s[r]);if(t)if(n)for(o=o||me(e),s=s||me(a),r=0,i=o.length;r<i;r++)He(o[r],s[r]);else He(e,a);return(s=me(a,"script")).length>0&&ye(s,!u&&me(e,"script")),a},cleanData:function(e){for(var t,n,r,i=w.event.special,o=0;void 0!==(n=e[o]);o++)if(V(n)){if(t=n[Q.expando]){if(t.events)for(r in t.events)i[r]?w.event.remove(n,r):w.removeEvent(n,r,t.handle);n[Q.expando]=void 0}n[G.expando]&&(n[G.expando]=void 0)}}}),w.fn.extend({detach:function(e){return Ie(this,e,!0)},remove:function(e){return Ie(this,e)},text:function(e){return $(this,(function(e){return void 0===e?w.text(this):this.empty().each((function(){1!==this.nodeType&&11!==this.nodeType&&9!==this.nodeType||(this.textContent=e)}))}),null,e,arguments.length)},append:function(){return Pe(this,arguments,(function(e){1!==this.nodeType&&11!==this.nodeType&&9!==this.nodeType||qe(this,e).appendChild(e)}))},prepend:function(){return Pe(this,arguments,(function(e){if(1===this.nodeType||11===this.nodeType||9===this.nodeType){var t=qe(this,e);t.insertBefore(e,t.firstChild)}}))},before:function(){return Pe(this,arguments,(function(e){this.parentNode&&this.parentNode.insertBefore(e,this)}))},after:function(){return Pe(this,arguments,(function(e){this.parentNode&&this.parentNode.insertBefore(e,this.nextSibling)}))},empty:function(){for(var e,t=0;null!=(e=this[t]);t++)1===e.nodeType&&(w.cleanData(me(e,!1)),e.textContent="");return this},clone:function(e,t){return e=null!=e&&e,t=null==t?e:t,this.map((function(){return w.clone(this,e,t)}))},html:function(e){return $(this,(function(e){var t=this[0]||{},n=0,r=this.length;if(void 0===e&&1===t.nodeType)return t.innerHTML;if("string"==typeof e&&!Ne.test(e)&&!ve[(he.exec(e)||["",""])[1].toLowerCase()]){e=w.htmlPrefilter(e);try{for(;n<r;n++)1===(t=this[n]||{}).nodeType&&(w.cleanData(me(t,!1)),t.innerHTML=e);t=0}catch(e){}}t&&this.empty().append(e)}),null,e,arguments.length)},replaceWith:function(){var e=[];return Pe(this,arguments,(function(t){var n=this.parentNode;w.inArray(this,e)<0&&(w.cleanData(me(this)),n&&n.replaceChild(t,this))}),e)}}),w.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},(function(e,t){w.fn[e]=function(e){for(var n,r=[],i=w(e),o=i.length-1,a=0;a<=o;a++)n=a===o?this:this.clone(!0),w(i[a])[t](n),s.apply(r,n.get());return this.pushStack(r)}}));var Re=new RegExp("^("+ee+")(?!px)[a-z%]+$","i"),We=function(t){var n=t.ownerDocument.defaultView;return n&&n.opener||(n=e),n.getComputedStyle(t)},Be=function(e,t,n){var r,i,o={};for(i in t)o[i]=e.style[i],e.style[i]=t[i];for(i in r=n.call(e),t)e.style[i]=o[i];return r},Me=new RegExp(ne.join("|"),"i");function Fe(e,t,n){var r,i,o,s,a=e.style;return(n=n||We(e))&&(""!==(s=n.getPropertyValue(t)||n[t])||ie(e)||(s=w.style(e,t)),!d.pixelBoxStyles()&&Re.test(s)&&Me.test(t)&&(r=a.width,i=a.minWidth,o=a.maxWidth,a.minWidth=a.maxWidth=a.width=s,s=n.width,a.width=r,a.minWidth=i,a.maxWidth=o)),void 0!==s?s+"":s}function $e(e,t){return{get:function(){if(!e())return(this.get=t).apply(this,arguments);delete this.get}}}!function(){function t(){if(c){l.style.cssText="position:absolute;left:-11111px;width:60px;margin-top:1px;padding:0;border:0",c.style.cssText="position:relative;display:block;box-sizing:border-box;overflow:scroll;margin:auto;border:1px;padding:1px;width:60%;top:1%",re.appendChild(l).appendChild(c);var t=e.getComputedStyle(c);r="1%"!==t.top,u=12===n(t.marginLeft),c.style.right="60%",s=36===n(t.right),i=36===n(t.width),c.style.position="absolute",o=12===n(c.offsetWidth/3),re.removeChild(l),c=null}}function n(e){return Math.round(parseFloat(e))}var r,i,o,s,a,u,l=v.createElement("div"),c=v.createElement("div");c.style&&(c.style.backgroundClip="content-box",c.cloneNode(!0).style.backgroundClip="",d.clearCloneStyle="content-box"===c.style.backgroundClip,w.extend(d,{boxSizingReliable:function(){return t(),i},pixelBoxStyles:function(){return t(),s},pixelPosition:function(){return t(),r},reliableMarginLeft:function(){return t(),u},scrollboxSize:function(){return t(),o},reliableTrDimensions:function(){var t,n,r,i;return null==a&&(t=v.createElement("table"),n=v.createElement("tr"),r=v.createElement("div"),t.style.cssText="position:absolute;left:-11111px;border-collapse:separate",n.style.cssText="border:1px solid",n.style.height="1px",r.style.height="9px",r.style.display="block",re.appendChild(t).appendChild(n).appendChild(r),i=e.getComputedStyle(n),a=parseInt(i.height,10)+parseInt(i.borderTopWidth,10)+parseInt(i.borderBottomWidth,10)===n.offsetHeight,re.removeChild(t)),a}}))}();var _e=["Webkit","Moz","ms"],ze=v.createElement("div").style,Ue={};function Xe(e){var t=w.cssProps[e]||Ue[e];return t||(e in ze?e:Ue[e]=function(e){for(var t=e[0].toUpperCase()+e.slice(1),n=_e.length;n--;)if((e=_e[n]+t)in ze)return e}(e)||e)}var Ve=/^(none|table(?!-c[ea]).+)/,Ye=/^--/,Qe={position:"absolute",visibility:"hidden",display:"block"},Ge={letterSpacing:"0",fontWeight:"400"};function Ke(e,t,n){var r=te.exec(t);return r?Math.max(0,r[2]-(n||0))+(r[3]||"px"):t}function Je(e,t,n,r,i,o){var s="width"===t?1:0,a=0,u=0;if(n===(r?"border":"content"))return 0;for(;s<4;s+=2)"margin"===n&&(u+=w.css(e,n+ne[s],!0,i)),r?("content"===n&&(u-=w.css(e,"padding"+ne[s],!0,i)),"margin"!==n&&(u-=w.css(e,"border"+ne[s]+"Width",!0,i))):(u+=w.css(e,"padding"+ne[s],!0,i),"padding"!==n?u+=w.css(e,"border"+ne[s]+"Width",!0,i):a+=w.css(e,"border"+ne[s]+"Width",!0,i));return!r&&o>=0&&(u+=Math.max(0,Math.ceil(e["offset"+t[0].toUpperCase()+t.slice(1)]-o-u-a-.5))||0),u}function Ze(e,t,n){var r=We(e),i=(!d.boxSizingReliable()||n)&&"border-box"===w.css(e,"boxSizing",!1,r),o=i,s=Fe(e,t,r),a="offset"+t[0].toUpperCase()+t.slice(1);if(Re.test(s)){if(!n)return s;s="auto"}return(!d.boxSizingReliable()&&i||!d.reliableTrDimensions()&&N(e,"tr")||"auto"===s||!parseFloat(s)&&"inline"===w.css(e,"display",!1,r))&&e.getClientRects().length&&(i="border-box"===w.css(e,"boxSizing",!1,r),(o=a in e)&&(s=e[a])),(s=parseFloat(s)||0)+Je(e,t,n||(i?"border":"content"),o,r,s)+"px"}function et(e,t,n,r,i){return new et.prototype.init(e,t,n,r,i)}w.extend({cssHooks:{opacity:{get:function(e,t){if(t){var n=Fe(e,"opacity");return""===n?"1":n}}}},cssNumber:{animationIterationCount:!0,columnCount:!0,fillOpacity:!0,flexGrow:!0,flexShrink:!0,fontWeight:!0,gridArea:!0,gridColumn:!0,gridColumnEnd:!0,gridColumnStart:!0,gridRow:!0,gridRowEnd:!0,gridRowStart:!0,lineHeight:!0,opacity:!0,order:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssProps:{},style:function(e,t,n,r){if(e&&3!==e.nodeType&&8!==e.nodeType&&e.style){var i,o,s,a=X(t),u=Ye.test(t),l=e.style;if(u||(t=Xe(a)),s=w.cssHooks[t]||w.cssHooks[a],void 0===n)return s&&"get"in s&&void 0!==(i=s.get(e,!1,r))?i:l[t];"string"===(o=typeof n)&&(i=te.exec(n))&&i[1]&&(n=ae(e,t,i),o="number"),null!=n&&n==n&&("number"!==o||u||(n+=i&&i[3]||(w.cssNumber[a]?"":"px")),d.clearCloneStyle||""!==n||0!==t.indexOf("background")||(l[t]="inherit"),s&&"set"in s&&void 0===(n=s.set(e,n,r))||(u?l.setProperty(t,n):l[t]=n))}},css:function(e,t,n,r){var i,o,s,a=X(t);return Ye.test(t)||(t=Xe(a)),(s=w.cssHooks[t]||w.cssHooks[a])&&"get"in s&&(i=s.get(e,!0,n)),void 0===i&&(i=Fe(e,t,r)),"normal"===i&&t in Ge&&(i=Ge[t]),""===n||n?(o=parseFloat(i),!0===n||isFinite(o)?o||0:i):i}}),w.each(["height","width"],(function(e,t){w.cssHooks[t]={get:function(e,n,r){if(n)return!Ve.test(w.css(e,"display"))||e.getClientRects().length&&e.getBoundingClientRect().width?Ze(e,t,r):Be(e,Qe,(function(){return Ze(e,t,r)}))},set:function(e,n,r){var i,o=We(e),s=!d.scrollboxSize()&&"absolute"===o.position,a=(s||r)&&"border-box"===w.css(e,"boxSizing",!1,o),u=r?Je(e,t,r,a,o):0;return a&&s&&(u-=Math.ceil(e["offset"+t[0].toUpperCase()+t.slice(1)]-parseFloat(o[t])-Je(e,t,"border",!1,o)-.5)),u&&(i=te.exec(n))&&"px"!==(i[3]||"px")&&(e.style[t]=n,n=w.css(e,t)),Ke(0,n,u)}}})),w.cssHooks.marginLeft=$e(d.reliableMarginLeft,(function(e,t){if(t)return(parseFloat(Fe(e,"marginLeft"))||e.getBoundingClientRect().left-Be(e,{marginLeft:0},(function(){return e.getBoundingClientRect().left})))+"px"})),w.each({margin:"",padding:"",border:"Width"},(function(e,t){w.cssHooks[e+t]={expand:function(n){for(var r=0,i={},o="string"==typeof n?n.split(" "):[n];r<4;r++)i[e+ne[r]+t]=o[r]||o[r-2]||o[0];return i}},"margin"!==e&&(w.cssHooks[e+t].set=Ke)})),w.fn.extend({css:function(e,t){return $(this,(function(e,t,n){var r,i,o={},s=0;if(Array.isArray(t)){for(r=We(e),i=t.length;s<i;s++)o[t[s]]=w.css(e,t[s],!1,r);return o}return void 0!==n?w.style(e,t,n):w.css(e,t)}),e,t,arguments.length>1)}}),w.Tween=et,et.prototype={constructor:et,init:function(e,t,n,r,i,o){this.elem=e,this.prop=n,this.easing=i||w.easing._default,this.options=t,this.start=this.now=this.cur(),this.end=r,this.unit=o||(w.cssNumber[n]?"":"px")},cur:function(){var e=et.propHooks[this.prop];return e&&e.get?e.get(this):et.propHooks._default.get(this)},run:function(e){var t,n=et.propHooks[this.prop];return this.options.duration?this.pos=t=w.easing[this.easing](e,this.options.duration*e,0,1,this.options.duration):this.pos=t=e,this.now=(this.end-this.start)*t+this.start,this.options.step&&this.options.step.call(this.elem,this.now,this),n&&n.set?n.set(this):et.propHooks._default.set(this),this}},et.prototype.init.prototype=et.prototype,et.propHooks={_default:{get:function(e){var t;return 1!==e.elem.nodeType||null!=e.elem[e.prop]&&null==e.elem.style[e.prop]?e.elem[e.prop]:(t=w.css(e.elem,e.prop,""))&&"auto"!==t?t:0},set:function(e){w.fx.step[e.prop]?w.fx.step[e.prop](e):1!==e.elem.nodeType||!w.cssHooks[e.prop]&&null==e.elem.style[Xe(e.prop)]?e.elem[e.prop]=e.now:w.style(e.elem,e.prop,e.now+e.unit)}}},et.propHooks.scrollTop=et.propHooks.scrollLeft={set:function(e){e.elem.nodeType&&e.elem.parentNode&&(e.elem[e.prop]=e.now)}},w.easing={linear:function(e){return e},swing:function(e){return.5-Math.cos(e*Math.PI)/2},_default:"swing"},w.fx=et.prototype.init,w.fx.step={};var tt,nt,rt=/^(?:toggle|show|hide)$/,it=/queueHooks$/;function ot(){nt&&(!1===v.hidden&&e.requestAnimationFrame?e.requestAnimationFrame(ot):e.setTimeout(ot,w.fx.interval),w.fx.tick())}function st(){return e.setTimeout((function(){tt=void 0})),tt=Date.now()}function at(e,t){var n,r=0,i={height:e};for(t=t?1:0;r<4;r+=2-t)i["margin"+(n=ne[r])]=i["padding"+n]=e;return t&&(i.opacity=i.width=e),i}function ut(e,t,n){for(var r,i=(lt.tweeners[t]||[]).concat(lt.tweeners["*"]),o=0,s=i.length;o<s;o++)if(r=i[o].call(n,t,e))return r}function lt(e,t,n){var r,i,o=0,s=lt.prefilters.length,a=w.Deferred().always((function(){delete u.elem})),u=function(){if(i)return!1;for(var t=tt||st(),n=Math.max(0,l.startTime+l.duration-t),r=1-(n/l.duration||0),o=0,s=l.tweens.length;o<s;o++)l.tweens[o].run(r);return a.notifyWith(e,[l,r,n]),r<1&&s?n:(s||a.notifyWith(e,[l,1,0]),a.resolveWith(e,[l]),!1)},l=a.promise({elem:e,props:w.extend({},t),opts:w.extend(!0,{specialEasing:{},easing:w.easing._default},n),originalProperties:t,originalOptions:n,startTime:tt||st(),duration:n.duration,tweens:[],createTween:function(t,n){var r=w.Tween(e,l.opts,t,n,l.opts.specialEasing[t]||l.opts.easing);return l.tweens.push(r),r},stop:function(t){var n=0,r=t?l.tweens.length:0;if(i)return this;for(i=!0;n<r;n++)l.tweens[n].run(1);return t?(a.notifyWith(e,[l,1,0]),a.resolveWith(e,[l,t])):a.rejectWith(e,[l,t]),this}}),c=l.props;for(!function(e,t){var n,r,i,o,s;for(n in e)if(i=t[r=X(n)],o=e[n],Array.isArray(o)&&(i=o[1],o=e[n]=o[0]),n!==r&&(e[r]=o,delete e[n]),(s=w.cssHooks[r])&&"expand"in s)for(n in o=s.expand(o),delete e[r],o)n in e||(e[n]=o[n],t[n]=i);else t[r]=i}(c,l.opts.specialEasing);o<s;o++)if(r=lt.prefilters[o].call(l,e,c,l.opts))return h(r.stop)&&(w._queueHooks(l.elem,l.opts.queue).stop=r.stop.bind(r)),r;return w.map(c,ut,l),h(l.opts.start)&&l.opts.start.call(e,l),l.progress(l.opts.progress).done(l.opts.done,l.opts.complete).fail(l.opts.fail).always(l.opts.always),w.fx.timer(w.extend(u,{elem:e,anim:l,queue:l.opts.queue})),l}w.Animation=w.extend(lt,{tweeners:{"*":[function(e,t){var n=this.createTween(e,t);return ae(n.elem,e,te.exec(t),n),n}]},tweener:function(e,t){h(e)?(t=e,e=["*"]):e=e.match(P);for(var n,r=0,i=e.length;r<i;r++)n=e[r],lt.tweeners[n]=lt.tweeners[n]||[],lt.tweeners[n].unshift(t)},prefilters:[function(e,t,n){var r,i,o,s,a,u,l,c,f="width"in t||"height"in t,p=this,d={},h=e.style,g=e.nodeType&&se(e),v=Q.get(e,"fxshow");for(r in n.queue||(null==(s=w._queueHooks(e,"fx")).unqueued&&(s.unqueued=0,a=s.empty.fire,s.empty.fire=function(){s.unqueued||a()}),s.unqueued++,p.always((function(){p.always((function(){s.unqueued--,w.queue(e,"fx").length||s.empty.fire()}))}))),t)if(i=t[r],rt.test(i)){if(delete t[r],o=o||"toggle"===i,i===(g?"hide":"show")){if("show"!==i||!v||void 0===v[r])continue;g=!0}d[r]=v&&v[r]||w.style(e,r)}if((u=!w.isEmptyObject(t))||!w.isEmptyObject(d))for(r in f&&1===e.nodeType&&(n.overflow=[h.overflow,h.overflowX,h.overflowY],null==(l=v&&v.display)&&(l=Q.get(e,"display")),"none"===(c=w.css(e,"display"))&&(l?c=l:(ce([e],!0),l=e.style.display||l,c=w.css(e,"display"),ce([e]))),("inline"===c||"inline-block"===c&&null!=l)&&"none"===w.css(e,"float")&&(u||(p.done((function(){h.display=l})),null==l&&(c=h.display,l="none"===c?"":c)),h.display="inline-block")),n.overflow&&(h.overflow="hidden",p.always((function(){h.overflow=n.overflow[0],h.overflowX=n.overflow[1],h.overflowY=n.overflow[2]}))),u=!1,d)u||(v?"hidden"in v&&(g=v.hidden):v=Q.access(e,"fxshow",{display:l}),o&&(v.hidden=!g),g&&ce([e],!0),p.done((function(){for(r in g||ce([e]),Q.remove(e,"fxshow"),d)w.style(e,r,d[r])}))),u=ut(g?v[r]:0,r,p),r in v||(v[r]=u.start,g&&(u.end=u.start,u.start=0))}],prefilter:function(e,t){t?lt.prefilters.unshift(e):lt.prefilters.push(e)}}),w.speed=function(e,t,n){var r=e&&"object"==typeof e?w.extend({},e):{complete:n||!n&&t||h(e)&&e,duration:e,easing:n&&t||t&&!h(t)&&t};return w.fx.off?r.duration=0:"number"!=typeof r.duration&&(r.duration in w.fx.speeds?r.duration=w.fx.speeds[r.duration]:r.duration=w.fx.speeds._default),null!=r.queue&&!0!==r.queue||(r.queue="fx"),r.old=r.complete,r.complete=function(){h(r.old)&&r.old.call(this),r.queue&&w.dequeue(this,r.queue)},r},w.fn.extend({animate:function(e,t,n,r){var i=w.isEmptyObject(e),o=w.speed(t,n,r),s=function(){var t=lt(this,w.extend({},e),o);(i||Q.get(this,"finish"))&&t.stop(!0)};return s.finish=s,i||!1===o.queue?this.each(s):this.queue(o.queue,s)},stop:function(e,t,n){var r=function(e){var t=e.stop;delete e.stop,t(n)};return"string"!=typeof e&&(n=t,t=e,e=void 0),t&&this.queue(e||"fx",[]),this.each((function(){var t=!0,i=null!=e&&e+"queueHooks",o=w.timers,s=Q.get(this);if(i)s[i]&&s[i].stop&&r(s[i]);else for(i in s)s[i]&&s[i].stop&&it.test(i)&&r(s[i]);for(i=o.length;i--;)o[i].elem!==this||null!=e&&o[i].queue!==e||(o[i].anim.stop(n),t=!1,o.splice(i,1));!t&&n||w.dequeue(this,e)}))},finish:function(e){return!1!==e&&(e=e||"fx"),this.each((function(){var t,n=Q.get(this),r=n[e+"queue"],i=n[e+"queueHooks"],o=w.timers,s=r?r.length:0;for(n.finish=!0,w.queue(this,e,[]),i&&i.stop&&i.stop.call(this,!0),t=o.length;t--;)o[t].elem===this&&o[t].queue===e&&(o[t].anim.stop(!0),o.splice(t,1));for(t=0;t<s;t++)r[t]&&r[t].finish&&r[t].finish.call(this);delete n.finish}))}}),w.each(["toggle","show","hide"],(function(e,t){var n=w.fn[t];w.fn[t]=function(e,r,i){return null==e||"boolean"==typeof e?n.apply(this,arguments):this.animate(at(t,!0),e,r,i)}})),w.each({slideDown:at("show"),slideUp:at("hide"),slideToggle:at("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},(function(e,t){w.fn[e]=function(e,n,r){return this.animate(t,e,n,r)}})),w.timers=[],w.fx.tick=function(){var e,t=0,n=w.timers;for(tt=Date.now();t<n.length;t++)(e=n[t])()||n[t]!==e||n.splice(t--,1);n.length||w.fx.stop(),tt=void 0},w.fx.timer=function(e){w.timers.push(e),w.fx.start()},w.fx.interval=13,w.fx.start=function(){nt||(nt=!0,ot())},w.fx.stop=function(){nt=null},w.fx.speeds={slow:600,fast:200,_default:400},w.fn.delay=function(t,n){return t=w.fx&&w.fx.speeds[t]||t,n=n||"fx",this.queue(n,(function(n,r){var i=e.setTimeout(n,t);r.stop=function(){e.clearTimeout(i)}}))},function(){var e=v.createElement("input"),t=v.createElement("select").appendChild(v.createElement("option"));e.type="checkbox",d.checkOn=""!==e.value,d.optSelected=t.selected,(e=v.createElement("input")).value="t",e.type="radio",d.radioValue="t"===e.value}();var ct,ft=w.expr.attrHandle;w.fn.extend({attr:function(e,t){return $(this,w.attr,e,t,arguments.length>1)},removeAttr:function(e){return this.each((function(){w.removeAttr(this,e)}))}}),w.extend({attr:function(e,t,n){var r,i,o=e.nodeType;if(3!==o&&8!==o&&2!==o)return void 0===e.getAttribute?w.prop(e,t,n):(1===o&&w.isXMLDoc(e)||(i=w.attrHooks[t.toLowerCase()]||(w.expr.match.bool.test(t)?ct:void 0)),void 0!==n?null===n?void w.removeAttr(e,t):i&&"set"in i&&void 0!==(r=i.set(e,n,t))?r:(e.setAttribute(t,n+""),n):i&&"get"in i&&null!==(r=i.get(e,t))?r:null==(r=w.find.attr(e,t))?void 0:r)},attrHooks:{type:{set:function(e,t){if(!d.radioValue&&"radio"===t&&N(e,"input")){var n=e.value;return e.setAttribute("type",t),n&&(e.value=n),t}}}},removeAttr:function(e,t){var n,r=0,i=t&&t.match(P);if(i&&1===e.nodeType)for(;n=i[r++];)e.removeAttribute(n)}}),ct={set:function(e,t,n){return!1===t?w.removeAttr(e,n):e.setAttribute(n,n),n}},w.each(w.expr.match.bool.source.match(/\w+/g),(function(e,t){var n=ft[t]||w.find.attr;ft[t]=function(e,t,r){var i,o,s=t.toLowerCase();return r||(o=ft[s],ft[s]=i,i=null!=n(e,t,r)?s:null,ft[s]=o),i}}));var pt=/^(?:input|select|textarea|button)$/i,dt=/^(?:a|area)$/i;function ht(e){return(e.match(P)||[]).join(" ")}function gt(e){return e.getAttribute&&e.getAttribute("class")||""}function vt(e){return Array.isArray(e)?e:"string"==typeof e&&e.match(P)||[]}w.fn.extend({prop:function(e,t){return $(this,w.prop,e,t,arguments.length>1)},removeProp:function(e){return this.each((function(){delete this[w.propFix[e]||e]}))}}),w.extend({prop:function(e,t,n){var r,i,o=e.nodeType;if(3!==o&&8!==o&&2!==o)return 1===o&&w.isXMLDoc(e)||(t=w.propFix[t]||t,i=w.propHooks[t]),void 0!==n?i&&"set"in i&&void 0!==(r=i.set(e,n,t))?r:e[t]=n:i&&"get"in i&&null!==(r=i.get(e,t))?r:e[t]},propHooks:{tabIndex:{get:function(e){var t=w.find.attr(e,"tabindex");return t?parseInt(t,10):pt.test(e.nodeName)||dt.test(e.nodeName)&&e.href?0:-1}}},propFix:{for:"htmlFor",class:"className"}}),d.optSelected||(w.propHooks.selected={get:function(e){var t=e.parentNode;return t&&t.parentNode&&t.parentNode.selectedIndex,null},set:function(e){var t=e.parentNode;t&&(t.selectedIndex,t.parentNode&&t.parentNode.selectedIndex)}}),w.each(["tabIndex","readOnly","maxLength","cellSpacing","cellPadding","rowSpan","colSpan","useMap","frameBorder","contentEditable"],(function(){w.propFix[this.toLowerCase()]=this})),w.fn.extend({addClass:function(e){var t,n,r,i,o,s,a,u=0;if(h(e))return this.each((function(t){w(this).addClass(e.call(this,t,gt(this)))}));if((t=vt(e)).length)for(;n=this[u++];)if(i=gt(n),r=1===n.nodeType&&" "+ht(i)+" "){for(s=0;o=t[s++];)r.indexOf(" "+o+" ")<0&&(r+=o+" ");i!==(a=ht(r))&&n.setAttribute("class",a)}return this},removeClass:function(e){var t,n,r,i,o,s,a,u=0;if(h(e))return this.each((function(t){w(this).removeClass(e.call(this,t,gt(this)))}));if(!arguments.length)return this.attr("class","");if((t=vt(e)).length)for(;n=this[u++];)if(i=gt(n),r=1===n.nodeType&&" "+ht(i)+" "){for(s=0;o=t[s++];)for(;r.indexOf(" "+o+" ")>-1;)r=r.replace(" "+o+" "," ");i!==(a=ht(r))&&n.setAttribute("class",a)}return this}});var mt=/\r/g;w.fn.extend({val:function(e){var t,n,r,i=this[0];return arguments.length?(r=h(e),this.each((function(n){var i;1===this.nodeType&&(null==(i=r?e.call(this,n,w(this).val()):e)?i="":"number"==typeof i?i+="":Array.isArray(i)&&(i=w.map(i,(function(e){return null==e?"":e+""}))),(t=w.valHooks[this.type]||w.valHooks[this.nodeName.toLowerCase()])&&"set"in t&&void 0!==t.set(this,i,"value")||(this.value=i))}))):i?(t=w.valHooks[i.type]||w.valHooks[i.nodeName.toLowerCase()])&&"get"in t&&void 0!==(n=t.get(i,"value"))?n:"string"==typeof(n=i.value)?n.replace(mt,""):null==n?"":n:void 0}}),w.extend({valHooks:{option:{get:function(e){var t=w.find.attr(e,"value");return null!=t?t:ht(w.text(e))}},select:{get:function(e){var t,n,r,i=e.options,o=e.selectedIndex,s="select-one"===e.type,a=s?null:[],u=s?o+1:i.length;for(r=o<0?u:s?o:0;r<u;r++)if(((n=i[r]).selected||r===o)&&!n.disabled&&(!n.parentNode.disabled||!N(n.parentNode,"optgroup"))){if(t=w(n).val(),s)return t;a.push(t)}return a},set:function(e,t){for(var n,r,i=e.options,o=w.makeArray(t),s=i.length;s--;)((r=i[s]).selected=w.inArray(w.valHooks.option.get(r),o)>-1)&&(n=!0);return n||(e.selectedIndex=-1),o}}}}),w.each(["radio","checkbox"],(function(){w.valHooks[this]={set:function(e,t){if(Array.isArray(t))return e.checked=w.inArray(w(e).val(),t)>-1}},d.checkOn||(w.valHooks[this].get=function(e){return null===e.getAttribute("value")?"on":e.value})})),d.focusin="onfocusin"in e;var yt=/^(?:focusinfocus|focusoutblur)$/,xt=function(e){e.stopPropagation()};w.extend(w.event,{trigger:function(t,n,r,i){var o,s,a,u,l,f,p,d,m=[r||v],y=c.call(t,"type")?t.type:t,x=c.call(t,"namespace")?t.namespace.split("."):[];if(s=d=a=r=r||v,3!==r.nodeType&&8!==r.nodeType&&!yt.test(y+w.event.triggered)&&(y.indexOf(".")>-1&&(x=y.split("."),y=x.shift(),x.sort()),l=y.indexOf(":")<0&&"on"+y,(t=t[w.expando]?t:new w.Event(y,"object"==typeof t&&t)).isTrigger=i?2:3,t.namespace=x.join("."),t.rnamespace=t.namespace?new RegExp("(^|\\.)"+x.join("\\.(?:.*\\.|)")+"(\\.|$)"):null,t.result=void 0,t.target||(t.target=r),n=null==n?[t]:w.makeArray(n,[t]),p=w.event.special[y]||{},i||!p.trigger||!1!==p.trigger.apply(r,n))){if(!i&&!p.noBubble&&!g(r)){for(u=p.delegateType||y,yt.test(u+y)||(s=s.parentNode);s;s=s.parentNode)m.push(s),a=s;a===(r.ownerDocument||v)&&m.push(a.defaultView||a.parentWindow||e)}for(o=0;(s=m[o++])&&!t.isPropagationStopped();)d=s,t.type=o>1?u:p.bindType||y,(f=(Q.get(s,"events")||Object.create(null))[t.type]&&Q.get(s,"handle"))&&f.apply(s,n),(f=l&&s[l])&&f.apply&&V(s)&&(t.result=f.apply(s,n),!1===t.result&&t.preventDefault());return t.type=y,i||t.isDefaultPrevented()||p._default&&!1!==p._default.apply(m.pop(),n)||!V(r)||l&&h(r[y])&&!g(r)&&((a=r[l])&&(r[l]=null),w.event.triggered=y,t.isPropagationStopped()&&d.addEventListener(y,xt),r[y](),t.isPropagationStopped()&&d.removeEventListener(y,xt),w.event.triggered=void 0,a&&(r[l]=a)),t.result}},simulate:function(e,t,n){var r=w.extend(new w.Event,n,{type:e,isSimulated:!0});w.event.trigger(r,null,t)}}),w.fn.extend({trigger:function(e,t){return this.each((function(){w.event.trigger(e,t,this)}))}}),d.focusin||w.each({focus:"focusin",blur:"focusout"},(function(e,t){var n=function(e){w.event.simulate(t,e.target,w.event.fix(e))};w.event.special[t]={setup:function(){var r=this.ownerDocument||this.document||this,i=Q.access(r,t);i||r.addEventListener(e,n,!0),Q.access(r,t,(i||0)+1)},teardown:function(){var r=this.ownerDocument||this.document||this,i=Q.access(r,t)-1;i?Q.access(r,t,i):(r.removeEventListener(e,n,!0),Q.remove(r,t))}}}));var bt=e.location,wt=(Date.now(),/\[\]$/),Tt=/\r?\n/g,Ct=/^(?:submit|button|image|reset|file)$/i,Et=/^(?:input|select|textarea|keygen)/i;function kt(e,t,n,r){var i;if(Array.isArray(t))w.each(t,(function(t,i){n||wt.test(e)?r(e,i):kt(e+"["+("object"==typeof i&&null!=i?t:"")+"]",i,n,r)}));else if(n||"object"!==x(t))r(e,t);else for(i in t)kt(e+"["+i+"]",t[i],n,r)}w.param=function(e,t){var n,r=[],i=function(e,t){var n=h(t)?t():t;r[r.length]=encodeURIComponent(e)+"="+encodeURIComponent(null==n?"":n)};if(null==e)return"";if(Array.isArray(e)||e.jquery&&!w.isPlainObject(e))w.each(e,(function(){i(this.name,this.value)}));else for(n in e)kt(n,e[n],t,i);return r.join("&")},w.fn.extend({serialize:function(){return w.param(this.serializeArray())},serializeArray:function(){return this.map((function(){var e=w.prop(this,"elements");return e?w.makeArray(e):this})).filter((function(){var e=this.type;return this.name&&!w(this).is(":disabled")&&Et.test(this.nodeName)&&!Ct.test(e)&&(this.checked||!de.test(e))})).map((function(e,t){var n=w(this).val();return null==n?null:Array.isArray(n)?w.map(n,(function(e){return{name:t.name,value:e.replace(Tt,"\r\n")}})):{name:t.name,value:n.replace(Tt,"\r\n")}})).get()}});"*/".concat("*");v.createElement("a").href=bt.href,w.fn.extend({wrapAll:function(e){var t;return this[0]&&(h(e)&&(e=e.call(this[0])),t=w(e,this[0].ownerDocument).eq(0).clone(!0),this[0].parentNode&&t.insertBefore(this[0]),t.map((function(){for(var e=this;e.firstElementChild;)e=e.firstElementChild;return e})).append(this)),this},wrapInner:function(e){return h(e)?this.each((function(t){w(this).wrapInner(e.call(this,t))})):this.each((function(){var t=w(this),n=t.contents();n.length?n.wrapAll(e):t.append(e)}))},wrap:function(e){var t=h(e);return this.each((function(n){w(this).wrapAll(t?e.call(this,n):e)}))},unwrap:function(e){return this.parent(e).not("body").each((function(){w(this).replaceWith(this.childNodes)})),this}}),w.expr.pseudos.hidden=function(e){return!w.expr.pseudos.visible(e)},w.expr.pseudos.visible=function(e){return!!(e.offsetWidth||e.offsetHeight||e.getClientRects().length)};var At;d.createHTMLDocument=((At=v.implementation.createHTMLDocument("").body).innerHTML="<form></form><form></form>",2===At.childNodes.length),w.parseHTML=function(e,t,n){return"string"!=typeof e?[]:("boolean"==typeof t&&(n=t,t=!1),t||(d.createHTMLDocument?((r=(t=v.implementation.createHTMLDocument("")).createElement("base")).href=v.location.href,t.head.appendChild(r)):t=v),o=!n&&[],(i=S.exec(e))?[t.createElement(i[1])]:(i=be([e],t,o),o&&o.length&&w(o).remove(),w.merge([],i.childNodes)));var r,i,o},w.expr.pseudos.animated=function(e){return w.grep(w.timers,(function(t){return e===t.elem})).length},w.offset={setOffset:function(e,t,n){var r,i,o,s,a,u,l=w.css(e,"position"),c=w(e),f={};"static"===l&&(e.style.position="relative"),a=c.offset(),o=w.css(e,"top"),u=w.css(e,"left"),("absolute"===l||"fixed"===l)&&(o+u).indexOf("auto")>-1?(s=(r=c.position()).top,i=r.left):(s=parseFloat(o)||0,i=parseFloat(u)||0),h(t)&&(t=t.call(e,n,w.extend({},a))),null!=t.top&&(f.top=t.top-a.top+s),null!=t.left&&(f.left=t.left-a.left+i),"using"in t?t.using.call(e,f):c.css(f)}},w.fn.extend({offset:function(e){if(arguments.length)return void 0===e?this:this.each((function(t){w.offset.setOffset(this,e,t)}));var t,n,r=this[0];return r?r.getClientRects().length?(t=r.getBoundingClientRect(),n=r.ownerDocument.defaultView,{top:t.top+n.pageYOffset,left:t.left+n.pageXOffset}):{top:0,left:0}:void 0},position:function(){if(this[0]){var e,t,n,r=this[0],i={top:0,left:0};if("fixed"===w.css(r,"position"))t=r.getBoundingClientRect();else{for(t=this.offset(),n=r.ownerDocument,e=r.offsetParent||n.documentElement;e&&(e===n.body||e===n.documentElement)&&"static"===w.css(e,"position");)e=e.parentNode;e&&e!==r&&1===e.nodeType&&((i=w(e).offset()).top+=w.css(e,"borderTopWidth",!0),i.left+=w.css(e,"borderLeftWidth",!0))}return{top:t.top-i.top-w.css(r,"marginTop",!0),left:t.left-i.left-w.css(r,"marginLeft",!0)}}},offsetParent:function(){return this.map((function(){for(var e=this.offsetParent;e&&"static"===w.css(e,"position");)e=e.offsetParent;return e||re}))}}),w.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},(function(e,t){var n="pageYOffset"===t;w.fn[e]=function(r){return $(this,(function(e,r,i){var o;if(g(e)?o=e:9===e.nodeType&&(o=e.defaultView),void 0===i)return o?o[t]:e[r];o?o.scrollTo(n?o.pageXOffset:i,n?i:o.pageYOffset):e[r]=i}),e,r,arguments.length)}})),w.each(["top","left"],(function(e,t){w.cssHooks[t]=$e(d.pixelPosition,(function(e,n){if(n)return n=Fe(e,t),Re.test(n)?w(e).position()[t]+"px":n}))})),w.each({Height:"height",Width:"width"},(function(e,t){w.each({padding:"inner"+e,content:t,"":"outer"+e},(function(n,r){w.fn[r]=function(i,o){var s=arguments.length&&(n||"boolean"!=typeof i),a=n||(!0===i||!0===o?"margin":"border");return $(this,(function(t,n,i){var o;return g(t)?0===r.indexOf("outer")?t["inner"+e]:t.document.documentElement["client"+e]:9===t.nodeType?(o=t.documentElement,Math.max(t.body["scroll"+e],o["scroll"+e],t.body["offset"+e],o["offset"+e],o["client"+e])):void 0===i?w.css(t,n,a):w.style(t,n,i,a)}),t,s?i:void 0,s)}}))})),w.fn.extend({bind:function(e,t,n){return this.on(e,null,t,n)},unbind:function(e,t){return this.off(e,null,t)},delegate:function(e,t,n,r){return this.on(t,e,n,r)},undelegate:function(e,t,n){return 1===arguments.length?this.off(e,"**"):this.off(t,e||"**",n)},hover:function(e,t){return this.mouseenter(e).mouseleave(t||e)}}),w.each("blur focus focusin focusout resize scroll click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup contextmenu".split(" "),(function(e,t){w.fn[t]=function(e,n){return arguments.length>0?this.on(t,null,e,n):this.trigger(t)}}));var Nt=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;w.isArray=Array.isArray,w.parseJSON=JSON.parse,w.nodeName=N,w.isFunction=h,w.isWindow=g,w.camelCase=X,w.type=x,w.now=Date.now,w.isNumeric=function(e){var t=w.type(e);return("number"===t||"string"===t)&&!isNaN(e-parseFloat(e))},w.trim=function(e){return null==e?"":(e+"").replace(Nt,"")},"function"==typeof define&&define.amd&&define("jquery",[],(function(){return w}));e.jQuery,e.$;return void 0===t&&(e.jQuery=e.$=w),w}));

(function (factory) {
	if (typeof define === "function" && define.amd) {
		define(["jquery"], factory)
	} else if (typeof exports !== "undefined") {
		module.exports = factory(require("jquery"))
	} else {
		factory(jQuery)
	}
})

	(function ($) {
		var EasyWheel = window.EasyWheel || {};
		EasyWheel = function () {
			var instanceUid = 0;

			function EasyWheel(element, settings) {
				var self = this,
					dataSettings;
				self.defaults = {
					items: [],
					width: 400,
					fontSize: 14,
					textOffset: 8,
					textLine: "h",
					textArc: false,
					letterSpacing: 0,
					textColor: "#fff",
					centerWidth: 45,
					shadow: "", // '#fff0', // egemen
					shadowOpacity: 0,
					centerLineWidth: 5,
					centerLineColor: "#424242",
					centerBackground: "#8e44ad",
					sliceLineWidth: 0,
					sliceLineColor: "#424242",
					selectedSliceColor: "#333",
					outerLineColor: "#424242",
					outerLineWidth: 5,
					centerImage: "",
					centerHtml: "",
					centerHtmlWidth: 45,
					centerImageWidth: 45,
					rotateCenter: false,
					easing: "easyWheel",
					markerAnimation: true,
					markerColor: "#CC3333",
					selector: "win",
					selected: [true],
					random: false,
					type: "spin",
					duration: 8000,
					rotates: 8,
					max: 0,
					frame: 1,
					onStart: function (results, spinCount, now) { },
					onStep: function (results, slicePercent, circlePercent) { },
					onProgress: function (results, spinCount, now) { },
					onComplete: function (results, spinCount, now) { },
					onFail: function (results, spinCount, now) { }
				};
				dataSettings = $(element).data("easyWheel") || {};
				self.o = $.extend({}, self.defaults, settings, dataSettings);
				self.initials = {
					slice: {
						id: null,
						results: null
					},
					currentSliceData: {
						id: null,
						results: null
					},
					winner: 0,
					rotates: parseInt(self.o.rotates),
					spinCount: 0,
					counter: 0,
					now: 0,
					currentSlice: 0,
					currentStep: 0,
					lastStep: 0,
					slicePercent: 0,
					circlePercent: 0,
					items: self.o.items,
					spinning: false,
					inProgress: false,
					nonce: null,
					$wheel: $(element)
				};
				$.extend(self, self.initials);
				$.extend($.easing, {
					easyWheel: function (x, t, b, c, d) {
						return -c * ((t = t / d - 1) * t * t * t - 1) + b
					}
				});
				$.extend($.easing, {
					easeOutQuad: function (x, t, b, c, d) {
						return -c * (t /= d) * (t - 2) + b
					}
				});
				$.extend($.easing, {
					MarkerEasing: function (x) {
						var n = -Math.pow(1 - x * 6, 2) + 1;
						if (n < 0) n = 0;
						return n
					}
				});
				self.instanceUid = "ew" + self.guid();
				self.validate();
				self.init();
				window.easyWheel = self; // egemen
			}
			return EasyWheel
		}();
		EasyWheel.prototype.validate = function (args) {
			var self = this;
			if (self.rotates < 1) {
				self.rotates = 1
			}
			if (parseInt(self.o.sliceLineWidth) > 10) {
				self.o.sliceLineWidth = 10
			}
			if (parseInt(self.o.centerLineWidth) > 10) {
				self.o.centerLineWidth = 10
			}
			if (parseInt(self.o.outerLineWidth) > 10) {
				self.o.outerLineWidth = 10
			}
			if (typeof $.easing[$.trim(self.o.easing)] == "undefined") {
				self.o.easing = "easyWheel"
			}
		};
		EasyWheel.prototype.destroy = function (args) {
			var self = this;
			if (self.spinning) {
				self.spinning.finish()
			}
			if (typeof args === "boolean" && args === true) self.$wheel.html("").removeClass(self.instanceUid);
			$.extend(self.o, self.defaults);
			$.extend(self, self.initials);
			$(document).off("click." + self.instanceUid);
			$(document).off("resize." + self.instanceUid)
		};
		EasyWheel.prototype.option = function (option, newValue) {
			var self = this;
			if ($.inArray(typeof newValue, ["undefined", "function"]) !== -1 || $.inArray(typeof self.o[option], ["undefined", "function"]) !== -1) return;
			var allowed = ["easing", "type", "duration", "rotates", "max"];
			if ($.inArray(option, allowed) == -1) return;
			self.o[option] = newValue
		};
		EasyWheel.prototype.finish = function () {
			var self = this;
			if (self.spinning) {
				self.spinning.finish()
			}
		};
		EasyWheel.prototype.init = function () {
			var self = this;
			self.initialize();
			self.execute()
		};
		EasyWheel.prototype.initialize = function () {
			var self = this;
			self.$wheel.addClass("easyWheel " + self.instanceUid);
			var color = "#ccc";
			var arcSize = 360 / self.totalSlices();
			var pStart = 0;
			var pEnd = 0;
			var colorIndex = 0;
			self.$wheel.html("");
			var wrapper = $("<div/>").addClass("eWheel-wrapper").appendTo(self.$wheel);
			var inner = $("<div/>").addClass("eWheel-inner").appendTo(wrapper);
			var spinner = $("<div/>").addClass("eWheel").prependTo(inner);
			var Layerbg = $("<div/>").addClass("eWheel-bg-layer").appendTo(spinner);
			var Layersvg = $(self.SVG("svg", {
				"version": "1.1",
				"xmlns": "http://www.w3.org/2000/svg",
				"xmlns:xlink": "http://www.w3.org/1999/xlink",
				"x": "0px",
				"y": "0px",
				"viewBox": "0 0 200 200",
				"xml:space": "preserve",
				"style": "enable-background:new 0 0 200 200;"
			}));
			Layersvg.appendTo(Layerbg);
			var slicesGroup = $("<g/>");
			var smallCirclesGroup = $("<g/>");
			slicesGroup.addClass("ew-slicesGroup").appendTo(Layersvg);
			if (self.o.textLine === "v" || self.o.textLine === "vertical") {
				var Layertext = $("<div/>");
				Layertext.addClass("eWheel-txt-wrap");
				Layertext.appendTo(spinner);
				var textHtml = $("<div/>");
				textHtml.addClass("eWheel-txt");
				textHtml.css({
					"-webkit-transform": "rotate(" + (-(360 / self.totalSlices()) / 2 + self.getDegree()) + "deg)",
					"-moz-transform": "rotate(" + (-(360 / self.totalSlices()) / 2 + self.getDegree()) + "deg)",
					"-ms-transform": "rotate(" + (-(360 / self.totalSlices()) / 2 + self.getDegree()) + "deg)",
					"-o-transform": "rotate(" + (-(360 / self.totalSlices()) / 2 + self.getDegree()) + "deg)",
					"transform": "rotate(" + (-(360 / self.totalSlices()) / 2 + self.getDegree()) + "deg)"
				});
				textHtml.appendTo(Layertext)
			} else {
				var textsGroup = $("<g/>");
				var LayerDefs = $("<defs/>");
				LayerDefs.appendTo(Layersvg);
				textsGroup.appendTo(Layersvg)
			}
			var Layercenter = $("<div/>");
			Layercenter.addClass("eWheel-center");
			Layercenter.appendTo(self.o.rotateCenter === true || self.o.rotateCenter === "true" ? spinner : inner);
			if (typeof self.o.centerHtml === "string" && $.trim(self.o.centerHtml) === "" && typeof self.o.centerImage === "string" && $.trim(self.o.centerImage) !== "") {
				var centerImage = $("<img />");
				if (!parseInt(self.o.centerImageWidth)) self.o.centerImageWidth = parseInt(self.o.centerWidth);
				centerImage.css("width", parseInt(self.o.centerImageWidth - 3) + "%");
				centerImage.attr("src", self.o.centerImage);
				centerImage.appendTo(Layercenter);
				Layercenter.append("<div class=\"ew-center-empty\" style=\"width:" + parseInt(self.o.centerImageWidth) + "%; height:" + parseInt(self.o.centerImageWidth) + "%\" />")
			}
			if (typeof self.o.centerHtml === "string" && $.trim(self.o.centerHtml) !== "") {
				var centerHtml = $("<div class=\"ew-center-html\">" + self.o.centerHtml + "</div>");
				if (!parseInt(self.o.centerHtmlWidth)) self.o.centerHtmlWidth = parseInt(self.o.centerWidth);
				centerHtml.css({
					"width": parseInt(self.o.centerHtmlWidth) + "%",
					"height": parseInt(self.o.centerHtmlWidth) + "%"
				});
				centerHtml.appendTo(Layercenter)
			}
			var Layermarker = false;
			if ($.trim(self.o.type) !== "color") {
				Layermarker = $("<div/>").addClass("eWheel-marker").appendTo(wrapper);
				Layermarker.append("<svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" viewBox=\"0 0 80 115\" style=\"enable-background:new 0 0 80 115;\" xml:space=\"preserve\">" + "<g>" + "<circle cx=\"40\" cy=\"25\" r=\"25\" fill=\"" + self.o.markerColor + "\" />" + "<path d=\"M41.5,103.1L63,34.5c0.3-1-0.4-2-1.5-2h-43c-1,0-1.8,1-1.5,2l21.5,68.6C39,104.5,41,104.5,41.5,103.1z\" fill=\"" + self.o.markerColor + "\" />" + "<circle cx=\"40\" cy=\"25\" r=\"10\" fill=\"#ffffff\" />" + "</g>" + "</svg>")
			}
			$.each(self.items, function (i, item) {
				var rotate = 360 / self.totalSlices() * i;
				pEnd += arcSize;
				var arcD = self.annularSector({
					centerX: 100,
					centerY: 100,
					startDegrees: pStart,
					endDegrees: pEnd,
					innerRadius: parseInt(self.o.centerWidth) + 0.5,
					outerRadius: 100.5 - parseInt(self.o.outerLineWidth)
				});
				slicesGroup.append(self.SVG("path", {
					"stroke-width": 0,
					"fill": item.color,
					"data-fill": item.color,
					"d": arcD
				}));
				var smallCirclePosition = self.getSmallCirclePosition({
					centerX: 100,
					centerY: 100,
					startDegrees: pEnd - 0.5,
					endDegrees: pEnd + 0.5,
					innerRadius: parseInt(self.o.centerWidth),
					outerRadius: 100.5 - parseInt(self.o.outerLineWidth) / 1.5
				});
				var rectWidth = 10;
				var rectHeight = 3;
				var rectRotate = rotate + 360 / self.items.length;
				smallCirclesGroup.append(self.SVG("rect", {
					"x": smallCirclePosition[0] - 1.5,
					"y": smallCirclePosition[1] - 1.5,
					"rx": 5,
					"ry": 5,
					"width": rectWidth,
					"height": rectHeight,
					"style": "fill:rgb(255,255,255);fill-opacity:1;",
					"transform": "rotate(" + rectRotate + " " + smallCirclePosition[0] + " " + smallCirclePosition[1] + ")"
				}));
				smallCirclesGroup.append(self.SVG("circle", {
					"cx": smallCirclePosition[0],
					"cy": smallCirclePosition[1],
					"r": 1.5,
					"fill": "black",
					"fill-opacity": 1
				}));
				var textColor = $.trim(self.o.textColor) !== "auto" ? $.trim(self.o.textColor) : self.brightness(item.color);
				if (self.o.textLine === "v" || self.o.textLine === "vertical") {
					var LayerTitle = $("<div/>");
					LayerTitle.addClass("eWheel-title");
					LayerTitle.html(item.name);
					LayerTitle.css({
						paddingRight: parseInt(self.o.textOffset) + "%",
						"-webkit-transform": "rotate(" + rotate + "deg) translate(0px, -50%)",
						"-moz-transform": "rotate(" + rotate + "deg) translate(0px, -50%)",
						"-ms-transform": "rotate(" + rotate + "deg) translate(0px, -50%)",
						"-o-transform": "rotate(" + rotate + "deg) translate(0px, -50%)",
						"transform": "rotate(" + rotate + "deg) translate(0px, -50%)",
						"color": textColor
					});
					LayerTitle.appendTo(textHtml);
					if (self.toNumber(self.o.letterSpacing) > 0) textHtml.css("letter-spacing", self.toNumber(self.o.letterSpacing))
				} else {
					var LayerText = "<text stroke-width=\"0\" fill=\"" + textColor + "\" dy=\"" + self.toNumber(self.o.textOffset) + "%\">" + "<textPath xlink:href=\"#ew-text-" + i + "\" startOffset=\"50%\" style=\"text-anchor: middle;\">" + item.name + "</textPath>" + "</text>";
					textsGroup.css("font-size", parseInt(self.o.fontSize) / 2);
					if (parseInt(self.o.letterSpacing) > 0) textsGroup.css("letter-spacing", parseInt(self.o.letterSpacing));
					textsGroup.append(LayerText);
					var firstArcSection = /(^.+?)L/;
					var newD = firstArcSection.exec(arcD)[1];
					if (self.o.textArc !== true) {
						var secArcSection = /(^.+?)A/;
						var Commas = /(^.+?),/;
						var newc = secArcSection.exec(newD);
						var replaceVal = newD.replace(newc[0], "");
						var getFirstANumber = Commas.exec(replaceVal);
						var nval = replaceVal.replace(getFirstANumber[1], 0);
						newD = newD.replace(replaceVal, nval)
					}
					LayerDefs.append(self.SVG("path", {
						"stroke-width": 0,
						"fill": "none",
						"id": "ew-text-" + i,
						"d": newD
					}))
				}
				var LayerTitleInner = $("<div/>");
				LayerTitleInner.html(item);
				LayerTitleInner.appendTo(LayerTitle);
				pStart += arcSize
			});
			var sliceLineWidth = parseInt(self.o.sliceLineWidth);
			if (self.o.textLine !== "v" || self.o.textLine !== "vertical") { }
			if (parseInt(self.o.centerWidth) > sliceLineWidth) {
				var centerCircle = self.SVG("circle", {
					"class": "centerCircle",
					"cx": "100",
					"cy": "100",
					"r": parseInt(self.o.centerWidth) + 1,
					"stroke": self.o.centerLineColor,
					"stroke-width": parseInt(self.o.centerLineWidth),
					"fill": self.o.centerBackground
				});
				$(centerCircle).appendTo(Layersvg)
			}
			var outerLine = self.SVG("circle", {
				"cx": "100",
				"cy": "100",
				"r": 100 - parseInt(self.o.outerLineWidth) / 2,
				"stroke": self.o.outerLineColor,
				"stroke-width": parseInt(self.o.outerLineWidth),
				"fill-opacity": 0,
				"fill": "#fff0"
			});
			$(outerLine).appendTo(Layersvg);
			smallCirclesGroup.addClass("ew-smallCirclesGroup").appendTo(Layersvg);
			Layerbg.html(Layerbg.html())
		};
		EasyWheel.prototype.toNumber = function (e) {
			return NaN === Number(e) ? 0 : Number(e)
		};
		EasyWheel.prototype.SVG = function (e, t) {
			var r = document.createElementNS("http://www.w3.org/2000/svg", e);
			for (var n in t) r.setAttribute(n, t[n]);
			return r
		};
		EasyWheel.prototype.getSmallCirclePosition = function (options) {
			var self = this;
			var lineSpace = parseInt(self.o.sliceLineWidth);
			var opts = self.oWithDefaults(options);
			return [opts.cx + opts.r2 * Math.cos((options.startDegrees + lineSpace / 4) * Math.PI / 180), opts.cy + opts.r2 * Math.sin((options.startDegrees + lineSpace / 4) * Math.PI / 180)]
		};
		EasyWheel.prototype.annularSector = function (options, line) {
			var self = this;
			var lineSpace = parseInt(self.o.sliceLineWidth);
			var opts = self.oWithDefaults(options);
			var p = [
				[opts.cx + opts.r2 * Math.cos((options.startDegrees + lineSpace / 4) * Math.PI / 180), opts.cy + opts.r2 * Math.sin((options.startDegrees + lineSpace / 4) * Math.PI / 180)],
				[opts.cx + opts.r2 * Math.cos((options.endDegrees - lineSpace / 4) * Math.PI / 180), opts.cy + opts.r2 * Math.sin((options.endDegrees - lineSpace / 4) * Math.PI / 180)],
				[opts.cx + opts.r1 * Math.cos((options.endDegrees - lineSpace) * Math.PI / 180), opts.cy + opts.r1 * Math.sin((options.endDegrees - lineSpace) * Math.PI / 180)],
				[opts.cx + opts.r1 * Math.cos((options.startDegrees + lineSpace) * Math.PI / 180), opts.cy + opts.r1 * Math.sin((options.startDegrees + lineSpace) * Math.PI / 180)]
			];
			var angleDiff = opts.closeRadians - opts.startRadians;
			var largeArc = angleDiff % (Math.PI * 2) > Math.PI ? 1 : 0;
			var N1 = 1;
			var N2 = 0;
			if (line === true && lineSpace >= parseInt(self.o.centerWidth)) {
				N1 = 0;
				N2 = 1
			} else if (line !== true && lineSpace >= parseInt(self.o.centerWidth)) {
				N1 = 1;
				N2 = 1
			}
			var cmds = [];
			cmds.push("M" + p[0].join());
			cmds.push("A" + [opts.r2, opts.r2, 0, largeArc, N1, p[1]].join());
			cmds.push("L" + p[2].join());
			cmds.push("A" + [opts.r1, opts.r1, 0, largeArc, N2, p[3]].join());
			cmds.push("z");
			return cmds.join(" ")
		};
		EasyWheel.prototype.oWithDefaults = function (o) {
			var o2 = {
				cx: o.centerX || 0,
				cy: o.centerY || 0,
				startRadians: (o.startDegrees || 0) * Math.PI / 180,
				closeRadians: (o.endDegrees || 0) * Math.PI / 180
			};
			var t = o.thickness !== undefined ? o.thickness : 100;
			if (o.innerRadius !== undefined) o2.r1 = o.innerRadius;
			else if (o.outerRadius !== undefined) o2.r1 = o.outerRadius - t;
			else o2.r1 = 200 - t;
			if (o.outerRadius !== undefined) o2.r2 = o.outerRadius;
			else o2.r2 = o2.r1 + t;
			if (o2.r1 < 0) o2.r1 = 0;
			if (o2.r2 < 0) o2.r2 = 0;
			return o2
		};
		EasyWheel.prototype.brightness = function (c) {
			var r, g, b, brightness;
			if (c.match(/^rgb/)) {
				c = c.match(/rgba?\(([^)]+)\)/)[1];
				c = c.split(/ *, */).map(Number);
				r = c[0];
				g = c[1];
				b = c[2]
			} else if ("#" == c[0] && 7 == c.length) {
				r = parseInt(c.slice(1, 3), 16);
				g = parseInt(c.slice(3, 5), 16);
				b = parseInt(c.slice(5, 7), 16)
			} else if ("#" == c[0] && 4 == c.length) {
				r = parseInt(c[1] + c[1], 16);
				g = parseInt(c[2] + c[2], 16);
				b = parseInt(c[3] + c[3], 16)
			}
			brightness = (r * 299 + g * 587 + b * 114) / 1000;
			if (brightness < 125) {
				return "#fff"
			} else {
				return "#333"
			}
		};
		EasyWheel.prototype.guid = function (r) {
			var t = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
				a = "";
			r || (r = 8);
			for (var o = 0; o < r; o++) a += t.charAt(Math.floor(Math.random() * t.length));
			return a
		};
		EasyWheel.prototype.randomColor = function () {
			for (var o = "#", r = 0; r < 6; r++) o += "0123456789ABCDEF"[Math.floor(16 * Math.random())];
			return o
		};
		EasyWheel.prototype.FontScale = function (slice) {
			var self = this;
			var Fscale = 1 + 1 * (self.$wheel.width() - parseInt(self.o.width)) / parseInt(self.o.width);
			if (Fscale > 4) {
				Fscale = 4
			} else if (Fscale < 0.1) {
				Fscale = 0.1
			}
			self.$wheel.find(".eWheel-wrapper").css("font-size", Fscale * 100 + "%")
		};
		EasyWheel.prototype.numberToArray = function (N) {
			var args = [];
			var i;
			for (i = 0; i < N; i++) {
				args[i] = i
			}
			return args
		};
		EasyWheel.prototype.totalSlices = function () {
			var self = this;
			return self.items.length
		};
		EasyWheel.prototype.getDegree = function (id) {
			var self = this;
			return 360 / self.totalSlices()
		};
		EasyWheel.prototype.degStart = function (id) {
			var self = this;
			return 360 - self.getDegree() * id
		};
		EasyWheel.prototype.degEnd = function (id) {
			var self = this;
			return 360 - (self.getDegree() * id + self.getDegree())
		};
		EasyWheel.prototype.getRandomInt = function (min, max) {
			return Math.floor(Math.random() * (max - min + 1) + min)
		};
		EasyWheel.prototype.calcSliceSize = function (slice) {
			var self = this;
			var start = self.degStart(slice) - (parseInt(self.o.sliceLineWidth) + 2);
			var end = self.degEnd(slice) + (parseInt(self.o.sliceLineWidth) + 2);
			var results = [];
			results.start = start;
			results.end = end;
			return results
		};
		EasyWheel.prototype.toObject = function (arr) {
			var rv = {};
			for (var i = 0; i < arr.length; ++i)
				if (arr[i] !== undefined) rv[i] = arr[i];
			return rv
		};
		EasyWheel.prototype.WinnerSelector = function () {
			var self = this;
			var obj = {};
			if (typeof self.o.selector !== "string") return false;
			$.each(self.items, function (i, item) {
				if (typeof item[self.o.selector] === "object" || typeof item[self.o.selector] === "array" || typeof item[self.o.selector] === "undefined") return false;
				obj[i] = item[self.o.selector]
			});
			return obj
		};
		EasyWheel.prototype.findWinner = function (value, type) {
			var self = this;
			var obj = undefined;
			if (type !== "custom" && (typeof self.o.selector !== "string" || typeof value === "number")) {
				if (typeof self.items[value] === "undefined") return undefined;
				return value
			}
			$.each(self.items, function (i, item) {
				if (typeof item[self.o.selector] === "object" || typeof item[self.o.selector] === "array" || typeof item[self.o.selector] === "undefined") return undefined;
				if (item[self.o.selector] === value) {
					obj = i
				}
			});
			return obj
		};
		EasyWheel.prototype.selectedSliceID = function (index) {
			var self = this;
			var selector = self.WinnerSelector();
			self.selected = self.o.selected;
			if ($.type(self.selected) === "object") {
				if (typeof self.selected[0] !== "undefined" && self.selected[0].selectedIndex !== undefined) return self.selected[0].selectedIndex
			} else if ($.type(self.selected) === "array") {
				if (self.o.selector !== false) {
					self.selected = $.map(selector, function (value, i) {
						if (value === self.o.selected[index]) return i
					})
				} else {
					self.selected = self.selected[index]
				}
			} else if ($.type(self.selected) === "string" && self.o.selector !== false) {
				self.selected = self.findWinner(self.selected)
			} else if ($.type(self.selected) !== "number") {
				return
			}
			if (typeof self.findWinner(parseInt(self.selected)) === "undefined") return;
			return parseInt(self.selected)
		};
		EasyWheel.prototype.start = function () {
			var self = this;
			self.run()
		};
		EasyWheel.prototype.run = function (selectedWinner) {
			var self = this;
			if (self.inProgress) return;
			if (selectedWinner || selectedWinner === 0) {
				var winner = self.findWinner(selectedWinner, "custom");
				if (typeof winner !== "undefined") {
					self.slice.id = winner
				} else {
					return
				}
			} else {
				if (self.o.max !== 0 && self.counter >= self.o.max) return;
				if (self.o.selector !== false) {
					self.slice.id = self.selectedSliceID(0)
				} else if (self.o.random === true) {
					self.slice.id = self.getRandomInt(0, self.totalSlices() - 1)
				} else {
					return
				}
			}
			self.inProgress = true;
			if (typeof self.items[self.slice.id] === "undefined") return;
			self.slice.results = self.items[self.slice.id];
			self.slice.length = self.slice.id;
			self.o.onStart.call(self.$wheel, self.slice.results, self.spinCount, self.now);
			var selectedSlicePos = self.calcSliceSize(self.slice.id); //var randomize = self.getRandomInt(selectedSlicePos.start, selectedSlicePos.end); //egemen
			var randomize = selectedSlicePos.start + (selectedSlicePos.end - selectedSlicePos.start) / 2;
			var _deg = 360 * parseInt(self.rotates) + randomize;
			self.lastStep = -1;
			self.currentStep = 0;
			var MarkerAnimator = false;
			var MarkerStep;
			var currentSlice = 0;
			var temp = self.numberToArray(self.totalSlices()).reverse();
			if (parseInt(self.o.frame) !== 0) {
				var oldinterval = $.fx.interval;
				$.fx.interval = parseInt(self.o.frame)
			}
			self.spinning = $({
				deg: self.now
			}).animate({
				deg: _deg
			}, {
				duration: parseInt(self.o.duration),
				easing: $.trim(self.o.easing),
				step: function (now, fx) {
					if (parseInt(self.o.frame) !== 0) $.fx.interval = parseInt(self.o.frame);
					self.now = now % 360;
					if ($.trim(self.o.type) !== "color") {
						self.$wheel.find(".eWheel").css({
							"-webkit-transform": "rotate(" + self.now + "deg)",
							"-moz-transform": "rotate(" + self.now + "deg)",
							"-ms-transform": "rotate(" + self.now + "deg)",
							"-o-transform": "rotate(" + self.now + "deg)",
							"transform": "rotate(" + self.now + "deg)"
						})
					}
					self.currentStep = Math.floor(now / (360 / self.totalSlices()));
					self.currentSlice = temp[self.currentStep % self.totalSlices()];
					var ewCircleSize = 400 * 4,
						ewTotalArcs = self.totalSlices(),
						ewArcSizeDeg = 360 / ewTotalArcs,
						ewArcSize = ewCircleSize / ewTotalArcs,
						point = ewCircleSize / 360,
						ewCirclePos = point * self.now,
						ewCirclePosPercent = ewCirclePos / ewCircleSize * 100,
						ewArcPos = (self.currentSlice + 1) * ewArcSize - (ewCircleSize - point * self.now),
						ewArcPosPercent = ewArcPos / ewArcSize * 100,
						cpercent = ewCirclePosPercent,
						apercent = ewArcPosPercent;
					self.slicePercent = ewArcPosPercent;
					self.circlePercent = ewCirclePosPercent;
					self.o.onProgress.call(self.$wheel, self.slicePercent, self.circlePercent);
					if (self.lastStep !== self.currentStep) {
						self.lastStep = self.currentStep;
						if (self.o.markerAnimation === true && $.inArray($.trim(self.o.easing), ["easeInElastic", "easeInBack", "easeInBounce", "easeOutElastic", "easeOutBack", "easeOutBounce", "easeInOutElastic", "easeInOutBack", "easeInOutBounce"]) === -1) {
							var Mduration = parseInt(self.o.duration) / self.totalSlices();
							MarkerStep = -38;
							if (MarkerAnimator) MarkerAnimator.stop();
							MarkerAnimator = $({
								deg: 0
							}).animate({
								deg: MarkerStep
							}, {
								easing: "MarkerEasing",
								duration: Mduration / (360 / 360 * 2),
								step: function (now) {
									$(".eWheel-marker").css({
										"-webkit-transform": "rotate(" + now + "deg)",
										"-moz-transform": "rotate(" + now + "deg)",
										"-ms-transform": "rotate(" + now + "deg)",
										"-o-transform": "rotate(" + now + "deg)",
										"transform": "rotate(" + now + "deg)"
									})
								}
							})
						}
						if ($.trim(self.o.type) === "color") {
							self.$wheel.find("svg>g.ew-slicesGroup>path").each(function (i) {
								$(this).attr("class", "").attr("fill", $(this).attr("data-fill"))
							});
							self.$wheel.find("svg>g.ew-slicesGroup>path").eq(self.currentSlice).attr("class", "ew-ccurrent").attr("fill", self.o.selectedSliceColor);
							self.$wheel.find(".eWheel-txt>.eWheel-title").removeClass("ew-ccurrent");
							self.$wheel.find(".eWheel-txt>.eWheel-title").eq(self.currentSlice).addClass("ew-ccurrent")
						} else {
							self.$wheel.find("svg>g.ew-slicesGroup>path").attr("class", "");
							self.$wheel.find("svg>g.ew-slicesGroup>path").eq(self.currentSlice).attr("class", "ew-current");
							self.$wheel.find(".eWheel-txt>.eWheel-title").removeClass("ew-current");
							self.$wheel.find(".eWheel-txt>.eWheel-title").eq(self.currentSlice).addClass("ew-current")
						}
						self.currentSliceData.id = self.currentSlice;
						self.currentSliceData.results = self.items[self.currentSliceData.id];
						self.currentSliceData.results.length = self.currentSliceData.id;
						self.o.onStep.call(self.$wheel, self.currentSliceData.results, self.slicePercent, self.circlePercent)
					}
					if (parseInt(self.o.frame) !== 0) $.fx.interval = oldinterval
				},
				fail: function (animation, progress, remainingMs) {
					self.inProgress = false;
					self.o.onFail.call(self.$wheel, self.slice.results, self.spinCount, self.now)
				},
				complete: function (animation, progress, remainingMs) {
					self.inProgress = false;
					self.o.onComplete.call(self.$wheel, self.slice.results, self.spinCount, self.now)
				}
			});
			self.counter++;
			self.spinCount++
		};
		EasyWheel.prototype.execute = function () {
			var self = this;
			self.currentSlice = self.totalSlices() - 1;
			self.$wheel.css("font-size", parseInt(self.o.fontSize));
			self.$wheel.width(parseInt(self.o.width));
			self.$wheel.height(self.$wheel.width());
			self.$wheel.find(".eWheel-wrapper").width(self.$wheel.width());
			self.$wheel.find(".eWheel-wrapper").height(self.$wheel.width());
			self.FontScale();
			$(window).on("resize." + self.instanceUid, function () {
				self.$wheel.height(self.$wheel.width());
				self.$wheel.find(".eWheel-wrapper").width(self.$wheel.width());
				self.$wheel.find(".eWheel-wrapper").height(self.$wheel.width());
				self.FontScale()
			})
		};
		$.fn.easyWheel = function () {
			var self = this,
				opt = arguments[0],
				args = Array.prototype.slice.call(arguments, 1),
				arg2 = Array.prototype.slice.call(arguments, 2),
				l = self.length,
				i, apply, allowed = ["destroy", "start", "finish", "option"];
			for (i = 0; i < l; i++) {
				if (typeof opt == "object" || typeof opt == "undefined") {
					self[i].easyWheel = new EasyWheel(self[i], opt)
				} else if ($.inArray($.trim(opt), allowed) !== -1) {
					if ($.trim(opt) === "option") {
						apply = self[i].easyWheel[opt].apply(self[i].easyWheel, args, arg2)
					} else {
						apply = self[i].easyWheel[opt].apply(self[i].easyWheel, args)
					}
				}
				if (typeof apply != "undefined") return apply
			}
			return self
		}
	});

function SpinToWin(config) {
	this.config = config;
	if (window.Android || window.BrowserTest) {
		this.convertConfigJson()
	}
	this.container = document.getElementById("container");
	if (this.config.taTemplate == "full_spin") {
		this.wheelContainerId = "wheel-container-full";
		document.getElementById("wheel-container-half").remove();
	} else {
		this.wheelContainerId = "wheel-container-half";
		document.getElementById("wheel-container-full").remove();
	}


	this.wheelContainer = document.getElementById(this.wheelContainerId);
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

	this.promocodesSoldoutMessageElement = document.getElementById("promocodes_soldout_message");

	this.formValidation = {
		email: true,
		consent: true
	};
	this.won = false;
	this.spinCompleted = false;
	this.easyWheelInitialized = false;
	this.config.windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	this.config.statusBarHeight = window.screen.height - window.innerHeight;
	this.config.windowHeightWidthRatio = window.innerHeight / window.innerWidth;
	if (this.config.taTemplate == "full_spin") {
		this.config.wheelContainerMarginLeft = this.config.windowHeightWidthRatio > 1.6 ? (window.innerWidth / 6) : (window.innerWidth / 5);
		this.config.r = parseFloat(window.innerWidth / 2) - this.config.wheelContainerMarginLeft;
		this.config.wheelContainerWidth = this.config.windowWidth - this.config.wheelContainerMarginLeft * 2
	} else {
		this.config.wheelContainerMarginLeft = 0;
		this.config.r = parseFloat(window.innerWidth / 2);
		this.config.wheelContainerWidth = this.config.windowWidth
	}
	this.config.selectedPromotionCode = "";
	this.convertStringsToNumber();
	this.setCloseButton();
	this.config.mailFormEnabled = config.mailSubscription;

	this.setContent();
	this.styleHandler();
	this.addFonts();
	this.setFonts();
	window.onresize = function () {
		window.spinToWin.styleHandler();
		window.spinToWin.setFonts();
	};
	window.spinToWin = this;
	this.createItems();
	this.createEasyWheel();

	if (this.config.taTemplate == "full_spin") {
		this.config.wheelContainerMarginLeft = this.config.wheelContainerMarginLeft - window.innerWidth * 0.06;
		this.wheelContainer.style.marginLeft = this.config.wheelContainerMarginLeft + "px";
	}

	this.handleVisibility()
	var rotates = 4;
	this.config.totalSliceCount = this.config.items.length * rotates;
	this.config.mailFormSubmitCompleted = false;
	this.checkAvailability();



};

SpinToWin.prototype.checkAvailability = function () {
	var availableSliceCount = 0;
	for (var i = 0; i < this.config.slices.length; i++) {
		if (this.config.slices[i].type == "staticcode") {
			availableSliceCount++;
		} else if (this.config.slices[i].type == "promotion" && this.config.slices[i].is_available) {
			availableSliceCount++;
		}
	}
	if (availableSliceCount == 0) {
		this.showSoldout();
	}
};

SpinToWin.prototype.showSoldout = function () {
	window.spinToWin.submitButton.removeEventListener("click", window.spinToWin.submit);
	window.spinToWin.wheelContainer.removeEventListener("swiped-right", window.spinToWin.startSpin);
	var vl_form_input = document.getElementById("vl-form-input");
	if (vl_form_input !== null) vl_form_input.setAttribute("disabled", "");
	var vl_form_checkbox = document.getElementById("vl-form-checkbox");
	if (vl_form_checkbox !== null) vl_form_checkbox.setAttribute("disabled", "");
	var vl_form_checkbox_emailpermit = document.getElementById("vl-form-checkbox-emailpermit");
	if (vl_form_checkbox_emailpermit !== null) vl_form_checkbox_emailpermit.setAttribute("disabled", "");
	var vl_form_submit_btn = document.getElementsByClassName("form-submit-btn");
	if (vl_form_submit_btn !== null) vl_form_submit_btn[0].classList.add("disabled");
	window.spinToWin.promocodesSoldoutMessageElement.style.display = "block";
};

SpinToWin.prototype.createItems = function () {
	this.config.items = [];
	var i = 0;
	for (; i < this.config.slices.length; i++) {
		this.config.items.push({
			id: i,
			name: window.spinToWin.breakString(this.config.slices[i].displayName, 10),
			color: this.config.slices[i].color,
			code: this.config.slices[i].code,
			type: this.config.slices[i].type,
			win: false
		})
	}
	if (this.config.sliceCount > this.config.slices.length) {
		while (this.config.items.length < this.config.sliceCount) {
			var sliceId = i % this.config.slices.length;
			this.config.items.push({
				id: i,
				name: window.spinToWin.breakString(this.config.slices[sliceId].displayName, 10),
				color: this.config.slices[sliceId].color,
				code: this.config.slices[sliceId].code,
				type: this.config.slices[sliceId].type,
				win: false
			});
			i++
		}
	}
};
SpinToWin.prototype.createEasyWheel = function () {
	$("#" + this.wheelContainerId).easyWheel({
		items: window.spinToWin.config.items,
		duration: 1,
		rotates: 4,
		frame: 1,
		easing: "easyWheel",
		type: "spin",
		width: window.spinToWin.config.wheelContainerWidth,
		fontSize: window.spinToWin.config.displaynameTextSize + 8,
		textOffset: 8,
		letterSpacing: 0,
		textLine: "v",
		textArc: true,
		outerLineWidth: 5,
		centerImage: window.spinToWin.config.img,
		centerWidth: 20,
		centerLineWidth: 5,
		centerImageWidth: 20,
		textColor: window.spinToWin.config.displaynameTextColor,
		markerColor: window.spinToWin.config.buttonColor,
		centerLineColor: "#ffffff",
		centerBackground: "#ffffff",
		sliceLineColor: "#ffffff",
		outerLineColor: "#ffffff",
		onStep: function (item, slicePercent, circlePercent) {
			if (window.spinToWin.easyWheelInitialized && window.spinToWin.config.completedStepCount < window.spinToWin.config.totalSliceCount) {
				if (window.spinToWin.easyWheelInitialized && window.spinToWin.config.completedStepCount < 1
					&& window.spinToWin.config.wheelSpinAction == "swipe") {
					window.spinToWin.wheelContainer.removeEventListener("swiped-right", window.spinToWin.startSpin);
				}
				window.spinToWin.playTick();
				window.spinToWin.config.completedStepCount = window.spinToWin.config.completedStepCount + 1;
			} else {
				window.spinToWin.config.completedStepCount = 0;
			}
		},
		onStart: function (results, spinCount, now) { },
		onComplete: function (results, count, now) {
			if (!window.spinToWin.easyWheelInitialized) {
				window.spinToWin.easyWheelInitialized = true;
				window.easyWheel.items[0].win = false;
				window.easyWheel.o.duration = 6000
			} else {
				window.spinToWin.resultHandler(results)
			}
		}
	});
	window.easyWheel.items[0].win = true;
	window.easyWheel.start()
};
SpinToWin.prototype.convertConfigJson = function () {
	//actiondata

	this.config.promocodesSoldoutMessage = this.config.actiondata.promocodes_soldout_message;
	this.config.mailSubscription = this.config.actiondata.mail_subscription;
	this.config.sliceCount = this.config.actiondata.slice_count;
	this.config.slices = this.config.actiondata.slices.map(slice => ({
		...slice
		, isAvailable: slice.isAvailable === undefined ? slice.is_available : slice.isAvailable
	}));

	this.config.img = this.config.actiondata.img;
	this.config.taTemplate = this.config.actiondata.taTemplate;
	this.config.promocodeTitle = this.config.actiondata.promocode_title;
	this.config.wheelSpinAction = this.config.actiondata.wheel_spin_action;
	this.config.copyButtonLabel = this.config.actiondata.copybutton_label;

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

	//ExtendedProps
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

	this.config.promocodesSoldoutMessageTextColor = extendedProps.promocodes_soldout_message_text_color;
	this.config.promocodesSoldoutMessageFontFamily = extendedProps.promocodes_soldout_message_font_family;
	this.config.promocodesSoldoutMessageTextSize = extendedProps.promocodes_soldout_message_text_size;
	this.config.promocodesSoldoutMessageBackgroundColor = extendedProps.promocodes_soldout_message_background_color;

	this.config.displaynameCustomFontFamilyAndroid = extendedProps.displayname_custom_font_family_android;
	this.config.titleCustomFontFamilyAndroid = extendedProps.title_custom_font_family_android;
	this.config.textCustomFontFamilyAndroid = extendedProps.text_custom_font_family_android;
	this.config.buttonCustomFontFamilyAndroid = extendedProps.button_custom_font_family_android;
	this.config.promocodeTitleCustomFontFamilyAndroid = extendedProps.promocode_title_custom_font_family_android;
	this.config.copybuttonCustomFontFamilyAndroid = extendedProps.copybutton_custom_font_family_android;
	this.config.promocodesSoldoutMessageCustomFontFamilyAndroid = extendedProps.promocodes_soldout_message_custom_font_family_android;

	//position
	this.config.titlePosition = extendedProps.title_position;
	this.config.textPosition = extendedProps.text_position;
	this.config.buttonPosition = extendedProps.button_position;
	this.config.copybuttonPosition = extendedProps.copybutton_position;

	//promocodeBanner
	this.config.promocodeBannerText = extendedProps.promocode_banner_text;
	this.config.promocodeBannerTextColor = extendedProps.promocode_banner_text_color;
	this.config.promocodeBannerBackgroundColor = extendedProps.promocode_banner_background_color;
	this.config.promocodeBannerButtonLabel = extendedProps.promocode_banner_button_label;
};
SpinToWin.prototype.addFonts = function () {
	if (this.config.fontFiles === undefined) {
		return
	}
	var addedFontFiles = [];
	for (var fontFileIndex in this.config.fontFiles) {
		var fontFile = this.config.fontFiles[fontFileIndex];
		if (addedFontFiles.includes(fontFile)) {
			continue;
		}
		var fontFamily = fontFile.split(".")[0];
		var newStyle = document.createElement('style');
		var cssContent = "@font-face{font-family:" + fontFamily + ";src:url('" + fontFile + "');}";
		newStyle.appendChild(document.createTextNode(cssContent));
		document.head.appendChild(newStyle);
		addedFontFiles.push(fontFile);
	}
};

SpinToWin.prototype.setFonts = function () {
	var isIos = window.webkit && window.webkit.messageHandlers;

	if (typeof this.config.displaynameFontFamily === "string" && this.config.displaynameFontFamily.toLowerCase() === "custom") {
		this.wheelContainer.style.fontFamily = isIos ? this.config.displaynameCustomFontFamilyIos : this.config.displaynameCustomFontFamilyAndroid;
	}
	if (typeof this.config.titleFontFamily === "string" && this.config.titleFontFamily.toLowerCase() === "custom") {
		this.titleElement.style.fontFamily = isIos ? this.config.titleCustomFontFamilyIos : this.config.titleCustomFontFamilyAndroid;
	}
	if (typeof this.config.textFontFamily === "string" && this.config.textFontFamily.toLowerCase() === "custom") {
		this.messageElement.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
		this.consentText.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
		this.emailPermitText.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
		this.invalidEmailMessageLi.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
		this.checkConsentMessageLi.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
		this.emailInput.style.fontFamily = isIos ? this.config.textCustomFontFamilyIos : this.config.textCustomFontFamilyAndroid;
	}
	if (typeof this.config.buttonFontFamily === "string" && this.config.buttonFontFamily.toLowerCase() === "custom") {
		this.submitButton.style.fontFamily = isIos ? this.config.buttonCustomFontFamilyIos : this.config.buttonCustomFontFamilyAndroid;
	}
	if (typeof this.config.promocodeTitleFontFamily === "string" && this.config.promocodeTitleFontFamily.toLowerCase() === "custom") {
		this.promocodeTitleElement.style.fontFamily = isIos ? this.config.promocodeTitleCustomFontFamilyIos : this.config.promocodeTitleCustomFontFamilyAndroid;
	}
	if (typeof this.config.copybuttonFontFamily === "string" && this.config.copybuttonFontFamily.toLowerCase() === "custom") {
		this.copyButton.style.fontFamily = isIos ? this.config.copybuttonCustomFontFamilyIos : this.config.copybuttonCustomFontFamilyAndroid;
		this.couponCode.style.fontFamily = isIos ? this.config.copybuttonCustomFontFamilyIos : this.config.copybuttonCustomFontFamilyAndroid;
	}
	if (typeof this.config.promocodesSoldoutMessageFontFamily === "string" && this.config.promocodesSoldoutMessageFontFamily.toLowerCase() === "custom") {
		this.promocodesSoldoutMessageElement.style.fontFamily = isIos ? this.config.promocodesSoldoutMessageCustomFontFamilyIos : this.config.promocodesSoldoutMessageCustomFontFamilyAndroid;
	}

};

SpinToWin.prototype.getPromotionCode = function () {
	if (window.Android) {
		Android.getPromotionCode()
	} else if (window.webkit && window.webkit.messageHandlers) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "getPromotionCode"
		})
	} else {
		window.BrowserTest.getPromotionCode()
	}
};
SpinToWin.prototype.subscribeEmail = function () {
	if (window.Android) {
		Android.subscribeEmail(this.emailInput.value.trim())
	} else if (window.webkit && window.webkit.messageHandlers) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "subscribeEmail",
			email: this.emailInput.value.trim()
		})
	}
};
SpinToWin.prototype.close = function () {
	if (window.Android) {
		Android.close()
	} else if (window.webkit && window.webkit.messageHandlers) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "close"
		})
	}
};
SpinToWin.prototype.copyToClipboard = function () {
	if (window.Android) {
		Android.copyToClipboard(this.couponCode.innerText)
	} else if (window.webkit.messageHandlers.eventHandler) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "copyToClipboard",
			couponCode: this.couponCode.innerText
		})
	}
};
SpinToWin.prototype.sendReport = function () {
	if (window.Android) {
		Android.sendReport()
	} else if (window.webkit && window.webkit.messageHandlers) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "sendReport"
		})
	}
};
SpinToWin.prototype.openUrl = function (url) {
	if (window.webkit && window.webkit.messageHandlers) {
		window.webkit.messageHandlers.eventHandler.postMessage({
			method: "openUrl",
			url: url
		})
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
	this.config.promocodesSoldoutMessageTextSize = isNaN(parseInt(this.config.promocodesSoldoutMessageTextSize)) ? 20 : parseInt(this.config.promocodesSoldoutMessageTextSize);
};

SpinToWin.prototype.setContent = function () {
	this.container.style.backgroundColor = this.config.backgroundColor;
	this.titleElement.innerHTML = this.config.title.replace(/\\n/g, "<br/>");
	this.titleElement.style.color = this.config.titleTextColor;
	this.titleElement.style.fontFamily = this.config.titleFontFamily;
	this.titleElement.style.fontSize = (this.config.titleTextSize + 20) + "px";
	this.messageElement.innerHTML = this.config.message.replace(/\\n/g, "<br/>");
	this.messageElement.style.color = this.config.textColor;
	this.messageElement.style.fontFamily = this.config.textFontFamily;
	this.messageElement.style.fontSize = (this.config.textSize + 10) + "px";
	this.emailInput.style.fontFamily = this.config.textFontFamily;
	this.emailInput.style.fontSize = (this.config.textSize + 10) + "px";
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
	this.successMessageElement.innerHTML = this.config.successMessage.replace(/\\n/g, "<br/>");
	this.successMessageElement.style.color = "green";
	this.promocodeTitleElement.innerHTML = this.config.promocodeTitle.replace(/\\n/g, "<br/>");
	this.promocodeTitleElement.style.color = this.config.promocodeTitleTextColor;
	this.promocodeTitleElement.style.fontFamily = this.config.promocodeTitleFontFamily;
	this.promocodeTitleElement.style.fontSize = (this.config.promocodeTitleTextSize + 20) + "px";

	if (this.config.promocodesSoldoutMessage !== undefined && this.config.promocodesSoldoutMessage.length > 0) {
		this.promocodesSoldoutMessageElement.innerHTML = this.config.promocodesSoldoutMessage.replace(/\\n/g, "<br/>");
	}

	this.promocodesSoldoutMessageElement.style.color = this.config.promocodesSoldoutMessageTextColor;
	this.promocodesSoldoutMessageElement.style.fontFamily = this.config.promocodesSoldoutMessageFontFamily;
	this.promocodesSoldoutMessageElement.style.fontSize = (this.config.promocodesSoldoutMessageTextSize + 20) + "px";
	this.promocodesSoldoutMessageElement.style.backgroundColor = this.config.promocodesSoldoutMessageBackgroundColor;

	if (this.config.taTemplate == "full_spin") {
		this.handlePositions();
	}

	this.container.addEventListener("click", function (event) {
		if (event.target.tagName != "INPUT") {
			document.activeElement.blur()
		}
	});
	this.emailInput.addEventListener("keypress", function (event) {
		if (event.key === 'Enter') {
			document.activeElement.blur()
		}
	});
	if (this.config.wheelSpinAction == "swipe") {
		if (this.config.mailFormEnabled) {
			this.submitButton.addEventListener("click", this.submit);
		} else {
			this.wheelContainer.addEventListener('swiped-right', this.startSpin);
		}
	} else {
		this.submitButton.addEventListener("click", this.submit);
	}
	this.closeButton.addEventListener("click", evt => this.close());
	this.copyButton.addEventListener("click", evt => this.copyToClipboard());
};

SpinToWin.prototype.handlePositions = function () {
	if (this.config.titlePosition == "bottom") {
		this.titleElement.remove();
		this.wheelContainer.parentNode.insertBefore(this.titleElement, this.wheelContainer.nextSibling);
		this.titleElement.style.marginTop = window.innerWidth * 0.88 + "px";
		if (this.config.textPosition == "bottom") {
			this.messageElement.remove();
			this.titleElement.parentNode.insertBefore(this.messageElement, this.titleElement.nextSibling);
			if (this.config.buttonPosition == "bottom") {
				this.submitButton.remove();
				this.messageElement.parentNode.insertBefore(this.submitButton, this.messageElement.nextSibling);
			}
			if (this.config.copybuttonPosition == "bottom") {
				this.copyButton.remove();
				this.messageElement.parentNode.insertBefore(this.copyButton, this.messageElement.nextSibling);
				this.copyButton.style.marginBottom = "60px";
			}
		} else {
			this.submitButton.remove();
			this.titleElement.parentNode.insertBefore(this.submitButton, this.titleElement.nextSibling);
			if (this.config.copybuttonPosition == "bottom") {
				this.copyButton.remove();
				this.titleElement.parentNode.insertBefore(this.copyButton, this.titleElement.nextSibling);
				this.copyButton.style.marginBottom = "60px";
			}
		}
	} else if (this.config.textPosition == "bottom") {
		this.messageElement.remove();
		this.wheelContainer.parentNode.insertBefore(this.messageElement, this.wheelContainer.nextSibling);
		this.messageElement.style.marginTop = window.innerWidth * 0.88 + "px";
		if (this.config.buttonPosition == "bottom") {
			this.submitButton.remove();
			this.messageElement.parentNode.insertBefore(this.submitButton, this.messageElement.nextSibling);
		}
		if (this.config.copybuttonPosition == "bottom") {
			this.copyButton.remove();
			this.messageElement.parentNode.insertBefore(this.copyButton, this.messageElement.nextSibling);
			this.copyButton.style.marginBottom = "60px";
		}
	} else if (this.config.buttonPosition == "bottom") {
		this.submitButton.remove();
		this.wheelContainer.parentNode.insertBefore(this.submitButton, this.wheelContainer.nextSibling);
		this.submitButton.style.marginTop = window.innerWidth * 0.88 + "px";
		if (this.config.copybuttonPosition == "bottom") {
			this.copyButton.remove();
			this.submitButton.parentNode.insertBefore(this.copyButton, this.submitButton.nextSibling);
			this.copyButton.style.marginTop = window.innerWidth * 0.88 + "px";
			this.copyButton.style.marginBottom = "60px";
		}
	} else if (this.config.copybuttonPosition == "bottom") {
		this.copyButton.remove();
		this.wheelContainer.parentNode.insertBefore(this.copyButton, this.wheelContainer.nextSibling);
		this.copyButton.style.marginTop = window.innerWidth * 0.88 + "px";
		this.copyButton.style.marginBottom = "60px";
	}
};

SpinToWin.prototype.validateForm = function () {
	var result = {
		email: true,
		consent: true
	};
	if (!this.validateEmail(this.emailInput.value)) {
		result.email = false
	}
	if (!this.isNullOrWhitespace(this.consentText.innerText)) {
		result.consent = this.consentCheckbox.checked
	}
	if (result.consent) {
		if (!this.isNullOrWhitespace(this.emailPermitText.innerText)) {
			result.consent = this.emailPermitCheckbox.checked
		}
	}
	this.formValidation = result;
	return result
};

SpinToWin.prototype.handleVisibility = function () {
	if (this.spinCompleted) {
		this.couponCode.style.display = "";
		this.emailInput.style.display = "none";
		this.submitButton.style.display = "none";
		this.consentContainer.style.display = "none";
		this.emailPermitContainer.style.display = "none";
		this.warning.style.display = "none";
		if (this.won) {
			this.promocodeTitleElement.style.display = "";
			this.copyButton.style.display = ""
		} else {
			this.promocodeTitleElement.style.display = "none";
			this.copyButton.style.display = "none"
		}
		if (this.config.mailFormEnabled) {
			this.successMessageElement.style.display = ""
		} else {
			this.successMessageElement.style.display = "none"
		}
		return
	} else {
		this.couponCode.style.display = "none";
		this.copyButton.style.display = "none";
		this.successMessageElement.style.display = "none";
		this.promocodeTitleElement.style.display = "none"
	}
	this.warning.style.display = "none";
	if (this.config.mailFormEnabled) {
		if (!this.formValidation.email || !this.formValidation.consent) {
			this.warning.style.display = "";
			if (this.formValidation.email) {
				this.invalidEmailMessageLi.style.display = "none"
			} else {
				this.invalidEmailMessageLi.style.display = ""
			}
			if (this.formValidation.consent) {
				this.checkConsentMessageLi.style.display = "none"
			} else {
				this.checkConsentMessageLi.style.display = ""
			}
		} else {
			this.warning.style.display = "none"
		}
		if (this.isNullOrWhitespace(this.consentText.innerHTML)) {
			this.consentCheckbox.style.display = "none";
			this.consentContainer.style.display = "none"
		}
		if (this.isNullOrWhitespace(this.emailPermitText.innerHTML)) {
			this.emailPermitCheckbox.style.display = "none";
			this.emailPermitContainer.style.display = "none"
		}
		if (this.config.mailFormSubmitCompleted) {
			this.successMessageElement.style.display = "";
			this.emailInput.style.display = "none";
			this.submitButton.style.display = "none";
			this.consentContainer.style.display = "none";
			this.emailPermitContainer.style.display = "none";
			this.warning.style.display = "none";
		}
	} else {
		if (this.config.taTemplate == "full_spin") {
			document.getElementById("wl_custom_fields_holder").style.display = "table";
		}

		this.emailInput.style.display = "none";
		this.consentContainer.style.display = "none";
		this.emailPermitContainer.style.display = "none";
		this.emailPermitContainer.height = "0px";
		if (this.config.wheelSpinAction == "swipe") {
			this.submitButton.style.display = "none";
		}
	}
};
SpinToWin.prototype.getWheelContainerMarginTop = function () {
	var marginTop = 10;
	if (window.innerHeight < 750) {
		marginTop = 10;
	} else if (window.innerHeight < 1000) {
		marginTop = 30;
	} else {
		marginTop = 50;
	}

	if (this.config.taTemplate == "full_spin") {
		marginTop = marginTop + 15;
	}

	return marginTop + "px";
};
SpinToWin.prototype.styleHandler = function () {
	this.wheelContainer.style.position = "absolute";
	this.wheelContainer.style.fontFamily = this.config.displaynameFontFamily;
	this.wheelContainer.style.marginLeft = this.config.wheelContainerMarginLeft + "px";
	if (window.Android) {
		if (this.config.taTemplate == "full_spin") {
			this.wheelContainer.style.marginTop = this.getWheelContainerMarginTop();
		} else {
			this.wheelContainer.style.bottom = -this.config.r + "px"
		}
	} else {
		if (this.config.taTemplate == "full_spin") {
			this.wheelContainer.style.marginTop = this.getWheelContainerMarginTop();
		} else {
			this.wheelContainer.style.bottom = -this.config.r + this.config.statusBarHeight + "px"
		}
	}

	var styleEl = document.createElement("style"),
		styleString = "#" + this.wheelContainerId + "{float:left;width:" + config.r
			+ "px;height:" + (2 * this.config.r) + "px}@media only screen and (max-width:2500px){"
			+ "#" + this.wheelContainerId + "{float:unset;width:100%;text-align:center;position:relative}" + "}";

	styleEl.id = "rd-styles";
	if (!document.getElementById("rd-styles")) {
		styleEl.innerHTML = styleString;
		document.head.appendChild(styleEl);
	} else {
		document.getElementById("rd-styles").innerHTML = styleString;
	}
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
	window.spinToWin.submitButton.removeEventListener("click", window.spinToWin.submit);
	window.spinToWin.config.mailFormSubmitCompleted = true;
	window.spinToWin.handleVisibility();
	if (config.wheelSpinAction == "swipe") {
		window.spinToWin.wheelContainer.addEventListener("swiped-right", window.spinToWin.startSpin);
	} else {
		window.spinToWin.startSpin();
	}
};
SpinToWin.prototype.startSpin = function () {
	window.spinToWin.sendReport();
	window.spinToWin.wheelContainer.removeEventListener("swiped-right", window.spinToWin.startSpin);
	window.spinToWin.getPromotionCode();
};
SpinToWin.prototype.spin = function (sliceIndex, promotionCode) {
	if (sliceIndex > -1) {
		window.spinToWin.won = true;
		window.spinToWin.config.items[sliceIndex].win = true;
		window.spinToWin.config.items[sliceIndex].code = promotionCode
	} else {
		var staticCodeSliceIndexes = [];
		for (var i = 0; i < window.spinToWin.config.items.length; i++) {
			if (window.spinToWin.config.items[i].type === "staticcode") {
				staticCodeSliceIndexes.push(i)
			}
		}
		if (staticCodeSliceIndexes.length > 0) {
			window.spinToWin.won = true;
			sliceIndex = staticCodeSliceIndexes[this.randomInt(0, staticCodeSliceIndexes.length)]
		} else {
			window.spinToWin.showSoldout();
			return;
		}
	}
	window.spinToWin.spinHandler(sliceIndex)
};
SpinToWin.prototype.spinHandler = function (result) {
	var vl_form_input = document.getElementById("vl-form-input");
	if (vl_form_input !== null) vl_form_input.setAttribute("disabled", "");
	var vl_form_checkbox = document.getElementById("vl-form-checkbox");
	if (vl_form_checkbox !== null) vl_form_checkbox.setAttribute("disabled", "");
	var vl_form_checkbox_emailpermit = document.getElementById("vl-form-checkbox-emailpermit");
	if (vl_form_checkbox_emailpermit !== null) vl_form_checkbox_emailpermit.setAttribute("disabled", "");
	var vl_form_submit_btn = document.getElementsByClassName("form-submit-btn");
	if (vl_form_submit_btn !== null) vl_form_submit_btn[0].classList.add("disabled");
	window.spinToWin.config.items[result].win = true;
	window.easyWheel.items[result].win = true;
	window.easyWheel.start();
};
SpinToWin.prototype.resultHandler = function (res) {
	this.spinCompleted = true;
	if (this.won) {
		this.promocodeTitleElement.innerHTML = this.promocodeTitleElement.innerHTML + "<br/>" + res.name;
		this.couponCode.innerText = res.code;
		this.couponCode.value = res.code
	} else {
		this.promocodeTitleElement.innerHTML = this.promocodeTitleElement.innerHTML + "<br/>" + res.name;
		this.couponCode.innerText = res.name;
		this.couponCode.value = res.name
	}
	this.handleVisibility()
}; //Helper functions
SpinToWin.prototype.breakString = function (str, limit) {
	let brokenString = "";
	for (let i = 0, count = 0; i < str.length; i++) {
		if (count >= limit && str[i] === " ") {
			count = 0;
			brokenString += "<br>"
		} else {
			count++;
			brokenString += str[i]
		}
	}
	return brokenString
};
SpinToWin.prototype.prepareCheckboxHtmls = function (text, url) {
	if (this.isNullOrWhitespace(text)) {
		return ""
	} else if (this.isNullOrWhitespace(url)) {
		return text.replace(new RegExp("<LINK>", "gm"), "").replace(new RegExp("</LINK>", "gm"), "")
	} else if (!text.includes("<LINK>")) {
		if (window.webkit && window.webkit.messageHandlers.eventHandler) {
			return "<a href=\"javascript:window.spinToWin.openUrl('" + url + "')\">" + text + "</a>"
		} else {
			return "<a href=\"" + url + "\">" + text + "</a>"
		}
	} else {
		var linkRegex = /<LINK>(.*?)<\/LINK>/g;
		var regexResult;
		while ((regexResult = linkRegex.exec(text)) !== null) {
			var outerHtml = regexResult[0];
			var innerHtml = regexResult[1];
			if (window.webkit && window.webkit.messageHandlers.eventHandler) {
				var link = "<a href=\"javascript:window.spinToWin.openUrl('" + url + "')\">" + innerHtml + "</a>";
				text = text.replace(outerHtml, link)
			} else {
				var link = "<a href=\"" + url + "\">" + innerHtml + "</a>";
				text = text.replace(outerHtml, link)
			}
		}
		return text
	}
};
SpinToWin.prototype.randomInt = function (min, max) {
	return Math.floor(Math.random() * (max - min) + min)
};
SpinToWin.prototype.isNullOrWhitespace = function (input) {
	if (typeof input === "undefined" || input == null) return true;
	return input.replace(/\s/g, "").length < 1
};
SpinToWin.prototype.validateEmail = function (email) {
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(email)
};
SpinToWin.prototype.setCloseButton = function () {
	if (this.config.closeButtonColor == "black") {
		this.closeButton.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABvUlEQVRoQ+2Z4U0DMQyFfYsxRIEN2AomQEjMgBisKG0jRac0sf2ec1ZF//Qkkvh9fo4vDZtcP08i8nN7Ll9b85zx8dyIKtp/i+BXEfnsqM0K00JU2S9FbO8PdUA2mLtaZyCZymyU8MteGA64WXO0M1ONVeB04IENQKWtzbRqwuIWpta0Lxn1xAVAJi292jctEARk1nBvE5sXIgK5Yo+6kWtBEMgdc9ZW3Qs7gKBYM5CiBwqgBIJjaECiYWAI6/GDEnDnEG1NrSM1Pi0wu2StIKwyYybkkmQPCApDh0BAvDAhECiIFSYMggGihdG8TrxlDu2RvTBNtkcwEATLEUtr7sHAEGwQT5lRICJALDA0iH+Qwa61bnqaK7SFlGen9Jvd6sQeCE4ovADgBBUGBdE4seQSEAGxQFhemi5NrknKclp61eQB8TjhOZuZtJkGg06EwlhAGE6EwWhBIiCoDUADEglBg5mBrICgwDz8JfZKJygN4GH/0XOkE5AzrSOZIMwNYMnJVHOpBf7q3ApIRifMZaYBmb1rwISrpw8TPgPJAjHdM0Xou4i8dfKSDWIE81HFfonIcwOTFaIH8y0ipz/jH10bOlDCXQAAAABJRU5ErkJggg=="
	} else {
		this.closeButton.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABwklEQVRoQ+2aa24CMQyE7QuUM7YHK2csF0i1qJF2A0lszxiiVfkDEnnM52fCoiIipZRvEfncPm8vVdX6ecX3UkrZ6bqq6pe2EKvDNBBV7nUD2dMdHLCaZ4ZaR1+uFGZTnbMBK8CYNP4leze83p0zJoh9DlgnvLKKeTQdyqxnYjaQV8tDv/AukAEU0fC08UUWYgFF9+528OiCCBCy5/AogizsBUL3mp6p0A0sQIw9piDZfYYBcW/aFotlwbAgXCBsGCaEG4QFw4YIgaAwGRBhkChMFgQE4oXJhIBBrDCWyojeRs3ldyTGYu3RfBSC4pEqMArDgKCCRMKMBUEH8cAwIf5BeonrzROmVyhVyxNSrRFYMBQQrycyYGAQFKJCoZ6BQCwQVaBnrOUk8ODVyCRrTrRWzoQJeQQRhMwdHnO8HmEIYawBhRZTAHMtV2dnbxzNs14EmXIkA8JzaraU5ilIJgQT5vw/mb7CE228I3ue97ECYhVvT0KuA20BON+jtxU8geTM3SMrQnhL8/AvHKy7Apo7JkPPBlm6KirUMn+qczRgFQhLmG2h9SMiHxn3aIulvWM6hr/VZD/ArOaJSTW7qerlF9bSa7Pl7TDpAAAAAElFTkSuQmCC"
	}
};

var Sound = (function () {
	var df = document.createDocumentFragment();
	return function Sound(src) {
		var snd = new Audio(src);
		snd.currentTime = 0;
		snd.setAttribute("playsinline", true);
		snd.setAttribute("autoplay", true);
		snd.setAttribute("preload", "auto");
		df.appendChild(snd); // keep in fragment until finished playing
		return snd;
	}
}());

var snd = null;

SpinToWin.prototype.playTick = function () {
	if (!snd && window.spinToWin.easyWheelInitialized) {
		snd = Sound("data:audio/wav;base64,UklGRlQbAABXQVZFZm10IBAAAAABAAEAgLsAAAB3AQACABAATElTVCgAAABJTkZPSUdOUgYAAABCbHVlcwBJU0ZUDgAAAExhdmY1OC43Ni4xMDAAZGF0YQAbAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAA/////wAAAAAAAAEAAQAAAAAAAAAAAAAAAAAAAAAA////////AAD/////AQACAAEAAAAAAAAA/////wAAAQAAAAAAAgACAAEA////////AAD/////AAAAAAAAAQD///7//v8BAAMAAgAAAP/////9//7/AgAEAAQAAQD//wAA///8//v//v///wAABwAKAAMA/P/9/////v/9//3//v////7///8CAAEA/v8DAAsACAABAAEA///3//f//P///wIABQADAAcACgAAAPf/+v/9//z/AgAKAAgA/f/1//r/BQAKAAQA+//1//j//v/7//n/BAAOAAgA//8BAAcACAACAPz///8HAAUA+P/2/wMACgAGAAAA9f/w//n/+//5/wMABQD4//7/DgAHAAUADwAAAO3/+v8KAAQA+//3////DgAJAPv/AAADAPb/9f/+//r/+P8JABcADQD1//D/AAAJAAkAEgAMAO3/5f8AAAcA8f/y/wsADAABAA0AFwD+/+v/9f///wMAAwD5//r/CgADAPT//f8HAAYACAD+//f/FAAoAAoA5v/b/+z/EAAZAAEA/P8BAO//8P8EAPv/9P8OABcABwAEAPr/4f/m/w0AIAAFAOr/+f8WABMABAADAAAA+P///xEADwD1/+P/8f8FAAQA+f/1/wEAGQAaAPz/6f/z/wwAHwAJANj/y//l////IgAzAAoA6/8FABIA//8AAAQA8//t//v/CwASAAUA+P8DAAwA+v/r//X/BgAMAP//9v8LABgA9v/T/+f/EgAbAA4ABgD3/+D/5f8CABQAJQAzABQA5f/o//n/3P/P/w4AQgAYAOT/8v/7/9X/4P8oAD4AIAAVAPz/v/+2//j/LQAvACAAFQALAAEA9P/o/+P/4v/j/+v/9/8LADMATQAdAM7/wP/c/+T/BQBEADsA+f/y/wcA3v/F/xAAVAAqAOn/8P/5/8//wf/y/xsAJQAtAB0A8f/g/+r/7v8DADEANgD3/8r/6/8VAAYA8P8AABcAFgD8/+L/6f////7/+P8IABoAGAABAOr/8P/6/+z/+P8UAPH/yP/8/0IASwA3AAkAxf+6//H/HwAeAPf/1P/l/xQAIQAJAPP//P8eACYA9//N/93/BgAPAPn/8v/7/+//7v8WACUA8v/R/wQARgA6APT/2P/p/+v//P8kABYA+f8XABsA6P/f/+7/4f/9/z0APgAAALn/o//o/zAAHwD6//r/+v8LACAABQDy/wwADADy//b/AwAQABYA7f/E/+n/FAABAP3/JwAoAPj//v8uABMAxP/A/+7//P8RADQAFADO/8P/+v8rACcADQASABMA8//z/wQA6f/i/xAAHQABAO//7f8PACYA5/++//X/BQDr/xcANAABAP//LwAuAB4AGQD7/9j/z//d/woAJgD7/8j/0v/5/wYA8f/i//r/GgARAPD/7P8VAD4AMAAQABsAKgAFAMf/rf/d/zQASAAAALr/vf/2/yYAJAAVAA0A5v/N//3/GgD+/woAMAAoABoACADV/8T/0/+//8z/GAAoAPT/8v8WABoAGgAzAEIAKAADAAYAGgD4/7P/pP/h/yAAIgAHAAYAAgDj/+X/BwDx/7v/3P9CAGcALADw/+f/9f/5//f/+v8IAA4AAAABABsACgDS/9X/DgAYAPL/4P/s/wMAHAAnABoAAQD1/wgAKwAuAPz/w//A/+L/7v/y/wQAAwADADEAQQD//9v/CwAlAPr/1//r/wUA8f/P/9b//P8kAD4AJgDr/+r/JAAzAPv/2v8CACkAFwD7/+//3P/f//n/4f/Q/xoASgAOAN3/8f8EAPT/3P/x/ywAJgD8/x0ANgD6/+n/CQD2/+7/CgABAPz/BQDX/83/IgA9APH/wP/i/yMALgABAP3/CgDa/9z/KgAQALr/5P8yACAAAwALAPv/7v8TACYA+P/t/ycAFgDH/+T/MgAgAPr/0P+P/93/dQA9AMT/CwBSAPr/q//R/x0AEQDA//b/dwA1ANL/MABJAMv/tP/N/7r/7v8EAMT/BQBsAC0A/v9HAE8ACgDo/+v/+f/n/9L/BwAbAMP/p//u/xMAGgAkABIAGgBFADMA2/+n//f/dQA+AHL/RP/y/30AKwCW/xkAEQFZAA7/5//5AIX/RP7T/10BSgDe/s7/WgFsAOb+HgBaAYP/bv59AIEBCgB2/zgATwCk/wf/HP8eAL0A/f9d/0QARQGGAET/tv+nADAAzP9FAMP/B//x/2gAXv/Q/zoB7P8V/gsAjgLpAJX+n/+zAI3/WP8kAKL//f+bAWAA//1o/1IBef8D/gsA+wH5AXYBAADN/Qz+dwBaADD+av9yAjwBfv7j/+YBOQBO/gj/4gAAAl8AY/0D//kDKwMv/bj7Sf/QAM3/if/e/+8AjAJ9ALv7Xf2GBI4Eb/0d/P4AEwLl/zYAXgFvAXABIP8P+1j8fgJcAyj+9fwdAX4CGQAI/4D/xf9IAD8A+v+vAbUC6P/o/dn/3QCq/lf8C/zj/1oGJQYw/j78BgNXA3L7+vrpAdcBMP2I/6UDmwGn/9v/jPzl+18DtAWP/V37fQLbAuD8sv6JBNACy/3j/Ej+0ADyAnr/Jvtr/14ELf+V+qsAYAZfBKkBQP/q+wP+RAG8+4v5VgVfCcb6g/YEBiMJXPqi+cwHxwch+rX1qwMnIyk8wRq7wxqbIdujMB431gBy3SLsChIyIjkJJOwz91YR3QgC9CQISTFrLeLyC7uBvcL5KjQUNAQLi/ZNBesTzA5i/fzp8+PZ80AFcwakB8gSHBMtAWvunuGS4csCVzKhMan4r81+2woDXR01H+gKO/dk/80RHgiJ7lnsRvt7/SL5tQHcDn4SXw67Al7vpuTh8EwI1BVQEmECMfE07V358wZcCDsBzP03AS4CQvpX8/H5dAbNBzcA7v8/CTUQmA35AYTyc+vq9DEExwkvBzUFgAIu/ST8ngB9ATT+4wBfCIsHCP4i+kYA/AXcBDEADfzm/NIFKQ2mBHXzOPCe/ZkI+wg/BysHJgYcBXYCU/rn9Pn91wwzDvwBn/id+kMDMwmcBKj4r/Tg/48MTguc/0v4T/zMBLAG9QAN/qkDbAlxBgn+J/nM+9kCgQZfAuX8Q/6lAsIBtf0Y/fH+g/+F/y0A/QBYAisDngDk/LP9twHeAWv99for/n8DCAUoAQ/9qP6lAxgEWP6B+XT7oQGlBAsBFvx4/QMEcwYDAb364voDAKEDHgLp/W38cf/XApgC6/89/pH+x/+RAGcA9f8IAEkAdAC9AA8A0v30/MH/2gJIAtP/Bv/8/z4BVgG7/oP76/xxAt8EsgFR/ln+EwCUAa4BRv+m/Kr96QDPAUwAcP/H/3cAPgHfAPH+Qf5SABYCHgEX/yz+5f7qAB0CYAD+/bv+JQFuAQkASP8j/3//xgAUAS//IP70/80BmQHtACgAeP7p/fD/dQFOAA7/i/9PAKgA3AD5/3/+/P4CAUwBxf8j/8z/agC/AIMAYP/Y/u7/wQAhALv/RABjAA0ABwCZ/67++/5rAOsAYQAtAB0A6v+IAFABPgA1/vz9iP+2APQAsgD+/5T/MQC1APX/Cf85/+r/SQB1AGoADgDj/xEA+/+V/4D/0P83AKAArAD7/03/lP9MAFgAw/9S/3L/NAAYAR0BGgBR/4v/AQDq/5//pP/g/ysAdgCFADkA7v/u/wIA6f+8/67/xf8AAEsAXwAYANn//f9AACoAyv+Y/8P/FwBCACAA3P/O/wsAQgA4AAUA2//S/+n/AwAEAPz/CQAcABsADQD4/9//2//5/xAACQABAAsADwD//+z/5P/0/xkAMQAbAPL/5P/t/+//7f/0/wIAEQAgACIACwDt/+L/6//3//3/AQAFAAwAEgAJAPL/6P/6/xAADQD3/+z/8/8BAAkABgD+/wAACwANAP//7f/o//H/AwAPAA4ABwAFAAUA/f/u/+n/9P8GABEADQACAP3/AAAAAPr/9f/6/wgADwAHAPX/7f/1/wQADQAMAAcABQAEAP3/8v/r//P/BQATABMACAD9//r/+v/5//b/+P8BAA0ADwAFAPj/9v/+/wUABgACAAEAAwADAPv/8//y//3/CQAOAAsABQAAAPz/+f/3//f//P8FAAwACgACAPz/+//+/wAA///+/wAAAwAEAAAA/P/8////AgADAAIAAgABAAEA///9//z//f8AAAMABAADAAEAAAD//////v/+////AQACAAIAAAD/////AAABAAAAAAAAAAEAAQAAAP7//v///wEAAgABAAEAAAAAAP//////////AQACAAEAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAA/////wAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
	}
	if (snd) {
		snd.play();
	}
};
