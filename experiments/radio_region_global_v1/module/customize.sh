#!/system/bin/sh
# Install through a root manager. No live property or radio changes here.
[ "$BOOTMODE" = true ] || abort "Install through the root manager while Android is running."
[ "$(getprop ro.product.device)" = dash ] || abort "This experiment is only for dash."
[ "$(getprop ro.build.version.incremental)" = OS4.0.3.0.XPLCNXM ] || abort "Expected ROM OS4.0.3.0.XPLCNXM."
[ "$(getprop ro.vendor.radio.build_region)" = cn ] || abort "Expected original radio region cn; disable other region overrides first."
getprop ro.vendor.radio.build_region > "$MODPATH/original_radio_region.txt"
ui_print "- Experimental property override: cn -> global on next boot."
ui_print "- LK unchanged. No SA/NSA, NV, modem or persistent property edits."
ui_print "- 5G reception is NOT verified. Disable module and reboot to undo."
set_perm_recursive "$MODPATH" 0 0 0755 0644
set_perm "$MODPATH/customize.sh" 0 0 0755
