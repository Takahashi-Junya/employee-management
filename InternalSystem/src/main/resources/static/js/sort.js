console.log("sort.js has been loaded!");  // 確実にロードされているか確認

function sortTable(column) {
    console.log("Sorting by:", column); // クリック時のログ

    const urlParams = new URLSearchParams(window.location.search);
    const currentSortColumn = urlParams.get("sortColumn");
    const currentAscending = urlParams.get("ascending") === "true";

    // 押したカラムが今のソート対象なら、昇順⇄降順を切り替え
    const newAscending = (column === currentSortColumn) ? !currentAscending : true;

    console.log("Clicked column:", column);
    console.log("Current sort column:", currentSortColumn);
    console.log("Current ascending:", currentAscending);
    console.log("New ascending:", newAscending);

    // URL を変更してページをリロード
    window.location.href = `/sections/list?sortColumn=${column}&ascending=${newAscending}`;
}

// `sortTable` をグローバルに登録
window.sortTable = sortTable;
console.log("sortTable function is now available in window!");
