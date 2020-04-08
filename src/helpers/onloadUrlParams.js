import getAllUrlParams from "./getAllUrlParams";
import decodeJWT from "./decodeJWT";

export default () => {
	const { access_token, refresh_token, rnNavigation } = getAllUrlParams();
	const deleteParams = [];
	if (refresh_token) {
		try {
			//Attempt to decode these, if they are not valid do not store them.
			if (access_token) {
				decodeJWT(access_token);
				localStorage.setItem("access_token", access_token);
				deleteParams.push("access_token");
			}

			decodeJWT(refresh_token);
			localStorage.setItem("refresh_token", refresh_token);
			deleteParams.push("refresh_token");
		} catch (e) {
			console.error("Invalid access / refresh token provided");
		}
	}
	if (rnNavigation) {
		localStorage.setItem("rnNavigation", "1");
		deleteParams.push("rnNavigation");
	} else if (rnNavigation === "0") {
		localStorage.removeItem("rnNavigation");
		deleteParams.push("rnNavigation");
	}

	//Don't manipulate the URL unless we need to.
	if (deleteParams.length) {
		deleteUrlParams(["access_token", "refresh_token", "rnNavigation"]);
	}
};

function deleteUrlParams(keys) {
	const url = window.location.search;
	const urlParams = new URLSearchParams(url);
	keys.forEach(key => {
		urlParams.delete(key);
	});
	window.history.replaceState({}, document.title, `${window.location.pathname}?${urlParams.toString()}`);
}
