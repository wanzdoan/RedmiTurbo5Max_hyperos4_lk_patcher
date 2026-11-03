// ffff00009074a610  size=120  FUN_ffff00009074a610

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff00009074a610(void)

{
  int iVar1;
  char local_14 [4];
  
  local_14[0] = '\0';
  _DAT_ffff000090877708 = FUN_ffff000090740610(1,5);
  iVar1 = FUN_ffff0000907404fc(_DAT_ffff000090877708,0x1ca3,local_14,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s_SPMI_read_fail_at___s___addr___d_ffff0000907fbd17,
               s_efuse_fsource_is_enabled_ffff0000907ee5a6,0x1ca3,iVar1);
  }
  return local_14[0] == '\x01';
}


