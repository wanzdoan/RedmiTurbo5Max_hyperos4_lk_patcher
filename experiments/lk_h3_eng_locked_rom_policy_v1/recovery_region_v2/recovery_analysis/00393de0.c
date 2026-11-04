
/* volume_for_mount_point(std::string const&) */

void volume_for_mount_point(string *param_1)

{
  string *in_x2;

  android::fs_mgr::GetEntryForMountPoint((fs_mgr *)&DAT_0057bcf0,(vector *)param_1,in_x2);
  return;
}
