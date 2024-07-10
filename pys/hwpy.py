import requests
from bs4 import BeautifulSoup


def search_domain(keyword):
    """
  使用中文关键字查询域名

  Args:
    keyword: 中文关键字

  Returns:
    一个包含搜索结果的列表
  """

    url = f"https://www.godaddy.com/zh-CN/domain-search?checkAvailability=1&domainToCheck={keyword}"
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    results = soup.find_all('div', class_='domain-search__result')

    domain_list = []
    for result in results:
        domain = result.find('a', class_='domain-search__result-domain').text
        domain_list.append(domain)
    return domain_list


# 示例用法
keyword = "google"
domain_list = search_domain(keyword)

if domain_list:
    print(f"找到以下域名：n{domain_list}")
else:
    print(f"未找到与关键词 '{keyword}' 匹配的域名")
