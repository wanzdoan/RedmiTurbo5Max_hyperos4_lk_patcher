
void FUN_ffff000090704504(void)

{
  int iVar1;
  
  DAT_ffff00009083d070 = FUN_ffff000090740610(1,5);
  if (DAT_ffff00009083d070 == 0) {
    FUN_ffff00009077e11c
              (s__s__get_spmi_device_fail_ffff0000907d71f6,s_mt6373_sdmadc_init_ffff0000907ed9da);
  }
  iVar1 = FUN_ffff000090736cb4(&PTR_s_second_pmic_sdmadc_ffff000090816400);
  if (-1 < iVar1) {
    return;
  }
  FUN_ffff00009077e11c
            (s__s_failed_to_register_auxadc_dev_ffff0000907fe926,
             s_mt6373_sdmadc_init_ffff0000907ed9da);
  return;
}


