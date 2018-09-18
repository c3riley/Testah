package org.testah.framework.dto;

import org.junit.runner.Description;
import org.testah.TS;
import org.testah.client.dto.KnownProblemDto;
import org.testah.client.dto.RunInfoDto;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

import java.util.Arrays;

/**
 * The Class TestDtoHelper.
 */
public class TestDtoHelper {

    /**
     * Creates the test plan dto.
     *
     * @param desc                 the desc
     * @param meta                 the meta
     * @param knownProblemFillFrom the known problem fill from
     * @return the test plan dto
     */
    public static TestPlanDto createTestPlanDto(final Description desc, final TestPlan meta,
                                                final KnownProblem knownProblemFillFrom) {
        TestPlanDto testPlanToFill = new TestPlanDto();
        if (null == meta || null == meta.name() || meta.name().length() == 0) {
            testPlanToFill.setName(desc.getClassName());
        } else {
            testPlanToFill.setName(meta.name());
        }
        testPlanToFill.setSource(desc.getTestClass().getCanonicalName());
        if (null != meta) {
            testPlanToFill = fill(testPlanToFill, meta, knownProblemFillFrom);
        }
        return testPlanToFill;
    }


    /**
     * Creates the test plan dto.
     *
     * @param testPlanClass        the test plan class
     * @param meta                 the meta
     * @param knownProblemFillFrom the known problem fill from
     * @return the test plan dto
     */
    public static TestPlanDto createTestPlanDto(final Class<?> testPlanClass, final TestPlan meta,
                                                final KnownProblem knownProblemFillFrom) {
        TestPlanDto testPlanToFill = new TestPlanDto();
        if (null == meta || null == meta.name() || meta.name().length() == 0) {
            testPlanToFill.setName(testPlanClass.getName());
        } else {
            testPlanToFill.setName(meta.name());
        }
        testPlanToFill.setSource(testPlanClass.getCanonicalName());
        if (null != meta) {
            testPlanToFill = fill(testPlanToFill, meta, knownProblemFillFrom);
        }
        return testPlanToFill;
    }

    /**
     * Fill.
     *
     * @param testPlanToFill       the test plan to fill
     * @param fillFromTestPlan     the fill from test plan
     * @param knownProblemFillFrom the known problem fill from
     * @return the test plan dto
     */
    public static TestPlanDto fill(TestPlanDto testPlanToFill, final TestPlan fillFromTestPlan,
                                   final KnownProblem knownProblemFillFrom) {
        if (null == testPlanToFill) {
            testPlanToFill = new TestPlanDto();
        }
        if (null != fillFromTestPlan) {
            testPlanToFill.setId(fillFromTestPlan.id());
            testPlanToFill.setDescription(fillFromTestPlan.description());
            testPlanToFill.setRelatedIds(Arrays.asList(fillFromTestPlan.relatedIds()));
            testPlanToFill.setRelatedLinks(Arrays.asList(fillFromTestPlan.relatedLinks()));
            testPlanToFill.setTags(Arrays.asList(fillFromTestPlan.tags()));
            testPlanToFill.setTestType(fillFromTestPlan.testType());
            testPlanToFill.setComponents(Arrays.asList(fillFromTestPlan.components()));
            testPlanToFill.setRunTypes(Arrays.asList(fillFromTestPlan.runTypes()));
            testPlanToFill.setDevices(Arrays.asList(fillFromTestPlan.devices()));
            testPlanToFill.setPlatforms(Arrays.asList(fillFromTestPlan.platforms()));
            testPlanToFill.setOwner(fillFromTestPlan.owner());
        }
        if (null != knownProblemFillFrom) {
            testPlanToFill.setKnownProblem(convertKnownProblemAnnToDto(knownProblemFillFrom));
        }
        return testPlanToFill;
    }

