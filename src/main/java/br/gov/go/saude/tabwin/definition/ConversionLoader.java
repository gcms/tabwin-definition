package br.gov.go.saude.tabwin.definition;

public interface ConversionLoader {
    ConversionFile loadFile(String name);

    default CategoryMapping loadMapping(Dimension dimension) {
        ConversionFile file = loadFile(dimension.getConversionFile());
        return file == null ? null : file.createMapping(dimension);
    }

    default DimensionConversor loadConversor(Dimension dimension) {
        ConversionFile file = loadFile(dimension.getConversionFile());
        return file == null ? null : file.createConversor(dimension);
    }
}
