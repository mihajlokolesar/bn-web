export default (id, slug) => {
	const updatedUrl = `${window.location.pathname}${
		window.location.search
	}`.replace(id, slug);

	window.history.replaceState("", "", updatedUrl);
};
