from flask import Flask, request, jsonify
from flask_cors import CORS
import re

app = Flask(__name__)
CORS(app)

CATEGORY_KEYWORDS = {
    "PAYMENT": ["payment", "paid", "charge", "charged", "refund", "transaction", "invoice", "billing"],
    "LOGIN": ["login", "log in", "password", "sign in", "otp", "account", "locked"],
    "DELIVERY": ["delivery", "deliver", "shipping", "shipment", "order", "tracking", "package"],
    "TECHNICAL": ["error", "bug", "crash", "not working", "failed", "failure", "server", "api"],
}

def classify(text):
    lower = text.lower()
    scores = {category: 0 for category in CATEGORY_KEYWORDS}

    for category, words in CATEGORY_KEYWORDS.items():
        for word in words:
            if word in lower:
                scores[category] += 1

    category = max(scores, key=scores.get)
    if scores[category] == 0:
        category = "GENERAL"

    urgent_words = ["urgent", "asap", "immediately", "critical", "blocked", "fraud", "stolen"]
    high = any(word in lower for word in urgent_words)

    if high:
        priority = "HIGH"
    elif any(word in lower for word in ["issue", "problem", "failed", "error", "unable"]):
        priority = "MEDIUM"
    else:
        priority = "LOW"

    return category, priority

def clean_name(name):
    name = (name or "Customer").strip()
    return re.sub(r"\s+", " ", name)[:80]

@app.get("/health")
def health():
    return jsonify({"status": "UP", "service": "ResolveX AI Service"})

@app.post("/analyze")
def analyze():
    data = request.get_json(silent=True) or {}
    subject = data.get("subject", "")
    description = data.get("description", "")
    customer_name = clean_name(data.get("customerName"))

    combined = f"{subject} {description}"
    category, priority = classify(combined)

    reply = (
        f"Hi {customer_name},\n\n"
        f"Thank you for contacting us regarding your {category.lower()} issue. "
        f"We have reviewed your request and classified it as {priority.lower()} priority. "
        f"Our support team will review the details and help resolve the issue as soon as possible.\n\n"
        f"Regards,\nResolveX Support"
    )

    return jsonify({
        "category": category,
        "priority": priority,
        "suggestedReply": reply
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
