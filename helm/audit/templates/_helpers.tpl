{{- define "audit.name" -}}
audit
{{- end }}

{{- define "audit.fullname" -}}
{{ include "audit.name" . }}
{{- end }}

{{- define "audit.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
    {{- default (include "audit.fullname" .) .Values.serviceAccount.name }}
{{- else }}
    {{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "audit.labels" -}}
app.kubernetes.io/name: audit
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}