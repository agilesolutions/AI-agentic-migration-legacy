{{- define "intelligence.name" -}}
intelligence
{{- end }}

{{- define "intelligence.fullname" -}}
{{ include "intelligence.name" . }}
{{- end }}

{{- define "intelligence.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
    {{- default (include "intelligence.fullname" .) .Values.serviceAccount.name }}
{{- else }}
    {{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "intelligence.labels" -}}
app.kubernetes.io/name: intelligence
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}