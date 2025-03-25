// 检查是否是图标字体的网页
const isIconFontPage = () => {
  const styles = Array.from(document.styleSheets);
  return styles.some(styleSheet => {
    try {
      const rules = Array.from(styleSheet.cssRules || []);
      return rules.some(rule => rule.cssText.includes('font-family'));
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
          font-family:  inherit !important; /* 使用浏览器默认字体 */
      }

  `;
} else {
  style.textContent = `
    body, p, li, div, span, h1, h2, h3, h4, h5, h6 {
        font-family: 'inherit', 'Google Sans' !important; 
    }

    /* Note: If page uses traditional chinese, characters seem too thin, that doesn't look nice.
        So, we need to set font-weight to 600 for traditional chinese pages.
        But this is not perfect, because it doesn't work for all pages.
        Cause some pages set font-weight to 400, so this style can not override it.
    */
    html[lang^="zh-Hant"], html[lang^="zh-hant"], html[lang^="zh-TW"],
    div[lang^="zh-Hant"], div[lang^="zh-hant"], div[lang^="zh-TW"],
    p[lang^="zh-Hant"], p[lang^="zh-hant"], p[lang^="zh-TW"]
    { 
        font-weight: 600 !important; 
     }

    *[class^="fa"],
    *[class*=" fa"],  /* Font Awesome */
    *[class^="glyphicon"],
    *[class*=" glyphicon"], /* Glyphicon */
    *[class^="material-icons"],
    *[class*=" material-icons"], /* Material Icons */
    *[class*="icon"],   /* taobao, ssp*/
     [class*=" icon-"], [class^=icon-] /* smzdm*/
    .icon, .material-symbols-outlined { 
      font-family: 'FontAwesome',
         'Glyphicons Halflings',  /* python anywhere*/
         'Google Symbols',  /* google.com */ 
         'FabricMDL2Icons', /* onedrive */
         'Material Icons',
         'sans-serif',
         'global-iconfont', /* taobao.com*/
         'sspai_community_icon', /* ssp */
         'zdm-icons', /* smzdm */
         'iconfont' /* jd */ 
         !important; 
   }
  `;
}
// 将样式元素添加到文档头部
document.head.appendChild(style);
