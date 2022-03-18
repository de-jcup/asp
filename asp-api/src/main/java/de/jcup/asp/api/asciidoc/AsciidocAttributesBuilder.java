package de.jcup.asp.api.asciidoc;

import java.net.URI;
import java.util.Date;

public class AsciidocAttributesBuilder {

    private final AsciidocAttributes asciidocAttributes = new AsciidocAttributes();

    AsciidocAttributesBuilder() {
        super();
    }

    /**
     * Source language attribute.
     * 
     * @param sourceLanguage value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder sourceLanguage(String sourceLanguage) {
        this.asciidocAttributes.setSourceLanguage(sourceLanguage);
        return this;
    }

    /**
     * Skips front matter.
     * 
     * @param skipFrontMatter value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder skipFrontMatter(boolean skipFrontMatter) {
        this.asciidocAttributes.setSkipFrontMatter(skipFrontMatter);
        return this;
    }

    /**
     * Sets ignore undefined flag so lines are kept when they contain a reference to
     * a missing attribute.
     * 
     * @param ignoreUndefinedAttributes value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder ignoreUndefinedAttributes(boolean ignoreUndefinedAttributes) {
        this.asciidocAttributes.setIgnoreUndefinedAttributes(ignoreUndefinedAttributes);
        return this;
    }

    /**
     * Sets max include depth attribute.
     * 
     * @param maxIncludeDepth value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder maxIncludeDepth(int maxIncludeDepth) {
        this.asciidocAttributes.setMaxIncludeDepth(maxIncludeDepth);
        return this;
    }

    /**
     * Sets sect num levels attribute.
     * 
     * @param sectnumlevels value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder sectNumLevels(int sectnumlevels) {
        this.asciidocAttributes.setSectNumLevels(sectnumlevels);
        return this;
    }

    /**
     * Sets attribute missing attribute. (Possible values skip, drop, drop-line)
     * 
     * @param attributeMissing value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder attributeMissing(String attributeMissing) {
        this.asciidocAttributes.setAttributeMissing(attributeMissing);
        return this;
    }

    /**
     * Sets attribute undefined attribute. (Possible values skip, drop, drop-line)
     * 
     * @param attributeUndefined value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder attributeUndefined(String attributeUndefined) {
        this.asciidocAttributes.setAttributeUndefined(attributeUndefined);
        return this;
    }

    /**
     * Sets setanchor flag.
     * 
     * @param setAnchors value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder setAnchors(boolean setAnchors) {
        this.asciidocAttributes.setAnchors(setAnchors);
        return this;
    }

    /**
     * Sets the untitled label value.
     * 
     * @param untitledLabel value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder untitledLabel(String untitledLabel) {
        this.asciidocAttributes.setUntitledLabel(untitledLabel);
        return this;
    }

    /**
     * Sets table of contents attribute.
     * 
     * @param asciidocPlacement where toc is rendered.
     * @return this instance.
     */
    public AsciidocAttributesBuilder tableOfContents(AsciidocPlacement asciidocPlacement) {
        this.asciidocAttributes.setTableOfContents(asciidocPlacement);
        return this;
    }

    /**
     * Sets allow uri read attribute.
     * 
     * @param allowUriRead value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder allowUriRead(boolean allowUriRead) {
        this.asciidocAttributes.setAllowUriRead(allowUriRead);
        return this;
    }

    /**
     * Sets showtitle value as an alias for notitle!
     * 
     * @param showTitle value.
     * @return this instance
     */
    public AsciidocAttributesBuilder showTitle(boolean showTitle) {
        this.asciidocAttributes.setShowTitle(showTitle);
        return this;
    }

    /**
     * Sets title of document.
     * 
     * @param title for document.
     * @return this instance.
     */
    public AsciidocAttributesBuilder title(String title) {
        this.asciidocAttributes.setTitle(title);
        return this;
    }

    /**
     * Sets image directory.
     * 
     * @param imagesDir location.
     * @return this instance.
     */
    public AsciidocAttributesBuilder imagesDir(String imagesDir) {
        this.asciidocAttributes.setImagesDir(imagesDir);
        return this;
    }

