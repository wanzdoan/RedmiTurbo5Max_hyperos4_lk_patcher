// ffff00009074a378  size=120  FUN_ffff00009074a378

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff00009074a378(void)

{
  int iVar1;
  char local_14 [4];
  
  local_14[0] = '\0';
  _DAT_ffff000090877670 = FUN_ffff000090740378(1,5);
  iVar1 = FUN_ffff000090740264(_DAT_ffff000090877670,0x1ca3,local_14,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s_SPMI_read_fail_at___s___addr___d_ffff0000907fbb7b,
               s_efuse_fsource_is_enabled_ffff0000907ee41e,0x1ca3,iVar1);
  }
  return local_14[0] == '\x01';
}


