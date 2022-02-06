package lq2007.plugins.gradle_plugin.support;

import com.github.javaparser_new.*;
import com.github.javaparser_new.ast.CompilationUnit;
import com.github.javaparser_new.ast.ImportDeclaration;
import com.github.javaparser_new.ast.Node;
import com.github.javaparser_new.ast.NodeList;
import com.github.javaparser_new.ast.body.BodyDeclaration;
import com.github.javaparser_new.ast.body.TypeDeclaration;
import com.github.javaparser_new.ast.expr.*;
import com.github.javaparser_new.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser_new.ast.nodeTypes.NodeWithIdentifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class PluginHelper {
    private static final String ANN_GENERATED = "lq2007.plugins.gradle_plugin.support.Generated";

    private final Path srcPath;
    private final Path assetsPath;
    private final Path projectPath;
    private final Path logPath;
    private final Path classesPath;

    public PluginHelper(Path srcPath, Path assetsPath, Path projectPath, Path logPath, Path classesPath) {
        this.srcPath = srcPath;
        this.assetsPath = assetsPath;
        this.projectPath = projectPath;
        this.logPath = logPath;
        this.classesPath = classesPath;
    }

    public final JavaParser parser = new JavaParser(new ParserConfiguration()
            .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE)
            .setTabSize(4));

    /**
     * ABcDef -> A_BC_DEF
     *
     * @param name name like ABcDef
     * @return result like A_BC_DEF
     */
    public String toUpperName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(String.valueOf(Character.toUpperCase(name.charAt(0))));
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_').append(c);
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * ABcDef -> a_bc_def
     *
     * @param name name like ABcDef
     * @return result like a_bc_def
     */
    public String toLowerName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(String.valueOf(Character.toLowerCase(name.charAt(0))));
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * a_bc_def/A_BC_DEF ->  ABcDef / aBcDef
     *
     * @param name       name like a_bc_def or A_BC_DEF
     * @param lowerFirst true if the first char is lower case
     * @return result like ABcDef or aBcDef
     */
    public String toCamelCase(String name, boolean lowerFirst) {
        StringBuilder sb = new StringBuilder();
        boolean first = false;
        for (String s : name.split("_")) {
            if (s.isEmpty()) continue;
            String s1 = s.toLowerCase(Locale.ROOT);
            if (lowerFirst && !first) {
                first = true;
                sb.append(s1);
            } else {
                sb.append(Character.toUpperCase(s1.charAt(0)));
                sb.append(s1.substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * parse fully qualified type name
     *
     * @param name   simple name
     * @param parent type, used for search inner class
     * @param unit   unit, used for search imports
     * @return fully qualified type name
     */
    public String getFullyName(NodeWithIdentifier<?> name, @Nullable TypeDeclaration<?> parent, CompilationUnit unit) {
        if (name instanceof Name n) {
            return getFullyName(n.asString(), parent, unit);
        } else {
            return getFullyName(name.getIdentifier(), parent, unit);
        }
    }

    /**
     * parse fully qualified type name
     *
     * @param name   simple name
     * @param parent type, used for search inner class
     * @param unit   unit, used for search imports
     * @return fully qualified type name
     */
    public String getFullyName(String name, @Nullable TypeDeclaration<?> parent, CompilationUnit unit) {
        int i = name.indexOf(".");
        if (i > 0) {
            return getFullyName(name.substring(0, i), parent, unit) + name.substring(i);
        } else {
            // 1 imports
            for (ImportDeclaration anImport : unit.getImports()) {
                if (name.equals(anImport.getName().getIdentifier())) {
                    return anImport.getNameAsString();
                }
            }
            // 2 inner class
            if (parent != null) {
                for (BodyDeclaration<?> member : parent.getMembers()) {
                    if (member instanceof TypeDeclaration<?> mt) {
                        if (name.equals(mt.getNameAsString())) {
                            LinkedList<String> list = new LinkedList<>();
                            list.add(name);
                            Optional<Node> node = member.getParentNode();
                            while (node.isPresent() && node.get() instanceof TypeDeclaration<?> nt) {
                                list.addFirst(nt.getNameAsString());
                                node = nt.getParentNode();
                            }
                            String fully = String.join(".", list);
                            return unit.getPackageDeclaration()
                                    .map(declaration -> declaration.getNameAsString() + "." + fully)
                                    .orElse(fully);
                        }
                    }
                }
            }
            // 3 other type
            if (unit.getTypes().stream().anyMatch(n -> name.equals(n.getNameAsString()))) {
                return unit.getPackageDeclaration()
                        .map(declaration -> declaration.getNameAsString() + "." + name)
                        .orElse(name);
            }
        }
        return unit.getPackageDeclaration()
                .map(declaration -> declaration.getNameAsString() + "." + name)
                .orElse(name);
    }

    /**
     * Build an ast tree from file
     *
     * @param file path to a file containing Java source code
     * @return CompilationUnit representing the Java source code
     * @throws ParseProblemException    if the source code has parser errors
     * @throws IllegalArgumentException if the result contains problems
     * @throws IOException              the path could not be accessed
     */
    public CompilationUnit buildAST(Path file) throws IOException {
        ParseResult<CompilationUnit> parse = parser.parse(file);
        return parse.getResult().orElseThrow(() -> {
            StringBuilder sb = new StringBuilder("Can't parse ").append(file).append('\n');
            for (Problem problem : parse.getProblems()) {
                sb.append(problem).append('\n');
            }
            return new IllegalArgumentException(sb.toString());
        });
    }

    /**
     * Save the new java source to file
     *
     * @param file   output file
     * @param backup true while need backup (file need existed)
     * @param unit   new java unit
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(Path file, boolean backup, CompilationUnit unit) throws IOException {
        if (backup && Files.isRegularFile(file)) {
            Path path2 = file.getParent().resolve(file.getFileName().toString() + ".bak");
            Files.copy(file, path2, StandardCopyOption.REPLACE_EXISTING);
        }

        if (!Files.isRegularFile(file)) {
            Files.createFile(file);
        }
        Files.write(file, unit.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Save the new java source to stream
     *
     * @param os   output stream
     * @param unit new java unit
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(OutputStream os, CompilationUnit unit) throws IOException {
        os.write(unit.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Save the new java source to writer
     *
     * @param writer output writer
     * @param unit   new java unit
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(Writer writer, CompilationUnit unit) throws IOException {
        writer.write(unit.toString());
    }

    /**
     * get annotation's default value
     */
    public Optional<Expression> getAnnotationDefaultValue(AnnotationExpr annotation) {
        if (annotation instanceof SingleMemberAnnotationExpr sa) {
            return Optional.of(sa.getMemberValue());
        } else if (annotation instanceof NormalAnnotationExpr na) {
            for (MemberValuePair pair : na.getPairs()) {
                if ("value".equals(pair.getName().getIdentifier())) {
                    return Optional.of(pair.getValue());
                }
            }
        }
        return Optional.empty();
    }

    /**
     * get all annotation's value, default value's key is "value"
     */
    public Map<String, Expression> getAnnotationValues(AnnotationExpr annotation) {
        if (annotation instanceof SingleMemberAnnotationExpr sa) {
            return Map.of("value", sa.getMemberValue());
        } else if (annotation instanceof NormalAnnotationExpr na) {
            Map<String, Expression> map = new HashMap<>();
            for (MemberValuePair pair : na.getPairs()) {
                map.put(pair.getNameAsString(), pair.getValue());
            }
            return Collections.unmodifiableMap(map);
        }
        return Map.of();
    }

    /**
     * search all annotations by type
     *
     * @param qualifier target annotation's fully qualified name
     * @param node      annotated node
     * @param parent    parent, used for get fully annotation name
     * @param unit      unit, used for get fully annotation name
     * @return all annotations
     */
    public Stream<AnnotationExpr> getAnnotations(String qualifier, NodeWithAnnotations<?> node, @Nullable TypeDeclaration<?> parent, CompilationUnit unit) {
        return node.getAnnotations().stream().filter(a -> qualifier.equals(getFullyName(a.getName(), parent, unit)));
    }

    /**
     * search all annotations by type
     *
     * @param qualifier target annotation's fully qualified name
     * @param node      annotated node
     * @param unit      unit, used for get fully annotation name
     * @return all annotations
     */
    public Stream<AnnotationExpr> getAnnotations(String qualifier, NodeWithAnnotations<?> node, CompilationUnit unit) {
        return getAnnotations(qualifier, node, node instanceof TypeDeclaration<?> n ? n : null, unit);
    }

    /**
     * check if the node has annotation
     *
     * @param qualifier target annotation's fully qualified name
     * @param node      annotated node
     * @param parent    parent, used for get fully annotation name
     * @param unit      unit, used for get fully annotation name
     * @return true if contains special annotation
     */
    public boolean hasAnnotation(String qualifier, NodeWithAnnotations<?> node, @Nullable TypeDeclaration<?> parent, CompilationUnit unit) {
        return getAnnotations(qualifier, node, parent, unit).findAny().isPresent();
    }

    /**
     * check if the node has annotation
     *
     * @param qualifier target annotation's fully qualified name
     * @param node      annotated node
     * @param unit      unit, used for get fully annotation name
     * @return true if contains special annotation
     */
    public boolean hasAnnotation(String qualifier, TypeDeclaration<?> node, CompilationUnit unit) {
        return getAnnotations(qualifier, node, unit).findAny().isPresent();
    }

    /**
     * get generated information
     *
     * @param node   annotated node
     * @param parent parent, used for get fully annotation name
     * @param unit   unit, used for get fully annotation name
     * @return information
     */
    public Optional<GeneratedEntry> getGeneratedInformation(NodeWithAnnotations<?> node, @Nullable TypeDeclaration<?> parent, CompilationUnit unit) {
        return getAnnotations(ANN_GENERATED, node, parent, unit)
                .map(a -> (NormalAnnotationExpr) a)
                .map(a -> {
                    String generator = "[unknown]";
                    int version = -1;
                    List<String> parameters = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    Consumer<String> addParameter = s -> {
                        parameters.add(s);
                        int eq = s.lastIndexOf("=");
                        if (eq > 0) {
                            map.put(s.substring(0, eq), s.substring(eq + 1));
                        }
                    };
                    for (MemberValuePair pair : a.getPairs()) {
                        switch (pair.getName().getIdentifier()) {
                            case "generator" -> generator = pair.getValue().asStringLiteralExpr().asString();
                            case "version" -> version = pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                            case "parameters" -> {
                                Expression value = pair.getValue();
                                if (value instanceof StringLiteralExpr sv) {
                                    addParameter.accept(sv.asString());
                                } else if (value instanceof ArrayInitializerExpr av) {
                                    av.getValues().stream()
                                            .map(Expression::asStringLiteralExpr)
                                            .map(StringLiteralExpr::asString)
                                            .forEach(addParameter);
                                }
                            }
                        }
                    }
                    return new GeneratedEntry(generator, version, parameters, map);
                })
                .findFirst();
    }

    /**
     * add generated annotation to node
     * @param generator generator
     * @param version version
     * @param parameters params
     * @param node generated node
     * @param unit unit
     */
    public void addGeneratedInformation(String generator, int version, List<String> parameters, NodeWithAnnotations<?> node, CompilationUnit unit) {
        NodeList<MemberValuePair> pairs = new NodeList<>();
        pairs.add(new MemberValuePair("generator", new StringLiteralExpr(generator)));
        pairs.add(new MemberValuePair("version", new IntegerLiteralExpr(version + "")));
        NodeList<Expression> paramStrings = new NodeList<>();
        parameters.forEach(s -> paramStrings.add(new StringLiteralExpr(s)));
        ArrayInitializerExpr params = new ArrayInitializerExpr(paramStrings);
        pairs.add(new MemberValuePair("parameters", params));
        NormalAnnotationExpr expr = new NormalAnnotationExpr(new Name("Generated"), pairs);
        NodeList<ImportDeclaration> imports = unit.getImports();
        if (imports.stream().noneMatch(i -> ANN_GENERATED.equals(i.getNameAsString()))) {
            imports.add(new ImportDeclaration(ANN_GENERATED, false, false));
        }
        node.addAnnotation(expr);
    }

    /**
     * add generated annotation to node
     * @param generator generator
     * @param version version
     * @param parameters params
     * @param node generated node
     * @param unit unit
     */
    public void addGeneratedInformation(ISourcePlugin generator, int version, Map<String, String> parameters, NodeWithAnnotations<?> node, CompilationUnit unit) {
        addGeneratedInformation(generator.getClass().getName(), version,
                parameters.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).toList(), node, unit);
    }

    /**
     * return the source path
     *
     * @return source path
     */
    public Path srcPath() {
        return srcPath;
    }

    /**
     * return the resources/assets path
     *
     * @return assets path
     */
    public Path assetsPath() {
        return assetsPath;
    }

    /**
     * return the mod gradle project path
     *
     * @return project path
     */
    public Path projectPath() {
        return projectPath;
    }

    /**
     * return the log file path
     *
     * @return log file path
     */
    public Path logPath() {
        return logPath;
    }

    /**
     * return the loop classes output directory
     *
     * @return build classes output
     */
    public Path classesPath() {
        return classesPath;
    }
}
