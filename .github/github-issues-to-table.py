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
              fieldValues(first: 6) {
                nodes {
                  ... on ProjectV2ItemFieldSingleSelectValue {
                    name
                    field {
                      ... on ProjectV2FieldCommon {
                        name
                      }
                    }
                  }
                }
              }
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
                  updatedAt
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
    sort = -1
    if issue["status"] == "Done":
        sort = 0
    elif issue["status"] == "In progress":
        sort = 1
    elif issue["status"] == "In review":
        sort = 2

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
    fieldValues = item["fieldValues"]["nodes"]
    status = "UNKNOWN"
    for fieldValue in fieldValues:
        if "field" not in fieldValue:
            continue
        if fieldValue["field"]["name"] == "Status":
            status = fieldValue["name"]
            break
    title = item["content"]["title"]
    url = ""
    state = "OPEN"
    created_at = item["content"]["createdAt"]
    updated_at = item["content"]["updatedAt"]
    milestone = "None"
    milestone_url = ""
    if "url" in item["content"]:
        url = item["content"]["url"]
    if "state" in item["content"] and item["content"]["state"] is not None:
        state = item["content"]["state"]
    if "milestone" in item["content"] and item["content"]["milestone"] is not None:
        milestone = item["content"]["milestone"]["title"]
        milestone_url = item["content"]["milestone"]["url"]
    issues.append({
        "title": title,
        "status": status,
        "state": state,
        "url": url,
        "created_at": created_at,
        "updated_at": updated_at,
        "milestone": milestone,
        "milestone_url": milestone_url
    })

issues.sort(key=sort_issue)

with open("issue_list.md", "w") as f:
    f.write("| Title | Status | Milestone | Created | Updated |\n")
    f.write("| --- | --- | --- | --- | --- |\n")
    print("Found " + str(len(data)) + " issues")
    for issue in issues:
        if issue["status"] != "Done" and issue["status"] != "In review":
            continue

        status = "**_~~Done~~_**"
        if issue["status"] != "Done":
            status = issue["status"]
        url = issue["url"]
        title = issue["title"]
        created_at = issue["created_at"]
        updated_at = issue["updated_at"]
        milestone = issue["milestone"]
        milestone_url = issue["milestone_url"]
        # Parse date
        created = datetime.datetime.fromisoformat(created_at).strftime("%d/%m/%Y %H:%M")
        updated = datetime.datetime.fromisoformat(updated_at).strftime("%d/%m/%Y %H:%M")
        # Check if created and updated are the same
        if created_at == updated_at:
            updated = "Never"

        f.write(f"| [{title}]({url}) | {status} |")
        if milestone == "None":
            f.write(" None |")
        else:
            f.write(f" [{milestone}]({milestone_url}) |")
        f.write(f" {created} | {updated} |\n")