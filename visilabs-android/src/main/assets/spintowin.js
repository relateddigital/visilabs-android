const AudioContext = window.AudioContext || window.webkitAudioContext;
const audioCtx = new AudioContext();

(function (window, document) {
	'use strict';
	if (typeof window.CustomEvent !== 'function') {

		window.CustomEvent = function (event, params) {

			params = params || { bubbles: false, cancelable: false, detail: undefined };

			var evt = document.createEvent('CustomEvent');
			evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
			return evt;
		};

		window.CustomEvent.prototype = window.Event.prototype;
	}

	document.addEventListener('touchstart', handleTouchStart, false);
	document.addEventListener('touchmove', handleTouchMove, false);
	document.addEventListener('touchend', handleTouchEnd, false);

	var xDown = null;
	var yDown = null;
	var xDiff = null;
	var yDiff = null;
	var timeDown = null;
	var startEl = null;

	function handleTouchEnd(e) {

		// if the user released on a different target, cancel!
		if (startEl !== e.target) return;

		var swipeThreshold = parseInt(getNearestAttribute(startEl, 'data-swipe-threshold', '20'), 10); // default 20px
		var swipeTimeout = parseInt(getNearestAttribute(startEl, 'data-swipe-timeout', '500'), 10);    // default 500ms
		var timeDiff = Date.now() - timeDown;
		var eventType = '';
		var changedTouches = e.changedTouches || e.touches || [];

		if (Math.abs(xDiff) > Math.abs(yDiff)) { // most significant
			if (Math.abs(xDiff) > swipeThreshold && timeDiff < swipeTimeout) {
				if (xDiff > 0) {
					eventType = 'swiped-left';
				}
				else {
					eventType = 'swiped-right';
				}
			}
		}
		else if (Math.abs(yDiff) > swipeThreshold && timeDiff < swipeTimeout) {
			if (yDiff > 0) {
				eventType = 'swiped-up';
			}
			else {
				eventType = 'swiped-down';
			}
		}

		if (eventType !== '') {

			var eventData = {
				dir: eventType.replace(/swiped-/, ''),
				xStart: parseInt(xDown, 10),
				xEnd: parseInt((changedTouches[0] || {}).clientX || -1, 10),
				yStart: parseInt(yDown, 10),
				yEnd: parseInt((changedTouches[0] || {}).clientY || -1, 10)
			};

			// fire `swiped` event event on the element that started the swipe
			startEl.dispatchEvent(new CustomEvent('swiped', { bubbles: true, cancelable: true, detail: eventData }));

			// fire `swiped-dir` event on the element that started the swipe
			startEl.dispatchEvent(new CustomEvent(eventType, { bubbles: true, cancelable: true, detail: eventData }));
		}

		// reset values
		xDown = null;
		yDown = null;
		timeDown = null;
	}

	/**
	 * Records current location on touchstart event
	 * @param {object} e - browser event object
	 * @returns {void}
	 */
	function handleTouchStart(e) {
		// if the element has data-swipe-ignore="true" we stop listening for swipe events
		if (e.target.getAttribute('data-swipe-ignore') === 'true') return;

		startEl = e.target;

		timeDown = Date.now();
		xDown = e.touches[0].clientX;
		yDown = e.touches[0].clientY;
		xDiff = 0;
		yDiff = 0;
	}

	function handleTouchMove(e) {

		if (!xDown || !yDown) return;

		var xUp = e.touches[0].clientX;
		var yUp = e.touches[0].clientY;

		xDiff = xDown - xUp;
		yDiff = yDown - yUp;
	}

	function getNearestAttribute(el, attributeName, defaultValue) {
		while (el && el !== document.documentElement) {

			var attributeValue = el.getAttribute(attributeName);

			if (attributeValue) {
				return attributeValue;
			}

			el = el.parentNode;
		}

		return defaultValue;
	}

}(window, document));

