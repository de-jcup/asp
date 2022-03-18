package de.jcup.asp.api.asciidoc;

import static de.jcup.asp.api.asciidoc.AsciidocAttribute.*;

import java.net.URI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AsciidocAttributes {

    private static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static Format TIME_FORMAT = new SimpleDateFormat("HH:mm:ss z");

    private Map<String, Object> attributes = new LinkedHashMap<>();

    AsciidocAttributes() {
        super();
    }

    /**
     * @return Empty AsciidocAttributesBuilder instance.
     */
    public static AsciidocAttributesBuilder builder() {
        return new AsciidocAttributesBuilder();
    }

    /**
     * Allow Asciidoctor to read content from an URI. Additionally the option
     * {@link AsciidocSafeMode} must be less than {@link AsciidocSafeMode#SECURE} to enable
     * inclusion of content from an URI.
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#include-content-from-a-uri">Asciidoctor
     *      User Manual</a>
     * @param allowUriRead {@code true} to enable inclusion of content from an URI
     */
    public void setAllowUriRead(boolean allowUriRead) {
        this.put(ALLOW_URI_READ, toAsciidoctorFlag(allowUriRead));
    }

    /**
     * Define how to handle missing attribute references. Possible values are:
     * <table>
     * <tr>
     * <td>{@code skip}</td>
     * <td>leave the reference in place (default setting)</td>
     * </tr>
     * <tr>
     * <td>{@code drop}</td>
     * <td>drop the reference, but not the line</td>
     * </tr>
     * <tr>
     * <td>{@code drop-line}</td>
     * <td>drop the line on which the reference occurs (compliant behavior)</td>
     * </tr>
     * <tr>
     * <td>{@code warn}</td>
     * <td>print a warning about the missing attribute</td>
     * </tr>
     * </table>
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#catch-a-missing-or-undefined-attribute">Asciidoctor
     *      User Manual</a>
     * @param attributeMissing One of the constants shown in the table above.
     */
    public void setAttributeMissing(String attributeMissing) {
        this.put(ATTRIBUTE_MISSING, attributeMissing);
    }

    /**
     * Define how to handle expressions that undefine an attribute. Possible values
     * are:
     * <table>
     * <tr>
     * <td>{@code drop}</td>
     * <td>substitute the expression with an empty string after processing it</td>
     * </tr>
     * <tr>
     * <td>{@code drop-line}</td>
     * <td>drop the line that contains this expression (default setting and
     * compliant behavior)</td>
     * </tr>
     * </table>
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#catch-a-missing-or-undefined-attribute">Asciidoctor
     *      User Manual</a>
     * @param attributeUndefined One of the constants shown in the table above.
     */
    public void setAttributeUndefined(String attributeUndefined) {
        this.put(ATTRIBUTE_UNDEFINED, attributeUndefined);
    }

    public void setTitle(String title) {
        this.put(TITLE, title);
    }

    /**
     * Sets the directory to which images are resolved if the image target is a
     * relative path.
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#set-the-images-directory">Asciidoctor
     *      User Manual</a>
     * @param imagesDir The name of the directory.
     */
    public void setImagesDir(String imagesDir) {
        this.put(IMAGESDIR, imagesDir);
    }

    /**
     * Globally sets the source language attribute when rendering source blocks.
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#source-code-blocks">Asciidoctor
     *      User Manual</a>
     * @param sourceLanguage The default source language to use, e.g. {@code Java}.
     */
    public void setSourceLanguage(String sourceLanguage) {
        this.put(SOURCE_LANGUAGE, sourceLanguage);
    }

    /**
     * Sets the source highlighter to use for rendering source blocks. Possible
     * values are:
     * <ul>
     * <li>coderay</li>
     * <li>highlightjs</li>
     * <li>prettify</li>
     * </ul>
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#source-code-blocks">Asciidoctor
     *      User Manual</a>
     * @param sourceHighlighter One of the constants mentioned above.
     */
    public void setSourceHighlighter(String sourceHighlighter) {
        this.put(SOURCE_HIGHLIGHTER, sourceHighlighter);
    }

    /**
     * Defines how many documents can be recursively included.
     * 
     * @see <a href="https://github.com/asciidoctor/asciidoctor/issues/581">Track
     *      include depth and enforce maximum depth #581</a>
     * @param maxIncludeDepth A positive integer.
     */
    public void setMaxIncludeDepth(int maxIncludeDepth) {
        this.put(MAX_INCLUDE_DEPTH, maxIncludeDepth);
    }

    /**
     * Sets the depth of section numbering. That is if set to 1 only the top level
     * section will be assigned a number. Default is 3.
     * 
     * @param sectNumLevels A positive integer.
     */
    public void setSectNumLevels(int sectNumLevels) {
        this.put(SECT_NUM_LEVELS, sectNumLevels);
    }

    /**
     * Enables or disables preserving of line breaks in a paragraph.
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#line-breaks">Asciidoctor User
     *      Manual</a>
     * @param hardbreaks {@code true} to enable preserving of line breaks in
     *                   paragraphs
     */
    public void setHardbreaks(boolean hardbreaks) {
        this.put(HARDBREAKS, toAsciidoctorFlag(hardbreaks));
    }

    /**
     * Enables or disables caching of content read from URIs
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#include-content-from-a-uri">Asciidoctor
     *      User Manual</a>
     * @param cacheUri {@code true} to enable caching of content read from URIs
     */
    public void setCacheUri(boolean cacheUri) {
        this.put(CACHE_URI, toAsciidoctorFlag(cacheUri));
    }

    /**
     * Enables or disables rendering of the URI scheme when rendering URLs.
     * 
     * @see <a href="http://asciidoctor.org/docs/user-manual/#url">Asciidoctor User
     *      Manual</a>
     * @param hideUriScheme
     */
    public void setHideUriScheme(boolean hideUriScheme) {
        this.put(HIDE_URI_SCHEME, toAsciidoctorFlag(hideUriScheme));
    }

    /**
     * Defines the prefix added to appendix sections. The default value is
     * {@code Appendix}
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#user-appendix">Asciidoctor
     *      User Manual</a>
     * @param appendixCaption The string that is prefixed to the section name in the
     *                        appendix.
     */
    public void setAppendixCaption(String appendixCaption) {
        this.put(APPENDIX_CAPTION, appendixCaption);
    }

    /**
     * Sets the interpreter to use for rendering stems, i.e. equations and formulas.
     * 
     * @see <a href=
     *      "http://asciidoctor.org/docs/user-manual/#activating-stem-support">Asciidoctor
     *      User Manual</a>
     * @param math The name of the interpreter, i.e. either {@code asciimath} or
     *             {@code latexmath}.
     */
    public void setMath(String math) {
        this.put(MATH, math);
    }

    /**
     * Skips front matter.
     * 
     * @param skipFrontMatter value.
     */
    public void setSkipFrontMatter(boolean skipFrontMatter) {
        this.put(SKIP_FRONT_MATTER, toAsciidoctorFlag(skipFrontMatter));
    }

    /**
     * Sets setanchor flag.
     * 
     * @param setAnchors value.
     */
    public void setAnchors(boolean setAnchors) {
        this.put(SET_ANCHORS, setAnchors);
    }

    /**
     * Sets the untitled label value.
     * 
     * @param untitledLabel value.
     */
    public void setUntitledLabel(String untitledLabel) {
        this.put(UNTITLED_LABEL, untitledLabel);
    }

    /**
     * Sets ignore undefined flag so lines are kept when they contain a reference to
     * a missing attribute.
     * 
     * @param ignoreUndefinedAttributes value.
     */
    public void setIgnoreUndefinedAttributes(boolean ignoreUndefinedAttributes) {
        this.put(IGNORE_UNDEFINED, toAsciidoctorFlag(ignoreUndefinedAttributes));
    }

    /**
     * Sets if a table of contents should be rendered or not.
     * 
     * @param asciidocPlacement position of toc.
     */
    public void setTableOfContents(AsciidocPlacement asciidocPlacement) {
        this.put(TOC, asciidocPlacement.getPosition());
    }

    /**
     * Sets the amount of levels which should be shown in the toc.
     * 
     * @param levels number of levels which should be shown in the toc.
     */
    public void setTocLevels(int levels) {
        this.put(TOC_LEVELS, levels);
    }

    /**
     * Sets showtitle value as an alias for notitle!
     * 
     * @param showTitle value.
     */
    public void setShowTitle(boolean showTitle) {
        if (showTitle) {
            this.put(SHOW_TITLE, true);
            this.attributes.remove(NOTITLE.getKey());
        } else {
            this.put(NOTITLE, true);
            this.attributes.remove(SHOW_TITLE.getKey());
        }
    }

    /**
     * Sets if a table of contents should be rendered or not.
     * 
     * @param toc value.
     */
    public void setTableOfContents(boolean toc) {
        if (toc) {
            this.put(TOC, "");
        } else {
            this.attributes.remove(TOC.getKey());
        }
    }

    /**
     * Sets date in format yyyy-MM-dd
     * 
     * @param localDate object.
     */
    public void setLocalDate(Date localDate) {
        this.put(LOCALDATE, toDate(localDate));
    }

    /**
     * Sets time in format HH:mm:ss z
     * 
     * @param localTime object.
     */
    public void setLocalTime(Date localTime) {
        this.put(LOCALTIME, toTime(localTime));
    }

    /**
     * Sets date in format yyyy-MM-dd
     * 
     * @param docDate object.
     */
    public void setDocDate(Date docDate) {
        this.put(DOCDATE, toDate(docDate));
    }

    /**
     * Sets time in format HH:mm:ss z
     * 
     * @param docTime object.
     */
    public void setDocTime(Date docTime) {
        this.put(DOCTIME, toTime(docTime));
    }

    /**
     * Sets stylesheet name.
     * 
     * @param styleSheetName of css file.
     */
    public void setStyleSheetName(String styleSheetName) {
        this.put(STYLESHEET_NAME, styleSheetName);
    }

    /**
     * Unsets stylesheet name so document will be generated without style.
     */
    public void unsetStyleSheet() {
        this.put(NOT_STYLESHEET_NAME, toAsciidoctorFlag(true));
    }

    /**
     * Sets the styles dir.
     * 
     * @param stylesDir directory.
     */
    public void setStylesDir(String stylesDir) {
        this.put(STYLES_DIR, stylesDir);
    }

    /**
     * Sets link css attribute.
     * 
     * @param linkCss true if css is linked, false if css is embedded.
     */
    public void setLinkCss(boolean linkCss) {
        this.put(LINK_CSS, toAsciidoctorFlag(linkCss));
    }

    /**
     * Sets copy css attribute.
     * 
     * @param copyCss true if css should be copied to the output location, false
     *                otherwise.
     */
    public void setCopyCss(boolean copyCss) {
        this.put(COPY_CSS, toAsciidoctorFlag(copyCss));
    }

    /**
     * Sets which admonition icons to use. AsciidocAttributes.IMAGE_ICONS constant can be
     * used to use the original icons with images or AsciidocAttributes.FONT_ICONS for font
     * icons (font-awesome).
     * 
     * @param iconsName value.
     */
    public void setIcons(String iconsName) {
        this.put(ICONS, iconsName);
    }

    /**
     * Enable icon font remote attribute. If enabled, will use the iconfont-cdn
     * value to load the icon font URI; if disabled, will use the iconfont-name
     * value to locate the icon font CSS file
     * 
     * @param iconFontRemote true if attribute enabled false otherwise.
     */
    public void setIconFontRemote(boolean iconFontRemote) {
        this.put(ICONFONT_REMOTE, toAsciidoctorFlag(iconFontRemote));
    }

    /**
     * The URI prefix of the icon font; looks for minified CSS file based on
     * iconfont-name value; used when iconfont-remote is set
     * 
     * @param cdnUri uri where css is stored.
     */
    public void setIconFontCdn(URI cdnUri) {
        this.put(ICONFONT_CDN, cdnUri.toASCIIString());
    }

    /**
     * The name of the stylesheet in the stylesdir to load (.css extension added
     * automatically)
     * 
     * @param iconFontName stylesheet name without .css extension.
     */
    public void setIconFontName(String iconFontName) {
        this.put(ICONFONT_NAME, iconFontName);
    }

    /**
     * Sets data-uri attribute.
     * 
     * @param dataUri true if images should be embedded, false otherwise.
     */
    public void setDataUri(boolean dataUri) {
        this.put(DATA_URI, toAsciidoctorFlag(dataUri));
    }

    /**
     * Sets icons directory.
     * 
     * @param iconsDir
     */
    public void setIconsDir(String iconsDir) {
        this.put(ICONS_DIR, iconsDir);
    }

    /**
     * auto-number section titles in the HTML backend
     * 
     * @param sectionNumbers
     */
    public void setSectionNumbers(boolean sectionNumbers) {
        this.put(SECTION_NUMBERS, toAsciidoctorFlag(sectionNumbers));
    }

    /**
     * Sets linkattrs attribute.
     * 
     * @param linkAttrs true if Asciidoctor should parse link macro attributes,
     *                  false otherwise.
     */
    public void setLinkAttrs(boolean linkAttrs) {
        this.put(LINK_ATTRS, toAsciidoctorFlag(linkAttrs));
    }

    /**
     * Sets experimental attribute.
     * 
     * @param experimental true if experimental features should be enabled, false
     *                     otherwise.
     */
    public void setExperimental(boolean experimental) {
        this.put(EXPERIMENTAL, experimental);
    }

    /**
     * Sets nofooter attribute.
     * 
     * @param noFooter true if the footer block should not be shown, false
     *                 otherwise.
     */
    public void setNoFooter(boolean noFooter) {
    }

    private void put(AsciidocAttribute atribute, Object object) {
        this.attributes.put(atribute.getKey(), object);
    }

    /**
     * Sets compat-mode attribute.
     * 
     * @param asciidocCompatMode value.
     */
    public void setCompatMode(AsciidocCompatMode asciidocCompatMode) {
        this.put(COMPAT_MODE, asciidocCompatMode.getMode());
    }
    
    /**
     * Adds all attributes.
     * 
     * @param attributes to add.
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public static String toAsciidoctorFlag(boolean flag) {
        return flag ? "" : null;
    }

    private static String toDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    private static String toTime(Date time) {
        return TIME_FORMAT.format(time);
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public void setCustomAttribute(String attribute, Object value) {
        this.attributes.put(attribute, value);
    }
}
