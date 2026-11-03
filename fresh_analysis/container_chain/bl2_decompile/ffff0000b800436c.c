
void FUN_ffff0000b800436c(void)

{
  int iVar1;
  
  DAT_ffff0000b810c058 = FUN_ffff0000b80263f0(1,4);
  if (DAT_ffff0000b810c058 == 0) {
    FUN_ffff0000b804c33c
              (s__s__d___s__get_spmi_device_fail_ffff0000b809f1af,
               s_mt6363_auxadc_init_ffff0000b80888fb,0x69,s_mt6363_auxadc_init_ffff0000b80888fb);
  }
  iVar1 = FUN_ffff0000b8020b14(&PTR_s_main_pmic_ffff0000b80ad3c0);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff0000b804c33c
            (s__s__d___s_failed_to_register_aux_ffff0000b8085e7e,
             s_mt6363_auxadc_init_ffff0000b80888fb,0x6d,s_mt6363_auxadc_init_ffff0000b80888fb);
  return;
}