    /**
     * Sets source highlighter processor. It should be supported by asciidoctor.
     * 
     * @param sourceHighlighter name of the source highlighting library (e.g.,
     *                          coderay).
     * @return this instance.
     */
    public AsciidocAttributesBuilder sourceHighlighter(String sourceHighlighter) {
        this.asciidocAttributes.setSourceHighlighter(sourceHighlighter);
        return this;
    }

    /**
     * Sets local date for document.
     * 
     * @param date
     * @return this instance.
     */
    public AsciidocAttributesBuilder localDate(Date date) {
        this.asciidocAttributes.setLocalDate(date);
        return this;
    }

    /**
     * Sets local time for document.
     * 
     * @param time
     * @return this instance.
     */
    public AsciidocAttributesBuilder localTime(Date time) {
        this.asciidocAttributes.setLocalTime(time);
        return this;
    }

    /**
     * Sets doc date for current document.
     * 
     * @param date
     * @return this instance.
     */
    public AsciidocAttributesBuilder docDate(Date date) {
        this.asciidocAttributes.setDocDate(date);
        return this;
    }

    /**
     * Sets doc time for current document.
     * 
     * @param time
     * @return this instance.
     */
    public AsciidocAttributesBuilder docTime(Date time) {
        this.asciidocAttributes.setDocTime(time);
        return this;
    }

    /**
     * Sets if table of contents should be rendered or not
     * 
     * @param toc value
     * @return this instance.
     */
    public AsciidocAttributesBuilder tableOfContents(boolean toc) {
        this.asciidocAttributes.setTableOfContents(toc);
        return this;
    }

    /**
     * Sets stylesheet name.
     * 
     * @param styleSheetName of css file.
     * @return this instance.
     */
    public AsciidocAttributesBuilder styleSheetName(String styleSheetName) {
        this.asciidocAttributes.setStyleSheetName(styleSheetName);
        return this;
    }

    /**
     * Unsets stylesheet name so document will be generated without style.
     * 
     * @return this instance.
     */
    public AsciidocAttributesBuilder unsetStyleSheet() {
        this.asciidocAttributes.unsetStyleSheet();
        return this;
    }

    /**
     * Sets the styles dir.
     * 
     * @param stylesDir directory.
     * @return this instance.
     */
    public AsciidocAttributesBuilder stylesDir(String stylesDir) {
        this.asciidocAttributes.setStylesDir(stylesDir);
        return this;
    }

    /**
     * Sets link css attribute.
     * 
     * @param linkCss true if css is linked, false if css is embedded.
     * @return this instance.
     */
    public AsciidocAttributesBuilder linkCss(boolean linkCss) {
        this.asciidocAttributes.setLinkCss(linkCss);
        return this;
    }

    /**
     * Sets copy css attribute.
     * 
     * @param copyCss true if css should be copied to the output location, false
     *                otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder copyCss(boolean copyCss) {
        this.asciidocAttributes.setCopyCss(copyCss);
        return this;
    }

    /**
     * Sets which admonition icons to use. AsciidocAttributes.IMAGE_ICONS constant can be
     * used to use the original icons with images or AsciidocAttributes.FONT_ICONS for font
     * icons (font-awesome).
     * 
     * @param icons value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder icons(String icons) {
        this.asciidocAttributes.setIcons(icons);
        return this;
    }

    /**
     * Enable icon font remote attribute. If enabled, will use the iconfont-cdn
     * value to load the icon font URI; if disabled, will use the iconfont-name
     * value to locate the icon font CSS file
     * 
     * @param iconFontRemote true if attribute enabled false otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder iconFontRemote(boolean iconFontRemote) {
        this.asciidocAttributes.setIconFontRemote(iconFontRemote);
        return this;
    }

    /**
     * The URI prefix of the icon font; looks for minified CSS file based on
     * iconfont-name value; used when iconfont-remote is set
     * 
     * @param cdnUri uri where css is stored.
     * @return this instance.
     */
    public AsciidocAttributesBuilder iconFontCdn(URI cdnUri) {
        this.asciidocAttributes.setIconFontCdn(cdnUri);
        return this;
    }

