
void FUN_ffff0000be003fbc(void)

{
  int iVar1;
  
  DAT_ffff0000be0f9038 = FUN_ffff0000be0303e8(1,4);
  if (DAT_ffff0000be0f9038 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__get_spmi_device_fail_ffff0000be0c3a7e,
               s_mt6363_auxadc_init_ffff0000be0a7935,0x69,s_mt6363_auxadc_init_ffff0000be0a7935);
  }
  iVar1 = FUN_ffff0000be029260(&PTR_s_main_pmic_ffff0000be0d5410);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d___s_failed_to_register_aux_ffff0000be0a40bf,
             s_mt6363_auxadc_init_ffff0000be0a7935,0x6d,s_mt6363_auxadc_init_ffff0000be0a7935);
  return;
}


