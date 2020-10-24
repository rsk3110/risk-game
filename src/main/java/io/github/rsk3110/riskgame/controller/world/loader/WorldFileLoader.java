package io.github.rsk3110.riskgame.controller.world.loader;

import io.github.rsk3110.riskgame.Continent;
import io.github.rsk3110.riskgame.Territory;
import io.github.rsk3110.riskgame.World;
import io.github.rsk3110.riskgame.TerritoryEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.GraphImporter;
import org.jgrapht.nio.graphml.GraphMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class WorldFileLoader implements WorldLoader {
    private static final String FILE_EXT = "xml";

    private final DocumentBuilder levelParser;
    private final Transformer levelTransformer;
    private final XPathFactory xPathFactory;

    private final Path levelDir;

    public WorldFileLoader(final Path levelDir) {
        if (!Files.exists(levelDir)) throw new IllegalArgumentException(String.format("directory '%s' does not exist", levelDir));
        this.levelDir = levelDir;

        this.levelParser = createXMLReader();
        this.levelTransformer = createXMLTransformer();
        this.xPathFactory = XPathFactory.newInstance();
    }

    private static GraphImporter<Territory, TerritoryEdge> createGraphMLImporter(final List<Continent> continents) {
        final GraphMLImporter<Territory, TerritoryEdge> importer = new GraphMLImporter<>();

        final Map<String, Continent> continentNameMap = continents.stream()
                .collect(Collectors.toMap(Continent::getName, Function.identity()));

        importer.addVertexAttributeConsumer((p, attrValue) -> {
            final Territory v = p.getFirst();
            final String attrName = p.getSecond();

            switch (attrName) {
                case "ID":
                    final String id = attrValue.getValue();
                    v.setName(id);
                    break;
                case "continent":
                    final String continentName = attrValue.getValue();
                    if (!continentNameMap.containsKey(continentName)) {
                        throw new LevelLoadException(String.format("continent '%s' not defined in world", continentName));
                    }
                    final Continent continent = continentNameMap.get(continentName);
                    v.setContinent(continent);
                    break;
            }
        });

        return importer;
    }

    private static DocumentBuilder createXMLReader() {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            return dbf.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new LevelLoadException(e);
        }
    }

    private static Transformer createXMLTransformer() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            return transformerFactory.newTransformer();
        } catch (final TransformerConfigurationException e) {
            throw new LevelLoadException(e);
        }
    }

    private Document parseLevelData(final Reader levelData) {
        try {
            return levelParser.parse(new InputSource(levelData));
        } catch (final SAXException | IOException e) {
            throw new LevelLoadException(e);
        }
    }

    private Reader readLevelFile(final String name) {
        final Path levelPath = this.levelDir.resolve(String.format("%s.%s", name, FILE_EXT));
        try {
            return Files.newBufferedReader(levelPath);
        } catch (IOException e) {
            throw new LevelLoadException(e);
        }
    }

    private <T> T queryXML(final String expr, final Object item, final Class<T> clazz) {
        QName returnType;
        if (clazz.equals(Number.class)) returnType = XPathConstants.NUMBER;
        else if  (clazz.equals(String.class)) returnType = XPathConstants.STRING;
        else if  (clazz.equals(Boolean.class)) returnType = XPathConstants.BOOLEAN;
        else if  (clazz.equals(NodeList.class)) returnType = XPathConstants.NODESET;
        else if  (clazz.equals(Node.class)) returnType = XPathConstants.NODE;
        else throw new IllegalArgumentException("Unsupported query result type '" + clazz.getSimpleName() + "'");

        try {
            return clazz.cast(this.xPathFactory.newXPath().evaluate(expr, item, returnType));
        } catch (final XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Continent parseContinentFromNode(final Node continentNode) {
        final String name = this.queryXML("name", continentNode, String.class);
        final String hexColor = this.queryXML("color", continentNode, String.class);
        final Number armies = this.queryXML("armies", continentNode, Number.class);
        return new Continent(name, new Color(Integer.decode(hexColor)), armies.intValue());
    }

    private List<Continent> parseLevelContinents(final Document levelDoc) {
        final NodeList continentNodes = this.queryXML("world/continents/continent", levelDoc, NodeList.class);
        return IntStream.range(0, continentNodes.getLength())
                .mapToObj(continentNodes::item)
                .map(this::parseContinentFromNode)
                .collect(Collectors.toList());
    }

    private InputStream getLevelLayoutInputStream(final Document levelDoc) {
        final Node graphmlNode = this.queryXML("world/layout/graphml", levelDoc, Node.class);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            levelTransformer.transform(new DOMSource(graphmlNode), new StreamResult(outputStream));
        } catch (final TransformerException e) {
            throw new LevelLoadException(e);
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private Graph<Territory, TerritoryEdge> parseLevelLayout(
            final Document levelDoc,
            final GraphImporter<Territory, TerritoryEdge> graphmlImporter) {
        final Graph<Territory, TerritoryEdge> levelGraph = GraphTypeBuilder
                .undirected()
                .allowingMultipleEdges(false).allowingSelfLoops(false)
                .vertexSupplier(new Supplier<Territory>() {
                    private int id = 0;

                    @Override
                    public Territory get() {
                        return new Territory(this.id++);
                    }
                })
                .edgeSupplier(TerritoryEdge::new)
                .buildGraph();

        graphmlImporter.importGraph(levelGraph, getLevelLayoutInputStream(levelDoc));

        return levelGraph;
    }

    @Override
    public World load(final String name) {
        final Document levelDoc = this.parseLevelData(this.readLevelFile(name));

        final List<Continent> levelContinents = this.parseLevelContinents(levelDoc);
        final Graph<Territory, TerritoryEdge> levelGraph = this.parseLevelLayout(levelDoc, createGraphMLImporter(levelContinents));

        return new World(levelGraph, levelContinents);
    }
}
