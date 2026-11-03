
void FUN_ffff000090767cc8(long param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009078395c();
  if (iVar1 == 0xfc11) {
    iVar1 = FUN_ffff000090783480(param_1 + 1);
    if (iVar1 != 0) {
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      return;
    }
    pcVar2 = s_MRDUMP_output_device_setting_fai_ffff0000908015e6;
  }
  else {
    pcVar2 = s_MRDUMP_is_not_support_now__ffff0000907f711d;
  }
  FUN_ffff000090709c9c(pcVar2);
  return;
}