    /**
     * Fill.
     *
     * @param testCaseToFill       the test case to fill
     * @param fillFromTestCase     the fill from test case
     * @param knownProblemFillFrom the known problem fill from
     * @param tpMeta               the tp meta
     * @return the test case dto
     */
    public static TestCaseDto fill(TestCaseDto testCaseToFill, final TestCase fillFromTestCase,
                                   final KnownProblem knownProblemFillFrom, final TestPlan tpMeta) {
        if (null == testCaseToFill) {
            testCaseToFill = new TestCaseDto();
        }
        if (null != testCaseToFill && null != fillFromTestCase) {
            testCaseToFill.setId(fillFromTestCase.id());

            if (null != tpMeta && fillFromTestCase.components().length == 0) {
                testCaseToFill.setComponents(Arrays.asList(tpMeta.components()));
            } else {
                testCaseToFill.setComponents(Arrays.asList(fillFromTestCase.components()));
            }

            if (null != tpMeta && fillFromTestCase.relatedLinks().length == 0) {
                testCaseToFill.setRelatedLinks(Arrays.asList(tpMeta.relatedLinks()));
            } else {
                testCaseToFill.setRelatedLinks(Arrays.asList(fillFromTestCase.relatedLinks()));
            }

            if (null != tpMeta && fillFromTestCase.relatedIds().length == 0) {
                testCaseToFill.setRelatedIds(Arrays.asList(tpMeta.relatedIds()));
            } else {
                testCaseToFill.setRelatedIds(Arrays.asList(fillFromTestCase.relatedIds()));
            }

            if (null != tpMeta && fillFromTestCase.devices().length == 0) {
                testCaseToFill.setDevices(Arrays.asList(tpMeta.devices()));
            } else {
                testCaseToFill.setDevices(Arrays.asList(fillFromTestCase.devices()));
            }

            if (null != tpMeta && fillFromTestCase.platforms().length == 0) {
                testCaseToFill.setPlatforms(Arrays.asList(tpMeta.platforms()));
            } else {
                testCaseToFill.setPlatforms(Arrays.asList(fillFromTestCase.platforms()));
            }

            if (null != tpMeta && fillFromTestCase.runTypes().length == 0) {
                testCaseToFill.setRunTypes(Arrays.asList(tpMeta.runTypes()));
            } else {
                testCaseToFill.setRunTypes(Arrays.asList(fillFromTestCase.runTypes()));
            }

            if (null != tpMeta && fillFromTestCase.tags().length == 0) {
                testCaseToFill.setTags(Arrays.asList(tpMeta.tags()));
            } else {
                testCaseToFill.setTags(Arrays.asList(fillFromTestCase.tags()));
            }

            if (null != tpMeta && fillFromTestCase.testType() == TestType.DEFAULT) {
                testCaseToFill.setTestType(tpMeta.testType());
            } else {
                testCaseToFill.setTestType(fillFromTestCase.testType());
            }

            testCaseToFill.setDescription(fillFromTestCase.description());

        }
        if (null != knownProblemFillFrom) {
            testCaseToFill.setKnownProblem(convertKnownProblemAnnToDto(knownProblemFillFrom));
        }
        return testCaseToFill;
    }

    /**
     * Fill.
     *
     * @param runInfo the run info
     * @return the run info dto
     */
    public static RunInfoDto fill(RunInfoDto runInfo) {
        if (null == runInfo) {
            runInfo = new RunInfoDto();
        }
        runInfo.setRunId(System.getProperty("testah.runId", TS.params().getRunInfo_runId()));
        runInfo.setRunLocation(System.getProperty("testah.runLocation", TS.params().getRunLocation()));
        runInfo.setBuildNumber(System.getProperty("testah.buildNumber", TS.params().getRunInfo_buildNumber()));
        runInfo.setRunType(System.getProperty("testah.runType", TS.params().getRunType()));
        runInfo.setVersionId(System.getProperty("testah.versionId", TS.params().getRunInfo_versionId()));
        return runInfo;

    }

    /**
     * Convert known problem ann to dto.
     *
     * @param knownProblem the known problem
     * @return the known problem dto
     */
    public static KnownProblemDto convertKnownProblemAnnToDto(final KnownProblem knownProblem) {
        final KnownProblemDto knownProblemDto = new org.testah.client.dto.KnownProblemDto();
        knownProblemDto.setLinkedIds(Arrays.asList(knownProblem.linkedIds()));
        knownProblemDto.setDescription(knownProblem.description());
        knownProblemDto.setTypeOfKnown(knownProblem.typeOfKnown());
        return knownProblemDto;
    }

    /**
     * Creates the test case dto.
     *
     * @param desc                 the desc
     * @param meta                 the meta
     * @param knownProblemFillFrom the known problem fill from
     * @param tpMeta               the tp meta
     * @return the test case dto
     */
    public static TestCaseDto createTestCaseDto(final Description desc, final TestCase meta,
                                                final KnownProblem knownProblemFillFrom, final TestPlan tpMeta) {
        TestCaseDto testCaseToFill = new TestCaseDto();
        if (null == meta || null == meta.name() || meta.name().length() == 0) {
            testCaseToFill.setName(desc.getMethodName());
        } else {
            testCaseToFill.setName(meta.name());
        }
        testCaseToFill.setSource(desc.getTestClass().getCanonicalName() + "#" + desc.getMethodName());
        if (null != meta) {
            testCaseToFill = fill(testCaseToFill, meta, knownProblemFillFrom, tpMeta);
        }
        return testCaseToFill;
    }

    /**
     * Creates the test case dto.
     *
     * @param testPlanClass        the test plan class
     * @param testCaseMethod       the test case method
     * @param meta                 the meta
     * @param knownProblemFillFrom the known problem fill from
     * @param tpMeta               the tp meta
     * @return the test case dto
     */
    public static TestCaseDto createTestCaseDto(final String testPlanClass, final String testCaseMethod,
                                                final TestCase meta, final KnownProblem knownProblemFillFrom, final TestPlan tpMeta) {
        TestCaseDto testCaseToFill = new TestCaseDto();
        if (null == meta || null == meta.name() || meta.name().length() == 0) {
            testCaseToFill.setName(testCaseMethod);
        } else {
            testCaseToFill.setName(meta.name());
        }
        testCaseToFill.setSource(testPlanClass + "#" + testCaseMethod);
        if (null != meta) {
            testCaseToFill = fill(testCaseToFill, meta, knownProblemFillFrom, tpMeta);
        }
        return testCaseToFill;
    }

    /**
     * Creates the run info.
     *
     * @return the run info dto
     */
    public static RunInfoDto createRunInfo() {
        return fill(new RunInfoDto());
    }

}
