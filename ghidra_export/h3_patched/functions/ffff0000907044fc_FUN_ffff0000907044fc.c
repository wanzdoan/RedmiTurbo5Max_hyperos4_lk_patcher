// ffff0000907044fc  size=4  FUN_ffff0000907044fc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907044fc(void)

{
  int iVar1;
  
  _DAT_ffff00009083d0c8 = FUN_ffff000090740378(1,4);
  if (_DAT_ffff00009083d0c8 == 0) {
    FUN_ffff00009077de54
              (s__s__d___s__get_spmi_device_fail_ffff0000907fe75d,
               s_mt6363_auxadc_init_ffff0000907d565a,0x69,s_mt6363_auxadc_init_ffff0000907d565a);
  }
  iVar1 = FUN_ffff000090736a1c(&PTR_s_main_pmic_ffff0000908163c0);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff00009077de54
            (s__s__d___s_failed_to_register_aux_ffff0000907d08ae,
             s_mt6363_auxadc_init_ffff0000907d565a,0x6d,s_mt6363_auxadc_init_ffff0000907d565a);
  return;
}


