import { fontFamilyDemiBold, secondaryHex } from "../../../../config/theme";

export const styleMap = {
	CODE: {
		backgroundColor: "rgba(0, 0, 0, 0.05)",
		fontFamily: '"Inconsolata", "Menlo", "Consolas", monospace',
		fontSize: 16,
		padding: 2
	},
	LINK: {
		color: secondaryHex,
		textDecoration: "underline",
		fontFamily: fontFamilyDemiBold
	}
};

export const INLINE_STYLES = [
	{ label: "Bold", style: "BOLD" },
	{ label: "Italic", style: "ITALIC" },
	{ label: "Underline", style: "UNDERLINE" },
	{ label: "Link", style: "LINK" }
	//{ label: "Monospace", style: "CODE" }
];

export const BLOCK_TYPES = [
	{ label: "Header 1", style: "header-one" },
	{ label: "Header 2", style: "header-two" },
	// { label: "H3", style: "header-three" },
	// { label: "H4", style: "header-four" },
	// { label: "H5", style: "header-five" },
	// { label: "H6", style: "header-six" },
	{ label: "Blockquote", style: "blockquote" },
	{ label: "List", style: "unordered-list-item" }
	//{ label: "OL", style: "ordered-list-item" }
	//{ label: "Code Block", style: "code-block" }
];