! function (a, b) {
	"object" == typeof module && "object" == typeof module.exports ? module.exports = a.document ? b(a, !0) : function (a) {
		if (!a.document) throw new Error("jQuery requires a window with a document");
		return b(a)
	} : b(a)
}("undefined" != typeof window ? window : this, function (a, b) {
	var c = [],
		d = c.slice,
		e = c.concat,
		f = c.push,
		g = c.indexOf,
		h = {},
		i = h.toString,
		j = h.hasOwnProperty,
		k = {},
		l = a.document,
		m = "2.1.1 -ajax,-ajax/jsonp,-ajax/load,-ajax/parseJSON,-ajax/parseXML,-ajax/script,-ajax/var/nonce,-ajax/var/rquery,-ajax/xhr,-manipulation/_evalUrl,-deprecated,-event-alias,-offset,-css/hiddenVisibleSelectors,-effects/animatedSelector,-wrap",
		n = function (a, b) {
			return new n.fn.init(a, b)
		},
		o = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,
		p = /^-ms-/,
		q = /-([\da-z])/gi,
		r = function (a, b) {
			return b.toUpperCase()
		};
	n.fn = n.prototype = {
		jquery: m,
		constructor: n,
		selector: "",
		length: 0,
		toArray: function () {
			return d.call(this)
		},
		get: function (a) {
			return null != a ? 0 > a ? this[a + this.length] : this[a] : d.call(this)
		},
		pushStack: function (a) {
			var b = n.merge(this.constructor(), a);
			return b.prevObject = this, b.context = this.context, b
		},
		each: function (a, b) {
			return n.each(this, a, b)
		},
		map: function (a) {
			return this.pushStack(n.map(this, function (b, c) {
				return a.call(b, c, b)
			}))
		},
		slice: function () {
			return this.pushStack(d.apply(this, arguments))
		},
		first: function () {
			return this.eq(0)
		},
		last: function () {
			return this.eq(-1)
		},
		eq: function (a) {
			var b = this.length,
				c = +a + (0 > a ? b : 0);
			return this.pushStack(c >= 0 && b > c ? [this[c]] : [])
		},
		end: function () {
			return this.prevObject || this.constructor(null)
		},
		push: f,
		sort: c.sort,
		splice: c.splice
	}, n.extend = n.fn.extend = function () {
		var a, b, c, d, e, f, g = arguments[0] || {},
			h = 1,
			i = arguments.length,
			j = !1;
		for ("boolean" == typeof g && (j = g, g = arguments[h] || {}, h++), "object" == typeof g || n.isFunction(g) || (g = {}), h === i && (g = this, h--); i > h; h++)
			if (null != (a = arguments[h]))
				for (b in a) c = g[b], d = a[b], g !== d && (j && d && (n.isPlainObject(d) || (e = n.isArray(d))) ? (e ? (e = !1, f = c && n.isArray(c) ? c : []) : f = c && n.isPlainObject(c) ? c : {}, g[b] = n.extend(j, f, d)) : void 0 !== d && (g[b] = d));
		return g
	}, n.extend({
		expando: "jQuery" + (m + Math.random()).replace(/\D/g, ""),
		isReady: !0,
		error: function (a) {
			throw new Error(a)
		},
		noop: function () { },
		isFunction: function (a) {
			return "function" === n.type(a)
		},
		isArray: Array.isArray,
		isWindow: function (a) {
			return null != a && a === a.window
		},
		isNumeric: function (a) {
			return !n.isArray(a) && a - parseFloat(a) >= 0
		},
		isPlainObject: function (a) {
			return "object" !== n.type(a) || a.nodeType || n.isWindow(a) ? !1 : a.constructor && !j.call(a.constructor.prototype, "isPrototypeOf") ? !1 : !0
		},
		isEmptyObject: function (a) {
			var b;
			for (b in a) return !1;
			return !0
		},
		type: function (a) {
			return null == a ? a + "" : "object" == typeof a || "function" == typeof a ? h[i.call(a)] || "object" : typeof a
		},
		globalEval: function (a) {
			var b, c = eval;
			a = n.trim(a), a && (1 === a.indexOf("use strict") ? (b = l.createElement("script"), b.text = a, l.head.appendChild(b).parentNode.removeChild(b)) : c(a))
		},
		camelCase: function (a) {
			return a.replace(p, "ms-").replace(q, r)
		},
		nodeName: function (a, b) {
			return a.nodeName && a.nodeName.toLowerCase() === b.toLowerCase()
		},
		each: function (a, b, c) {
			var d, e = 0,
				f = a.length,
				g = s(a);
			if (c) {
				if (g) {
					for (; f > e; e++)
						if (d = b.apply(a[e], c), d === !1) break
				} else
					for (e in a)
						if (d = b.apply(a[e], c), d === !1) break
			} else if (g) {
				for (; f > e; e++)
					if (d = b.call(a[e], e, a[e]), d === !1) break
			} else
				for (e in a)
					if (d = b.call(a[e], e, a[e]), d === !1) break;
			return a
		},
		trim: function (a) {
			return null == a ? "" : (a + "").replace(o, "")
		},
		makeArray: function (a, b) {
			var c = b || [];
			return null != a && (s(Object(a)) ? n.merge(c, "string" == typeof a ? [a] : a) : f.call(c, a)), c
		},
		inArray: function (a, b, c) {
			return null == b ? -1 : g.call(b, a, c)
		},
		merge: function (a, b) {
			for (var c = +b.length, d = 0, e = a.length; c > d; d++) a[e++] = b[d];
			return a.length = e, a
		},
		grep: function (a, b, c) {
			for (var d, e = [], f = 0, g = a.length, h = !c; g > f; f++) d = !b(a[f], f), d !== h && e.push(a[f]);
			return e
		},
		map: function (a, b, c) {
			var d, f = 0,
				g = a.length,
				h = s(a),
				i = [];
			if (h)
				for (; g > f; f++) d = b(a[f], f, c), null != d && i.push(d);
			else
				for (f in a) d = b(a[f], f, c), null != d && i.push(d);
			return e.apply([], i)
		},
		guid: 1,
		proxy: function (a, b) {
			var c, e, f;
			return "string" == typeof b && (c = a[b], b = a, a = c), n.isFunction(a) ? (e = d.call(arguments, 2), f = function () {
				return a.apply(b || this, e.concat(d.call(arguments)))
			}, f.guid = a.guid = a.guid || n.guid++, f) : void 0
		},
		now: Date.now,
		support: k
	}), n.each("Boolean Number String Function Array Date RegExp Object Error".split(" "), function (a, b) {
		h["[object " + b + "]"] = b.toLowerCase()
	});

	function s(a) {
		var b = a.length,
			c = n.type(a);
		return "function" === c || n.isWindow(a) ? !1 : 1 === a.nodeType && b ? !0 : "array" === c || 0 === b || "number" == typeof b && b > 0 && b - 1 in a
	}
	var t = a.document.documentElement,
		u, v = t.matches || t.webkitMatchesSelector || t.mozMatchesSelector || t.oMatchesSelector || t.msMatchesSelector,
		w = function (a, b) {
			if (a === b) return u = !0, 0;
			var c = b.compareDocumentPosition && a.compareDocumentPosition && a.compareDocumentPosition(b);
			return c ? 1 & c ? a === l || n.contains(l, a) ? -1 : b === l || n.contains(l, b) ? 1 : 0 : 4 & c ? -1 : 1 : a.compareDocumentPosition ? -1 : 1
		};
	n.extend({
		find: function (a, b, c, d) {
			var e, f, g = 0;
			if (c = c || [], b = b || l, !a || "string" != typeof a) return c;
			if (1 !== (f = b.nodeType) && 9 !== f) return [];
			if (d)
				while (e = d[g++]) n.find.matchesSelector(e, a) && c.push(e);
			else n.merge(c, b.querySelectorAll(a));
			return c
		},
		unique: function (a) {
			var b, c = [],
				d = 0,
				e = 0;
			if (u = !1, a.sort(w), u) {
				while (b = a[d++]) b === a[d] && (e = c.push(d));
				while (e--) a.splice(c[e], 1)
			}
			return a
		},
		text: function (a) {
			var b, c = "",
				d = 0,
				e = a.nodeType;
			if (e) {
				if (1 === e || 9 === e || 11 === e) return a.textContent;
				if (3 === e || 4 === e) return a.nodeValue
			} else
				while (b = a[d++]) c += n.text(b);
			return c
		},
		contains: function (a, b) {
			var c = 9 === a.nodeType ? a.documentElement : a,
				d = b && b.parentNode;
			return a === d || !(!d || 1 !== d.nodeType || !c.contains(d))
		},
		isXMLDoc: function (a) {
			return "HTML" !== (a.ownerDocument || a).documentElement.nodeName
		},
		expr: {
			attrHandle: {},
			match: {
				bool: /^(?:checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped)$/i,
				needsContext: /^[\x20\t\r\n\f]*[>+~]/
			}
		}
	}), n.extend(n.find, {
		matches: function (a, b) {
			return n.find(a, null, null, b)
		},
		matchesSelector: function (a, b) {
			return v.call(a, b)
		},
		attr: function (a, b) {
			return a.getAttribute(b)
		}
	});
	var x = n.expr.match.needsContext,
		y = /^<(\w+)\s*\/?>(?:<\/\1>|)$/,
		z = /^.[^:#\[\.,]*$/;

	function A(a, b, c) {
		if (n.isFunction(b)) return n.grep(a, function (a, d) {
			return !!b.call(a, d, a) !== c
		});
		if (b.nodeType) return n.grep(a, function (a) {
			return a === b !== c
		});
		if ("string" == typeof b) {
			if (z.test(b)) return n.filter(b, a, c);
			b = n.filter(b, a)
		}
		return n.grep(a, function (a) {
			return g.call(b, a) >= 0 !== c
		})
	}
	n.filter = function (a, b, c) {
		var d = b[0];
		return c && (a = ":not(" + a + ")"), 1 === b.length && 1 === d.nodeType ? n.find.matchesSelector(d, a) ? [d] : [] : n.find.matches(a, n.grep(b, function (a) {
			return 1 === a.nodeType
		}))
	}, n.fn.extend({
		find: function (a) {
			var b, c = this.length,
				d = [],
				e = this;
			if ("string" != typeof a) return this.pushStack(n(a).filter(function () {
				for (b = 0; c > b; b++)
					if (n.contains(e[b], this)) return !0
			}));
			for (b = 0; c > b; b++) n.find(a, e[b], d);
			return d = this.pushStack(c > 1 ? n.unique(d) : d), d.selector = this.selector ? this.selector + " " + a : a, d
		},
		filter: function (a) {
			return this.pushStack(A(this, a || [], !1))
		},
		not: function (a) {
			return this.pushStack(A(this, a || [], !0))
		},
		is: function (a) {
			return !!A(this, "string" == typeof a && x.test(a) ? n(a) : a || [], !1).length
		}
	});
	var B, C = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,
		D = n.fn.init = function (a, b) {
			var c, d;
			if (!a) return this;
			if ("string" == typeof a) {
				if (c = "<" === a[0] && ">" === a[a.length - 1] && a.length >= 3 ? [null, a, null] : C.exec(a), !c || !c[1] && b) return !b || b.jquery ? (b || B).find(a) : this.constructor(b).find(a);
				if (c[1]) {
					if (b = b instanceof n ? b[0] : b, n.merge(this, n.parseHTML(c[1], b && b.nodeType ? b.ownerDocument || b : l, !0)), y.test(c[1]) && n.isPlainObject(b))
						for (c in b) n.isFunction(this[c]) ? this[c](b[c]) : this.attr(c, b[c]);
					return this
				}
				return d = l.getElementById(c[2]), d && d.parentNode && (this.length = 1, this[0] = d), this.context = l, this.selector = a, this
			}
			return a.nodeType ? (this.context = this[0] = a, this.length = 1, this) : n.isFunction(a) ? "undefined" != typeof B.ready ? B.ready(a) : a(n) : (void 0 !== a.selector && (this.selector = a.selector, this.context = a.context), n.makeArray(a, this))
		};
	D.prototype = n.fn, B = n(l);
	var E = /^(?:parents|prev(?:Until|All))/,
		F = {
			children: !0,
			contents: !0,
			next: !0,
			prev: !0
		};
	n.extend({
		dir: function (a, b, c) {
			var d = [],
				e = void 0 !== c;
			while ((a = a[b]) && 9 !== a.nodeType)
				if (1 === a.nodeType) {
					if (e && n(a).is(c)) break;
					d.push(a)
				} return d
		},
		sibling: function (a, b) {
			for (var c = []; a; a = a.nextSibling) 1 === a.nodeType && a !== b && c.push(a);
			return c
		}
	}), n.fn.extend({
		has: function (a) {
			var b = n(a, this),
				c = b.length;
			return this.filter(function () {
				for (var a = 0; c > a; a++)
					if (n.contains(this, b[a])) return !0
			})
		},
		closest: function (a, b) {
			for (var c, d = 0, e = this.length, f = [], g = x.test(a) || "string" != typeof a ? n(a, b || this.context) : 0; e > d; d++)
				for (c = this[d]; c && c !== b; c = c.parentNode)
					if (c.nodeType < 11 && (g ? g.index(c) > -1 : 1 === c.nodeType && n.find.matchesSelector(c, a))) {
						f.push(c);
						break
					} return this.pushStack(f.length > 1 ? n.unique(f) : f)
		},
		index: function (a) {
			return a ? "string" == typeof a ? g.call(n(a), this[0]) : g.call(this, a.jquery ? a[0] : a) : this[0] && this[0].parentNode ? this.first().prevAll().length : -1
		},
		add: function (a, b) {
			return this.pushStack(n.unique(n.merge(this.get(), n(a, b))))
		},
		addBack: function (a) {
			return this.add(null == a ? this.prevObject : this.prevObject.filter(a))
		}
	});

	function G(a, b) {
		while ((a = a[b]) && 1 !== a.nodeType);
		return a
	}
	n.each({
		parent: function (a) {
			var b = a.parentNode;
			return b && 11 !== b.nodeType ? b : null
		},
		parents: function (a) {
			return n.dir(a, "parentNode")
		},
		parentsUntil: function (a, b, c) {
			return n.dir(a, "parentNode", c)
		},
		next: function (a) {
			return G(a, "nextSibling")
		},
		prev: function (a) {
			return G(a, "previousSibling")
		},
		nextAll: function (a) {
			return n.dir(a, "nextSibling")
		},
		prevAll: function (a) {
			return n.dir(a, "previousSibling")
		},
		nextUntil: function (a, b, c) {
			return n.dir(a, "nextSibling", c)
		},
		prevUntil: function (a, b, c) {
			return n.dir(a, "previousSibling", c)
		},
		siblings: function (a) {
			return n.sibling((a.parentNode || {}).firstChild, a)
		},
		children: function (a) {
			return n.sibling(a.firstChild)
		},
		contents: function (a) {
			return a.contentDocument || n.merge([], a.childNodes)
		}
	}, function (a, b) {
		n.fn[a] = function (c, d) {
			var e = n.map(this, b, c);
			return "Until" !== a.slice(-5) && (d = c), d && "string" == typeof d && (e = n.filter(d, e)), this.length > 1 && (F[a] || n.unique(e), E.test(a) && e.reverse()), this.pushStack(e)
		}
	});
	var H = /\S+/g,
		I = {};

	function J(a) {
		var b = I[a] = {};
		return n.each(a.match(H) || [], function (a, c) {
			b[c] = !0
		}), b
	}
	n.Callbacks = function (a) {
		a = "string" == typeof a ? I[a] || J(a) : n.extend({}, a);
		var b, c, d, e, f, g, h = [],
			i = !a.once && [],
			j = function (l) {
				for (b = a.memory && l, c = !0, g = e || 0, e = 0, f = h.length, d = !0; h && f > g; g++)
					if (h[g].apply(l[0], l[1]) === !1 && a.stopOnFalse) {
						b = !1;
						break
					} d = !1, h && (i ? i.length && j(i.shift()) : b ? h = [] : k.disable())
			},
			k = {
				add: function () {
					if (h) {
						var c = h.length;
						! function g(b) {
							n.each(b, function (b, c) {
								var d = n.type(c);
								"function" === d ? a.unique && k.has(c) || h.push(c) : c && c.length && "string" !== d && g(c)
							})
						}(arguments), d ? f = h.length : b && (e = c, j(b))
					}
					return this
				},
				remove: function () {
					return h && n.each(arguments, function (a, b) {
						var c;
						while ((c = n.inArray(b, h, c)) > -1) h.splice(c, 1), d && (f >= c && f--, g >= c && g--)
					}), this
				},
				has: function (a) {
					return a ? n.inArray(a, h) > -1 : !(!h || !h.length)
				},
				empty: function () {
					return h = [], f = 0, this
				},
				disable: function () {
					return h = i = b = void 0, this
				},
				disabled: function () {
					return !h
				},
				lock: function () {
					return i = void 0, b || k.disable(), this
				},
				locked: function () {
					return !i
				},
				fireWith: function (a, b) {
					return !h || c && !i || (b = b || [], b = [a, b.slice ? b.slice() : b], d ? i.push(b) : j(b)), this
				},
				fire: function () {
					return k.fireWith(this, arguments), this
				},
				fired: function () {
					return !!c
				}
			};
		return k
	}, n.extend({
		Deferred: function (a) {
			var b = [
				["resolve", "done", n.Callbacks("once memory"), "resolved"],
				["reject", "fail", n.Callbacks("once memory"), "rejected"],
				["notify", "progress", n.Callbacks("memory")]
			],
				c = "pending",
				d = {
					state: function () {
						return c
					},
					always: function () {
						return e.done(arguments).fail(arguments), this
					},
					then: function () {
						var a = arguments;
						return n.Deferred(function (c) {
							n.each(b, function (b, f) {
								var g = n.isFunction(a[b]) && a[b];
								e[f[1]](function () {
									var a = g && g.apply(this, arguments);
									a && n.isFunction(a.promise) ? a.promise().done(c.resolve).fail(c.reject).progress(c.notify) : c[f[0] + "With"](this === d ? c.promise() : this, g ? [a] : arguments)
								})
							}), a = null
						}).promise()
					},
					promise: function (a) {
						return null != a ? n.extend(a, d) : d
					}
				},
				e = {};
			return d.pipe = d.then, n.each(b, function (a, f) {
				var g = f[2],
					h = f[3];
				d[f[1]] = g.add, h && g.add(function () {
					c = h
				}, b[1 ^ a][2].disable, b[2][2].lock), e[f[0]] = function () {
					return e[f[0] + "With"](this === e ? d : this, arguments), this
				}, e[f[0] + "With"] = g.fireWith
			}), d.promise(e), a && a.call(e, e), e
		},
		when: function (a) {
			var b = 0,
				c = d.call(arguments),
				e = c.length,
				f = 1 !== e || a && n.isFunction(a.promise) ? e : 0,
				g = 1 === f ? a : n.Deferred(),
				h = function (a, b, c) {
					return function (e) {
						b[a] = this, c[a] = arguments.length > 1 ? d.call(arguments) : e, c === i ? g.notifyWith(b, c) : --f || g.resolveWith(b, c)
					}
				},
				i, j, k;
			if (e > 1)
				for (i = new Array(e), j = new Array(e), k = new Array(e); e > b; b++) c[b] && n.isFunction(c[b].promise) ? c[b].promise().done(h(b, k, c)).fail(g.reject).progress(h(b, j, i)) : --f;
			return f || g.resolveWith(k, c), g.promise()
		}
	});
	var K;
	n.fn.ready = function (a) {
		return n.ready.promise().done(a), this
	}, n.extend({
		isReady: !1,
		readyWait: 1,
		holdReady: function (a) {
			a ? n.readyWait++ : n.ready(!0)
		},
		ready: function (a) {
			(a === !0 ? --n.readyWait : n.isReady) || (n.isReady = !0, a !== !0 && --n.readyWait > 0 || (K.resolveWith(l, [n]), n.fn.triggerHandler && (n(l).triggerHandler("ready"), n(l).off("ready"))))
		}
	});

	function L() {
		l.removeEventListener("DOMContentLoaded", L, !1), a.removeEventListener("load", L, !1), n.ready()
	}
	n.ready.promise = function (b) {
		return K || (K = n.Deferred(), "complete" === l.readyState ? setTimeout(n.ready) : (l.addEventListener("DOMContentLoaded", L, !1), a.addEventListener("load", L, !1))), K.promise(b)
	}, n.ready.promise();
	var M = n.access = function (a, b, c, d, e, f, g) {
		var h = 0,
			i = a.length,
			j = null == c;
		if ("object" === n.type(c)) {
			e = !0;
			for (h in c) n.access(a, b, h, c[h], !0, f, g)
		} else if (void 0 !== d && (e = !0, n.isFunction(d) || (g = !0), j && (g ? (b.call(a, d), b = null) : (j = b, b = function (a, b, c) {
			return j.call(n(a), c)
		})), b))
			for (; i > h; h++) b(a[h], c, g ? d : d.call(a[h], h, b(a[h], c)));
		return e ? a : j ? b.call(a) : i ? b(a[0], c) : f
	};
	n.acceptData = function (a) {
		return 1 === a.nodeType || 9 === a.nodeType || !+a.nodeType
	};

	function N() {
		Object.defineProperty(this.cache = {}, 0, {
			get: function () {
				return {}
			}
		}), this.expando = n.expando + Math.random()
	}
	N.uid = 1, N.accepts = n.acceptData, N.prototype = {
		key: function (a) {
			if (!N.accepts(a)) return 0;
			var b = {},
				c = a[this.expando];
			if (!c) {
				c = N.uid++;
				try {
					b[this.expando] = {
						value: c
					}, Object.defineProperties(a, b)
				} catch (d) {
					b[this.expando] = c, n.extend(a, b)
				}
			}
			return this.cache[c] || (this.cache[c] = {}), c
		},
		set: function (a, b, c) {
			var d, e = this.key(a),
				f = this.cache[e];
			if ("string" == typeof b) f[b] = c;
			else if (n.isEmptyObject(f)) n.extend(this.cache[e], b);
			else
				for (d in b) f[d] = b[d];
			return f
		},
		get: function (a, b) {
			var c = this.cache[this.key(a)];
			return void 0 === b ? c : c[b]
		},
		access: function (a, b, c) {
			var d;
			return void 0 === b || b && "string" == typeof b && void 0 === c ? (d = this.get(a, b), void 0 !== d ? d : this.get(a, n.camelCase(b))) : (this.set(a, b, c), void 0 !== c ? c : b)
		},
		remove: function (a, b) {
			var c, d, e, f = this.key(a),
				g = this.cache[f];
			if (void 0 === b) this.cache[f] = {};
			else {
				n.isArray(b) ? d = b.concat(b.map(n.camelCase)) : (e = n.camelCase(b), b in g ? d = [b, e] : (d = e, d = d in g ? [d] : d.match(H) || [])), c = d.length;
				while (c--) delete g[d[c]]
			}
		},
		hasData: function (a) {
			return !n.isEmptyObject(this.cache[a[this.expando]] || {})
		},
		discard: function (a) {
			a[this.expando] && delete this.cache[a[this.expando]]
		}
	};
	var O = new N,
		P = new N,
		Q = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,
		R = /([A-Z])/g;

	function S(a, b, c) {
		var d;
		if (void 0 === c && 1 === a.nodeType)
			if (d = "data-" + b.replace(R, "-$1").toLowerCase(), c = a.getAttribute(d), "string" == typeof c) {
				try {
					c = "true" === c ? !0 : "false" === c ? !1 : "null" === c ? null : +c + "" === c ? +c : Q.test(c) ? n.parseJSON(c) : c
				} catch (e) { }
				P.set(a, b, c)
			} else c = void 0;
		return c
	}
	n.extend({
		hasData: function (a) {
			return P.hasData(a) || O.hasData(a)
		},
		data: function (a, b, c) {
			return P.access(a, b, c)
		},
		removeData: function (a, b) {
			P.remove(a, b)
		},
		_data: function (a, b, c) {
			return O.access(a, b, c)
		},
		_removeData: function (a, b) {
			O.remove(a, b)
		}
	}), n.fn.extend({
		data: function (a, b) {
			var c, d, e, f = this[0],
				g = f && f.attributes;
			if (void 0 === a) {
				if (this.length && (e = P.get(f), 1 === f.nodeType && !O.get(f, "hasDataAttrs"))) {
					c = g.length;
					while (c--) g[c] && (d = g[c].name, 0 === d.indexOf("data-") && (d = n.camelCase(d.slice(5)), S(f, d, e[d])));
					O.set(f, "hasDataAttrs", !0)
				}
				return e
			}
			return "object" == typeof a ? this.each(function () {
				P.set(this, a)
			}) : M(this, function (b) {
				var c, d = n.camelCase(a);
				if (f && void 0 === b) {
					if (c = P.get(f, a), void 0 !== c) return c;
					if (c = P.get(f, d), void 0 !== c) return c;
					if (c = S(f, d, void 0), void 0 !== c) return c
				} else this.each(function () {
					var c = P.get(this, d);
					P.set(this, d, b), -1 !== a.indexOf("-") && void 0 !== c && P.set(this, a, b)
				})
			}, null, b, arguments.length > 1, null, !0)
		},
		removeData: function (a) {
			return this.each(function () {
				P.remove(this, a)
			})
		}
	}), n.extend({
		queue: function (a, b, c) {
			var d;
			return a ? (b = (b || "fx") + "queue", d = O.get(a, b), c && (!d || n.isArray(c) ? d = O.access(a, b, n.makeArray(c)) : d.push(c)), d || []) : void 0
		},
		dequeue: function (a, b) {
			b = b || "fx";
			var c = n.queue(a, b),
				d = c.length,
				e = c.shift(),
				f = n._queueHooks(a, b),
				g = function () {
					n.dequeue(a, b)
				};
			"inprogress" === e && (e = c.shift(), d--), e && ("fx" === b && c.unshift("inprogress"), delete f.stop, e.call(a, g, f)), !d && f && f.empty.fire()
		},
		_queueHooks: function (a, b) {
			var c = b + "queueHooks";
			return O.get(a, c) || O.access(a, c, {
				empty: n.Callbacks("once memory").add(function () {
					O.remove(a, [b + "queue", c])
				})
			})
		}
	}), n.fn.extend({
		queue: function (a, b) {
			var c = 2;
			return "string" != typeof a && (b = a, a = "fx", c--), arguments.length < c ? n.queue(this[0], a) : void 0 === b ? this : this.each(function () {
				var c = n.queue(this, a, b);
				n._queueHooks(this, a), "fx" === a && "inprogress" !== c[0] && n.dequeue(this, a)
			})
		},
		dequeue: function (a) {
			return this.each(function () {
				n.dequeue(this, a)
			})
		},
		clearQueue: function (a) {
			return this.queue(a || "fx", [])
		},
		promise: function (a, b) {
			var c, d = 1,
				e = n.Deferred(),
				f = this,
				g = this.length,
				h = function () {
					--d || e.resolveWith(f, [f])
				};
			"string" != typeof a && (b = a, a = void 0), a = a || "fx";
			while (g--) c = O.get(f[g], a + "queueHooks"), c && c.empty && (d++, c.empty.add(h));
			return h(), e.promise(b)
		}
	});
	var T = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,
		U = ["Top", "Right", "Bottom", "Left"],
		V = function (a, b) {
			return a = b || a, "none" === n.css(a, "display") || !n.contains(a.ownerDocument, a)
		},
		W = /^(?:checkbox|radio)$/i;
	! function () {
		var a = l.createDocumentFragment(),
			b = a.appendChild(l.createElement("div")),
			c = l.createElement("input");
		c.setAttribute("type", "radio"), c.setAttribute("checked", "checked"), c.setAttribute("name", "t"), b.appendChild(c), k.checkClone = b.cloneNode(!0).cloneNode(!0).lastChild.checked, b.innerHTML = "<textarea>x</textarea>", k.noCloneChecked = !!b.cloneNode(!0).lastChild.defaultValue
	}();
	var X = "undefined";
	k.focusinBubbles = "onfocusin" in a;
	var Y = /^key/,
		Z = /^(?:mouse|pointer|contextmenu)|click/,
		$ = /^(?:focusinfocus|focusoutblur)$/,
		_ = /^([^.]*)(?:\.(.+)|)$/;

	function ab() {
		return !0
	}

	function bb() {
		return !1
	}

	function cb() {
		try {
			return l.activeElement
		} catch (a) { }
	}
	n.event = {
		global: {},
		add: function (a, b, c, d, e) {
			var f, g, h, i, j, k, l, m, o, p, q, r = O.get(a);
			if (r) {
				c.handler && (f = c, c = f.handler, e = f.selector), c.guid || (c.guid = n.guid++), (i = r.events) || (i = r.events = {}), (g = r.handle) || (g = r.handle = function (b) {
					return typeof n !== X && n.event.triggered !== b.type ? n.event.dispatch.apply(a, arguments) : void 0
				}), b = (b || "").match(H) || [""], j = b.length;
				while (j--) h = _.exec(b[j]) || [], o = q = h[1], p = (h[2] || "").split(".").sort(), o && (l = n.event.special[o] || {}, o = (e ? l.delegateType : l.bindType) || o, l = n.event.special[o] || {}, k = n.extend({
					type: o,
					origType: q,
					data: d,
					handler: c,
					guid: c.guid,
					selector: e,
					needsContext: e && n.expr.match.needsContext.test(e),
					namespace: p.join(".")
				}, f), (m = i[o]) || (m = i[o] = [], m.delegateCount = 0, l.setup && l.setup.call(a, d, p, g) !== !1 || a.addEventListener && a.addEventListener(o, g, !1)), l.add && (l.add.call(a, k), k.handler.guid || (k.handler.guid = c.guid)), e ? m.splice(m.delegateCount++, 0, k) : m.push(k), n.event.global[o] = !0)
			}
		},
		remove: function (a, b, c, d, e) {
			var f, g, h, i, j, k, l, m, o, p, q, r = O.hasData(a) && O.get(a);
			if (r && (i = r.events)) {
				b = (b || "").match(H) || [""], j = b.length;
				while (j--)
					if (h = _.exec(b[j]) || [], o = q = h[1], p = (h[2] || "").split(".").sort(), o) {
						l = n.event.special[o] || {}, o = (d ? l.delegateType : l.bindType) || o, m = i[o] || [], h = h[2] && new RegExp("(^|\\.)" + p.join("\\.(?:.*\\.|)") + "(\\.|$)"), g = f = m.length;
						while (f--) k = m[f], !e && q !== k.origType || c && c.guid !== k.guid || h && !h.test(k.namespace) || d && d !== k.selector && ("**" !== d || !k.selector) || (m.splice(f, 1), k.selector && m.delegateCount--, l.remove && l.remove.call(a, k));
						g && !m.length && (l.teardown && l.teardown.call(a, p, r.handle) !== !1 || n.removeEvent(a, o, r.handle), delete i[o])
					} else
						for (o in i) n.event.remove(a, o + b[j], c, d, !0);
				n.isEmptyObject(i) && (delete r.handle, O.remove(a, "events"))
			}
		},
		trigger: function (b, c, d, e) {
			var f, g, h, i, k, m, o, p = [d || l],
				q = j.call(b, "type") ? b.type : b,
				r = j.call(b, "namespace") ? b.namespace.split(".") : [];
			if (g = h = d = d || l, 3 !== d.nodeType && 8 !== d.nodeType && !$.test(q + n.event.triggered) && (q.indexOf(".") >= 0 && (r = q.split("."), q = r.shift(), r.sort()), k = q.indexOf(":") < 0 && "on" + q, b = b[n.expando] ? b : new n.Event(q, "object" == typeof b && b), b.isTrigger = e ? 2 : 3, b.namespace = r.join("."), b.namespace_re = b.namespace ? new RegExp("(^|\\.)" + r.join("\\.(?:.*\\.|)") + "(\\.|$)") : null, b.result = void 0, b.target || (b.target = d), c = null == c ? [b] : n.makeArray(c, [b]), o = n.event.special[q] || {}, e || !o.trigger || o.trigger.apply(d, c) !== !1)) {
				if (!e && !o.noBubble && !n.isWindow(d)) {
					for (i = o.delegateType || q, $.test(i + q) || (g = g.parentNode); g; g = g.parentNode) p.push(g), h = g;
					h === (d.ownerDocument || l) && p.push(h.defaultView || h.parentWindow || a)
				}
				f = 0;
				while ((g = p[f++]) && !b.isPropagationStopped()) b.type = f > 1 ? i : o.bindType || q, m = (O.get(g, "events") || {})[b.type] && O.get(g, "handle"), m && m.apply(g, c), m = k && g[k], m && m.apply && n.acceptData(g) && (b.result = m.apply(g, c), b.result === !1 && b.preventDefault());
				return b.type = q, e || b.isDefaultPrevented() || o._default && o._default.apply(p.pop(), c) !== !1 || !n.acceptData(d) || k && n.isFunction(d[q]) && !n.isWindow(d) && (h = d[k], h && (d[k] = null), n.event.triggered = q, d[q](), n.event.triggered = void 0, h && (d[k] = h)), b.result
			}
		},
		dispatch: function (a) {
			a = n.event.fix(a);
			var b, c, e, f, g, h = [],
				i = d.call(arguments),
				j = (O.get(this, "events") || {})[a.type] || [],
				k = n.event.special[a.type] || {};
			if (i[0] = a, a.delegateTarget = this, !k.preDispatch || k.preDispatch.call(this, a) !== !1) {
				h = n.event.handlers.call(this, a, j), b = 0;
				while ((f = h[b++]) && !a.isPropagationStopped()) {
					a.currentTarget = f.elem, c = 0;
					while ((g = f.handlers[c++]) && !a.isImmediatePropagationStopped()) (!a.namespace_re || a.namespace_re.test(g.namespace)) && (a.handleObj = g, a.data = g.data, e = ((n.event.special[g.origType] || {}).handle || g.handler).apply(f.elem, i), void 0 !== e && (a.result = e) === !1 && (a.preventDefault(), a.stopPropagation()))
				}
				return k.postDispatch && k.postDispatch.call(this, a), a.result
			}
		},
		handlers: function (a, b) {
			var c, d, e, f, g = [],
				h = b.delegateCount,
				i = a.target;
			if (h && i.nodeType && (!a.button || "click" !== a.type))
				for (; i !== this; i = i.parentNode || this)
					if (i.disabled !== !0 || "click" !== a.type) {
						for (d = [], c = 0; h > c; c++) f = b[c], e = f.selector + " ", void 0 === d[e] && (d[e] = f.needsContext ? n(e, this).index(i) >= 0 : n.find(e, this, null, [i]).length), d[e] && d.push(f);
						d.length && g.push({
							elem: i,
							handlers: d
						})
					} return h < b.length && g.push({
						elem: this,
						handlers: b.slice(h)
					}), g
		},
		props: "altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
		fixHooks: {},
		keyHooks: {
			props: "char charCode key keyCode".split(" "),
			filter: function (a, b) {
				return null == a.which && (a.which = null != b.charCode ? b.charCode : b.keyCode), a
			}
		},
		mouseHooks: {
			props: "button buttons clientX clientY offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
			filter: function (a, b) {
				var c, d, e, f = b.button;
				return null == a.pageX && null != b.clientX && (c = a.target.ownerDocument || l, d = c.documentElement, e = c.body, a.pageX = b.clientX + (d && d.scrollLeft || e && e.scrollLeft || 0) - (d && d.clientLeft || e && e.clientLeft || 0), a.pageY = b.clientY + (d && d.scrollTop || e && e.scrollTop || 0) - (d && d.clientTop || e && e.clientTop || 0)), a.which || void 0 === f || (a.which = 1 & f ? 1 : 2 & f ? 3 : 4 & f ? 2 : 0), a
			}
		},
		fix: function (a) {
			if (a[n.expando]) return a;
			var b, c, d, e = a.type,
				f = a,
				g = this.fixHooks[e];
			g || (this.fixHooks[e] = g = Z.test(e) ? this.mouseHooks : Y.test(e) ? this.keyHooks : {}), d = g.props ? this.props.concat(g.props) : this.props, a = new n.Event(f), b = d.length;
			while (b--) c = d[b], a[c] = f[c];
			return a.target || (a.target = l), 3 === a.target.nodeType && (a.target = a.target.parentNode), g.filter ? g.filter(a, f) : a
		},
		special: {
			load: {
				noBubble: !0
			},
			focus: {
				trigger: function () {
					return this !== cb() && this.focus ? (this.focus(), !1) : void 0
				},
				delegateType: "focusin"
			},
			blur: {
				trigger: function () {
					return this === cb() && this.blur ? (this.blur(), !1) : void 0
				},
				delegateType: "focusout"
			},
			click: {
				trigger: function () {
					return "checkbox" === this.type && this.click && n.nodeName(this, "input") ? (this.click(), !1) : void 0
				},
				_default: function (a) {
					return n.nodeName(a.target, "a")
				}
			},
			beforeunload: {
				postDispatch: function (a) {
					void 0 !== a.result && a.originalEvent && (a.originalEvent.returnValue = a.result)
				}
			}
		},
		simulate: function (a, b, c, d) {
			var e = n.extend(new n.Event, c, {
				type: a,
				isSimulated: !0,
				originalEvent: {}
			});
			d ? n.event.trigger(e, null, b) : n.event.dispatch.call(b, e), e.isDefaultPrevented() && c.preventDefault()
		}
	}, n.removeEvent = function (a, b, c) {
		a.removeEventListener && a.removeEventListener(b, c, !1)
	}, n.Event = function (a, b) {
		return this instanceof n.Event ? (a && a.type ? (this.originalEvent = a, this.type = a.type, this.isDefaultPrevented = a.defaultPrevented || void 0 === a.defaultPrevented && a.returnValue === !1 ? ab : bb) : this.type = a, b && n.extend(this, b), this.timeStamp = a && a.timeStamp || n.now(), void (this[n.expando] = !0)) : new n.Event(a, b)
	}, n.Event.prototype = {
		isDefaultPrevented: bb,
		isPropagationStopped: bb,
		isImmediatePropagationStopped: bb,
		preventDefault: function () {
			var a = this.originalEvent;
			this.isDefaultPrevented = ab, a && a.preventDefault && a.preventDefault()
		},
		stopPropagation: function () {
			var a = this.originalEvent;
			this.isPropagationStopped = ab, a && a.stopPropagation && a.stopPropagation()
		},
		stopImmediatePropagation: function () {
			var a = this.originalEvent;
			this.isImmediatePropagationStopped = ab, a && a.stopImmediatePropagation && a.stopImmediatePropagation(), this.stopPropagation()
		}
	}, n.each({
		mouseenter: "mouseover",
		mouseleave: "mouseout",
		pointerenter: "pointerover",
		pointerleave: "pointerout"
	}, function (a, b) {
		n.event.special[a] = {
			delegateType: b,
			bindType: b,
			handle: function (a) {
				var c, d = this,
					e = a.relatedTarget,
					f = a.handleObj;
				return (!e || e !== d && !n.contains(d, e)) && (a.type = f.origType, c = f.handler.apply(this, arguments), a.type = b), c
			}
		}
	}), k.focusinBubbles || n.each({
		focus: "focusin",
		blur: "focusout"
	}, function (a, b) {
		var c = function (a) {
			n.event.simulate(b, a.target, n.event.fix(a), !0)
		};
		n.event.special[b] = {
			setup: function () {
				var d = this.ownerDocument || this,
					e = O.access(d, b);
				e || d.addEventListener(a, c, !0), O.access(d, b, (e || 0) + 1)
			},
			teardown: function () {
				var d = this.ownerDocument || this,
					e = O.access(d, b) - 1;
				e ? O.access(d, b, e) : (d.removeEventListener(a, c, !0), O.remove(d, b))
			}
		}
	}), n.fn.extend({
		on: function (a, b, c, d, e) {
			var f, g;
			if ("object" == typeof a) {
				"string" != typeof b && (c = c || b, b = void 0);
				for (g in a) this.on(g, b, c, a[g], e);
				return this
			}
			if (null == c && null == d ? (d = b, c = b = void 0) : null == d && ("string" == typeof b ? (d = c, c = void 0) : (d = c, c = b, b = void 0)), d === !1) d = bb;
			else if (!d) return this;
			return 1 === e && (f = d, d = function (a) {
				return n().off(a), f.apply(this, arguments)
			}, d.guid = f.guid || (f.guid = n.guid++)), this.each(function () {
				n.event.add(this, a, d, c, b)
			})
		},
		one: function (a, b, c, d) {
			return this.on(a, b, c, d, 1)
		},
		off: function (a, b, c) {
			var d, e;
			if (a && a.preventDefault && a.handleObj) return d = a.handleObj, n(a.delegateTarget).off(d.namespace ? d.origType + "." + d.namespace : d.origType, d.selector, d.handler), this;
			if ("object" == typeof a) {
				for (e in a) this.off(e, b, a[e]);
				return this
			}
			return (b === !1 || "function" == typeof b) && (c = b, b = void 0), c === !1 && (c = bb), this.each(function () {
				n.event.remove(this, a, c, b)
			})
		},
		trigger: function (a, b) {
			return this.each(function () {
				n.event.trigger(a, b, this)
			})
		},
		triggerHandler: function (a, b) {
			var c = this[0];
			return c ? n.event.trigger(a, b, c, !0) : void 0
		}
	});
	var db = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,
		eb = /<([\w:]+)/,
		fb = /<|&#?\w+;/,
		gb = /<(?:script|style|link)/i,
		hb = /checked\s*(?:[^=]|=\s*.checked.)/i,
		ib = /^$|\/(?:java|ecma)script/i,
		jb = /^true\/(.*)/,
		kb = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,
		lb = {
			option: [1, "<select multiple='multiple'>", "</select>"],
			thead: [1, "<table>", "</table>"],
			col: [2, "<table><colgroup>", "</colgroup></table>"],
			tr: [2, "<table><tbody>", "</tbody></table>"],
			td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
			_default: [0, "", ""]
		};
	lb.optgroup = lb.option, lb.tbody = lb.tfoot = lb.colgroup = lb.caption = lb.thead, lb.th = lb.td;

	function mb(a, b) {
		return n.nodeName(a, "table") && n.nodeName(11 !== b.nodeType ? b : b.firstChild, "tr") ? a.getElementsByTagName("tbody")[0] || a.appendChild(a.ownerDocument.createElement("tbody")) : a
	}

	function nb(a) {
		return a.type = (null !== a.getAttribute("type")) + "/" + a.type, a
	}

	function ob(a) {
		var b = jb.exec(a.type);
		return b ? a.type = b[1] : a.removeAttribute("type"), a
	}

	function pb(a, b) {
		for (var c = 0, d = a.length; d > c; c++) O.set(a[c], "globalEval", !b || O.get(b[c], "globalEval"))
	}

	function qb(a, b) {
		var c, d, e, f, g, h, i, j;
		if (1 === b.nodeType) {
			if (O.hasData(a) && (f = O.access(a), g = O.set(b, f), j = f.events)) {
				delete g.handle, g.events = {};
				for (e in j)
					for (c = 0, d = j[e].length; d > c; c++) n.event.add(b, e, j[e][c])
			}
			P.hasData(a) && (h = P.access(a), i = n.extend({}, h), P.set(b, i))
		}
	}

	function rb(a, b) {
		var c = a.getElementsByTagName ? a.getElementsByTagName(b || "*") : a.querySelectorAll ? a.querySelectorAll(b || "*") : [];
		return void 0 === b || b && n.nodeName(a, b) ? n.merge([a], c) : c
	}

	function sb(a, b) {
		var c = b.nodeName.toLowerCase();
		"input" === c && W.test(a.type) ? b.checked = a.checked : ("input" === c || "textarea" === c) && (b.defaultValue = a.defaultValue)
	}
	n.extend({
		clone: function (a, b, c) {
			var d, e, f, g, h = a.cloneNode(!0),
				i = n.contains(a.ownerDocument, a);
			if (!(k.noCloneChecked || 1 !== a.nodeType && 11 !== a.nodeType || n.isXMLDoc(a)))
				for (g = rb(h), f = rb(a), d = 0, e = f.length; e > d; d++) sb(f[d], g[d]);
			if (b)
				if (c)
					for (f = f || rb(a), g = g || rb(h), d = 0, e = f.length; e > d; d++) qb(f[d], g[d]);
				else qb(a, h);
			return g = rb(h, "script"), g.length > 0 && pb(g, !i && rb(a, "script")), h
		},
		buildFragment: function (a, b, c, d) {
			for (var e, f, g, h, i, j, k = b.createDocumentFragment(), l = [], m = 0, o = a.length; o > m; m++)
				if (e = a[m], e || 0 === e)
					if ("object" === n.type(e)) n.merge(l, e.nodeType ? [e] : e);
					else if (fb.test(e)) {
						f = f || k.appendChild(b.createElement("div")), g = (eb.exec(e) || ["", ""])[1].toLowerCase(), h = lb[g] || lb._default, f.innerHTML = h[1] + e.replace(db, "<$1></$2>") + h[2], j = h[0];
						while (j--) f = f.lastChild;
						n.merge(l, f.childNodes), f = k.firstChild, f.textContent = ""
					} else l.push(b.createTextNode(e));
			k.textContent = "", m = 0;
			while (e = l[m++])
				if ((!d || -1 === n.inArray(e, d)) && (i = n.contains(e.ownerDocument, e), f = rb(k.appendChild(e), "script"), i && pb(f), c)) {
					j = 0;
					while (e = f[j++]) ib.test(e.type || "") && c.push(e)
				} return k
		},
		cleanData: function (a) {
			for (var b, c, d, e, f = n.event.special, g = 0; void 0 !== (c = a[g]); g++) {
				if (n.acceptData(c) && (e = c[O.expando], e && (b = O.cache[e]))) {
					if (b.events)
						for (d in b.events) f[d] ? n.event.remove(c, d) : n.removeEvent(c, d, b.handle);
					O.cache[e] && delete O.cache[e]
				}
				delete P.cache[c[P.expando]]
			}
		}
	}), n.fn.extend({
		text: function (a) {
			return M(this, function (a) {
				return void 0 === a ? n.text(this) : this.empty().each(function () {
					(1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) && (this.textContent = a)
				})
			}, null, a, arguments.length)
		},
		append: function () {
			return this.domManip(arguments, function (a) {
				if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
					var b = mb(this, a);
					b.appendChild(a)
				}
			})
		},
		prepend: function () {
			return this.domManip(arguments, function (a) {
				if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
					var b = mb(this, a);
					b.insertBefore(a, b.firstChild)
				}
			})
		},
		before: function () {
			return this.domManip(arguments, function (a) {
				this.parentNode && this.parentNode.insertBefore(a, this)
			})
		},
		after: function () {
			return this.domManip(arguments, function (a) {
				this.parentNode && this.parentNode.insertBefore(a, this.nextSibling)
			})
		},
		remove: function (a, b) {
			for (var c, d = a ? n.filter(a, this) : this, e = 0; null != (c = d[e]); e++) b || 1 !== c.nodeType || n.cleanData(rb(c)), c.parentNode && (b && n.contains(c.ownerDocument, c) && pb(rb(c, "script")), c.parentNode.removeChild(c));
			return this
		},
		empty: function () {
			for (var a, b = 0; null != (a = this[b]); b++) 1 === a.nodeType && (n.cleanData(rb(a, !1)), a.textContent = "");
			return this
		},
		clone: function (a, b) {
			return a = null == a ? !1 : a, b = null == b ? a : b, this.map(function () {
				return n.clone(this, a, b)
			})
		},
		html: function (a) {
			return M(this, function (a) {
				var b = this[0] || {},
					c = 0,
					d = this.length;
				if (void 0 === a && 1 === b.nodeType) return b.innerHTML;
				if ("string" == typeof a && !gb.test(a) && !lb[(eb.exec(a) || ["", ""])[1].toLowerCase()]) {
					a = a.replace(db, "<$1></$2>");
					try {
						for (; d > c; c++) b = this[c] || {}, 1 === b.nodeType && (n.cleanData(rb(b, !1)), b.innerHTML = a);
						b = 0
					} catch (e) { }
				}
				b && this.empty().append(a)
			}, null, a, arguments.length)
		},
		replaceWith: function () {
			var a = arguments[0];
			return this.domManip(arguments, function (b) {
				a = this.parentNode, n.cleanData(rb(this)), a && a.replaceChild(b, this)
			}), a && (a.length || a.nodeType) ? this : this.remove()
		},
		detach: function (a) {
			return this.remove(a, !0)
		},
		domManip: function (a, b) {
			a = e.apply([], a);
			var c, d, f, g, h, i, j = 0,
				l = this.length,
				m = this,
				o = l - 1,
				p = a[0],
				q = n.isFunction(p);
			if (q || l > 1 && "string" == typeof p && !k.checkClone && hb.test(p)) return this.each(function (c) {
				var d = m.eq(c);
				q && (a[0] = p.call(this, c, d.html())), d.domManip(a, b)
			});
			if (l && (c = n.buildFragment(a, this[0].ownerDocument, !1, this), d = c.firstChild, 1 === c.childNodes.length && (c = d), d)) {
				for (f = n.map(rb(c, "script"), nb), g = f.length; l > j; j++) h = c, j !== o && (h = n.clone(h, !0, !0), g && n.merge(f, rb(h, "script"))), b.call(this[j], h, j);
				if (g)
					for (i = f[f.length - 1].ownerDocument, n.map(f, ob), j = 0; g > j; j++) h = f[j], ib.test(h.type || "") && !O.access(h, "globalEval") && n.contains(i, h) && (h.src ? n._evalUrl && n._evalUrl(h.src) : n.globalEval(h.textContent.replace(kb, "")))
			}
			return this
		}
	}), n.each({
		appendTo: "append",
		prependTo: "prepend",
		insertBefore: "before",
		insertAfter: "after",
		replaceAll: "replaceWith"
	}, function (a, b) {
		n.fn[a] = function (a) {
			for (var c, d = [], e = n(a), g = e.length - 1, h = 0; g >= h; h++) c = h === g ? this : this.clone(!0), n(e[h])[b](c), f.apply(d, c.get());
			return this.pushStack(d)
		}
	});
	var tb, ub = {};

	function vb(b, c) {
		var d, e = n(c.createElement(b)).appendTo(c.body),
			f = a.getDefaultComputedStyle && (d = a.getDefaultComputedStyle(e[0])) ? d.display : n.css(e[0], "display");
		return e.detach(), f
	}

	function wb(a) {
		var b = l,
			c = ub[a];
		return c || (c = vb(a, b), "none" !== c && c || (tb = (tb || n("<iframe frameborder='0' width='0' height='0'/>")).appendTo(b.documentElement), b = tb[0].contentDocument, b.write(), b.close(), c = vb(a, b), tb.detach()), ub[a] = c), c
	}
	var xb = /^margin/,
		yb = new RegExp("^(" + T + ")(?!px)[a-z%]+$", "i"),
		zb = function (a) {
			return a.ownerDocument.defaultView.getComputedStyle(a, null)
		};

	function Ab(a, b, c) {
		var d, e, f, g, h = a.style;
		return c = c || zb(a), c && (g = c.getPropertyValue(b) || c[b]), c && ("" !== g || n.contains(a.ownerDocument, a) || (g = n.style(a, b)), yb.test(g) && xb.test(b) && (d = h.width, e = h.minWidth, f = h.maxWidth, h.minWidth = h.maxWidth = h.width = g, g = c.width, h.width = d, h.minWidth = e, h.maxWidth = f)), void 0 !== g ? g + "" : g
	}

	function Bb(a, b) {
		return {
			get: function () {
				return a() ? void delete this.get : (this.get = b).apply(this, arguments)
			}
		}
	} ! function () {
		var b, c, d = l.documentElement,
			e = l.createElement("div"),
			f = l.createElement("div");
		if (f.style) {
			f.style.backgroundClip = "content-box", f.cloneNode(!0).style.backgroundClip = "", k.clearCloneStyle = "content-box" === f.style.backgroundClip, e.style.cssText = "border:0;width:0;height:0;top:0;left:-9999px;margin-top:1px;position:absolute", e.appendChild(f);

			function g() {
				f.style.cssText = "-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;margin-top:1%;top:1%;border:1px;padding:1px;width:4px;position:absolute", f.innerHTML = "", d.appendChild(e);
				var g = a.getComputedStyle(f, null);
				b = "1%" !== g.top, c = "4px" === g.width, d.removeChild(e)
			}
			a.getComputedStyle && n.extend(k, {
				pixelPosition: function () {
					return g(), b
				},
				boxSizingReliable: function () {
					return null == c && g(), c
				},
				reliableMarginRight: function () {
					var b, c = f.appendChild(l.createElement("div"));
					return c.style.cssText = f.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0", c.style.marginRight = c.style.width = "0", f.style.width = "1px", d.appendChild(e), b = !parseFloat(a.getComputedStyle(c, null).marginRight), d.removeChild(e), b
				}
			})
		}
	}(), n.swap = function (a, b, c, d) {
		var e, f, g = {};
		for (f in b) g[f] = a.style[f], a.style[f] = b[f];
		e = c.apply(a, d || []);
		for (f in b) a.style[f] = g[f];
		return e
	};
	var Cb = /^(none|table(?!-c[ea]).+)/,
		Db = new RegExp("^(" + T + ")(.*)$", "i"),
		Eb = new RegExp("^([+-])=(" + T + ")", "i"),
		Fb = {
			position: "absolute",
			visibility: "hidden",
			display: "block"
		},
		Gb = {
			letterSpacing: "0",
			fontWeight: "400"
		},
		Hb = ["Webkit", "O", "Moz", "ms"];

	function Ib(a, b) {
		if (b in a) return b;
		var c = b[0].toUpperCase() + b.slice(1),
			d = b,
			e = Hb.length;
		while (e--)
			if (b = Hb[e] + c, b in a) return b;
		return d
	}

	function Jb(a, b, c) {
		var d = Db.exec(b);
		return d ? Math.max(0, d[1] - (c || 0)) + (d[2] || "px") : b
	}

	function Kb(a, b, c, d, e) {
		for (var f = c === (d ? "border" : "content") ? 4 : "width" === b ? 1 : 0, g = 0; 4 > f; f += 2) "margin" === c && (g += n.css(a, c + U[f], !0, e)), d ? ("content" === c && (g -= n.css(a, "padding" + U[f], !0, e)), "margin" !== c && (g -= n.css(a, "border" + U[f] + "Width", !0, e))) : (g += n.css(a, "padding" + U[f], !0, e), "padding" !== c && (g += n.css(a, "border" + U[f] + "Width", !0, e)));
		return g
	}

	function Lb(a, b, c) {
		var d = !0,
			e = "width" === b ? a.offsetWidth : a.offsetHeight,
			f = zb(a),
			g = "border-box" === n.css(a, "boxSizing", !1, f);
		if (0 >= e || null == e) {
			if (e = Ab(a, b, f), (0 > e || null == e) && (e = a.style[b]), yb.test(e)) return e;
			d = g && (k.boxSizingReliable() || e === a.style[b]), e = parseFloat(e) || 0
		}
		return e + Kb(a, b, c || (g ? "border" : "content"), d, f) + "px"
	}

	function Mb(a, b) {
		for (var c, d, e, f = [], g = 0, h = a.length; h > g; g++) d = a[g], d.style && (f[g] = O.get(d, "olddisplay"), c = d.style.display, b ? (f[g] || "none" !== c || (d.style.display = ""), "" === d.style.display && V(d) && (f[g] = O.access(d, "olddisplay", wb(d.nodeName)))) : (e = V(d), "none" === c && e || O.set(d, "olddisplay", e ? c : n.css(d, "display"))));
		for (g = 0; h > g; g++) d = a[g], d.style && (b && "none" !== d.style.display && "" !== d.style.display || (d.style.display = b ? f[g] || "" : "none"));
		return a
	}
	n.extend({
		cssHooks: {
			opacity: {
				get: function (a, b) {
					if (b) {
						var c = Ab(a, "opacity");
						return "" === c ? "1" : c
					}
				}
			}
		},
		cssNumber: {
			columnCount: !0,
			fillOpacity: !0,
			flexGrow: !0,
			flexShrink: !0,
			fontWeight: !0,
			lineHeight: !0,
			opacity: !0,
			order: !0,
			orphans: !0,
			widows: !0,
			zIndex: !0,
			zoom: !0
		},
		cssProps: {
			"float": "cssFloat"
		},
		style: function (a, b, c, d) {
			if (a && 3 !== a.nodeType && 8 !== a.nodeType && a.style) {
				var e, f, g, h = n.camelCase(b),
					i = a.style;
				return b = n.cssProps[h] || (n.cssProps[h] = Ib(i, h)), g = n.cssHooks[b] || n.cssHooks[h], void 0 === c ? g && "get" in g && void 0 !== (e = g.get(a, !1, d)) ? e : i[b] : (f = typeof c, "string" === f && (e = Eb.exec(c)) && (c = (e[1] + 1) * e[2] + parseFloat(n.css(a, b)), f = "number"), null != c && c === c && ("number" !== f || n.cssNumber[h] || (c += "px"), k.clearCloneStyle || "" !== c || 0 !== b.indexOf("background") || (i[b] = "inherit"), g && "set" in g && void 0 === (c = g.set(a, c, d)) || (i[b] = c)), void 0)
			}
		},
		css: function (a, b, c, d) {
			var e, f, g, h = n.camelCase(b);
			return b = n.cssProps[h] || (n.cssProps[h] = Ib(a.style, h)), g = n.cssHooks[b] || n.cssHooks[h], g && "get" in g && (e = g.get(a, !0, c)), void 0 === e && (e = Ab(a, b, d)), "normal" === e && b in Gb && (e = Gb[b]), "" === c || c ? (f = parseFloat(e), c === !0 || n.isNumeric(f) ? f || 0 : e) : e
		}
	}), n.each(["height", "width"], function (a, b) {
		n.cssHooks[b] = {
			get: function (a, c, d) {
				return c ? Cb.test(n.css(a, "display")) && 0 === a.offsetWidth ? n.swap(a, Fb, function () {
					return Lb(a, b, d)
				}) : Lb(a, b, d) : void 0
			},
			set: function (a, c, d) {
				var e = d && zb(a);
				return Jb(a, c, d ? Kb(a, b, d, "border-box" === n.css(a, "boxSizing", !1, e), e) : 0)
			}
		}
	}), n.cssHooks.marginRight = Bb(k.reliableMarginRight, function (a, b) {
		return b ? n.swap(a, {
			display: "inline-block"
		}, Ab, [a, "marginRight"]) : void 0
	}), n.each({
		margin: "",
		padding: "",
		border: "Width"
	}, function (a, b) {
		n.cssHooks[a + b] = {
			expand: function (c) {
				for (var d = 0, e = {}, f = "string" == typeof c ? c.split(" ") : [c]; 4 > d; d++) e[a + U[d] + b] = f[d] || f[d - 2] || f[0];
				return e
			}
		}, xb.test(a) || (n.cssHooks[a + b].set = Jb)
	}), n.fn.extend({
		css: function (a, b) {
			return M(this, function (a, b, c) {
				var d, e, f = {},
					g = 0;
				if (n.isArray(b)) {
					for (d = zb(a), e = b.length; e > g; g++) f[b[g]] = n.css(a, b[g], !1, d);
					return f
				}
				return void 0 !== c ? n.style(a, b, c) : n.css(a, b)
			}, a, b, arguments.length > 1)
		},
		show: function () {
			return Mb(this, !0)
		},
		hide: function () {
			return Mb(this)
		},
		toggle: function (a) {
			return "boolean" == typeof a ? a ? this.show() : this.hide() : this.each(function () {
				V(this) ? n(this).show() : n(this).hide()
			})
		}
	});

	function Nb(a, b, c, d, e) {
		return new Nb.prototype.init(a, b, c, d, e)
	}
	n.Tween = Nb, Nb.prototype = {
		constructor: Nb,
		init: function (a, b, c, d, e, f) {
			this.elem = a, this.prop = c, this.easing = e || "swing", this.options = b, this.start = this.now = this.cur(), this.end = d, this.unit = f || (n.cssNumber[c] ? "" : "px")
		},
		cur: function () {
			var a = Nb.propHooks[this.prop];
			return a && a.get ? a.get(this) : Nb.propHooks._default.get(this)
		},
		run: function (a) {
			var b, c = Nb.propHooks[this.prop];
			return this.pos = b = this.options.duration ? n.easing[this.easing](a, this.options.duration * a, 0, 1, this.options.duration) : a, this.now = (this.end - this.start) * b + this.start, this.options.step && this.options.step.call(this.elem, this.now, this), c && c.set ? c.set(this) : Nb.propHooks._default.set(this), this
		}
	}, Nb.prototype.init.prototype = Nb.prototype, Nb.propHooks = {
		_default: {
			get: function (a) {
				var b;
				return null == a.elem[a.prop] || a.elem.style && null != a.elem.style[a.prop] ? (b = n.css(a.elem, a.prop, ""), b && "auto" !== b ? b : 0) : a.elem[a.prop]
			},
			set: function (a) {
				n.fx.step[a.prop] ? n.fx.step[a.prop](a) : a.elem.style && (null != a.elem.style[n.cssProps[a.prop]] || n.cssHooks[a.prop]) ? n.style(a.elem, a.prop, a.now + a.unit) : a.elem[a.prop] = a.now
			}
		}
	}, Nb.propHooks.scrollTop = Nb.propHooks.scrollLeft = {
		set: function (a) {
			a.elem.nodeType && a.elem.parentNode && (a.elem[a.prop] = a.now)
		}
	}, n.easing = {
		linear: function (a) {
			return a
		},
		swing: function (a) {
			return .5 - Math.cos(a * Math.PI) / 2
		}
	}, n.fx = Nb.prototype.init, n.fx.step = {};
	var Ob, Pb, Qb = /^(?:toggle|show|hide)$/,
		Rb = new RegExp("^(?:([+-])=|)(" + T + ")([a-z%]*)$", "i"),
		Sb = /queueHooks$/,
		Tb = [Yb],
		Ub = {
			"*": [function (a, b) {
				var c = this.createTween(a, b),
					d = c.cur(),
					e = Rb.exec(b),
					f = e && e[3] || (n.cssNumber[a] ? "" : "px"),
					g = (n.cssNumber[a] || "px" !== f && +d) && Rb.exec(n.css(c.elem, a)),
					h = 1,
					i = 20;
				if (g && g[3] !== f) {
					f = f || g[3], e = e || [], g = +d || 1;
					do h = h || ".5", g /= h, n.style(c.elem, a, g + f); while (h !== (h = c.cur() / d) && 1 !== h && --i)
				}
				return e && (g = c.start = +g || +d || 0, c.unit = f, c.end = e[1] ? g + (e[1] + 1) * e[2] : +e[2]), c
			}]
		};

	function Vb() {
		return setTimeout(function () {
			Ob = void 0
		}), Ob = n.now()
	}

	function Wb(a, b) {
		var c, d = 0,
			e = {
				height: a
			};
		for (b = b ? 1 : 0; 4 > d; d += 2 - b) c = U[d], e["margin" + c] = e["padding" + c] = a;
		return b && (e.opacity = e.width = a), e
	}

	function Xb(a, b, c) {
		for (var d, e = (Ub[b] || []).concat(Ub["*"]), f = 0, g = e.length; g > f; f++)
			if (d = e[f].call(c, b, a)) return d
	}

	function Yb(a, b, c) {
		var d, e, f, g, h, i, j, k, l = this,
			m = {},
			o = a.style,
			p = a.nodeType && V(a),
			q = O.get(a, "fxshow");
		c.queue || (h = n._queueHooks(a, "fx"), null == h.unqueued && (h.unqueued = 0, i = h.empty.fire, h.empty.fire = function () {
			h.unqueued || i()
		}), h.unqueued++, l.always(function () {
			l.always(function () {
				h.unqueued--, n.queue(a, "fx").length || h.empty.fire()
			})
		})), 1 === a.nodeType && ("height" in b || "width" in b) && (c.overflow = [o.overflow, o.overflowX, o.overflowY], j = n.css(a, "display"), k = "none" === j ? O.get(a, "olddisplay") || wb(a.nodeName) : j, "inline" === k && "none" === n.css(a, "float") && (o.display = "inline-block")), c.overflow && (o.overflow = "hidden", l.always(function () {
			o.overflow = c.overflow[0], o.overflowX = c.overflow[1], o.overflowY = c.overflow[2]
		}));
		for (d in b)
			if (e = b[d], Qb.exec(e)) {
				if (delete b[d], f = f || "toggle" === e, e === (p ? "hide" : "show")) {
					if ("show" !== e || !q || void 0 === q[d]) continue;
					p = !0
				}
				m[d] = q && q[d] || n.style(a, d)
			} else j = void 0;
		if (n.isEmptyObject(m)) "inline" === ("none" === j ? wb(a.nodeName) : j) && (o.display = j);
		else {
			q ? "hidden" in q && (p = q.hidden) : q = O.access(a, "fxshow", {}), f && (q.hidden = !p), p ? n(a).show() : l.done(function () {
				n(a).hide()
			}), l.done(function () {
				var b;
				O.remove(a, "fxshow");
				for (b in m) n.style(a, b, m[b])
			});
			for (d in m) g = Xb(p ? q[d] : 0, d, l), d in q || (q[d] = g.start, p && (g.end = g.start, g.start = "width" === d || "height" === d ? 1 : 0))
		}
	}

	function Zb(a, b) {
		var c, d, e, f, g;
		for (c in a)
			if (d = n.camelCase(c), e = b[d], f = a[c], n.isArray(f) && (e = f[1], f = a[c] = f[0]), c !== d && (a[d] = f, delete a[c]), g = n.cssHooks[d], g && "expand" in g) {
				f = g.expand(f), delete a[d];
				for (c in f) c in a || (a[c] = f[c], b[c] = e)
			} else b[d] = e
	}

	function $b(a, b, c) {
		var d, e, f = 0,
			g = Tb.length,
			h = n.Deferred().always(function () {
				delete i.elem
			}),
			i = function () {
				if (e) return !1;
				for (var b = Ob || Vb(), c = Math.max(0, j.startTime + j.duration - b), d = c / j.duration || 0, f = 1 - d, g = 0, i = j.tweens.length; i > g; g++) j.tweens[g].run(f);
				return h.notifyWith(a, [j, f, c]), 1 > f && i ? c : (h.resolveWith(a, [j]), !1)
			},
			j = h.promise({
				elem: a,
				props: n.extend({}, b),
				opts: n.extend(!0, {
					specialEasing: {}
				}, c),
				originalProperties: b,
				originalOptions: c,
				startTime: Ob || Vb(),
				duration: c.duration,
				tweens: [],
				createTween: function (b, c) {
					var d = n.Tween(a, j.opts, b, c, j.opts.specialEasing[b] || j.opts.easing);
					return j.tweens.push(d), d
				},
				stop: function (b) {
					var c = 0,
						d = b ? j.tweens.length : 0;
					if (e) return this;
					for (e = !0; d > c; c++) j.tweens[c].run(1);
					return b ? h.resolveWith(a, [j, b]) : h.rejectWith(a, [j, b]), this
				}
			}),
			k = j.props;
		for (Zb(k, j.opts.specialEasing); g > f; f++)
			if (d = Tb[f].call(j, a, k, j.opts)) return d;
		return n.map(k, Xb, j), n.isFunction(j.opts.start) && j.opts.start.call(a, j), n.fx.timer(n.extend(i, {
			elem: a,
			anim: j,
			queue: j.opts.queue
		})), j.progress(j.opts.progress).done(j.opts.done, j.opts.complete).fail(j.opts.fail).always(j.opts.always)
	}
	n.Animation = n.extend($b, {
		tweener: function (a, b) {
			n.isFunction(a) ? (b = a, a = ["*"]) : a = a.split(" ");
			for (var c, d = 0, e = a.length; e > d; d++) c = a[d], Ub[c] = Ub[c] || [], Ub[c].unshift(b)
		},
		prefilter: function (a, b) {
			b ? Tb.unshift(a) : Tb.push(a)
		}
	}), n.speed = function (a, b, c) {
		var d = a && "object" == typeof a ? n.extend({}, a) : {
			complete: c || !c && b || n.isFunction(a) && a,
			duration: a,
			easing: c && b || b && !n.isFunction(b) && b
		};
		return d.duration = n.fx.off ? 0 : "number" == typeof d.duration ? d.duration : d.duration in n.fx.speeds ? n.fx.speeds[d.duration] : n.fx.speeds._default, (null == d.queue || d.queue === !0) && (d.queue = "fx"), d.old = d.complete, d.complete = function () {
			n.isFunction(d.old) && d.old.call(this), d.queue && n.dequeue(this, d.queue)
		}, d
	}, n.fn.extend({
		fadeTo: function (a, b, c, d) {
			return this.filter(V).css("opacity", 0).show().end().animate({
				opacity: b
			}, a, c, d)
		},
		animate: function (a, b, c, d) {
			var e = n.isEmptyObject(a),
				f = n.speed(b, c, d),
				g = function () {
					var b = $b(this, n.extend({}, a), f);
					(e || O.get(this, "finish")) && b.stop(!0)
				};
			return g.finish = g, e || f.queue === !1 ? this.each(g) : this.queue(f.queue, g)
		},
		stop: function (a, b, c) {
			var d = function (a) {
				var b = a.stop;
				delete a.stop, b(c)
			};
			return "string" != typeof a && (c = b, b = a, a = void 0), b && a !== !1 && this.queue(a || "fx", []), this.each(function () {
				var b = !0,
					e = null != a && a + "queueHooks",
					f = n.timers,
					g = O.get(this);
				if (e) g[e] && g[e].stop && d(g[e]);
				else
					for (e in g) g[e] && g[e].stop && Sb.test(e) && d(g[e]);
				for (e = f.length; e--;) f[e].elem !== this || null != a && f[e].queue !== a || (f[e].anim.stop(c), b = !1, f.splice(e, 1));
				(b || !c) && n.dequeue(this, a)
			})
		},
		finish: function (a) {
			return a !== !1 && (a = a || "fx"), this.each(function () {
				var b, c = O.get(this),
					d = c[a + "queue"],
					e = c[a + "queueHooks"],
					f = n.timers,
					g = d ? d.length : 0;
				for (c.finish = !0, n.queue(this, a, []), e && e.stop && e.stop.call(this, !0), b = f.length; b--;) f[b].elem === this && f[b].queue === a && (f[b].anim.stop(!0), f.splice(b, 1));
				for (b = 0; g > b; b++) d[b] && d[b].finish && d[b].finish.call(this);
				delete c.finish
			})
		}
	}), n.each(["toggle", "show", "hide"], function (a, b) {
		var c = n.fn[b];
		n.fn[b] = function (a, d, e) {
			return null == a || "boolean" == typeof a ? c.apply(this, arguments) : this.animate(Wb(b, !0), a, d, e)
		}
	}), n.each({
		slideDown: Wb("show"),
		slideUp: Wb("hide"),
		slideToggle: Wb("toggle"),
		fadeIn: {
			opacity: "show"
		},
		fadeOut: {
			opacity: "hide"
		},
		fadeToggle: {
			opacity: "toggle"
		}
	}, function (a, b) {
		n.fn[a] = function (a, c, d) {
			return this.animate(b, a, c, d)
		}
	}), n.timers = [], n.fx.tick = function () {
		var a, b = 0,
			c = n.timers;
		for (Ob = n.now(); b < c.length; b++) a = c[b], a() || c[b] !== a || c.splice(b--, 1);
		c.length || n.fx.stop(), Ob = void 0
	}, n.fx.timer = function (a) {
		n.timers.push(a), a() ? n.fx.start() : n.timers.pop()
	}, n.fx.interval = 13, n.fx.start = function () {
		Pb || (Pb = setInterval(n.fx.tick, n.fx.interval))
	}, n.fx.stop = function () {
		clearInterval(Pb), Pb = null
	}, n.fx.speeds = {
		slow: 600,
		fast: 200,
		_default: 400
	}, n.fn.delay = function (a, b) {
		return a = n.fx ? n.fx.speeds[a] || a : a, b = b || "fx", this.queue(b, function (b, c) {
			var d = setTimeout(b, a);
			c.stop = function () {
				clearTimeout(d)
			}
		})
	},
		function () {
			var a = l.createElement("input"),
				b = l.createElement("select"),
				c = b.appendChild(l.createElement("option"));
			a.type = "checkbox", k.checkOn = "" !== a.value, k.optSelected = c.selected, b.disabled = !0, k.optDisabled = !c.disabled, a = l.createElement("input"), a.value = "t", a.type = "radio", k.radioValue = "t" === a.value
		}();
	var _b, ac, bc = n.expr.attrHandle;
	n.fn.extend({
		attr: function (a, b) {
			return M(this, n.attr, a, b, arguments.length > 1)
		},
		removeAttr: function (a) {
			return this.each(function () {
				n.removeAttr(this, a)
			})
		}
	}), n.extend({
		attr: function (a, b, c) {
			var d, e, f = a.nodeType;
			if (a && 3 !== f && 8 !== f && 2 !== f) return typeof a.getAttribute === X ? n.prop(a, b, c) : (1 === f && n.isXMLDoc(a) || (b = b.toLowerCase(), d = n.attrHooks[b] || (n.expr.match.bool.test(b) ? ac : _b)), void 0 === c ? d && "get" in d && null !== (e = d.get(a, b)) ? e : (e = n.find.attr(a, b), null == e ? void 0 : e) : null !== c ? d && "set" in d && void 0 !== (e = d.set(a, c, b)) ? e : (a.setAttribute(b, c + ""), c) : void n.removeAttr(a, b))
		},
		removeAttr: function (a, b) {
			var c, d, e = 0,
				f = b && b.match(H);
			if (f && 1 === a.nodeType)
				while (c = f[e++]) d = n.propFix[c] || c, n.expr.match.bool.test(c) && (a[d] = !1), a.removeAttribute(c)
		},
		attrHooks: {
			type: {
				set: function (a, b) {
					if (!k.radioValue && "radio" === b && n.nodeName(a, "input")) {
						var c = a.value;
						return a.setAttribute("type", b), c && (a.value = c), b
					}
				}
			}
		}
	}), ac = {
		set: function (a, b, c) {
			return b === !1 ? n.removeAttr(a, c) : a.setAttribute(c, c), c
		}
	}, n.each(n.expr.match.bool.source.match(/\w+/g), function (a, b) {
		var c = bc[b] || n.find.attr;
		bc[b] = function (a, b, d) {
			var e, f;
			return d || (f = bc[b], bc[b] = e, e = null != c(a, b, d) ? b.toLowerCase() : null, bc[b] = f), e
		}
	});
	var cc = /^(?:input|select|textarea|button)$/i;
	n.fn.extend({
		prop: function (a, b) {
			return M(this, n.prop, a, b, arguments.length > 1)
		},
		removeProp: function (a) {
			return this.each(function () {
				delete this[n.propFix[a] || a]
			})
		}
	}), n.extend({
		propFix: {
			"for": "htmlFor",
			"class": "className"
		},
		prop: function (a, b, c) {
			var d, e, f, g = a.nodeType;
			if (a && 3 !== g && 8 !== g && 2 !== g) return f = 1 !== g || !n.isXMLDoc(a), f && (b = n.propFix[b] || b, e = n.propHooks[b]), void 0 !== c ? e && "set" in e && void 0 !== (d = e.set(a, c, b)) ? d : a[b] = c : e && "get" in e && null !== (d = e.get(a, b)) ? d : a[b]
		},
		propHooks: {
			tabIndex: {
				get: function (a) {
					return a.hasAttribute("tabindex") || cc.test(a.nodeName) || a.href ? a.tabIndex : -1
				}
			}
		}
	}), k.optSelected || (n.propHooks.selected = {
		get: function (a) {
			var b = a.parentNode;
			return b && b.parentNode && b.parentNode.selectedIndex, null
		}
	}), n.each(["tabIndex", "readOnly", "maxLength", "cellSpacing", "cellPadding", "rowSpan", "colSpan", "useMap", "frameBorder", "contentEditable"], function () {
		n.propFix[this.toLowerCase()] = this
	});
	var dc = /[\t\r\n\f]/g;
	n.fn.extend({
		addClass: function (a) {
			var b, c, d, e, f, g, h = "string" == typeof a && a,
				i = 0,
				j = this.length;
			if (n.isFunction(a)) return this.each(function (b) {
				n(this).addClass(a.call(this, b, this.className))
			});
			if (h)
				for (b = (a || "").match(H) || []; j > i; i++)
					if (c = this[i], d = 1 === c.nodeType && (c.className ? (" " + c.className + " ").replace(dc, " ") : " ")) {
						f = 0;
						while (e = b[f++]) d.indexOf(" " + e + " ") < 0 && (d += e + " ");
						g = n.trim(d), c.className !== g && (c.className = g)
					} return this
		},
		removeClass: function (a) {
			var b, c, d, e, f, g, h = 0 === arguments.length || "string" == typeof a && a,
				i = 0,
				j = this.length;
			if (n.isFunction(a)) return this.each(function (b) {
				n(this).removeClass(a.call(this, b, this.className))
			});
			if (h)
				for (b = (a || "").match(H) || []; j > i; i++)
					if (c = this[i], d = 1 === c.nodeType && (c.className ? (" " + c.className + " ").replace(dc, " ") : "")) {
						f = 0;
						while (e = b[f++])
							while (d.indexOf(" " + e + " ") >= 0) d = d.replace(" " + e + " ", " ");
						g = a ? n.trim(d) : "", c.className !== g && (c.className = g)
					} return this
		},
		toggleClass: function (a, b) {
			var c = typeof a;
			return "boolean" == typeof b && "string" === c ? b ? this.addClass(a) : this.removeClass(a) : this.each(n.isFunction(a) ? function (c) {
				n(this).toggleClass(a.call(this, c, this.className, b), b)
			} : function () {
				if ("string" === c) {
					var b, d = 0,
						e = n(this),
						f = a.match(H) || [];
					while (b = f[d++]) e.hasClass(b) ? e.removeClass(b) : e.addClass(b)
				} else (c === X || "boolean" === c) && (this.className && O.set(this, "__className__", this.className), this.className = this.className || a === !1 ? "" : O.get(this, "__className__") || "")
			})
		},
		hasClass: function (a) {
			for (var b = " " + a + " ", c = 0, d = this.length; d > c; c++)
				if (1 === this[c].nodeType && (" " + this[c].className + " ").replace(dc, " ").indexOf(b) >= 0) return !0;
			return !1
		}
	});
	var ec = /\r/g;
	n.fn.extend({
		val: function (a) {
			var b, c, d, e = this[0]; {
				if (arguments.length) return d = n.isFunction(a), this.each(function (c) {
					var e;
					1 === this.nodeType && (e = d ? a.call(this, c, n(this).val()) : a, null == e ? e = "" : "number" == typeof e ? e += "" : n.isArray(e) && (e = n.map(e, function (a) {
						return null == a ? "" : a + ""
					})), b = n.valHooks[this.type] || n.valHooks[this.nodeName.toLowerCase()], b && "set" in b && void 0 !== b.set(this, e, "value") || (this.value = e))
				});
				if (e) return b = n.valHooks[e.type] || n.valHooks[e.nodeName.toLowerCase()], b && "get" in b && void 0 !== (c = b.get(e, "value")) ? c : (c = e.value, "string" == typeof c ? c.replace(ec, "") : null == c ? "" : c)
			}
		}
	}), n.extend({
		valHooks: {
			option: {
				get: function (a) {
					var b = n.find.attr(a, "value");
					return null != b ? b : n.trim(n.text(a))
				}
			},
			select: {
				get: function (a) {
					for (var b, c, d = a.options, e = a.selectedIndex, f = "select-one" === a.type || 0 > e, g = f ? null : [], h = f ? e + 1 : d.length, i = 0 > e ? h : f ? e : 0; h > i; i++)
						if (c = d[i], !(!c.selected && i !== e || (k.optDisabled ? c.disabled : null !== c.getAttribute("disabled")) || c.parentNode.disabled && n.nodeName(c.parentNode, "optgroup"))) {
							if (b = n(c).val(), f) return b;
							g.push(b)
						} return g
				},
				set: function (a, b) {
					var c, d, e = a.options,
						f = n.makeArray(b),
						g = e.length;
					while (g--) d = e[g], (d.selected = n.inArray(d.value, f) >= 0) && (c = !0);
					return c || (a.selectedIndex = -1), f
				}
			}
		}
	}), n.each(["radio", "checkbox"], function () {
		n.valHooks[this] = {
			set: function (a, b) {
				return n.isArray(b) ? a.checked = n.inArray(n(a).val(), b) >= 0 : void 0
			}
		}, k.checkOn || (n.valHooks[this].get = function (a) {
			return null === a.getAttribute("value") ? "on" : a.value
		})
	}), n.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "), function (a, b) {
		n.fn[b] = function (a, c) {
			return arguments.length > 0 ? this.on(b, null, a, c) : this.trigger(b)
		}
	}), n.fn.extend({
		hover: function (a, b) {
			return this.mouseenter(a).mouseleave(b || a)
		},
		bind: function (a, b, c) {
			return this.on(a, null, b, c)
		},
		unbind: function (a, b) {
			return this.off(a, null, b)
		},
		delegate: function (a, b, c, d) {
			return this.on(b, a, c, d)
		},
		undelegate: function (a, b, c) {
			return 1 === arguments.length ? this.off(a, "**") : this.off(b, a || "**", c)
		}
	});
	var fc = /%20/g,
		gc = /\[\]$/,
		hc = /\r?\n/g,
		ic = /^(?:submit|button|image|reset|file)$/i,
		jc = /^(?:input|select|textarea|keygen)/i;

	function kc(a, b, c, d) {
		var e;
		if (n.isArray(b)) n.each(b, function (b, e) {
			c || gc.test(a) ? d(a, e) : kc(a + "[" + ("object" == typeof e ? b : "") + "]", e, c, d)
		});
		else if (c || "object" !== n.type(b)) d(a, b);
		else
			for (e in b) kc(a + "[" + e + "]", b[e], c, d)
	}
	n.param = function (a, b) {
		var c, d = [],
			e = function (a, b) {
				b = n.isFunction(b) ? b() : null == b ? "" : b, d[d.length] = encodeURIComponent(a) + "=" + encodeURIComponent(b)
			};
		if (void 0 === b && (b = n.ajaxSettings && n.ajaxSettings.traditional), n.isArray(a) || a.jquery && !n.isPlainObject(a)) n.each(a, function () {
			e(this.name, this.value)
		});
		else
			for (c in a) kc(c, a[c], b, e);
		return d.join("&").replace(fc, "+")
	}, n.fn.extend({
		serialize: function () {
			return n.param(this.serializeArray())
		},
		serializeArray: function () {
			return this.map(function () {
				var a = n.prop(this, "elements");
				return a ? n.makeArray(a) : this
			}).filter(function () {
				var a = this.type;
				return this.name && !n(this).is(":disabled") && jc.test(this.nodeName) && !ic.test(a) && (this.checked || !W.test(a))
			}).map(function (a, b) {
				var c = n(this).val();
				return null == c ? null : n.isArray(c) ? n.map(c, function (a) {
					return {
						name: b.name,
						value: a.replace(hc, "\r\n")
					}
				}) : {
					name: b.name,
					value: c.replace(hc, "\r\n")
				}
			}).get()
		}
	}), n.parseHTML = function (a, b, c) {
		if (!a || "string" != typeof a) return null;
		"boolean" == typeof b && (c = b, b = !1), b = b || l;
		var d = y.exec(a),
			e = !c && [];
		return d ? [b.createElement(d[1])] : (d = n.buildFragment([a], b, e), e && e.length && n(e).remove(), n.merge([], d.childNodes))
	}, n.each({
		Height: "height",
		Width: "width"
	}, function (a, b) {
		n.each({
			padding: "inner" + a,
			content: b,
			"": "outer" + a
		}, function (c, d) {
			n.fn[d] = function (d, e) {
				var f = arguments.length && (c || "boolean" != typeof d),
					g = c || (d === !0 || e === !0 ? "margin" : "border");
				return M(this, function (b, c, d) {
					var e;
					return n.isWindow(b) ? b.document.documentElement["client" + a] : 9 === b.nodeType ? (e = b.documentElement, Math.max(b.body["scroll" + a], e["scroll" + a], b.body["offset" + a], e["offset" + a], e["client" + a])) : void 0 === d ? n.css(b, c, g) : n.style(b, c, d, g)
				}, b, f ? d : void 0, f, null)
			}
		})
	}), "function" == typeof define && define.amd && define("jquery", [], function () {
		return n
	});
	var lc = a.jQuery,
		mc = a.$;
	return n.noConflict = function (b) {
		return a.$ === n && (a.$ = mc), b && a.jQuery === n && (a.jQuery = lc), n
	}, typeof b === X && (a.jQuery = a.$ = n), n
});;
(function (factory) { //'use strict';
	if (typeof define === "function" && define.amd) {
		define(["jquery"], factory)
	} else if (typeof exports !== "undefined") {
		module.exports = factory(require("jquery"))
	} else {
		factory(jQuery)
	}
})(function ($) { //'use strict';
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
	this.wheelContainer = document.getElementById("wheel-container");
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
		this.config.wheelContainerMarginLeft = this.config.windowHeightWidthRatio > 1.6 ? window.innerWidth / 6 : window.innerWidth / 5;
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
	$("#wheel-container").easyWheel({
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

	this.config.promocodes_soldout_message_text_color = extendedProps.promocodes_soldout_message_text_color;
	this.config.promocodes_soldout_message_font_family = extendedProps.promocodes_soldout_message_font_family;
	this.config.promocodes_soldout_message_text_size = extendedProps.promocodes_soldout_message_text_size;
	this.config.promocodes_soldout_message_background_color = extendedProps.promocodes_soldout_message_background_color;

	this.config.displaynameCustomFontFamilyAndroid = extendedProps.displayname_custom_font_family_android;
	this.config.titleCustomFontFamilyAndroid = extendedProps.title_custom_font_family_android;
	this.config.textCustomFontFamilyAndroid = extendedProps.text_custom_font_family_android;
	this.config.buttonCustomFontFamilyAndroid = extendedProps.button_custom_font_family_android;
	this.config.promocodeTitleCustomFontFamilyAndroid = extendedProps.promocode_title_custom_font_family_android;
	this.config.copybuttonCustomFontFamilyAndroid = extendedProps.copybutton_custom_font_family_android;
	this.config.promocodesSoldoutMessageCustomFontFamilyAndroid = extendedProps.promocodes_soldout_message_custom_font_family_android;

};
SpinToWin.prototype.addFonts = function () {
	if(this.config.fontFiles === undefined) {
		return
	}
	for (var fontFileIndex in this.config.fontFiles) {
	    var fontFile = this.config.fontFiles[fontFileIndex];
	    var fontFamily = fontFile.split(".")[0];
		var newStyle = document.createElement('style');
        var cssContent = "@font-face{font-family:"+fontFamily+";src:url('"+fontFile+"');}";
        newStyle.appendChild(document.createTextNode(cssContent));
        document.head.appendChild(newStyle);
	}
};

SpinToWin.prototype.setFonts = function () {
	if (window.Android || window.BrowserTest) {
		if (typeof this.config.displaynameFontFamily === "string" && this.config.displaynameFontFamily.toLowerCase() === "custom") {
			this.wheelContainer.style.fontFamily = this.config.displaynameCustomFontFamilyAndroid;
		}
	} else if (window.webkit && window.webkit.messageHandlers) {
		if (typeof this.config.displaynameFontFamily === "string" && this.config.displaynameFontFamily.toLowerCase() === "custom") {
			this.wheelContainer.style.fontFamily = this.config.displaynameCustomFontFamilyIOS;
		}
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
	this.config.promocodes_soldout_message_text_size = isNaN(parseInt(this.config.promocodes_soldout_message_text_size)) ? 20 : parseInt(this.config.promocodes_soldout_message_text_size);
};
SpinToWin.prototype.setContent = function () {
	this.container.style.backgroundColor = this.config.backgroundColor;
	this.titleElement.innerHTML = this.config.title.replace(/\\n/g, "<br/>");
	this.titleElement.style.color = this.config.titleTextColor;
	this.titleElement.style.fontFamily = this.config.titleFontFamily;
	this.titleElement.style.fontSize = this.config.titleTextSize + 20 + "px";
	this.messageElement.innerHTML = this.config.message.replace(/\\n/g, "<br/>");
	this.messageElement.style.color = this.config.textColor;
	this.messageElement.style.fontFamily = this.config.textFontFamily;
	this.messageElement.style.fontSize = this.config.textSize + 10 + "px";
	this.submitButton.innerHTML = this.config.buttonLabel;
	this.submitButton.style.color = this.config.buttonTextColor;
	this.submitButton.style.backgroundColor = this.config.buttonColor;
	this.submitButton.style.fontFamily = this.config.buttonFontFamily;
	this.submitButton.style.fontSize = this.config.buttonTextSize + 20 + "px";
	this.emailInput.placeholder = this.config.placeholder;
	this.consentText.innerHTML = this.prepareCheckboxHtmls(this.config.consentText, this.config.consentTextUrl);
	this.consentText.style.fontSize = this.config.consentTextSize + 10 + "px";
	this.consentText.style.fontFamily = this.config.textFontFamily;
	this.emailPermitText.innerHTML = this.prepareCheckboxHtmls(this.config.emailPermitText, this.config.emailpermitTextUrl);
	this.emailPermitText.style.fontSize = this.config.consentTextSize + 10 + "px";
	this.emailPermitText.style.fontFamily = this.config.textFontFamily;
	this.copyButton.innerHTML = this.config.copyButtonLabel;
	this.copyButton.style.color = this.config.copybuttonTextColor;
	this.copyButton.style.backgroundColor = this.config.copybuttonColor;
	this.copyButton.style.fontFamily = this.config.copybuttonFontFamily;
	this.copyButton.style.fontSize = this.config.copybuttonTextSize + 20 + "px";
	this.invalidEmailMessageLi.innerHTML = this.config.invalidEmailMessage;
	this.invalidEmailMessageLi.style.fontSize = this.config.consentTextSize + 10 + "px";
	this.invalidEmailMessageLi.style.fontFamily = this.config.textFontFamily;
	this.checkConsentMessageLi.innerHTML = this.config.checkConsentMessage;
	this.checkConsentMessageLi.style.fontSize = this.config.consentTextSize + 10 + "px";
	this.checkConsentMessageLi.style.fontFamily = this.config.textFontFamily;
	this.couponCode.style.color = this.config.promocodeTextColor;
	this.couponCode.style.backgroundColor = this.config.promocodeBackgroundColor;
	this.couponCode.style.fontFamily = this.config.copybuttonFontFamily;
	this.couponCode.style.fontSize = this.config.copybuttonTextSize + 20 + "px";
	this.successMessageElement.innerHTML = this.config.successMessage.replace(/\\n/g, "<br/>");
	this.successMessageElement.style.color = "green";
	this.promocodeTitleElement.innerHTML = this.config.promocodeTitle.replace(/\\n/g, "<br/>");
	this.promocodeTitleElement.style.color = this.config.promocodeTitleTextColor;
	this.promocodeTitleElement.style.fontFamily = this.config.promocodeTitleFontFamily;
	this.promocodeTitleElement.style.fontSize = this.config.promocodeTitleTextSize + 20 + "px";

	if (this.config.promocodesSoldoutMessage !== undefined && this.config.promocodesSoldoutMessage.length > 0) {
		this.promocodesSoldoutMessageElement.innerHTML = this.config.promocodesSoldoutMessage.replace(/\\n/g, "<br/>");
	}

	this.promocodesSoldoutMessageElement.style.color = this.config.promocodes_soldout_message_text_color;
	this.promocodesSoldoutMessageElement.style.fontFamily = this.config.promocodes_soldout_message_font_family;
	this.promocodesSoldoutMessageElement.style.fontSize = this.config.promocodes_soldout_message_text_size + 20 + "px";
	this.promocodesSoldoutMessageElement.style.backgroundColor = this.config.promocodes_soldout_message_background_color;

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
	this.copyButton.addEventListener("click", evt => this.copyToClipboard())
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
		this.emailInput.style.display = "none";
		this.consentContainer.style.display = "none";
		this.emailPermitContainer.style.display = "none";
		if (this.config.wheelSpinAction == "swipe") {
			this.submitButton.style.display = "none";
		}
	}
};
SpinToWin.prototype.getWheelContainerMarginTop = function () {
	if (window.innerHeight < 750) {
		return "10px"
	} else if (window.innerHeight < 1000) {
		return "30px"
	} else {
		return "50px"
	}
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
		styleString = "#wheel-container{float:left;width:" + config.r + "px;height:" + 2 * this.config.r + "px}" + "#form-title, #form-message, #success-message, #promocode-title{text-align:center;}" + "#warning{display:none; position: absolute; z-index: 3; background: #fcf6c1; font-size: 12px; border: 1px solid #ccc; top: 105%;width: 100%; box-sizing: border-box;}" + "#warning>ul{margin: 2px;padding-inline-start: 20px;}" + ".form-submit-btn{transition:.2s filter ease-in-out;}" + ".form-submit-btn:hover{filter: brightness(110%);transition:.2s filter ease-in-out;}" + ".form-submit-btn.disabled{filter: grayscale(100%);transition:.2s filter ease-in-out;}" + "@media only screen and (max-width:2500px){" + "#wheel-container{float:unset;width:100%;text-align:center;position:relative}" + "}";
	styleEl.id = "vl-styles";
	if (!document.getElementById("vl-styles")) {
		styleEl.innerHTML = styleString;
		document.head.appendChild(styleEl);
	} else {
		document.getElementById("vl-styles").innerHTML = styleString;
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
	window.easyWheel.start()
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
		//snd.addEventListener('ended', function () {df.removeChild(snd);});
		//snd.play();
		return snd;
	}
}());

var snd = null; //Sound("data:audio/wav;base64,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+//+/wAA///+/wAAAwAEAAAA/P/8////AgADAAIAAgABAAEA///9//z//f8AAAMABAADAAEAAAD//////v/+////AQACAAIAAAD/////AAABAAAAAAAAAAEAAQAAAP7//v///wEAAgABAAEAAAAAAP//////////AQACAAEAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAA/////wAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");


SpinToWin.prototype.playTick = function () {
	if (!snd && window.spinToWin.easyWheelInitialized) {
		snd = Sound("data:audio/wav;base64,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+//+/wAA///+/wAAAwAEAAAA/P/8////AgADAAIAAgABAAEA///9//z//f8AAAMABAADAAEAAAD//////v/+////AQACAAIAAAD/////AAABAAAAAAAAAAEAAQAAAP7//v///wEAAgABAAEAAAAAAP//////////AQACAAEAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAP//AAAAAAAAAAAAAAAAAAAAAAAA/////wAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
	}
	//snd.setAttribute("playsinline", true);
	//snd.setAttribute("autoplay", true);
	//snd.setAttribute("preload", "auto");
	//snd.currentTime = 0;
	if (snd) {
		snd.play();
	}
};


//var snd = Sound("data:audio/wav;base64," + base64string);
