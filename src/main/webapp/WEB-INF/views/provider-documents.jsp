<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Business Documents | Smart Parking</title>
    <style>
        *{box-sizing:border-box}body{margin:0;background:#080d18;color:#edf2ff;font-family:Arial,sans-serif}
        .page{width:min(950px,94%);margin:auto;padding:30px 0 50px}
        header{display:flex;justify-content:space-between;gap:15px;margin-bottom:30px}
        .brand{font-size:24px;font-weight:800}.brand span{color:#4f8cff}
        a{color:#91b7ff;text-decoration:none}a:hover{color:white}
        .panel{padding:24px;border:1px solid #25334b;border-radius:18px;background:#10192a;margin-bottom:22px}
        h1{font-size:clamp(27px,5vw,38px)}h2{font-size:20px}
        p{color:#97a7c4;line-height:1.7;font-size:14px}
        label{display:block;margin:16px 0 8px;color:#c8d3e7;font-size:13px}
        input,select{width:100%;padding:13px;border:1px solid #2b3953;border-radius:9px;background:#0a1120;color:white}
        button{margin-top:18px;padding:13px 18px;border:0;border-radius:9px;background:#3478ef;color:white;font-weight:bold;cursor:pointer}
        .status{display:inline-block;padding:6px 9px;border-radius:20px;background:#1b2a43;color:#a9c7ff;font-size:11px;font-weight:bold}
        .item{padding:16px 0;border-bottom:1px solid #29354a}.item:last-child{border-bottom:0}
        .message{padding:13px;border:1px solid #315b8e;border-radius:10px;background:#12213a;color:#c6dcff;overflow-wrap:anywhere}
        .hint{font-size:12px}
        @media(min-width:700px){.upload-grid{display:grid;grid-template-columns:1fr 1fr;gap:18px}}
    </style>
</head>
<body>
<div class="page">
    <header>
        <div class="brand">Smart<span>Parking</span></div>
        <a href="${pageContext.request.contextPath}/provider/applications">← Applications</a>
    </header>

    <h1>Business Documents</h1>
    <p>Application: <strong><c:out value="${application.businessName}"/></strong></p>
    <p>Application status: <span class="status"><c:out value="${application.status}"/></span></p>

    <c:if test="${not empty sessionScope.providerApplicationMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.providerApplicationMessage}"/>
        </div>
        <c:remove var="providerApplicationMessage" scope="session"/>
    </c:if>

    <section class="panel">
        <h2>Upload a Document</h2>
        <p>Accepted formats: PDF, JPG, JPEG, PNG. Maximum file size: 5 MB.</p>

        <c:choose>
            <c:when test="${application.status == 'PENDING'}">
                <form method="post"
                      enctype="multipart/form-data"
                      action="${pageContext.request.contextPath}/provider/documents">

                    <input type="hidden" name="applicationId" value="${application.id}">

                    <div class="upload-grid">
                        <div>
                            <label for="documentType">Document Type *</label>
                            <select id="documentType" name="documentType" required>
                                <option value="">Select document type</option>
                                <option value="IDENTITY_PROOF">Identity Proof</option>
                                <option value="OWNERSHIP_PROOF">Ownership Proof</option>
                                <option value="LEASE_AGREEMENT">Lease Agreement</option>
                                <option value="BUSINESS_LICENSE">Business License</option>
                                <option value="OTHER">Other Supporting Document</option>
                            </select>
                        </div>
                        <div>
                            <label for="documentFile">Choose File *</label>
                            <input id="documentFile" type="file" name="documentFile"
                                   accept=".pdf,.jpg,.jpeg,.png" required>
                        </div>
                    </div>

                    <button type="submit">Upload Document</button>
                </form>
            </c:when>
            <c:otherwise>
                <p>Uploads are closed because this application is no longer pending.</p>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="panel">
        <h2>Submitted Documents</h2>

        <c:choose>
            <c:when test="${not empty documents}">
                <c:forEach var="document" items="${documents}">
                    <article class="item">
                        <h3><c:out value="${document.documentType}"/></h3>
                        <p>Status: <span class="status"><c:out value="${document.status}"/></span></p>
                        <p>Uploaded: <c:out value="${document.uploadedAt}"/></p>

                        <c:if test="${not empty document.reviewNote}">
                            <p>Review note: <c:out value="${document.reviewNote}"/></p>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/provider/documents?action=download&amp;id=${document.id}">
                            Download document
                        </a>
                    </article>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No documents have been uploaded for this application.</p>
            </c:otherwise>
        </c:choose>
    </section>
</div>
</body>
</html>