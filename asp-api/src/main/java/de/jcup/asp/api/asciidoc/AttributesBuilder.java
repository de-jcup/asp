package de.jcup.asp.api.asciidoc;

import java.net.URI;
import java.util.Date;

public class AttributesBuilder {

    private final Attributes attributes = new Attributes();

    AttributesBuilder() {
        super();
    }

    /**
     * Source language attribute.
     * 
     * @param sourceLanguage value.
     * @return this instance.
     */
    public AttributesBuilder sourceLanguage(String sourceLanguage) {
        this.attributes.setSourceLanguage(sourceLanguage);
        return this;
    }

    /**
     * Skips front matter.
     * 
     * @param skipFrontMatter value.
     * @return this instance.
     */
    public AttributesBuilder skipFrontMatter(boolean skipFrontMatter) {
        this.attributes.setSkipFrontMatter(skipFrontMatter);
        return this;
    }

    /**
     * Sets ignore undefined flag so lines are kept when they contain a reference to
     * a missing attribute.
     * 
     * @param ignoreUndefinedAttributes value.
     * @return this instance.
     */
    public AttributesBuilder ignoreUndefinedAttributes(boolean ignoreUndefinedAttributes) {
        this.attributes.setIgnoreUndefinedAttributes(ignoreUndefinedAttributes);
        return this;
    }

    /**
     * Sets max include depth attribute.
     * 
     * @param maxIncludeDepth value.
     * @return this instance.
     */
    public AttributesBuilder maxIncludeDepth(int maxIncludeDepth) {
        this.attributes.setMaxIncludeDepth(maxIncludeDepth);
        return this;
    }

    /**
     * Sets sect num levels attribute.
     * 
     * @param sectnumlevels value.
     * @return this instance.
     */
    public AttributesBuilder sectNumLevels(int sectnumlevels) {
        this.attributes.setSectNumLevels(sectnumlevels);
        return this;
    }

    /**
     * Sets attribute missing attribute. (Possible values skip, drop, drop-line)
     * 
     * @param attributeMissing value.
     * @return this instance.
     */
    public AttributesBuilder attributeMissing(String attributeMissing) {
        this.attributes.setAttributeMissing(attributeMissing);
        return this;
    }

    /**
     * Sets attribute undefined attribute. (Possible values skip, drop, drop-line)
     * 
     * @param attributeUndefined value.
     * @return this instance.
     */
    public AttributesBuilder attributeUndefined(String attributeUndefined) {
        this.attributes.setAttributeUndefined(attributeUndefined);
        return this;
    }

    /**
     * Sets setanchor flag.
     * 
     * @param setAnchors value.
     * @return this instance.
     */
    public AttributesBuilder setAnchors(boolean setAnchors) {
        this.attributes.setAnchors(setAnchors);
        return this;
    }

    /**
     * Sets the untitled label value.
     * 
     * @param untitledLabel value.
     * @return this instance.
     */
    public AttributesBuilder untitledLabel(String untitledLabel) {
        this.attributes.setUntitledLabel(untitledLabel);
        return this;
    }

    /**
     * Sets table of contents attribute.
     * 
     * @param placement where toc is rendered.
     * @return this instance.
     */
    public AttributesBuilder tableOfContents(Placement placement) {
        this.attributes.setTableOfContents(placement);
        return this;
    }

    /**
     * Sets allow uri read attribute.
     * 
     * @param allowUriRead value.
     * @return this instance.
     */
    public AttributesBuilder allowUriRead(boolean allowUriRead) {
        this.attributes.setAllowUriRead(allowUriRead);
        return this;
    }

    /**
     * Sets showtitle value as an alias for notitle!
     * 
     * @param showTitle value.
     * @return this instance
     */
    public AttributesBuilder showTitle(boolean showTitle) {
        this.attributes.setShowTitle(showTitle);
        return this;
    }

    /**
     * Sets title of document.
     * 
     * @param title for document.
     * @return this instance.
     */
    public AttributesBuilder title(String title) {
        this.attributes.setTitle(title);
        return this;
    }

