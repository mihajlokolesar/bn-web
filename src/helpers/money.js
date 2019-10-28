export const dollars = (cents, trimDecimalIfZero = false, prefix = "$") => {
	if (!cents) {
		return `${prefix}0`;
	}

	//Prepend the minus operator to currency symbol if cents is a negative value
	if (cents < 0) {
		prefix = `-${prefix}`;
		cents = cents * -1;
	}

	const dollars = cents / 100;

	if (trimDecimalIfZero && dollars % 1 === 0) {
		return `${prefix}${dollars.toFixed(0)}`;
	}

	return `${prefix}${(cents / 100).toFixed(2)}`;
};
