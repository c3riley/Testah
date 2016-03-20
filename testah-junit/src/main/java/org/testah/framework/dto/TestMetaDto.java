package org.testah.framework.dto;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.enums.TestType;

public class TestMetaDto {

	private String uuid = "";
	private String name = "";
	private String description = "";
	private List<String> relatedLinks = new ArrayList<String>();
	private List<String> relatedIds = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private KnownProblemDto knownProblem = null;
	private TestType testType;
	private String source = null;
	private List<String> components = new ArrayList<String>();
	private List<String> runTypes = new ArrayList<String>();

	public TestMetaDto() {
		this(null);
	}

	public TestMetaDto(final TestMeta metadata) {
		fillFromTestMeta(metadata);
	}

	public TestMetaDto(final String name, final String description) {
		this.name = name;
		this.description = description;
	}

	public TestMetaDto fillFromTestMeta(final TestMeta metadata) {
		return fillFromTestMeta(metadata, null);
	}

	public TestMetaDto fillFromTestMeta(final TestMeta metadata, final KnownProblem knownProblem) {
		if (null != metadata) {
			this.setUuid(metadata.uuid());
			this.setName(metadata.name());
			this.setDescription(metadata.description());
			this.setRelatedIds(Arrays.asList(metadata.relatedIds()));
			this.setRelatedLinks(Arrays.asList(metadata.relatedLinks()));
			this.setTags(Arrays.asList(metadata.tags()));
			this.setTestType(metadata.testType());
			this.setComponents(Arrays.asList(metadata.components()));
			this.setRunTypes(Arrays.asList(metadata.runTypes()));
		}
		if (null != knownProblem) {
			setKnownProblem(knownProblem);
		}
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public TestMetaDto setUuid(final String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public TestMetaDto setName(final String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public TestMetaDto setDescription(final String description) {
		this.description = description;
		return this;
	}

	public List<String> getRelatedLinks() {
		return relatedLinks;
	}

	public TestMetaDto setRelatedLinks(final List<String> relatedLinks) {
		this.relatedLinks = relatedLinks;
		return this;
	}

	public List<String> getRelatedIds() {
		return relatedIds;
	}

	public TestMetaDto setRelatedIds(final List<String> relatedIds) {
		this.relatedIds = relatedIds;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public TestMetaDto setTags(final List<String> tags) {
		this.tags = tags;
		return this;
	}

	public TestType getTestType() {
		return testType;
	}

	public TestMetaDto setTestType(final TestType testType) {
		this.testType = testType;
		return this;
	}

	public KnownProblemDto getKnownProblem() {
		return knownProblem;
	}

	public TestMetaDto setKnownProblem(final KnownProblem knownProblem) {
		setKnownProblem(new KnownProblemDto(knownProblem));
		return this;
	}

	public TestMetaDto setKnownProblem(final KnownProblemDto knownProblem) {
		this.knownProblem = knownProblem;
		return this;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public List<String> getComponents() {
		return components;
	}

	public void setComponents(final List<String> components) {
		this.components = components;
	}

	public List<String> getRunTypes() {
		return runTypes;
	}

	public void setRunTypes(final List<String> runTypes) {
		this.runTypes = runTypes;
	}

}
