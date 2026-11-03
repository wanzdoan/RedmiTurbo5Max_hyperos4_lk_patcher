// ffff00009074a304  size=116  FUN_ffff00009074a304

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff00009074a304(void)

{
  int iVar1;
  
  _DAT_ffff000090877670 = FUN_ffff000090740378(1,5);
  iVar1 = FUN_ffff0000907402d4(_DAT_ffff000090877670,0x1ca3,0,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s_SPMI_write_fail_at___s___addr____ffff0000907f4c3b,
               s_efuse_fsource_close_ffff0000907ee40a,0x1e21,iVar1);
  }
  else {
    FUN_ffff00009077cbd8(10000);
  }
  return iVar1 < 0;
}


