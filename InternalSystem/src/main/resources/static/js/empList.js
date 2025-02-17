document.addEventListener("DOMContentLoaded", function () {
    // ステータス更新を感知
    document.querySelectorAll("input[name='status']").forEach(function (radio) {
        radio.addEventListener("change", updateStatusFilter);
    });

    // カラムのソートクリックを検知
    document.querySelectorAll(".sortable").forEach(function (th) {
        th.addEventListener("click", function () {
            updateSortOrder(th.dataset.sort);
        });
    });

    // ページネーションのボタン監視
    document.getElementById("prevPage")?.addEventListener("click", function () {
        updatePage(-1);
    });

    document.getElementById("nextPage")?.addEventListener("click", function () {
        updatePage(1);
    });
    
    document.addEventListener("onclick", function () {
		attention();
	})

    // リロード時に現在のURLからステータスを復元
    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    if (status) {
        const selectedRadio = document.querySelector(`input[name="status"][value="${status}"]`);
        if (selectedRadio) {
            selectedRadio.checked = true; // チェックをつける
        }
    }

    // ページネーションのUIを更新
    updatePaginationUI();
});

// ステータスフィルターの更新
function updateStatusFilter() {
    const selectedStatus = document.querySelector("input[name='status']:checked").value;
    updateURLParams({ status: selectedStatus, page: 0 }); // ステータス変更時は最初のページ (0) に戻す
}

// ソートの更新
function updateSortOrder(sortBy) {
    const params = new URLSearchParams(window.location.search);
    const currentSortBy = params.get("sortBy") || "id";
    const currentOrder = params.get("order") || "ASC";

    let newOrder = "ASC";
    if (sortBy === currentSortBy) {
        newOrder = currentOrder === "ASC" ? "DESC" : "ASC";
    }

    updateURLParams({ sortBy: sortBy, order: newOrder, page: 0 }); // ソート変更時は最初のページ (0) に戻す
}

// ページの更新
function updatePage(offset) {
    const params = new URLSearchParams(window.location.search);
    let currentPage = parseInt(params.get("page")) || 0;
    
    const newPage = Math.max(0, currentPage + offset); // 最小値0
    updateURLParams({ page: newPage });
}

function attention(event) {
    // 確認ダイアログを表示
    if (!confirm("この作業は修正できません。本当によろしいですか？")) {
        event.preventDefault(); // キャンセルした場合、リンク遷移を防ぐ
    }
}



// URL パラメータを更新してリロード
function updateURLParams(newParams) {
    const params = new URLSearchParams(window.location.search);
    Object.keys(newParams).forEach(key => {
        params.set(key, newParams[key]);
    });

    window.location.href = `${window.location.pathname}?${params.toString()}`;
}

// ページネーションのUIを更新
function updatePaginationUI() {
    const params = new URLSearchParams(window.location.search);
    let currentPage = parseInt(params.get("page")) || 0;
    const totalPages = parseInt(document.getElementById("totalPages")?.dataset.value) || 1;

    const prevButton = document.getElementById("prevPage");
    const nextButton = document.getElementById("nextPage");

    // 最初のページなら「前へ」ボタンを非表示
    if (currentPage <= 0) {
        prevButton.style.display = "none";
    } else {
        prevButton.style.display = "inline-block";
    }

    // 最後のページなら「次へ」ボタンを非表示
    if (currentPage >= totalPages - 1) {
        nextButton.style.display = "none";
    } else {
        nextButton.style.display = "inline-block";
    }
}
