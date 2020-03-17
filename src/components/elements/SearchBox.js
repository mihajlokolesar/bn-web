import React, { Component } from "react";
import PropTypes from "prop-types";
import BoxInput from "./form/BoxInput";
import { urlQueryParam } from "./pagination";

const DEBOUNCE_DELAY = 500;

class SearchBox extends Component {
	state = {
		searchQuery: urlQueryParam() || ""
	};

	componentWillUnmount() {
		this.clearDebounceTimer();
	}

	clearDebounceTimer() {
		if (this.debounceTimeout) {
			clearTimeout(this.debounceTimeout);
		}
	}

	onSearch({ target }) {
		const { onSearch } = this.props;
		const searchQuery = target.value;
		this.setState({ searchQuery }, () => {
			this.clearDebounceTimer();
			this.debounceTimeout = setTimeout(
				() => onSearch(searchQuery),
				DEBOUNCE_DELAY
			);
		});
	}

	render() {
		const { searchQuery } = this.state;
		const { placeholder } = this.props;
		return (
			<BoxInput
				name="Search"
				value={searchQuery}
				placeholder={placeholder}
				onChange={this.onSearch.bind(this)}
			/>
		);
	}
}

SearchBox.propTypes = {
	onSearch: PropTypes.func.isRequired,
	placeholder: PropTypes.string
};

export default SearchBox;
