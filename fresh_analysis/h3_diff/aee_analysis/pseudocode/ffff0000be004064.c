
void FUN_ffff0000be004064(void)

{
  int iVar1;
  
  DAT_ffff0000be0f9040 = FUN_ffff0000be0303e8(1,5);
  if (DAT_ffff0000be0f9040 == 0) {
    FUN_ffff0000be05fd98
              (s__s__get_spmi_device_fail_ffff0000be0a8b7b,s_mt6373_sdmadc_init_ffff0000be0b840b);
  }
  iVar1 = FUN_ffff0000be029260(&PTR_s_second_pmic_sdmadc_ffff0000be0d5450);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff0000be05fd98
            (s__s_failed_to_register_auxadc_dev_ffff0000be0c3a9f,
             s_mt6373_sdmadc_init_ffff0000be0b840b);
  return;
}


