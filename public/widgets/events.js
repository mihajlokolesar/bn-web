const BigNeonWidget = {};
(function(context) {
	/** Helper Functions */
	function getSyncScriptParams() {
		const lastScript = [...document.getElementsByTagName("script")].filter(
			script => script.getAttribute("data-organization-id")
		)[0];
		return {
			organizationId: lastScript.getAttribute("data-organization-id"),
			target: lastScript.getAttribute("data-target"),
			apiUrl: lastScript.getAttribute("data-api-url"),
			baseUrl: lastScript.getAttribute("data-base-url"),
			style: lastScript.getAttribute("data-style")
		};
	}

	function xhr(method, uri, body, handler) {
		const req = new XMLHttpRequest();
		req.onreadystatechange = function() {
			if (req.readyState === 4 && handler) {
				handler(req.responseText);
			}
		};
		req.open(method, uri, true);
		req.setRequestHeader("Content-Type", "application/json");
		req.send(body);
	}

	function formatAMPM(date) {
		let hours = date.getHours();
		let minutes = date.getMinutes();
		const ampm = hours >= 12 ? "pm" : "am";
		hours = hours % 12;
		hours = hours ? hours : 12; // the hour '0' should be '12'
		minutes = ("0" + minutes).slice(-2);
		return `<span class="bn-event-date-hours">${hours}</span><span class="bn-event-date-colon">:</span><span class="bn-event-date-minutes">${minutes}</span> <span class="bn-event-date-meridian">${ampm}</span>`;
	}

	function prepareDateTime(dateTime) {
		if (!dateTime) {
			return false;
		}
		dateTime = dateTime
			.replace(/[a-zA-Z]+$/g, "")
			.replace(/\.\d+$/g, "")
			.replace(/ [-|+]+\d{4}$/, "");
		const parts = dateTime.split("T");
		return parts.join(" ");
	}

	function parseLocalizedDateTime(localizedDateTime, utcDateTime) {
		let date;
		if (localizedDateTime) {
			//Use the date time exactly as it is displayed
			date = new Date(prepareDateTime(localizedDateTime));
		} else {
			//Set it as a UTC time and show the users current local version
			date = new Date(prepareDateTime(utcDateTime) + " UTC");
		}
		return date;
	}

	function getPrice(event) {
		const min = event.min_ticket_price / 100;
		const max = event.max_ticket_price / 100;
		let priceText = "";
		if (min > 0 || max > 0) {
			priceText = min === max ? `$${min}` : `$${min} - $${max}`;
		}
		return priceText;
	}

	function optimizeCloudinary(url, quality = "low", size = "f_auto") {
		if (!url || typeof url !== "string") {
			return url;
		}

		//Only manipulate urls served from cloudinary and ones that have not already been manipulated
		if (
			url.indexOf("res.cloudinary.com") === -1 ||
			url.indexOf("/q_auto:") > -1
		) {
			return url;
		}

		const insertAfterString = "/image/upload/";
		const index = url.indexOf(insertAfterString);
		if (index === -1) {
			return url;
		}

		const qualityParams = `${size}/q_auto:${quality}/`;
		const indexToInsert = index + insertAfterString.length;

		return [
			url.slice(0, indexToInsert),
			qualityParams,
			url.slice(indexToInsert)
		].join("");
	}

	/** End Helper Functions */
	const DAYS = ["Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"];
	const MONTHS = [
		"Jan",
		"Feb",
		"Mar",
		"Apr",
		"May",
		"Jun",
		"Jul",
		"Aug",
		"Sep",
		"Oct",
		"Nov",
		"Dec"
	];
	const TEMPLATES = {
		_date: (tag, dateClass, eventDate) => {
			const weekday = `<span class="${dateClass}-weekday">${
				DAYS[eventDate.getDay()]
			}</span>`;
			const month = `<span class="${dateClass}-month">${
				MONTHS[eventDate.getMonth()]
			}</span>`;
			const day = `<span class="${dateClass}-day">${eventDate.getDate()}</span>`;
			const year = `<span class="${dateClass}-year">${eventDate.getFullYear()}</span>`;

			return `<${tag} class="${dateClass}">${weekday} ${month} ${day} ${year}</${tag}>`;
		},
		_image: (imageUrl, eventName) => {
			if (imageUrl) {
				const src = optimizeCloudinary(imageUrl);
				return `<img src="${src}" alt="${eventName || "Big Neon Event"}"/>`;
			}
			return "";
		},
		//Left column
		dateAndImageContainer: function(eventDate, imageUrl, eventName) {
			const date = TEMPLATES._date("h3", "bn-event-date", eventDate);
			const image = TEMPLATES._image(imageUrl, eventName);
			return `<div class="bn-event-image">${date}${image}</div>`;
		},
		_time: (time, className, prefix) => {
			return `<span class="bn-time-prefix ${className}">${prefix} </span>${formatAMPM(
				time
			)}`;
		},
		//Middle column
		nameAndDescriptionContainer: (
			eventDate,
			eventName,
			venueName,
			doortime
		) => {
			const mobileEventDate = TEMPLATES._date(
				"h3",
				"bn-event-date-mobile",
				eventDate
			);
			const eventNameDiv = `<h2 class="bn-event-name bn-event-artists">${eventName}</h2>`;
			const venueNameDiv = `<h4 class="bn-v1 bn-event-venue-name">${venueName}</h4>`;
			const times = `<p class="bn-v1 bn-event-time">${TEMPLATES._time(
				doortime,
				"bn-key-door-time",
				"Doors"
			)} / ${TEMPLATES._time(eventDate, "bn-key-show-time", "Show")}</p>`;
			const oldTimes = `<p class="bn-event-time bn-v0">${formatAMPM(
				doortime
			)}</p>`;
			return `<div class="bn-event-text">${mobileEventDate}${eventNameDiv}${venueNameDiv}${times}${oldTimes}</div>`;
		},
		//Right column
		purchaseContainer: (eventId, priceValue, categoryValue) => {
			const button = `<button class="bn-buy-button bn-buy-button-module" id="bigneon-buy-button-${eventId}">Tickets</button>`;
			const price = `<p class="bn-event-price">${priceValue}</p>`;
			const category = `<p class="bn-v1 bn-event-category">${categoryValue}</p>`;
			return `<div class="bn-event-button">${button}${price}${category}</div>`;
		},

		eventContainer: eventHtml =>
			`<div class="bn-event-container">${eventHtml}</div>`,
		eventRow: (eventUrl, rowHtml) =>
			`<a class="bn-event-row" href="${eventUrl}" target="_blank">${rowHtml}</a>`
	};
	context.events = false;
	context.params = getSyncScriptParams();

	context.fetch = function(page) {
		page = page || 0;
		const uri = `${context.params.apiUrl}events?page=${page}&organization_id=${
			context.params.organizationId
		}`;
		xhr("GET", uri, null, function(eventsString) {
			try {
				context.events = JSON.parse(eventsString);
				context.render(context.events, true);
			} catch (e) {
				console.error(e);
			}
		});
	};

	context.render = function(events, firstRender) {
		if (firstRender) {
			const head = document.head || document.getElementsByTagName("head")[0],
				style = document.createElement("style");
			style.type = "text/css";
			head.appendChild(style);
			//v1 changes
			style.appendChild(document.createTextNode(".bn-v1 {display: none;}"));
			if (context.params.style) {
				style.appendChild(
					document.createTextNode(window.atob(context.params.style))
				);
			}
		}
		if (!events) {
			events = context.events;
		}

		let target = context.params.target;
		const regexp = /^[^a-zA-Z]/;
		target = regexp.test(target) ? target : `#${target}`;
		const parent = document.querySelector(target);

		events.data.forEach(event => {
			const { id, promo_image_url, name, slug, event_type } = event;
			const eventDate = parseLocalizedDateTime(
				event.localized_times.event_start,
				event.event_start
			);
			const doorTime = parseLocalizedDateTime(
				event.localized_times.door_time || event.localized_times.event_start,
				event.door_time || event.event_start
			);
			const priceText = getPrice(event);
			let eventHtml = TEMPLATES.dateAndImageContainer(
				eventDate,
				promo_image_url,
				name
			);
			eventHtml += TEMPLATES.nameAndDescriptionContainer(
				eventDate,
				name,
				event.venue.name,
				doorTime
			);
			eventHtml += TEMPLATES.purchaseContainer(id, priceText, event_type);
			const eventContainer = TEMPLATES.eventContainer(eventHtml);
			const row = TEMPLATES.eventRow(
				`${context.params.baseUrl}events/${slug}`,
				eventContainer
			);
			parent.insertAdjacentHTML("beforeend", row);
		});
	};
	context.fetch();
})(BigNeonWidget);
