{{- define "notification.name" -}}
notification
{{- end }}

{{- define "notification.fullname" -}}
{{ include "notification.name" . }}
{{- end }}

{{- define "notification.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
    {{- default (include "notification.fullname" .) .Values.serviceAccount.name }}
{{- else }}
    {{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "notification.labels" -}}
app.kubernetes.io/name: notification
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}