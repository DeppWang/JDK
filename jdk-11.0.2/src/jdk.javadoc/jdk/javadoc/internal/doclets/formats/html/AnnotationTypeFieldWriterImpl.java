/*
 * Copyright (c) 2013, 2018, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.formats.html;

import jdk.javadoc.internal.doclets.formats.html.markup.Table;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import jdk.javadoc.internal.doclets.formats.html.markup.TableHeader;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlConstants;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTag;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.StringContent;
import jdk.javadoc.internal.doclets.toolkit.AnnotationTypeFieldWriter;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.MemberSummaryWriter;

/**
 * Writes annotation type field documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Bhavesh Patel
 */
public class AnnotationTypeFieldWriterImpl extends AbstractMemberWriter
    implements AnnotationTypeFieldWriter, MemberSummaryWriter {

    /**
     * Construct a new AnnotationTypeFieldWriterImpl.
     *
     * @param writer         the writer that will write the output.
     * @param annotationType the AnnotationType that holds this member.
     */
    public AnnotationTypeFieldWriterImpl(SubWriterHolderWriter writer,
            TypeElement annotationType) {
        super(writer, annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader(TypeElement typeElement,
            Content memberSummaryTree) {
        memberSummaryTree.addContent(
                HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_SUMMARY);
        Content memberTree = writer.getMemberTreeHeader();
        writer.addSummaryHeader(this, typeElement, memberTree);
        return memberTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberTreeHeader() {
        return writer.getMemberTreeHeader();
    }

    /**
     * {@inheritDoc}
     */
    public void addMemberTree(Content memberSummaryTree, Content memberTree) {
        writer.addMemberTree(memberSummaryTree, memberTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotationFieldDetailsMarker(Content memberDetails) {
        memberDetails.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_DETAILS);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotationDetailsTreeHeader(TypeElement typeElement,
            Content memberDetailsTree) {
        if (!writer.printedAnnotationFieldHeading) {
            memberDetailsTree.addContent(links.createAnchor(
                    SectionName.ANNOTATION_TYPE_FIELD_DETAIL));
            Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                    contents.fieldDetailsLabel);
            memberDetailsTree.addContent(heading);
            writer.printedAnnotationFieldHeading = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDocTreeHeader(Element member,
            Content annotationDetailsTree) {
        annotationDetailsTree.addContent(links.createAnchor(name(member)));
        Content annotationDocTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(name(member));
        annotationDocTree.addContent(heading);
        return annotationDocTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature(Element member) {
        Content pre = new HtmlTree(HtmlTag.PRE);
        writer.addAnnotationInfo(member, pre);
        addModifiers(member, pre);
        Content link =
                writer.getLink(new LinkInfoImpl(configuration,
                        LinkInfoImpl.Kind.MEMBER, getType(member)));
        pre.addContent(link);
        pre.addContent(Contents.SPACE);
        if (configuration.linksource) {
            Content memberName = new StringContent(name(member));
            writer.addSrcLink(member, memberName, pre);
        } else {
            addName(name(member), pre);
        }
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated(Element member, Content annotationDocTree) {
        addDeprecatedInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments(Element member, Content annotationDocTree) {
        addComment(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addTags(Element member, Content annotationDocTree) {
        writer.addTagsInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDetails(Content annotationDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(annotationDetailsTree));
            return htmlTree;
        }
        return getMemberTree(annotationDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDoc(Content annotationDocTree,
            boolean isLastContent) {
        return getMemberTree(annotationDocTree, isLastContent);
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                contents.fieldSummaryLabel);
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableHeader getSummaryTableHeader(Element member) {
        return new TableHeader(contents.modifierAndTypeLabel, contents.fields,
                contents.descriptionLabel);
    }

    @Override
    protected Table createSummaryTable() {
        String summary = resources.getText("doclet.Member_Table_Summary",
            resources.getText("doclet.Field_Summary"),
            resources.getText("doclet.fields"));
        Content caption = contents.getContent("doclet.Fields");

        TableHeader header = new TableHeader(contents.modifierAndTypeLabel, contents.fields,
            contents.descriptionLabel);

        return new Table(configuration.htmlVersion, HtmlStyle.memberSummary)
                .setSummary(summary)
                .setCaption(caption)
                .setHeader(header)
                .setRowScopeColumn(1)
                .setColumnStyles(HtmlStyle.colFirst, HtmlStyle.colSecond, HtmlStyle.colLast)
                .setUseTBody(false);  // temporary? compatibility mode for TBody
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSummaryAnchor(TypeElement typeElement, Content memberTree) {
        memberTree.addContent(links.createAnchor(
                SectionName.ANNOTATION_TYPE_FIELD_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritedSummaryAnchor(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInheritedSummaryLabel(TypeElement typeElement, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSummaryLink(LinkInfoImpl.Kind context, TypeElement typeElement, Element member,
            Content tdSummary) {
        Content memberLink = HtmlTree.SPAN(HtmlStyle.memberNameLink,
                writer.getDocLink(context, member, name(member), false));
        Content code = HtmlTree.CODE(memberLink);
        tdSummary.addContent(code);
    }

    /**
     * {@inheritDoc}
     */
    protected void addInheritedSummaryLink(TypeElement typeElement,
            Element member, Content linksTree) {
        //Not applicable.
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType(Element member, Content tdSummaryType) {
        addModifierAndType(member, getType(member), tdSummaryType);
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink(Element member) {
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER,
                member, utils.getFullyQualifiedName(member));
    }

    private TypeMirror getType(Element member) {
        if (utils.isConstructor(member))
            return null;
        if (utils.isExecutableElement(member))
            return utils.getReturnType((ExecutableElement)member);
        return member.asType();
    }
}
