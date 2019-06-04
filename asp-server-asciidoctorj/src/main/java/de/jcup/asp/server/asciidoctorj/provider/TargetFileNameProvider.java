package de.jcup.asp.server.asciidoctorj.provider;

import java.io.File;

import de.jcup.asp.api.Backend;

public class TargetFileNameProvider {

    public File resolveTargetFileFor(File file, Backend backend) {
        String tartgetFileName = file.getName();
        int index = tartgetFileName.lastIndexOf('.');
        if (index != -1) {
            tartgetFileName = tartgetFileName.substring(0, index);
        }
        tartgetFileName += "." + backend.getFileNameEnding();
        return new File(file.getParent(), tartgetFileName);
    }

}
