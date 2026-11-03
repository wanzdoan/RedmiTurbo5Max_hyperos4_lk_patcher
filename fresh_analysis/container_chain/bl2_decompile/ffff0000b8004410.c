
void FUN_ffff0000b8004410(void)

{
  int iVar1;
  
  DAT_ffff0000b810c060 = FUN_ffff0000b80263f0(1,5);
  if (DAT_ffff0000b810c060 == 0) {
    FUN_ffff0000b804c33c
              (s__s__get_spmi_device_fail_ffff0000b80897f3,s_mt6373_sdmadc_init_ffff0000b8095ed6);
  }
  iVar1 = FUN_ffff0000b8020b14(&PTR_s_second_pmic_sdmadc_ffff0000b80ad400);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff0000b804c33c
            (s__s_failed_to_register_auxadc_dev_ffff0000b809f1d0,
             s_mt6373_sdmadc_init_ffff0000b8095ed6);
  return;
}


