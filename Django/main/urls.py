from django.conf.urls import include
from . import views
from rest_framework.urlpatterns import format_suffix_patterns
from django.urls import path

urlpatterns = [

    path('')

]

urlpatterns = format_suffix_patterns(urlpatterns)