    /**
     * The name of the stylesheet in the stylesdir to load (.css extension added
     * automatically)
     * 
     * @param iconFontName stylesheet name without .css extension.
     * @return this instance.
     */
    public AsciidocAttributesBuilder iconFontName(String iconFontName) {
        this.asciidocAttributes.setIconFontName(iconFontName);
        return this;
    }

    /**
     * Sets icons directory location.
     * 
     * @param iconsDir location.
     * @return this instance.
     */
    public AsciidocAttributesBuilder iconsDir(String iconsDir) {
        this.asciidocAttributes.setIconsDir(iconsDir);
        return this;
    }

    /**
     * Sets data-uri attribute.
     * 
     * @param dataUri true if images should be embedded, false otherwise.
     */
    public AsciidocAttributesBuilder dataUri(boolean dataUri) {
        this.asciidocAttributes.setDataUri(dataUri);
        return this;
    }

    /**
     * Auto-number section titles in the HTML backend.
     * 
     * @param sectionNumbers true if numbers should be autonumbered, false
     *                       otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder sectionNumbers(boolean sectionNumbers) {
        this.asciidocAttributes.setSectionNumbers(sectionNumbers);
        return this;
    }

    /**
     * Sets hardbreaks at the end of each line.
     * 
     * @param hardbreaks true if each line should be added a hardbreak, false
     *                   otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder hardbreaks(boolean hardbreaks) {
        this.asciidocAttributes.setHardbreaks(hardbreaks);
        return this;
    }

    /**
     * Sets cache-uri flag.
     * 
     * @param cacheUri true if URI content should be cached, false otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder cacheUri(boolean cacheUri) {
        this.asciidocAttributes.setCacheUri(cacheUri);
        return this;
    }

    /**
     * Sets hide-uri-scheme flag.
     * 
     * @param hiddenUriScheme true if URI is hidden, false otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder hiddenUriScheme(boolean hiddenUriScheme) {
        this.asciidocAttributes.setHideUriScheme(hiddenUriScheme);
        return this;
    }

    /**
     * Sets appendix-caption label.
     * 
     * @param appendixCaption value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder appendixCaption(String appendixCaption) {
        this.asciidocAttributes.setAppendixCaption(appendixCaption);
        return this;
    }

    /**
     * Sets math default engine.
     * 
     * @param math value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder math(String math) {
        this.asciidocAttributes.setMath(math);
        return this;
    }

    /**
     * Sets linkattrs attribute.
     * 
     * @param linkAttrs true if Asciidoctor should parse link macro asciidocAttributes,
     *                  false otherwise.
     * 
     * @return this instance.
     */
    public AsciidocAttributesBuilder linkAttrs(boolean linkAttrs) {
        this.asciidocAttributes.setLinkAttrs(linkAttrs);
        return this;
    }

    /**
     * Sets experimental attribute.
     * 
     * @param experimental true if experimental features should be enabled, false
     *                     otherwise.
     */
    public AsciidocAttributesBuilder experimental(boolean experimental) {
        this.asciidocAttributes.setExperimental(experimental);
        return this;
    }

    /**
     * Sets nofooter attribute.
     * 
     * @param noFooter true if footer block should not be shown, false otherwise.
     * @return this instance.
     */
    public AsciidocAttributesBuilder noFooter(boolean noFooter) {
        this.asciidocAttributes.setNoFooter(noFooter);
        return this;
    }

    /**
     * Sets compat mode attribute.
     * 
     * @param asciidocCompatMode value.
     * @return this instance.
     */
    public AsciidocAttributesBuilder asciidocCompatMode(AsciidocCompatMode asciidocCompatMode) {
        this.asciidocAttributes.setCompatMode(asciidocCompatMode);
        return this;
    }
    
    public AsciidocAttributesBuilder customAttribute(String attribute, Object value) {
        this.asciidocAttributes.setCustomAttribute(attribute,value);
        return this;
    }

    /**
     * Returns a valid AsciidocAttributes instance.
     *
     * @return asciidocAttributes instance.
     */
    public AsciidocAttributes build() {
        return this.asciidocAttributes;
    }
}
