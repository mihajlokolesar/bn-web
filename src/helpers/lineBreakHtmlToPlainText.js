export default content => {
	content = content
		.replace(/<\/?[strong|em|u|a]+>/gim, "")
		.replace(/<br\/>/gim, "\n")
		.replace(/<\/.*?>/gim, "\n")
		.replace(/<.*?>/gim, "");
	return content;
};
