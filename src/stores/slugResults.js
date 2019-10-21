import { observable, computed, action } from "mobx";
import moment from "moment";
import Bigneon from "../helpers/bigneon";
import changeUrlParam from "../helpers/changeUrlParam";
import notification from "./notifications";

class SlugResults {
	@observable
	events = null;

	@observable
	isLoading = false;

	@observable
	filters = {};

	@observable
	venueInfo = null;

	@observable
	orgInfo = null;

	@observable
	cityInfo = null;

	@action
	refreshResults(slug, params, onSuccess, onError) {
		this.isLoading = true;

		Bigneon()
			.slugs.read({ id: slug, ...params, status: "Published" }) //Always force published
			.then(response => {
				const result = [];

				const { events, organization, venue, city } = response.data;
				events.forEach(eventData => {
					const { venue, promo_image_url, cancelled_at, ...event } = eventData;

					//TODO remove this when it's added as a filter in the API
					if (cancelled_at) {
						return;
					}

					result.push({
						...event,
						formattedEventDate: moment(
							event.event_start,
							moment.HTML5_FMT.DATETIME_LOCAL_MS
						).format("dddd, MMM D"),
						min_ticket_price: event.min_ticket_price || 0,
						max_ticket_price: event.max_ticket_price || 0,
						venue,
						promo_image_url: promo_image_url || "/images/event-placeholder.png"
					});
				});

				this.venueInfo = venue;
				this.cityInfo = city;
				this.events = result;
				this.orgInfo = organization;
				this.isLoading = false;

				onSuccess();
			})
			.catch(error => {
				console.error(error);
				this.isLoading = false;

				let message = "Loading events failed.";
				if (
					error.response &&
					error.response.data &&
					error.response.data.error
				) {
					message = error.response.data.error;
				}

				onError(message);
			});
	}
}

const slugResults = new SlugResults();
export default slugResults;
