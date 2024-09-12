# Convert Github issues to table

import requests
import json
import datetime
import os

# Use GraphQL API to get issues
url = "https://api.github.com/graphql"
headers = {
    "Authorization": "Bearer " + os.environ["GITHUB_TOKEN"]
}
payload = {
    "query": """
    {
      node(id: "PVT_kwHOATyE084Anu4d") {
        ... on ProjectV2 {
          items(first: 100) {
            nodes {
              id
              content {
                ... on DraftIssue {
                  title
                  createdAt
                }
                ... on Issue {
                  title
                  url
                  state
                  createdAt
                  milestone {
                    title
                    url
                  }
                }
              }
            }
          }
        }
      }
    }
    """
}

def sort_issue(issue):
    sort = 0
    if issue["status"] == "OPEN":
        sort = 1
    if issue["milestone"] == "None":
        return sort + 1000000000
    milestones = issue["milestone"].split(".")
    return sort + int(milestones[0]) * 100 + int(milestones[1]) * 10 + int(milestones[2])

response = requests.post(url, headers=headers, json=payload)
data = response.json()
data = data["data"]["node"]["items"]["nodes"]

issues = []
# content title
for item in data:
    print(item)
    title = item["content"]["title"]
    url = ""
    status = "OPEN"
    created_at = item["content"]["createdAt"]
    milestone = "None"
    milestone_url = ""
    if "url" in item["content"]:
        url = item["content"]["url"]
    if "state" in item["content"] and item["content"]["state"] is not None:
        status = item["content"]["state"]
    if "milestone" in item["content"] and item["content"]["milestone"] is not None:
        milestone = item["content"]["milestone"]["title"]
        milestone_url = item["content"]["milestone"]["url"]
    issues.append({
        "title": title,
        "status": status,
        "url": url,
        "created_at": created_at,
        "milestone": milestone,
        "milestone_url": milestone_url
    })

issues.sort(key=sort_issue)

with open("issue_list.md", "w") as f:
    f.write("| Title | Status | Milestone | Created |\n")
    f.write("| --- | --- | --- | --- |\n")
    print("Found " + str(len(data)) + " issues")
    for issue in issues:
        status = "**_~~Done~~_**"
        if issue["status"] == "OPEN":
            status = "Planned"
        url = issue["url"]
        title = issue["title"]
        created_at = issue["created_at"]
        milestone = issue["milestone"]
        milestone_url = issue["milestone_url"]
        # Parse date
        created = datetime.datetime.fromisoformat(created_at).strftime("%d/%m/%Y")

        f.write(f"| [{title}]({url}) | {status} |")
        if milestone == "None":
            f.write(" None |")
        else:
            f.write(f" [{milestone}]({milestone_url}) |")
        f.write(f" {created} |\n")