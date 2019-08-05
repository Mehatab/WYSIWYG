package me.saket.markdownrenderer.flexmark.stylers

import com.vladsch.flexmark.ast.ListBlock
import me.saket.markdownrenderer.SpanWriter
import me.saket.markdownrenderer.WysiwygTheme
import me.saket.markdownrenderer.flexmark.NodeVisitor
import me.saket.markdownrenderer.spans.ParagraphLeadingMarginSpan
import me.saket.markdownrenderer.spans.pool.SpanPool

class ListBlockVisitor : NodeVisitor<ListBlock> {

  override fun visit(
    node: ListBlock,
    pool: SpanPool,
    writer: SpanWriter,
    theme: WysiwygTheme
  ) {
    writer.add(
        pool.leadingMargin(theme.listBlockIndentationMargin), node.startOffset, node.endOffset
    )
  }

  private fun SpanPool.leadingMargin(margin: Int) =
    get { ParagraphLeadingMarginSpan(recycler) }.apply {
      this.margin = margin
    }
}