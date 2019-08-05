package me.saket.markdownrenderer.flexmark

import android.text.Spannable
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.sequence.SubSequence
import me.saket.markdownrenderer.MarkdownParser
import me.saket.markdownrenderer.SpanWriter
import me.saket.markdownrenderer.WysiwygTheme
import me.saket.markdownrenderer.spans.WysiwygSpan
import me.saket.markdownrenderer.spans.pool.SpanPool

/**
 * Usage:
 * FlexmarkMarkdownParser(WysiwygTheme, MarkdownSpanPool)
 */
class FlexmarkMarkdownParser(theme: WysiwygTheme) : MarkdownParser {

  private val pool: SpanPool = SpanPool(theme)
  private val syntaxStylers: FlexmarkSyntaxStylers = FlexmarkSyntaxStylers()
  private val markdownNodeTreeVisitor = FlexmarkNodeTreeVisitor(syntaxStylers, pool)
  private val parser: Parser = syntaxStylers.buildParser()

  @WorkerThread
  override fun parseSpans(text: Spannable): SpanWriter {
    // Instead of creating immutable CharSequences, Flexmark uses SubSequence that
    // maintains a mutable text and changes its visible window whenever a new
    // text is required to reduce object creation. SubSequence.of() internally skips
    // creating a new object if the text is already a SubSequence. This leads to bugs
    // that are hard to track. For instance, try adding a new line break at the end
    // and then delete it. It'll result in a crash because ThematicBreakSpan keeps
    // a reference to the mutable text. When the underlying text is trimmed, the
    // bounds (start, end) become larger than the actual text.
    val immutableText = SubSequence.of(text.toString())

    val spanWriter = SpanWriter()
    val markdownRootNode = parser.parse(immutableText)
    markdownNodeTreeVisitor.visit(markdownRootNode, spanWriter)
    return spanWriter
  }

  /**
   * Called on every text change so that stale spans can
   * be removed before applying new ones.
   */
  @UiThread
  override fun removeSpans(text: Spannable) {
    val spans = text.getSpans(0, text.length, Any::class.java)
    for (span in spans) {
      if (span is WysiwygSpan) {
        text.removeSpan(span)
        span.recycle()
      }
    }
  }
}
