package me.saket.markdownrenderer.flexmark.stylers

import com.vladsch.flexmark.ast.ListItem
import me.saket.markdownrenderer.SpanWriter
import me.saket.markdownrenderer.WysiwygTheme
import me.saket.markdownrenderer.flexmark.SimpleFlexmarkSyntaxStyler
import me.saket.markdownrenderer.spans.pool.SpanPool

class ListItemStyler : SimpleFlexmarkSyntaxStyler<ListItem>() {

  override fun visit(
    node: ListItem,
    pool: SpanPool,
    writer: SpanWriter,
    theme: WysiwygTheme
  ) {
    writer.add(pool.foregroundColor(theme.syntaxColor), node.startOffset, node.startOffset + 1)
  }
}
