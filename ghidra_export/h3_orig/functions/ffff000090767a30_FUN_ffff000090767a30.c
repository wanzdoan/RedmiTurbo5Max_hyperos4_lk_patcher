// ffff000090767a30  size=108  FUN_ffff000090767a30

void FUN_ffff000090767a30(long param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff000090783694();
  if (iVar1 == 0xfc11) {
    iVar1 = FUN_ffff0000907831b8(param_1 + 1);
    if (iVar1 != 0) {
      FUN_ffff000090709d50(&DAT_ffff0000907df295);
      return;
    }
    pcVar2 = s_MRDUMP_output_device_setting_fai_ffff00009080140a;
  }
  else {
    pcVar2 = s_MRDUMP_is_not_support_now__ffff0000907f6f93;
  }
  FUN_ffff000090709d10(pcVar2);
  return;
}


