// ffff000090704458  size=4  FUN_ffff000090704458

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090704458(void)

{
  int iVar1;
  
  _DAT_ffff00009083d068 = FUN_ffff000090740610(1,4);
  if (_DAT_ffff00009083d068 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___s__get_spmi_device_fail_ffff0000907fe905,
               s_mt6363_auxadc_init_ffff0000907d5697,0x69,s_mt6363_auxadc_init_ffff0000907d5697);
  }
  iVar1 = FUN_ffff000090736cb4(&PTR_s_main_pmic_ffff0000908163c0);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff00009077e11c
            (s__s__d___s_failed_to_register_aux_ffff0000907d08c5,
             s_mt6363_auxadc_init_ffff0000907d5697,0x6d,s_mt6363_auxadc_init_ffff0000907d5697);
  return;
}


