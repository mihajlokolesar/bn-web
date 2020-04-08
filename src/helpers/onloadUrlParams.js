import getAllUrlParams from "./getAllUrlParams";
import decodeJWT from "./decodeJWT";

export default () => {
	const { access_token, refresh_token, rnNavigation } = getAllUrlParams();
	if (refresh_token) {
		try {
			//Attempt to decode these, if they are not valid do not store them.
			if (access_token) {
				decodeJWT(access_token);
				localStorage.setItem("access_token", access_token);
			}

			decodeJWT(refresh_token);
			localStorage.setItem("refresh_token", refresh_token);
		} catch (e) {
			console.error("Invalid access / refresh token provided");
		}
	}
	if (rnNavigation) {
		localStorage.setItem("rnNavigation", "1");
	} else if (rnNavigation === "0") {
		localStorage.removeItem("rnNavigation");
	}
	deleteUrlParams(["access_token", "refresh_token", "rnNavigation"]);
};

function deleteUrlParams(keys) {
	const url = window.location.search;
	const urlParams = new URLSearchParams(url);
	keys.forEach(key => {
		urlParams.delete(key);
	});
	window.history.replaceState({}, document.title, `${window.location.pathname}?${urlParams.toString()}`);
}
