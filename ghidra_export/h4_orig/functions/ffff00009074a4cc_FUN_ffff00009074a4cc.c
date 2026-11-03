// ffff00009074a4cc  size=208  FUN_ffff00009074a4cc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074a4cc(void)

{
  int iVar1;
  
  _DAT_ffff000090877708 = FUN_ffff000090740610(1,5);
  if (_DAT_ffff000090877708 == 0) {
    FUN_ffff00009077e11c
              (s__s__get_spmi_device_fail_ffff0000907d71f6,s_efuse_fsource_set_ffff0000907e3232);
  }
  else {
    iVar1 = FUN_ffff00009074056c(_DAT_ffff000090877708,0x1e21,4,0xf,0);
    if (((-1 < iVar1) &&
        (iVar1 = FUN_ffff00009074056c(_DAT_ffff000090877708,0x1e20,0,0xf,0), -1 < iVar1)) &&
       (iVar1 = FUN_ffff00009074056c(_DAT_ffff000090877708,0x1ca3,1,1,0), -1 < iVar1)) {
      FUN_ffff00009077cea0(10000);
      return 0;
    }
    FUN_ffff00009077e11c
              (s_SPMI_write_fail_at___s___addr____ffff0000907f4dbf,
               s_efuse_fsource_set_ffff0000907e3232,0x1e21,iVar1);
  }
  return 1;
}


