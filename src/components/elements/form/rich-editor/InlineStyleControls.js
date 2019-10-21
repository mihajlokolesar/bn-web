import { INLINE_STYLES } from "./EditorSettings";
import StyleButton from "./StyleButton";
import React from "react";

const InlineStyleControls = props => {
	const currentStyle = props.editorState.getCurrentInlineStyle();

	return (
		<div className="RichEditor-controls">
			{INLINE_STYLES.map(type => {
				return (
					<StyleButton
						key={type.label}
						active={currentStyle.has(type.style)}
						label={type.label}
						onToggle={props.onToggle}
						style={type.style}
					/>
				);
			})}
		</div>
	);
};

export default InlineStyleControls;
