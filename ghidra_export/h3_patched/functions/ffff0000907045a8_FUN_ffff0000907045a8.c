// ffff0000907045a8  size=108  FUN_ffff0000907045a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907045a8(void)

{
  int iVar1;
  
  _DAT_ffff00009083d0d0 = FUN_ffff000090740378(1,5);
  if (_DAT_ffff00009083d0d0 == 0) {
    FUN_ffff00009077de54
              (s__s__get_spmi_device_fail_ffff0000907d7160,s_mt6373_sdmadc_init_ffff0000907ed840);
  }
  iVar1 = FUN_ffff000090736a1c(&PTR_s_second_pmic_sdmadc_ffff000090816400);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff00009077de54
            (s__s_failed_to_register_auxadc_dev_ffff0000907fe77e,
             s_mt6373_sdmadc_init_ffff0000907ed840);
  return;
}


