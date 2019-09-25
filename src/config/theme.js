import { createMuiTheme } from "@material-ui/core/styles";

const primaryHex = "#707CED";
const secondaryHex = "#FF20B1";
const textColorPrimary = "#656D78";
const textColorSecondary = "#4EB0E5";
const warningHex = "#ff6333";
const borderRadius = 3;
const dialogBackgroundColor = "rgba(112, 124, 237, 0.7)";
const callToActionBackground = "linear-gradient(to left, #e53d96, #5491cc)";

const toolBarHeight = {
	minHeight: 56,
	"@media (min-width:0px) and (orientation: landscape)": {
		minHeight: 48
	},
	"@media (min-width:600px)": { minHeight: 70 }
};

const fontFamily = "TTCommons-Regular";
const fontFamilyBold = "TTCommons-Bold";
const fontFamilyDemiBold = "TTCommons-DemiBold";
const fontFamilyBebas = "BebasNeue-Regular";

const textFieldLabelStyle = {
	fontFamily,
	color: "#3c383f",
	textTransform: "capitalize",
	fontSize: 16
};

const theme = createMuiTheme({
	typography: {
		fontFamily,
		fontSize: 16
	},
	shape: {
		borderRadius
	},
	palette: {
		background: {
			default: "rgba(245, 247, 250)"
		},
		primary: {
			light: primaryHex,
			main: primaryHex,
			dark: primaryHex,
			contrastText: "#FFF"
		},
		secondary: {
			light: secondaryHex,
			main: secondaryHex,
			dark: secondaryHex,
			contrastText: "#FFF"
		},
		divider: "#d1d1d1"
	},
	overrides: {
		MuiDialog: {
			root: {
				backgroundColor: dialogBackgroundColor
			}
		},
		MuiAppBar: {
			root: {
				background: `linear-gradient(45deg, #FFF 30%, #FFF 90%)`,
				color: textColorPrimary,
				boxShadow: "none",
				borderStyle: "solid",
				borderWidth: 0.6,
				borderColor: "rgba(157,163,180,0.25)",
				padding: 0
			}
		},
		MuiToolbar: {
			root: {}
		},
		MuiInput: {
			root: {
				fontFamily,
				fontSize: 16
			}
		},
		MuiInputLabel: {
			root: textFieldLabelStyle
		},
		MuiFormHelperText: {
			root: {
				paddingBottom: 20
			}
		},
		MuiButton: {
			root: {
				boxShadow: "none",
				borderRadius
			}
		},
		MuiExpansionPanelSummary: {
			root: {
				padding: 0,
				borderRadius
			},
			content: {
				margin: 0
			},
			expanded: {
				padding: 0,
				margin: 0
			}
		},

		//For date picker
		MuiPickersToolbar: {
			toolbar: {
				backgroundColor: primaryHex
			}
		},
		MuiPickersCalendarHeader: {
			switchHeader: {
				// backgroundColor: lightBlue.A200,
				// color: 'white',
			}
		},
		MuiPickersDay: {
			day: {
				color: primaryHex
			},
			isSelected: {
				backgroundColor: secondaryHex
			},
			current: {
				color: secondaryHex
			}
		},
		MuiPickersModal: {
			dialogAction: {
				color: secondaryHex
			}
		}
	}
});

export {
	primaryHex,
	secondaryHex,
	textColorPrimary,
	textColorSecondary,
	warningHex,
	toolBarHeight,
	theme,
	callToActionBackground,
	fontFamily,
	fontFamilyBold,
	fontFamilyDemiBold,
	fontFamilyBebas,
	textFieldLabelStyle
};
