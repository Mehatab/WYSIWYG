package me.saket.markdownrenderer.flexmark.stylers

import com.vladsch.flexmark.ast.Heading
import me.saket.markdownrenderer.SpanWriter
import me.saket.markdownrenderer.WysiwygTheme
import me.saket.markdownrenderer.flexmark.FlexmarkNodeTreeVisitor
import me.saket.markdownrenderer.flexmark.FlexmarkSyntaxStyler
import me.saket.markdownrenderer.flexmark.NodeVisitor
import me.saket.markdownrenderer.spans.pool.SpanPool

@Suppress("SpellCheckingInspection")
class HeadingStyler : FlexmarkSyntaxStyler<Heading> {

  override fun visitor(node: Heading): NodeVisitor<Heading>? {
    // Setext styles aren't supported. Setext-style headers are "underlined" using "="
    // (for first-level headers) and dashes (for second-level headers). For example:
    // This is an H1
    // =============
    //
    // This is an H2
    // -------------
    return when {
      node.isAtxHeading -> headingVisitor()
      else -> null
    }
  }

  private fun headingVisitor() = object : NodeVisitor<Heading> {
    override fun visit(
      node: Heading,
      pool: SpanPool,
      writer: SpanWriter,
      theme: WysiwygTheme,
      parentVisitor: FlexmarkNodeTreeVisitor
    ) {
      writer.add(pool.heading(node.level, theme.markwonTheme), node.startOffset, node.endOffset)
      writer.add(
          pool.foregroundColor(theme.syntaxColor),
          node.startOffset,
          node.startOffset + node.openingMarker.length
      )
      parentVisitor.visitChildren(node, writer)
    }
  }
}