export default (key) => {
	const url = window.location.search;
	const urlParams = new URLSearchParams(url);
	urlParams.delete(key);
	const params = {};
	for (const key of urlParams.keys()) {
		const values = urlParams.getAll(key);
		params[key] = values.join();
	}
	return params;
};
