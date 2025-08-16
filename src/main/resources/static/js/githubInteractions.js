(() => {
    async function fetchPinnedRepos() {
        const resp = await fetch('/api/github/pinned', { headers: { 'Accept': 'application/json' } });
        if (!resp.ok) return [];
        const json = await resp.json();
        return json?.data?.user?.pinnedItems?.nodes ?? [];
    }

    function createLanguageBars(languages) {
        const total = languages.reduce((s, l) => s + l.size, 0) || 1;
        const wrap = document.createElement("div");
        wrap.className = "relative h-4 mt-4 flex rounded-md overflow-hidden";
        languages.forEach(({ node, size }) => {
            const pct = (size / total) * 100;
            const bar = document.createElement("div");
            bar.style.width = pct + "%";
            bar.style.backgroundColor = node.color || "#ccc";
            bar.className = "h-full";
            bar.title = `${node.name}: ${pct.toFixed(2)}%`;
            wrap.appendChild(bar);
        });
        return wrap;
    }

    function createLanguageBalls(languages) {
        const total = languages.reduce((s, l) => s + l.size, 0) || 1;
        const wrap = document.createElement("div");
        wrap.className = "flex flex-wrap items-center gap-2 mt-2";
        languages.forEach(({ node, size }) => {
            const dot = document.createElement("span");
            dot.className = "inline-block w-3 h-3 rounded-full";
            dot.style.backgroundColor = node.color || "#ccc";

            const label = document.createElement("span");
            label.className = "text-sm ml-1";
            label.textContent = `${node.name} (${((size/total)*100).toFixed(2)}%)`;

            const item = document.createElement("div");
            item.className = "flex items-center gap-1";
            item.append(dot, label);
            wrap.appendChild(item);
        });
        return wrap;
    }

    async function renderPinnedRepos() {
        const container = document.getElementById("pinned-repos");
        if (!container) return;
        container.innerHTML = "";
        const repos = await fetchPinnedRepos();

        repos.forEach((repo) => {
            const card = document.createElement("div");
            card.className = "bg-white dark:bg-slate-900 p-6 rounded-lg shadow-md";

            const title = document.createElement("h3");
            title.className = "text-xl font-bold mb-2";
            title.textContent = repo.name;

            const desc = document.createElement("p");
            desc.className = "text-gray-600 dark:text-gray-400 mb-4";
            desc.textContent = repo.description || "No description available.";

            const link = document.createElement("a");
            link.className = "text-rose-600 dark:text-purple-600 font-semibold hover:underline";
            link.href = repo.url;
            link.target = "_blank";
            link.textContent = "View Repository →";

            const edges = repo.languages?.edges ?? [];
            if (edges.length) {
                card.appendChild(createLanguageBalls(edges));
                card.appendChild(createLanguageBars(edges));
            }

            card.append(title, desc, link);
            container.appendChild(card);
        });
    }

    // run after DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', renderPinnedRepos);
    } else {
        renderPinnedRepos();
    }
})();
