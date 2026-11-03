// ffff00009074a59c  size=116  FUN_ffff00009074a59c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff00009074a59c(void)

{
  int iVar1;
  
  _DAT_ffff000090877708 = FUN_ffff000090740610(1,5);
  iVar1 = FUN_ffff00009074056c(_DAT_ffff000090877708,0x1ca3,0,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s_SPMI_write_fail_at___s___addr____ffff0000907f4dbf,
               s_efuse_fsource_close_ffff0000907ee592,0x1e21,iVar1);
  }
  else {
    FUN_ffff00009077cea0(10000);
  }
  return iVar1 < 0;
}