    /**
     * Sets image directory.
     * 
     * @param imagesDir location.
     * @return this instance.
     */
    public AttributesBuilder imagesDir(String imagesDir) {
        this.attributes.setImagesDir(imagesDir);
        return this;
    }

    /**
     * Sets source highlighter processor. It should be supported by asciidoctor.
     * 
     * @param sourceHighlighter name of the source highlighting library (e.g.,
     *                          coderay).
     * @return this instance.
     */
    public AttributesBuilder sourceHighlighter(String sourceHighlighter) {
        this.attributes.setSourceHighlighter(sourceHighlighter);
        return this;
    }

    /**
     * Sets local date for document.
     * 
     * @param date
     * @return this instance.
     */
    public AttributesBuilder localDate(Date date) {
        this.attributes.setLocalDate(date);
        return this;
    }

    /**
     * Sets local time for document.
     * 
     * @param time
     * @return this instance.
     */
    public AttributesBuilder localTime(Date time) {
        this.attributes.setLocalTime(time);
        return this;
    }

    /**
     * Sets doc date for current document.
     * 
     * @param date
     * @return this instance.
     */
    public AttributesBuilder docDate(Date date) {
        this.attributes.setDocDate(date);
        return this;
    }

    /**
     * Sets doc time for current document.
     * 
     * @param time
     * @return this instance.
     */
    public AttributesBuilder docTime(Date time) {
        this.attributes.setDocTime(time);
        return this;
    }

    /**
     * Sets if table of contents should be rendered or not
     * 
     * @param toc value
     * @return this instance.
     */
    public AttributesBuilder tableOfContents(boolean toc) {
        this.attributes.setTableOfContents(toc);
        return this;
    }

    /**
     * Sets stylesheet name.
     * 
     * @param styleSheetName of css file.
     * @return this instance.
     */
    public AttributesBuilder styleSheetName(String styleSheetName) {
        this.attributes.setStyleSheetName(styleSheetName);
        return this;
    }

    /**
     * Unsets stylesheet name so document will be generated without style.
     * 
     * @return this instance.
     */
    public AttributesBuilder unsetStyleSheet() {
        this.attributes.unsetStyleSheet();
        return this;
    }

    /**
     * Sets the styles dir.
     * 
     * @param stylesDir directory.
     * @return this instance.
     */
    public AttributesBuilder stylesDir(String stylesDir) {
        this.attributes.setStylesDir(stylesDir);
        return this;
    }

    /**
     * Sets link css attribute.
     * 
     * @param linkCss true if css is linked, false if css is embedded.
     * @return this instance.
     */
    public AttributesBuilder linkCss(boolean linkCss) {
        this.attributes.setLinkCss(linkCss);
        return this;
    }

    /**
     * Sets copy css attribute.
     * 
     * @param copyCss true if css should be copied to the output location, false
     *                otherwise.
     * @return this instance.
     */
    public AttributesBuilder copyCss(boolean copyCss) {
        this.attributes.setCopyCss(copyCss);
        return this;
    }

