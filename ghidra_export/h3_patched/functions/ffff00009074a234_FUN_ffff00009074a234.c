// ffff00009074a234  size=208  FUN_ffff00009074a234

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009074a234(void)

{
  int iVar1;
  
  _DAT_ffff000090877670 = FUN_ffff000090740378(1,5);
  if (_DAT_ffff000090877670 == 0) {
    FUN_ffff00009077de54
              (s__s__get_spmi_device_fail_ffff0000907d7160,s_efuse_fsource_set_ffff0000907e314a);
  }
  else {
    iVar1 = FUN_ffff0000907402d4(_DAT_ffff000090877670,0x1e21,4,0xf,0);
    if (((-1 < iVar1) &&
        (iVar1 = FUN_ffff0000907402d4(_DAT_ffff000090877670,0x1e20,0,0xf,0), -1 < iVar1)) &&
       (iVar1 = FUN_ffff0000907402d4(_DAT_ffff000090877670,0x1ca3,1,1,0), -1 < iVar1)) {
      FUN_ffff00009077cbd8(10000);
      return 0;
    }
    FUN_ffff00009077de54
              (s_SPMI_write_fail_at___s___addr____ffff0000907f4c3b,
               s_efuse_fsource_set_ffff0000907e314a,0x1e21,iVar1);
  }
  return 1;
}


