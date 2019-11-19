export default address => {
	if (!address || typeof address !== "string") {
		return null;
	}

	const removeThese = ["USA", "United States"];
	let noCountry = "";
	let output = "";
	removeThese.forEach(subStr => {
		if (output) return;

		if (address.includes(subStr)) {
			noCountry = address.substring(0, address.indexOf(subStr));
			output = noCountry.replace(/,\s*$/, "");
		} else {
			output = address;
		}
	});

	return output;
};
