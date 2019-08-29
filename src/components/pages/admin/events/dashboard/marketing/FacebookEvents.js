import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import PropTypes from "prop-types";

import Bigneon from "../../../../../../helpers/bigneon";
import user from "../../../../../../stores/user";
import Loader from "../../../../../elements/loaders/Loader";
import SelectGroup from "../../../../../common/form/SelectGroup";
import Button from "../../../../../elements/Button";
import { FacebookButton } from "../../../../authentication/social/FacebookButton";
import Grid from "@material-ui/core/Grid";

const styles = theme => ({
	root: {}
});

class FacebookEvents extends Component {
	constructor(props) {
		super(props);

		this.state = {
			eventName: null,
			pages: [],
			pageId: null,
			facebookCategory: null,
			isSubmitting: false,
			isFacebookLinked: false
		};
	}

	componentDidMount() {
		const { eventId } = this.props;

		Bigneon()
			.external.facebook.pages()
			.then(response =>
				this.setState({ pages: response.data, isFacebookLinked: true })
			)
			.catch(error => {
				console.error(error);
				this.setState({ isFacebookLinked: false });
			});
		//If you need event details, use the eventId prop
		// Bigneon()
		// 	.events.read({ id: eventId })
		// 	.then(response => {
		// 		this.setState({ event: response.data });
		// 	})
		// 	.catch(error => {
		// 		console.error(error);
		// 	});
	}

	onSubmit() {
		const { pageId, facebookCategory } = this.state;
		this.setState({ isSubmitting: true });
		Bigneon().external.facebook.createEvent({
			event_id: this.props.eventId,
			page_id: pageId,
			category: facebookCategory
		});
	}

	render() {
		const {
			pages,
			pageId,
			facebookCategory,
			isSubmitting,
			isFacebookLinked
		} = this.state;

		return (
			<div>
				<Grid container>
					<Grid item xs={12} sm={6} md={6} lg={6}>
						<h1>Create Facebook Event</h1>
						<p>
							Increase your event's reach by{" "}
							<strong>creating a Facebook event</strong>. It's easy!
						</p>
						{isFacebookLinked ? (
							<div>
								<SelectGroup
									items={pages.map(page => ({
										value: page.id,
										name: page.name
									}))}
									value={pageId}
									onChange={e => this.setState({ pageId: e.target.value })}
								/>

								<SelectGroup
									items={[{ value: "MUSIC_EVENT", name: "Music Event" }]}
									value={facebookCategory}
									onChange={e =>
										this.setState({ facebookCategory: e.target.value })
									}
								/>
								<Button
									size="large"
									type="submit"
									variant="callToAction"
									onClick={this.onSubmit.bind(this)}
									disabled={isSubmitting}
								>
									Publish
								</Button>
							</div>
						) : (
							<div>
								<FacebookButton scopes={["manage_pages", "publish_pages"]}/>
							</div>
						)}
					</Grid>
					<Grid item xs={12} sm={12} md={6} lg={6}>
						<h2>Please Note:</h2>
						<ul>
							<li>
								A user with admin priviledges for your Facebook page needs to
								give Big Neon permission to create the event
							</li>
							<li>
								Don't worry, we won't create the event until you review the
								settings and tell us to
							</li>
						</ul>
					</Grid>
				</Grid>
			</div>
		);
	}
}

FacebookEvents.propTypes = {
	classes: PropTypes.object.isRequired,
	eventId: PropTypes.string.isRequired
};

export default withStyles(styles)(FacebookEvents);
