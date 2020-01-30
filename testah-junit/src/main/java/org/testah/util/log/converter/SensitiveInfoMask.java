package org.testah.util.log.converter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.testah.TS;

@Plugin(name = "SensitiveInfoMaskConverter", category = "Converter")
@ConverterKeys({ "sdmsg" })
public class SensitiveInfoMask extends LogEventPatternConverter
{
    protected SensitiveInfoMask(String name, String style) {
        super(name, style);
    }

    /**
     * Obtains an instance of SequencePatternConverter.
     *
     * @param options options, currently ignored, may be null.
     * @return instance of SequencePatternConverter.
     */
    public static SensitiveInfoMask newInstance(String[] options) {
        return new SensitiveInfoMask("sdmsg", "sdmsg");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        toAppendTo.append(TS.sanitizeString(event.getMessage().getFormattedMessage()));
    }
}
