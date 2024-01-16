@import fr.not_here_dev.helpers.SSR

@param String title

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title th:text="${title}">Title</title>
    <script src="static/client.js"></script>
    ${SSR.Companion.getInstance().hydrationScript()}
</head>
<body>
test
${SSR.Companion.getInstance().reactiveComponent("HelloWorld")}
</body>
</html>