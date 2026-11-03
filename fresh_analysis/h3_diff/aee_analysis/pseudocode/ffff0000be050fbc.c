
void FUN_ffff0000be050fbc(long param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff0000be065774();
  if (iVar1 == 0xfc11) {
    iVar1 = FUN_ffff0000be0650b4(param_1 + 1);
    if (iVar1 != 0) {
      FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
      return;
    }
    pcVar2 = s_MRDUMP_output_device_setting_fai_ffff0000be0c586f;
  }
  else {
    pcVar2 = s_MRDUMP_is_not_support_now__ffff0000be0beb9d;
  }
  FUN_ffff0000be0137e8(pcVar2);
  return;
}


