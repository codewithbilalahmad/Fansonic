package com.muhammad.fansonic.html_text

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HtmlTextScreen() {
    HtmlText(
        modifier = Modifier.fillMaxSize(),
        html = """
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1" />
<title>Zigzag Drawing App — Privacy Policy</title>
<style>
:root {
  --bg:#f3f4f6;
  --card-bg:#ffffff;
  --text:#111827;
  --muted:#6b7280;
  --accent:#0ea5a4;
  --container-padding:26px;
  --max-width:860px;
  font-family:-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial;
  line-height:1.7;
}
html, body {
  height:100%;
  margin:0;
  background:var(--bg);
  color:var(--text);
}
.wrap {
  max-width:var(--max-width);
  margin:32px auto;
  padding:var(--container-padding);
  background:var(--card-bg);
  border-radius:16px;
  box-shadow:0 4px 18px rgba(0,0,0,0.08);
}
header {
  border-bottom:1px solid #e5e7eb;
  padding-bottom:14px;
  margin-bottom:24px;
}
h1 {
  font-size:1.8rem;
  margin:0 0 6px;
  color:var(--accent);
}
p.lead {
  margin:0;
  color:var(--muted);
  font-size:1rem;
}
section {
  margin:26px 0;
  padding-bottom:18px;
  border-bottom:1px solid #f0f2f6;
}
h2 {
  font-size:1.25rem;
  margin:0 0 12px;
  color:var(--accent);
  font-weight:600;
}
p {
  margin:0 0 14px;
  font-size:1rem;
}
ul {
  margin:10px 0 0 22px;
  color:var(--muted);
  padding-left:10px;
}
li {
  margin:8px 0;
}
.muted {
  color:var(--muted);
  font-size:.95rem;
}
.small {
  font-size:.9rem;
  color:var(--muted);
}
footer {
  margin-top:28px;
  border-top:1px solid #e5e7eb;
  padding-top:14px;
  color:var(--muted);
  font-size:.9rem;
  text-align:center;
}
a {
  color:var(--accent);
  text-decoration:none;
  font-weight:500;
}
a:hover {
  text-decoration:underline;
}
code {
  background:#f9fafb;
  padding:2px 6px;
  border-radius:6px;
  font-family:monospace;
  font-size:.9rem;
}
</style>
</head>
<body>
<div class="wrap">
<header>
<h1>Zigzag — Privacy Policy</h1>
<p class="lead">Effective Date: <strong>27-09-2025</strong></p>
</header>

<section>
<p>Welcome to <strong>Zigzag Drawing App</strong>. I built this app to let you create fun zigzag drawings. This Privacy Policy explains what information the app accesses, how it’s used, and the choices you have.</p>
</section>

<section>
<h2>1. Information We Collect</h2>
<p>The app is designed to be simple and private. It collects <strong>no personal information</strong> by default.</p>
<ul>
<li><strong>No account required:</strong> You don’t need to sign up or provide any email address.</li>
<li><strong>Local drawings:</strong> All creations remain <em>only on your device</em>, unless you export or share them via your system’s share options.</li>
<li><strong>Crash reports:</strong> If you installed via Google Play or App Store, those platforms may collect basic crash/usage analytics — we do not add third-party tracking.</li>
</ul>
</section>

<section>
<h2>2. How We Use Information</h2>
<p>Since no personal data is collected, we don’t use any data for ads, profiling, or selling. The app only uses device storage to:</p>
<ul>
<li>Save your drawings and projects.</li>
<li>Restore your work between app sessions.</li>
</ul>
</section>

<section>
<h2>3. Sharing & Third-Party Services</h2>
<p>We do not share your drawings or personal data. The app does not integrate ads, trackers, or analytics.</p>
<p class="muted">If you share a drawing using your device’s share sheet, the destination app/service will receive that content — beyond our control.</p>
</section>

<section>
<h2>4. Data Security</h2>
<p>Your drawings remain on your device. We never upload to servers. Note: if device/cloud backups are enabled, system backups may include app data.</p>
</section>

<section>
<h2>5. Children</h2>
<p>The app is for general audiences and does not knowingly collect data from children under 13. If you believe otherwise, please contact us immediately.</p>
</section>

<section>
<h2>6. Changes to this Policy</h2>
<p>We may update this policy from time to time. The Effective Date will always reflect the latest update.</p>
</section>

<section>
<h2>7. Contact</h2>
<p>If you have any questions or concerns, reach us at:</p>
<p class="small"><strong>Email:</strong> <a href="mailto:mbilal10388@gmail.com">mbilal10388@gmail.com</a></p>
</section>

<footer>
<p class="small">Zigzag Drawing App — Built with care. Last updated: <strong>27-09-2025</strong>.</p>
</footer>
</div>
</body>
</html>
        """
    )
}

@Composable
fun HtmlText(html : String,modifier: Modifier = Modifier)   {
    AndroidView(factory = {context ->
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = false
                builtInZoomControls = false
                displayZoomControls = false
                loadWithOverviewMode = true
                useWideViewPort = true
            }
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            loadDataWithBaseURL(
                null,
                html,
                "text/html",
                "utf-8",
                null
            )
        }
    }, modifier = modifier)
}