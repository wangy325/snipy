// 检查是否是图标字体的网页
const isIconFontPage = () => {
  const styles = Array.from(document.styleSheets);
  return styles.some(styleSheet => {
    try {
      const rules = Array.from(styleSheet.cssRules || []);
      return rules.some(rule => rule.cssText.includes('font-family') && rule.cssText.includes('FontAwesome'));
    } catch (e) {
      return false; // 处理跨域样式表的异常
    }
  });
};

const style = document.createElement('style');
if (!isIconFontPage()) {
  // 没有默认字体的网页，强制所有元素使用浏览器默认字体
  style.textContent = `
      * {
          font-family: inherit !important; /* 使用浏览器默认字体 */
          font-display: swap
      }
  `;
} else {
  style.textContent = `
    body, p, div, span, h1, h2, h3, h4, h5, h6 {
        font-family: inherit !important; /* 使用浏览器默认字体 */
    }
    /* 保留网页的图标字体 */
    @font-face {
        font-family: 'FontAwesome'; /* 将 'CustomFont' 替换为你想保留的图标字体名称 */
        font-family: 'FontAwesome', 'Glyphicons Halflings', 'Material Icons', 'sans-serif' !important;
        src: local('FontAwesome');  /* 替换为实际的图标字体文件名 */
        font-display: swap;
    }
  `;
}
// 将样式元素添加到文档头部
document.head.appendChild(style);