    /**
     * Sets which admonition icons to use. Attributes.IMAGE_ICONS constant can be
     * used to use the original icons with images or Attributes.FONT_ICONS for font
     * icons (font-awesome).
     * 
     * @param icons value.
     * @return this instance.
     */
    public AttributesBuilder icons(String icons) {
        this.attributes.setIcons(icons);
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
    public AttributesBuilder iconFontRemote(boolean iconFontRemote) {
        this.attributes.setIconFontRemote(iconFontRemote);
        return this;
    }

    /**
     * The URI prefix of the icon font; looks for minified CSS file based on
     * iconfont-name value; used when iconfont-remote is set
     * 
     * @param cdnUri uri where css is stored.
     * @return this instance.
     */
    public AttributesBuilder iconFontCdn(URI cdnUri) {
        this.attributes.setIconFontCdn(cdnUri);
        return this;
    }

    /**
     * The name of the stylesheet in the stylesdir to load (.css extension added
     * automatically)
     * 
     * @param iconFontName stylesheet name without .css extension.
     * @return this instance.
     */
    public AttributesBuilder iconFontName(String iconFontName) {
        this.attributes.setIconFontName(iconFontName);
        return this;
    }

    /**
     * Sets icons directory location.
     * 
     * @param iconsDir location.
     * @return this instance.
     */
    public AttributesBuilder iconsDir(String iconsDir) {
        this.attributes.setIconsDir(iconsDir);
        return this;
    }

    /**
     * Sets data-uri attribute.
     * 
     * @param dataUri true if images should be embedded, false otherwise.
     */
    public AttributesBuilder dataUri(boolean dataUri) {
        this.attributes.setDataUri(dataUri);
        return this;
    }

    /**
     * Auto-number section titles in the HTML backend.
     * 
     * @param sectionNumbers true if numbers should be autonumbered, false
     *                       otherwise.
     * @return this instance.
     */
    public AttributesBuilder sectionNumbers(boolean sectionNumbers) {
        this.attributes.setSectionNumbers(sectionNumbers);
        return this;
    }

    /**
     * Sets hardbreaks at the end of each line.
     * 
     * @param hardbreaks true if each line should be added a hardbreak, false
     *                   otherwise.
     * @return this instance.
     */
    public AttributesBuilder hardbreaks(boolean hardbreaks) {
        this.attributes.setHardbreaks(hardbreaks);
        return this;
    }

    /**
     * Sets cache-uri flag.
     * 
     * @param cacheUri true if URI content should be cached, false otherwise.
     * @return this instance.
     */
    public AttributesBuilder cacheUri(boolean cacheUri) {
        this.attributes.setCacheUri(cacheUri);
        return this;
    }

    /**
     * Sets hide-uri-scheme flag.
     * 
     * @param hiddenUriScheme true if URI is hidden, false otherwise.
     * @return this instance.
     */
    public AttributesBuilder hiddenUriScheme(boolean hiddenUriScheme) {
        this.attributes.setHideUriScheme(hiddenUriScheme);
        return this;
    }

    /**
     * Sets appendix-caption label.
     * 
     * @param appendixCaption value.
     * @return this instance.
     */
    public AttributesBuilder appendixCaption(String appendixCaption) {
        this.attributes.setAppendixCaption(appendixCaption);
        return this;
    }

    /**
     * Sets math default engine.
     * 
     * @param math value.
     * @return this instance.
     */
    public AttributesBuilder math(String math) {
        this.attributes.setMath(math);
        return this;
    }

    /**
     * Sets linkattrs attribute.
     * 
     * @param linkAttrs true if Asciidoctor should parse link macro attributes,
     *                  false otherwise.
     * 
     * @return this instance.
     */
    public AttributesBuilder linkAttrs(boolean linkAttrs) {
        this.attributes.setLinkAttrs(linkAttrs);
        return this;
    }

    /**
     * Sets experimental attribute.
     * 
     * @param experimental true if experimental features should be enabled, false
     *                     otherwise.
     */
    public AttributesBuilder experimental(boolean experimental) {
        this.attributes.setExperimental(experimental);
        return this;
    }

    /**
     * Sets nofooter attribute.
     * 
     * @param noFooter true if footer block should not be shown, false otherwise.
     * @return this instance.
     */
    public AttributesBuilder noFooter(boolean noFooter) {
        this.attributes.setNoFooter(noFooter);
        return this;
    }

    /**
     * Sets compat mode attribute.
     * 
     * @param compatMode value.
     * @return this instance.
     */
    public AttributesBuilder compatMode(CompatMode compatMode) {
        this.attributes.setCompatMode(compatMode);
        return this;
    }
    
    public AttributesBuilder customAttribute(String attribute, Object value) {
        this.attributes.setCustomAttribute(attribute,value);
        return this;
    }

    /**
     * Returns a valid Attributes instance.
     *
     * @return attributes instance.
     */
    public Attributes build() {
        return this.attributes;
    }
}
