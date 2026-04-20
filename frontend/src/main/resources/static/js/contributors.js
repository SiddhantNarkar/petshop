document.addEventListener("DOMContentLoaded", () => {
    const cards = Array.from(document.querySelectorAll(".card"));
    const statNumbers = Array.from(document.querySelectorAll(".hero-stat-value"));

    cards.forEach((card, index) => {
        window.setTimeout(() => {
            card.classList.add("visible");
        }, index * 80);
    });

    statNumbers.forEach((node) => {
        const target = Number(node.dataset.countUp || "0");
        const suffix = node.dataset.suffix || "";
        countUp(node, target, suffix, 900);
    });
});

function countUp(node, target, suffix, duration) {
    const start = performance.now();

    function frame(now) {
        const elapsed = now - start;
        const progress = Math.min(elapsed / duration, 1);
        const eased = 1 - Math.pow(1 - progress, 3);
        const current = Math.round(target * eased);

        node.textContent = progress === 1 ? `${target}${suffix}` : String(current);

        if (progress < 1) {
            requestAnimationFrame(frame);
        }
    }

    requestAnimationFrame(frame);
}
