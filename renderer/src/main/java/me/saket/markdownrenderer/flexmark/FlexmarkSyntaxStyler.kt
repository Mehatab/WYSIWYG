package me.saket.markdownrenderer.flexmark

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.parser.Parser
import me.saket.markdownrenderer.SpanWriter
import me.saket.markdownrenderer.WysiwygTheme
import me.saket.markdownrenderer.spans.pool.SpanPool

interface FlexmarkSyntaxStyler<in T : Node> {
  fun visitor(node: T): NodeVisitor<T>?
  fun buildParser(): Parser = TODO()
}

interface NodeVisitor<in T : Node> {
  fun visit(
    node: T,
    pool: SpanPool,
    writer: SpanWriter,
    theme: WysiwygTheme
  )

  fun visitWithChildren(
    node: T,
    pool: SpanPool,
    writer: SpanWriter,
    theme: WysiwygTheme,
    parentVisitor: FlexmarkNodeTreeVisitor
  ) {
    visit(node, pool, writer, theme)
    parentVisitor.visitChildren(node, writer)
  }

  companion object {
    val EMPTY = object : NodeVisitor<Node> {
      override fun visit(
        node: Node,
        pool: SpanPool,
        writer: SpanWriter,
        theme: WysiwygTheme
      ) = Unit
    }
  }
}
